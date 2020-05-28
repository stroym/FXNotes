package cz.stroym.fxnotes.controller;

import cz.stroym.fxnotes.model.Note;
import cz.stroym.fxnotes.model.Notebook;
import cz.stroym.fxnotes.model.Tag;
import cz.stroym.fxnotes.util.DataUtils;
import cz.stroym.fxnotes.util.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class MainController {

  private static final ExtensionFilter      TEXT_FILTER  = new ExtensionFilter("Json files", "*.json", "*.JSON");
  private static final FileChooser          FILE_CHOOSER = new FileChooser();

  Notebook notebook = new Notebook();

  private static       ObservableList<Note> notes        = FXCollections.observableArrayList();
  private static       ObservableList<Tag>  tags         = FXCollections.observableArrayList();
  private static       File                 selectedFile;

  @FXML
  private ListView<Note> notesListView;
  @FXML
  private ListView<Tag>  noteTagsListView;
  @FXML
  private ListView<Tag>  globalTagsListView;

  @FXML
  private TextArea noteTextArea;

  @FXML
  private Label statusLabel;

  /**
   * Initializes the application after it starts.
   */
  @FXML
  private void initialize() {
    FILE_CHOOSER.getExtensionFilters().add(TEXT_FILTER);
    notesListView.setItems(notes);
    globalTagsListView.setItems(tags);

    setupDragAndDrop();
    setupEditableListViews();
  }

  /**
   * Handles clicks on {@link MainController#notesListView}. Serves as a selector.
   */
  @FXML
  private void noteListViewLeftClick() {
    Note selected = getSelectedNote();

    if (selected != null) {
      noteTextArea.setText(selected.getContent());
      noteTagsListView.setItems(FXCollections.observableArrayList(selected.getTags()));
    }
  }

  /**
   * Updates the currently selected {@link Note Note's} text.
   */
  @FXML
  private void editNoteText() {
    getSelectedNote().setContent(noteTextArea.getText());
  }

  /**
   * Handles adding new {@link Note}.
   */
  @FXML
  private void addNote() {
    notes.add(new Note());

    notesListView.layout();
    notesListView.getSelectionModel().selectLast();
    notesListView.edit(notesListView.getSelectionModel().getSelectedIndex());
    notesListView.scrollTo(notesListView.getSelectionModel().getSelectedIndex());
  }

  /**
   * Handles deleting {@link Note}.
   */
  @FXML
  private void deleteNote() {
    notes.remove(notesListView.getSelectionModel().getSelectedItem());
  }

  /**
   * Handles removing a {@link Tag} from a {@link Note}.
   */
  @FXML
  private void untagNote() {
    getSelectedNote().removeTag(getSelectedNoteTag());
    noteTagsListView.setItems(FXCollections.observableArrayList(getSelectedNote().getTags()));
  }

  /**
   * Handles the adding of a new {@link Tag}.
   */
  @FXML
  private void addTag() {
    tags.add(new Tag());

    globalTagsListView.layout();
    globalTagsListView.getSelectionModel().selectLast();
    globalTagsListView.edit(globalTagsListView.getSelectionModel().getSelectedIndex());
    globalTagsListView.scrollTo(globalTagsListView.getSelectionModel().getSelectedIndex());
  }

  /**
   * Handles resorting {@link MainController#globalTagsListView} and
   * refreshing the {@link MainController#noteTagsListView} after a {@link Tag} is edited.
   */
  @FXML
  private void commitTagEdit() {
    Collections.sort(tags);

    if (getSelectedNote() != null) {
      noteTagsListView.setItems(FXCollections.observableArrayList(getSelectedNote().getTags()));
    }
  }

  /**
   * Handles deleting a {@link Tag}.
   */
  @FXML
  private void deleteTag() {
    Tag selected = getSelectedGlobalTag();

    if (selected != null) {
      notes.stream().filter(note -> note.getTags().contains(selected)).forEach(note -> note.removeTag(selected));
      tags.remove(getSelectedGlobalTag());
    }

    if (getSelectedNote() != null) {
      noteTagsListView.setItems(FXCollections.observableArrayList(getSelectedNote().getTags()));
    }
  }

  /**
   * Displays all {@link Note Notes}.
   */
  @FXML
  private void showAll() {
    notesListView.getSelectionModel().select(-1);
    noteTextArea.setText("");
    noteTagsListView.setItems(null);
    notesListView.setItems(notes);
  }

  /**
   * Displays only {@link Note Notes} that have a specified {@link Tag}.
   */
  @FXML
  private void showTagged() {
    ChoiceDialog<Tag> dialog = DialogUtils.generateTagChoiceDialog("Choose a tag", null, null, tags);
    dialog.showAndWait();
    Tag filterTag = dialog.getResult();

    if (filterTag != null) {
      notesListView.getSelectionModel().select(-1);
      noteTextArea.setText("");
      noteTagsListView.setItems(null);
      notesListView.setItems(FXCollections.observableArrayList(notes.stream().filter(note -> note.getTags().contains(filterTag)).collect(Collectors.toList())));
    }
  }

  /**
   * Handles importing from text (json) format.
   */
  @FXML
  private void importFromJson() {
    try {
      FILE_CHOOSER.getExtensionFilters().setAll(TEXT_FILTER);
      selectedFile = FILE_CHOOSER.showOpenDialog(null);

      notes = FXCollections.observableArrayList();
      tags = FXCollections.observableArrayList();

      DataUtils.readJson(selectedFile, notes, tags);

      notesListView.setItems(notes);
      globalTagsListView.setItems(tags);
    } catch (IOException e) {
      DialogUtils.generateError("An error has occurred!", "Couldn't read from file", e).showAndWait();
    }
  }

  /**
   * Saves current application data to the data file that was last loaded.
   */
  @FXML
  private void saveToCurrentFile() {
    try {
      if (!isThereAnythingToSave()) {
        DialogUtils.generateInfo("Information", "Nothing to save", null).showAndWait();
      }

      if (selectedFile != null) {
        DataUtils.writeJson(selectedFile, new ArrayList<>(notes), new ArrayList<>(tags));
      } else {
        Alert alert = DialogUtils.generateWarning(null, "No file is currently selected!", null);
        alert.showAndWait();
      }
    } catch (IOException e) {
      DialogUtils.generateError("An error has occurred!", "Couldn't save to file", e).showAndWait();
    }
  }

  /**
   * Handles exporting to text (json) format.
   */
  @FXML
  private void exportToJson() {
    handleExport(FILE_CHOOSER.showSaveDialog(null));
  }

  private void handleExport(File file) {
    try {
      if (!isThereAnythingToSave()) {
        DialogUtils.generateInfo("Information", "Nothing to save", null).showAndWait();
      }

      statusLabel.setText("Saving to file " + file.getName());
      DataUtils.writeJson(file, new ArrayList<>(notes), new ArrayList<>(tags));
      statusLabel.setText("");
    } catch (IOException e) {
      DialogUtils.generateError("An error has occurred!", "Couldn't write to file", e).showAndWait();
    }
  }

  /**
   * Checks if there's anything to save.
   */
  private boolean isThereAnythingToSave() {
    return notes.size() > 0 || tags.size() > 0;
  }

  /**
   * Shortcut method to get the currently selected {@link Note}.
   */
  private Note getSelectedNote() {
    return notesListView.getSelectionModel().getSelectedItem();
  }

  /**
   * Shortcut method to get the currently selected {@link Note Note's} {@link Tag}.
   */
  private Tag getSelectedNoteTag() {
    return noteTagsListView.getSelectionModel().getSelectedItem();
  }

  /**
   * Shortcut method to get the currently selected global {@link Tag}.
   */
  private Tag getSelectedGlobalTag() {
    return globalTagsListView.getSelectionModel().getSelectedItem();
  }

  /**
   * Adds cell factories to relevant {@link ListView} elements.
   * Necessary to display meaningful data from an named object (e.g. {@link Note})
   * and also the ability to change an object's name.
   */
  private void setupEditableListViews() {
    notesListView.setCellFactory(lv -> {
      TextFieldListCell<Note> cell = new TextFieldListCell<>();

      cell.setConverter(new StringConverter<>() {
        @Override
        public String toString(Note object) {
          return object.toString();
        }

        @Override
        public Note fromString(String string) {
          Note selected = getSelectedNote();
          selected.setValue(string);
          return selected;
        }
      });

      return cell;
    });

    globalTagsListView.setCellFactory(lv -> {
      TextFieldListCell<Tag> cell = new TextFieldListCell<>();

      cell.setConverter(new StringConverter<>() {
        @Override
        public String toString(Tag object) {
          return object.toString();
        }

        @Override
        public Tag fromString(String string) {
          Tag selected = getSelectedGlobalTag();
          selected.setValue(string);
          return selected;
        }
      });

      return cell;
    });
  }

  /**
   * Sets up basic drag and drop functionality to provide a simple way to add {@link Tag Tags} to {@link Note Notes}.
   */
  private void setupDragAndDrop() {
    //copy from source to application clipboard
    globalTagsListView.setOnDragDetected(event -> {
      Dragboard        db      = globalTagsListView.startDragAndDrop(TransferMode.COPY);
      ClipboardContent content = new ClipboardContent();
      content.putString(globalTagsListView.getSelectionModel().getSelectedItem().getValue());
      db.setContent(content);

      event.consume();
    });

    /*
    sets up accepting "drop" for noteTagsListView
    only accept the drop if all of these conditions are fulfilled:
    a note is currently selected;
    drop target is not the data source;
    a string is currently in the application clipboard;
    the currently selected note does not already contain the tag that is being added
     */
//    noteTagsListView.setOnDragOver(event -> {
//      if (notesListView.getSelectionModel().getSelectedItem() != null &&
//              event.getGestureSource() != event.getGestureTarget() &&
//              event.getDragboard().hasString() &&
//              !noteTagsListView.getItems().contains(new Tag(event.getDragboard().getString()))) {
//
//        event.acceptTransferModes(TransferMode.COPY);
//      }
//
//      event.consume();
//    });

    /*
    before a tag is added to a note, given that the data is being transported as a string,
    it's necessary to make sure such a tag actually exists - if it does and is not already present in a note,
    the drop is accepted and the noteTagsListView is refreshed
     */
//    noteTagsListView.setOnDragDropped(event -> {
//      Dragboard dragboard = event.getDragboard();
//
//      if (dragboard.hasString()) {
//        Tag tag = tags.stream().filter(t -> t.equals(new Tag(dragboard.getString()))).findFirst().orElse(null);
//
//        if (tag != null && !noteTagsListView.getItems().contains(tag)) {
//          Note selected = getSelectedNote();
//          selected.tag(tag);
//          noteTagsListView.setItems(FXCollections.observableArrayList(selected.getTags()));
//          event.setDropCompleted(true);
//        }
//      }
//
//      event.consume();
//    });
  }

}
