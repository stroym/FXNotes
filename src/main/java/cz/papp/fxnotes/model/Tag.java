package cz.papp.fxnotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Comparable<Tag>, Serializable {

  private String name = "";

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Tag tag = (Tag) o;

    return this.name.equals(tag.name);
  }

  @Override
  public int compareTo(Tag tag) {
    return this.name.compareTo(tag.name);
  }

}
