package cz.stroym.fxnotes.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.*;

import java.io.Serializable;

@Getter
public abstract class Base implements Serializable, Comparable<Base> {
  
  @JsonTypeId
  protected final long id;
  
  @Setter
  protected String value;
  
  public Base(long id, String value) {
    this.id = id;
    this.value = value;
  }
  
  @Override
  public int compareTo(Base o) {
    return this.value.compareTo(o.value);
  }
  
  @Override
  public String toString() {
    return value;
  }
  
}
