package cz.papp.fxnotes;

import cz.papp.fxnotes.util.DialogUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FXNotesApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Handles all errors that aren't otherwise being caught and managed.
   *
   * @param t threat in which exceptions are being intercepted
   * @param e exception thrown
   */
  private void showErrorDialog(Thread t, Throwable e) {
    DialogUtils.generateException(e).showAndWait();
    Platform.exit();
  }

  @Override
  public void start(Stage stage) {
    Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> showErrorDialog(t, e)));
    Thread.currentThread().setUncaughtExceptionHandler(this::showErrorDialog);

    try {
      Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainWindow.fxml"));

      Scene scene = new Scene(root, 800, 500);
      stage.setTitle("FXNotes");
      stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
      stage.setScene(scene);
      stage.show();
    } catch (Throwable t) {
      showErrorDialog(Thread.currentThread(), t);
    }
  }
}
