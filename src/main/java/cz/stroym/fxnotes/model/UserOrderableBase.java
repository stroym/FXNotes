package cz.stroym.fxnotes.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UserOrderableBase extends Base {
  
  private long userOrder;
  
  public UserOrderableBase(long id, String value, long userOrder) {
    super(id, value);
    this.userOrder = userOrder;
  }
  
}
