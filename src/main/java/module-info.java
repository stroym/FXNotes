module cz.stroym.fxnotes {
  requires javafx.controls;
  requires javafx.fxml;
  
  requires cz.stroym.fxcontrols;
  
  requires static lombok;
  requires static com.fasterxml.jackson.databind;
  requires static com.fasterxml.jackson.annotation;
  requires static com.fasterxml.jackson.core;
  requires org.controlsfx.controls;
  
  opens cz.stroym.fxnotes to javafx.fxml;
  opens cz.stroym.fxnotes.controller to javafx.fxml;
  
  exports cz.stroym.fxnotes to javafx.graphics;
  exports cz.stroym.fxnotes.controller;
  
}
