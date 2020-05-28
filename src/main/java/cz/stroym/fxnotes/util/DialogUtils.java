package cz.stroym.fxnotes.util;

import cz.stroym.fxnotes.model.Tag;
import javafx.application.Platform;
import javafx.scene.control.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static javafx.scene.control.Alert.AlertType;

public class DialogUtils {

  /**
   * Populates common {@link Dialog} fields.
   *
   * @param dialog  dialog to populate
   * @param title   title to set
   * @param header  header to set
   * @param content content to set
   */
  private static void populateStrings(Dialog<?> dialog, String title, String header, String content) {
    dialog.setResizable(true);
    //necessary due to a bug in JavaFX that causes dialogs to be practically invisible on Linux
    dialog.onShownProperty().addListener(e -> Platform.runLater(() -> dialog.setResizable(false)));
    dialog.setTitle(title);
    dialog.setHeaderText(header);
    dialog.setContentText(content);
  }

  /**
   * Generates an informational {@link Alert}.
   *
   * @param title   title to set
   * @param header  header to set
   * @param content content to set
   * @return generated alert
   */
  public static Alert generateInfo(String title, String header, String content) {
    Alert alert = new Alert(AlertType.INFORMATION);
    populateStrings(alert, title, header, content);
    return alert;
  }

  /**
   * Generates a warning {@link Alert}.
   *
   * @param title   title to set
   * @param header  header to set
   * @param content content to set
   * @return generated alert
   */
  public static Alert generateWarning(String title, String header, String content) {
    Alert alert = new Alert(AlertType.WARNING);
    populateStrings(alert, title, header, content);
    return alert;
  }

  /**
   * Generates an error {@link Alert}.
   *
   * @param title  title to set
   * @param header header to set
   * @param e      exception to report
   * @return generated alert
   */
  public static Alert generateError(String title, String header, Throwable e) {
    Alert alert = new Alert(AlertType.ERROR);
    populateStrings(alert, title, header, "Click below to view the stack trace");
    alert.getDialogPane().setMinWidth(600);

    StringWriter outError = new StringWriter();
    e.printStackTrace(new PrintWriter(outError));

    TextArea textArea = new TextArea(outError.toString());
    textArea.setEditable(false);
    textArea.setWrapText(true);

    alert.getDialogPane().setExpandableContent(textArea);
    return alert;
  }

  /**
   * Generates an {@link Alert} fit for reporting unhandled exceptions to the user.
   *
   * @param e unhandled exception
   * @return generated alert
   */
  public static Alert generateException(Throwable e) {
    Alert alert = new Alert(AlertType.ERROR);
    populateStrings(alert, "An unexpected exception has occurred!", e.getMessage(), "Click below to view the stack trace");
    alert.getDialogPane().setMinWidth(600);

    StringWriter outError = new StringWriter();
    e.printStackTrace(new PrintWriter(outError));

    TextArea textArea = new TextArea(outError.toString());
    textArea.setEditable(false);
    textArea.setWrapText(true);

    alert.getDialogPane().setExpandableContent(textArea);

    return alert;
  }

  /**
   * Generates a confirmation box {@link Alert}.
   *
   * @param title   title to set
   * @param header  header to set
   * @param content content to set
   * @return generated alert
   */
  public static Alert generateConfirmation(String title, String header, String content) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    populateStrings(alert, title, header, content);
    return alert;
  }

  /**
   * Generates a text input {@link Dialog}.
   *
   * @param title   title to set
   * @param header  header to set
   * @param content content to set
   * @return generated alert
   */
  public static TextInputDialog generateTextInput(String title, String header, String content) {
    TextInputDialog dialog = new TextInputDialog();
    populateStrings(dialog, title, header, content);
    return dialog;
  }

  /**
   * Generates a choose one {@link Dialog}.
   *
   * @param title   title to set
   * @param header  header to set
   * @param content content to set
   * @param choices list to choose from
   * @return generated alert
   */
  public static ChoiceDialog<Tag> generateTagChoiceDialog(String title, String header, String content, List<Tag> choices) {
    ChoiceDialog<Tag> dialog = new ChoiceDialog<>(null, choices);
    populateStrings(dialog, title, header, content);
    return dialog;
  }

}
