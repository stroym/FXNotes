package cz.stroym.fxnotes.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Base implements Serializable, Comparable<Base> {

  @JsonTypeId
  protected long id;

  @Setter
  protected String value = "";

  @Override
  public int compareTo(Base o) {
    return this.value.compareTo(o.value);
  }

  @Override
  public String toString() {
    return value;
  }

}
