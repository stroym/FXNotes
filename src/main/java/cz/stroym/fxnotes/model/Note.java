package cz.stroym.fxnotes.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Note extends Base {

  private String content = "";

  private boolean archived = false;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Note.NOTE_STATE state;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Set<Tag> tags = new TreeSet<>();

  public void tag(Tag tag) {
    tags.add(tag);
  }

  public void removeTag(Tag tag) {
    tags.remove(tag);
  }

  public boolean isState(Note.NOTE_STATE state) {
    return this.state == state;
  }

  public enum NOTE_STATE {
    NEW,
    READ,
    PROCESSED,
    DISCARDED,

//    @Getter
//    private String displayValue;
//
//    NOTE_STATE(String displayValue) {
//      this.displayValue = displayValue;
//    }

  }

}
