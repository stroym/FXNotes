package cz.stroym.fxnotes.controller;

import cz.stroym.fxnotes.model.Note;
import cz.stroym.fxnotes.model.Notebook;
import cz.stroym.fxnotes.model.Section;
import cz.stroym.fxnotes.model.Tag;
import cz.stroym.fxnotes.util.DataUtils;
import cz.stroym.fxnotes.util.DialogUtils;
import cz.stroym.fxnotes.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;

public class MainController {

  private static final ExtensionFilter TEXT_FILTER  = new ExtensionFilter("Json files", "*.json", "*.JSON");
  private static final FileChooser     FILE_CHOOSER = new FileChooser();
  private static       File            selectedFile;

  private static Notebook notebook = new Notebook();

  @FXML
  private ListView<Note>    notesView;
  @FXML
  private ListView<Section> sectionsView;
  @FXML
  private ListView<Tag>     tagsView;

  @FXML
  private TextArea noteTextArea;

  /**
   * Initializes the application after it starts.
   */
  @FXML
  private void initialize() {
    //TODO init notebook from preferences

    FILE_CHOOSER.getExtensionFilters().add(TEXT_FILTER);
    notesView.setItems(notebook.getDefaultSection().getObservableNotes());
    tagsView.setItems(notebook.getObservableTags());

    setupDragAndDrop();
    setupEditableListViews();
  }

  @FXML
  private void noteListViewLeftClick() {
    noteTextArea.setText(getSelectedNote().getContent());
  }

  @FXML
  private void editNoteText() {
    getSelectedNote().setContent(noteTextArea.getText());
  }

  @FXML
  private void addNote() {
    getSelectedSection().addNote(new Note());

    notesView.layout();
    notesView.getSelectionModel().selectLast();
    notesView.edit(notesView.getSelectionModel().getSelectedIndex());
    notesView.scrollTo(notesView.getSelectionModel().getSelectedIndex());
  }

  @FXML
  private void deleteNote() {
    getSelectedSection().getNotes().remove(getSelectedNote());
  }

  @FXML
  private void untagNote() {
    getSelectedNote().removeTag(getSelectedTag());
  }

  @FXML
  private void addTag() {
    notebook.addTag(new Tag());

    tagsView.layout();
    tagsView.getSelectionModel().selectLast();
    tagsView.edit(tagsView.getSelectionModel().getSelectedIndex());
    tagsView.scrollTo(tagsView.getSelectionModel().getSelectedIndex());
  }

  @FXML
  private void showAll() {
    sectionsView.getSelectionModel().select(-1);
    notesView.getSelectionModel().select(-1);
    noteTextArea.setText("");
    sectionsView.setItems(notebook.getObservableSections());
    notesView.setItems(null);
  }

  //TODO
//  @FXML
//  private void showTagged() {
//    ChoiceDialog<Tag> dialog = DialogUtils.generateTagChoiceDialog("Choose a tag", null, null, tags);
//    dialog.showAndWait();
//    Tag filterTag = dialog.getResult();
//
//    if (filterTag != null) {
//      notesListView.getSelectionModel().select(-1);
//      noteTextArea.setText("");
//      sectionListView.setItems(null);
//      notesListView.setItems(FXCollections.observableArrayList(notes.stream().filter(note -> note.getTags().contains(filterTag)).collect(Collectors.toList())));
//    }
//  }

  //TODO
  @FXML
  private void importFromJson() {
//    try {
//
////      FILE_CHOOSER.getExtensionFilters().setAll(TEXT_FILTER);
////      selectedFile = FILE_CHOOSER.showOpenDialog(null);
////
////      notes = FXCollections.observableArrayList();
////      tags = FXCollections.observableArrayList();
////
////      DataUtils.readJson(selectedFile, notes, tags);
////
////      notesListView.setItems(notes);
////      tagsListView.setItems(tags);
//    } catch (IOException e) {
//      DialogUtils.generateError("An error has occurred!", "Couldn't read from file", e).showAndWait();
//    }
  }

  @FXML
  private void saveToCurrentFile() {
    handleExport(selectedFile);
  }

  @FXML
  private void exportToJson() {
    handleExport(FILE_CHOOSER.showSaveDialog(null));
  }

  private void handleExport(File file) {
    try {
      if (notebook.getSections().isEmpty()) {
        DialogUtils.generateInfo("Information", "Nothing to save", null).showAndWait();
      }

      DataUtils.writeJson(file, notebook);
    } catch (IOException e) {
      DialogUtils.generateError("An error has occurred!", "Couldn't write to file", e).showAndWait();
    }
  }

  @SneakyThrows(InvalidSelectionException.class)
  private Note getSelectedNote() {
    return (Note) FXUtils.getSelected(notesView);
  }

  @SneakyThrows(InvalidSelectionException.class)
  private Section getSelectedSection() {
    return (Section) FXUtils.getSelected(sectionsView);
  }

  @SneakyThrows(InvalidSelectionException.class)
  private Tag getSelectedTag() {
    return (Tag) FXUtils.getSelected(tagsView);
  }

  /**
   * Adds cell factories to relevant {@link ListView} elements.
   * Necessary to display meaningful data from an named object (e.g. {@link Note})
   * and also the ability to change an object's name.
   */
  private void setupEditableListViews() {
    notesView.setCellFactory(lv -> {
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

    tagsView.setCellFactory(lv -> {
      TextFieldListCell<Tag> cell = new TextFieldListCell<>();

      cell.setConverter(new StringConverter<>() {
        @Override
        public String toString(Tag object) {
          return object.toString();
        }

        @Override
        public Tag fromString(String string) {
          Tag selected = getSelectedTag();
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
    tagsView.setOnDragDetected(event -> {
      Dragboard        db      = tagsView.startDragAndDrop(TransferMode.COPY);
      ClipboardContent content = new ClipboardContent();
      content.putString(tagsView.getSelectionModel().getSelectedItem().getValue());
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
