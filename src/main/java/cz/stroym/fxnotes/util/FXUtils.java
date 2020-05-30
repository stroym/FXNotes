package cz.stroym.fxnotes.util;

import cz.stroym.fxnotes.controller.InvalidSelectionException;
import javafx.scene.control.ListView;

public class FXUtils {

  public static Object getSelected(ListView<?> listView) throws InvalidSelectionException {
    Object o = listView.getSelectionModel().getSelectedItem();

    if (o == null) {
      throw new InvalidSelectionException();
    }

    return o;
  }

}
