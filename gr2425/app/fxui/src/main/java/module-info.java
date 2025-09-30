module com.fxui {
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires transitive com.core;
  requires javafx.graphics;
  requires java.net.http;
  requires javafx.swing;
  requires com.fasterxml.jackson.datatype.jsr310;
  requires com.fasterxml.jackson.databind;
  requires java.logging;


  opens com.fxui to javafx.fxml, javafx.graphics, org.testfx, org.junit.jupiter.api;

  exports com.fxui;
}
