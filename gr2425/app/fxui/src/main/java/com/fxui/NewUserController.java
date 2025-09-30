package com.fxui;

import com.core.EmailValidation;
import com.core.PasswordValidation;
import com.core.User;
import com.fxui.util.DisplayErrorMessage;
import com.fxui.util.LoadPage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for handling a new user.
 */
public class NewUserController {

  @FXML
  private TextField fullNameField;

  @FXML
  private TextField eMailField;

  @FXML
  private TextField userNameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField confirmPasswordField;

  @FXML
  private CheckBox newUserTerms;

  @FXML
  private Label errorLabel;

  @FXML
  void handleReturnButton() {
    try {
      RepsAndRecordsUi.setRoot("login");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void handleCreateNewUser() {

    boolean allGood = true;

    if (!EmailValidation.validateEmail(eMailField.getText())) {
      DisplayErrorMessage
      .showError("Email must start with a letter", "Email Validatior Error", "Invalid Email");
      allGood = false;
    }

    if (!PasswordValidation.validatePassword(passwordField.getText())) {
      DisplayErrorMessage.showError(
          "Password must be at least 8 characters long, " 
              + "contain at least one uppercase letter, "
              + "one lowercase letter, one digit, and one special character.",
          "Password Validation Error", "Invalid Password");
      allGood = false;
    }

    if (!passwordField.getText().equals(confirmPasswordField.getText())) {
      DisplayErrorMessage
        .showError("Passwordfields must be equal", "Password Validation Error", "Invalid Password");
      allGood = false;
    }

    if (!newUserTerms.isSelected()) {
      DisplayErrorMessage
          .showError("You must agree to our terms and services", "Terms and Services Error",
          "Unchecked box");
      allGood = false;
    }

    if (allGood) {

      String eMail = eMailField.getText();
      String userName = userNameField.getText();
      String fullName = fullNameField.getText();
      String password = passwordField.getText();

      if (eMail.isBlank() || userName.isBlank() || fullName.isBlank()) {
        DisplayErrorMessage
        .showError("Please fill in all the fields", "Form Validation Error", "Empty Fields");
      } else {

        UserManagerAccess holder = UserManagerAccessProvider.getInstance();

        holder.createUser(new User(userName, 
              password, 
              fullName, 
              eMail, 
              holder.getUserManager().getExerciseDatabase()));

        holder.setUsername(userName);

        System.out.println("User successfully created");

        Platform.runLater(() -> LoadPage.loadPage("mainPage"));
      }
    }
  }
}
