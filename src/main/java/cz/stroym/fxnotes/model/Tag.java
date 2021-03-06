package cz.stroym.fxnotes.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tag extends Base {
  
  private String category;
  
  public Tag(long id, String value, String category) {
    super(id, value);
    this.category = category;
  }
  
  public Tag(long id, String value) {
    super(id, value);
  }
  
  private final BooleanProperty on = new SimpleBooleanProperty();
  
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
  
  //TODO tag groups for organization...
  
  @Override
  public String toString() {
    return category == null ? super.toString() : category + ":" + value;
  }
  
}
