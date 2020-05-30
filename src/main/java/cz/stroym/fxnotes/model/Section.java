package cz.stroym.fxnotes.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Section extends Base {

  private long userOrder;

  @Singular
  private List<Note> notes = new ArrayList<>();

  public void addNote(Note note) {
    notes.add(note);
  }

  public void deleteNote(Note note) {
    notes.remove(note);
  }

  public ObservableList<Note> getObservableNotes() {
    Collections.sort(notes);
    return FXCollections.observableArrayList(notes);
  }

  public ObservableList<Note> getObservableNotes(Note.NOTE_STATE state) {
    return FXCollections.observableArrayList(notes.stream().filter(note -> note.isState(state)).collect(Collectors.toList()));
  }

}
