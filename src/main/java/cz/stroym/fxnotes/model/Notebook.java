package cz.stroym.fxnotes.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notebook extends Base {

  private List<Note> notes = new ArrayList<>();

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Set<Tag> tags = new TreeSet<>();

  public void addTag(Tag tag) {
    tags.add(tag);
  }

  public void deleteTag(Tag tag) {
    tags.remove(tag);
  }

  public ObservableList<Note> getObservableNotes() {
    return FXCollections.observableArrayList(notes);
  }

  public ObservableList<Tag> getObservableTags() {
    return FXCollections.observableArrayList(tags);
  }

  public ObservableList<Note> getObservableNotes(Note.NOTE_STATE state) {
    return FXCollections.observableArrayList(notes.stream().filter(note -> note.isState(state)).collect(Collectors.toList()));
  }

}
