package com.fxui;

import com.fxui.util.DisplayErrorMessage;
import com.fxui.util.LoadPage;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for GUI login page.
 */
public class LoginController {

  @FXML
  private TextField usernameTextField;

  @FXML
  private PasswordField passwordField;

  @FXML
  void handleLoginButton() {

    String username = usernameTextField.getText();
    String password = passwordField.getText();

    if (username.isBlank() || password.isBlank()) {
      DisplayErrorMessage
      .showError("Enter your username and password", "Field Validation Error", "Empty Fields");

    } else {

      if (!UserManagerAccessProvider.getInstance()
          .getUserManager().validateUser(username, password)) {
        DisplayErrorMessage.showError("Enter your username and password", "Field Validation Error",
            "Invalid username or Password");
            
      } else {
        UserManagerAccessProvider.getInstance().setUsername(username);
        LoadPage.loadPage("mainPage");
      }
    }
  }

  @FXML
  void handleNewUser() {
    LoadPage.loadPage("newUser");
  }

}
