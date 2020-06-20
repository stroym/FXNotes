module fxnotes {
  requires javafx.controls;
  requires javafx.fxml;
  
  requires static lombok;
  requires static com.fasterxml.jackson.databind;
  requires static com.fasterxml.jackson.annotation;
  requires static com.fasterxml.jackson.core;
  
  opens cz.stroym.fxnotes to javafx.fxml;
  opens cz.stroym.fxnotes.controller to javafx.fxml;
  
  exports cz.stroym.fxnotes to javafx.graphics;
  exports cz.stroym.fxnotes.controller;
  exports cz.stroym.controls;
  
}
