package cz.stroym.fxnotes.controller;

import cz.stroym.fxcontrols.control.SearchableListView;
import cz.stroym.fxcontrols.control.TagBox;
import cz.stroym.fxnotes.model.Note;
import cz.stroym.fxnotes.model.Notebook;
import cz.stroym.fxnotes.model.Section;
import cz.stroym.fxnotes.model.Tag;
import cz.stroym.fxnotes.util.DataUtils;
import cz.stroym.fxnotes.util.DialogUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class MainController {
  
  //TODO when user config is a thing, generate a default one in this location - might be better suited to go in app class
  private static final File            userPreferences =
      new File(System.getenv("APPDATA") + "FXNotes/userPreferences.json");
  private static final ExtensionFilter TEXT_FILTER     =
      new ExtensionFilter("Json files", "*.json", "*.JSON");
  private static final FileChooser     FILE_CHOOSER    = new FileChooser();
  
  private static File selectedFile;
  
  private static Notebook notebook = new Notebook();
  
  @FXML
  private GridPane rootControl;
  
  @FXML
  private SearchableListView<Note>    notesView;
  @FXML
  private SearchableListView<Section> sectionsView;
  
  @FXML
  private ContextMenu sectionsContext;
  
  @FXML
  private ContextMenu notesContext;
  
  @FXML
  private ContextMenu tagsContext;
  
  @FXML
  private TextArea noteTextArea;
  
  @FXML
  private TagBox tagBox;
  
  @FXML
  private void initialize() {
    tagBox.setComboBoxItems(
        FXCollections.observableArrayList(Arrays.asList("asdasdasd", "asdasddas", "asdasdsadasdggtr", "dtzjzujzujwe"))
    );
    
    //TODO init notebook from preferences when that's a thing
    FILE_CHOOSER.getExtensionFilters().add(TEXT_FILTER);
    notesView.setItems(notebook.getDEFAULT_SECTION().getObservableNotes());
    //    tagsView.setItems(notebook.getObservableTags());
    
    for (int i = 0; i < 30; i++) {
      notesView.getItems().add(new Note(i, new Random().nextInt() + "", -1, "", false, Note.NOTE_STATE.NEW));
    }
    
    notesView.setItems(notesView.getItems().sorted());
    notesView.refresh();
    
    // registerEmptyListViewClickConsumers();
    setupEditableListViews();
  }
  
  private Note getSelectedNote() {
    return notesView.getSelectionModel().getSelectedItem();
  }
  

//  private void setupDragAndDrop() {
//    //copy from source to application clipboard
//    tagsView.setOnDragDetected(event -> {
//      Dragboard        db      = tagsView.startDragAndDrop(TransferMode.COPY);
//      ClipboardContent content = new ClipboardContent();
//      content.putString(tagsView.getSelectionModel().getSelectedItem().getValue());
//      db.setContent(content);
//
//      event.consume();
//    });
    
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
//  }
  
  @FXML
  private void noteListViewLeftClick() {
    Note selected = getSelectedNote();
    
    noteTextArea.setText(selected.getContent());
  }
  
  @FXML
  private void editNoteText() {
    getSelectedNote().setContent(noteTextArea.getText());
  }
  
  @FXML
  private void addNote() {
    getSelectedSection().addEmptyNote();
    
    notesView.layout();
    notesView.getSelectionModel().selectLast();
    notesView.edit(notesView.getSelectionModel().getSelectedIndex());
    notesView.scrollTo(notesView.getSelectionModel().getSelectedIndex());
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
  
  private Section getSelectedSection() {
    return sectionsView.getSelectionModel().getSelectedItem();
  }
  
  @FXML
  private void deleteNote() {
    getSelectedSection().getNotes().remove(getSelectedNote());
  }
  
  @FXML
  private void untagNote() {
//    getSelectedNote().untag(getSelectedTag());
  }
  
  @FXML
  private void addTag() {
    //TODO get value from input
    notebook.addTag("");
    
//    tagsView.layout();
//    tagsView.getSelectionModel().selectLast();
//    tagsView.edit(tagsView.getSelectionModel().getSelectedIndex());
//    tagsView.scrollTo(tagsView.getSelectionModel().getSelectedIndex());
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
  
  @FXML
  private void exportToJson() {
    handleExport(FILE_CHOOSER.showSaveDialog(null));
  }
  
  private void registerEmptyListViewClickConsumers() {
    notesView.setCellFactory(lv -> {
      ListCell<Note> cell = new ListCell<>() {
        @Override
        protected void updateItem(Note item, boolean empty) {
          super.updateItem(item, empty);
        }
      };
      cell.setOnMouseClicked(e -> {
        if (!cell.isEmpty()) {
          e.consume();
        }
      });
      return cell;
    });
    
    notesView.setOnMouseClicked(e -> { });
    
    sectionsView.setCellFactory(lv -> {
      ListCell<Section> cell = new ListCell<>() {
        @Override
        protected void updateItem(Section item, boolean empty) {
          super.updateItem(item, empty);
        }
      };
      cell.setOnMouseClicked(e -> {
        if (!cell.isEmpty()) {
          e.consume();
        }
      });
      return cell;
    });
    
    sectionsView.setOnMouseClicked(e -> { });
  }
  
  /**
   * Adds cell factories to relevant {@link ListView} elements. Necessary to display meaningful data from an named
   * object (e.g. {@link Note}) and also the ability to change an object's name.
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
    
    //    tagsView.setCellFactory(lv -> {
    //      TextFieldListCell<Tag> cell = new TextFieldListCell<>();
    //
    //      cell.setConverter(new StringConverter<>() {
    //        @Override
    //        public String toString(Tag object) {
    //          return object.toString();
    //        }
    //
    //        @Override
    //        public Tag fromString(String string) {
    //          Tag selected = getSelectedTag();
    //          selected.setValue(string);
    //          return selected;
    //        }
    //      });
    //
    //      return cell;
    //    });
  }
  
}
