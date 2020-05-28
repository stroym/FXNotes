package cz.stroym.fxnotes.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tag extends Base implements Comparable<Tag> {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Tag tag = (Tag) o;

    return this.value.equals(tag.value);
  }

  @Override
  public int compareTo(Tag tag) {
    return this.value.compareTo(tag.value);
  }

}
