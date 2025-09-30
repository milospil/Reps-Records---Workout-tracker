package com.fxui;

import com.core.User;
import com.core.WorkoutSession;
import com.fxui.util.GetUser;
import com.fxui.util.LoadPage;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for a close overlay.
 */
public class CloseOverlayController {

  @FXML
  private Button closeButton;

  private LocalTime duration;

  @FXML
  void handleCloseButton() {
    closeWindow();
    LoadPage.loadPage("mainPage");
  }

  @FXML
  void handleCloseAndSaveButton() {

    User user = GetUser.getUser();

    UserManagerAccess holder = UserManagerAccessProvider.getInstance();

    WorkoutSession currentSession = 
        holder.getCurrentWorkoutSession();

    currentSession.setDurationTime(duration);

    holder.addWorkoutSessionToUser(user, currentSession);

    holder.autoSaveUserManager();

    closeWindow();

    LoadPage.loadPage("mainPage");

  }

  public void setSessionDurationTime(LocalTime duration) {
    this.duration = duration;
  }

  private void closeWindow() {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }
}
