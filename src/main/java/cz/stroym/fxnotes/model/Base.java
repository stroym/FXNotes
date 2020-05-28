package cz.stroym.fxnotes.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "value")
public abstract class Base implements Serializable {

  @JsonTypeId
  protected long id;

  @Setter
  protected String value = "";

}
