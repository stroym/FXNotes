package cz.stroym.fxnotes.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Section extends UserOrderableBase {
  
  @Singular
  private List<Note> notes = new ArrayList<>();
  
  public Section(long id, String value, long userOrder) {
    super(id, value, userOrder);
  }
  
  public void addNote(Note note) {
    notes.add(note);
  }
  
  public void addEmptyNote() {
    //TODO
  }
  
  public void deleteNote(Note note) {
    notes.remove(note);
  }
  
  public ObservableList<Note> getObservableNotes() {
    Collections.sort(notes);
    return FXCollections.observableArrayList(notes);
  }
  
  public ObservableList<Note> getObservableNotes(Note.NOTE_STATE state) {
    return FXCollections.observableArrayList(
        notes.stream().filter(note -> note.isState(state)).collect(Collectors.toList()));
  }
  
}
