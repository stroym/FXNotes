package cz.stroym.fxnotes.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

//TODO builder, maybe
@Getter
@NoArgsConstructor
public class Notebook {
  
  private static final Section DEFAULT_SECTION = new Section(0, "DEFAULT_APP_SECTION", -1);
  
  @Setter
  private       String        title          = "";
  private       List<Section> sections       = new ArrayList<>();
  private       Set<Tag>      tags           = new TreeSet<>();
  
  public Section getDEFAULT_SECTION() {
    return DEFAULT_SECTION;
  }
  
  public void addSection(Section section) {
    sections.add(section);
  }
  
  public void deleteSection(Section section) {
    sections.remove(section);
  }
  
  public void addTag(String value) {
    //    tags.add(tag); TODO
  }
  
  public void deleteTag(Tag tag) {
    tags.remove(tag);
  }
  
  public ObservableList<Section> getObservableSections() {
    return FXCollections.observableArrayList(sections);
  }
  
  public ObservableList<Tag> getObservableTags() {
    return FXCollections.observableArrayList(tags);
  }
  
  //TODO for global filtering
  //  public ObservableList<Note> getObservableNotes() {
  //    throw new UnsupportedOperationException();
  //  }
  //
  //  public ObservableList<Note> getObservableNotes(Note.NOTE_STATE state) {
  //    throw new UnsupportedOperationException();
  //  }
  
}
