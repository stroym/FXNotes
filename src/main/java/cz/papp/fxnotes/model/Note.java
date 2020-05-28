package cz.papp.fxnotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Note implements Serializable {

  @Setter
  private String title = "";
  @Setter
  private String text  = "";

  private List<Tag> tags = new ArrayList<>() {
    public boolean add(Tag mt) {
      super.add(mt);
      Collections.sort(tags);
      return true;
    }
  };

  public void tag(Tag tag) {
    tags.add(tag);
  }

  public void untag(Tag tag) {
    tags.remove(tag);
  }

  @Override
  public String toString() {
    return title;
  }

}
