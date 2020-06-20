package cz.stroym.controls;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
public class AutocompleteTextField<T> extends TextField {
  
  private final SortedSet<T> entries           = new TreeSet<>();
  private       ContextMenu  candidatesContext = new ContextMenu();
  
  public AutocompleteTextField() {
    super();
    
    setListener();
  }
  
  private void setListener() {
    textProperty().addListener((observable, oldValue, newValue) -> {
      String enteredText = getText();
      
      if (enteredText == null || enteredText.isEmpty()) {
        candidatesContext.hide();
      } else {
        List<T> candidates = entries.stream()
                                    .filter(e -> e.toString().toLowerCase().contains(enteredText.toLowerCase()))
                                    .collect(Collectors.toList());
        
        if (!candidates.isEmpty()) {
          populatePopup(candidates, enteredText);
          
          if (!candidatesContext.isShowing()) {
            candidatesContext.show(AutocompleteTextField.this, Side.BOTTOM, 0, 0);
          }
        } else {
          candidatesContext.hide();
        }
      }
    });
    
    focusedProperty().addListener((observableValue, oldValue, newValue) -> {
      candidatesContext.hide();
    });
  }
  
  private void populatePopup(List<T> searchResult, String searchReauest) {
    List<CustomMenuItem> candidates = new LinkedList<>();
    int                  maxEntries = 10;
    int                  count      = Math.min(searchResult.size(), maxEntries);
    
    for (int i = 0; i < count; i++) {
      final T result = searchResult.get(i);
      
      Label entryLabel = new Label();
      entryLabel.setGraphic(buildTextFlow(result.toString(), searchReauest));
      entryLabel.setPrefHeight(10);
      CustomMenuItem item = new CustomMenuItem(entryLabel, true);
      candidates.add(item);
      
      item.setOnAction(actionEvent -> {
        setText(result.toString());
        positionCaret(result.toString().length());
        candidatesContext.hide();
      });
    }
    
    candidatesContext.getItems().clear();
    candidatesContext.getItems().addAll(candidates);
  }
  
  public static TextFlow buildTextFlow(String text, String filter) {
    int  filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
    Text textBefore  = new Text(text.substring(0, filterIndex));
    Text textAfter   = new Text(text.substring(filterIndex + filter.length()));
    Text textFilter = new Text(
        text.substring(filterIndex, filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
    textFilter.setFill(Color.ORANGE);
    textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
    return new TextFlow(textBefore, textFilter, textAfter);
  }
  
}
