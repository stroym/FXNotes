package cz.stroym.fxnotes;

import cz.stroym.fxnotes.util.DialogUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class FXNotesApplication extends Application {
  
  public static void main(String[] args) {
    launch(args);
  }
  
  private void showErrorDialog(Thread t, Throwable e) {
    DialogUtils.generateException(e).showAndWait();
    Platform.exit();
  }
  
  @Override
  public void start(Stage stage) throws IOException {
    //    Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> showErrorDialog(t, e)));
    //    Thread.currentThread().setUncaughtExceptionHandler(this::showErrorDialog);
    
    //    try {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainWindow.fxml"));
    
    Scene scene = new Scene(root);
    scene.getStylesheets().addAll(getClass().getResource("/css/main.css").toExternalForm(),
                                  getClass().getResource("/css/scrollbar.css").toExternalForm()
    );
    
    stage.setTitle("FXNotes");
    stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
    stage.setScene(scene);
    stage.show();
    //    } catch (Throwable t) {
    //      showErrorDialog(Thread.currentThread(), t);
    //    }
  }
  
}
