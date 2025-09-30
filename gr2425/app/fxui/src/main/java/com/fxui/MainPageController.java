package com.fxui;

import com.core.Exercise;
import com.fxui.util.GetUser;
import com.fxui.util.LoadPage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controller for the GUI main page.
 */
public class MainPageController {

  @FXML
  private Button newWorkoutButton;

  @FXML
  private Button repeatWorkoutButton;

  @FXML
  private ListView<Exercise> exerciseDatabaseListView;

  @FXML
  private AnchorPane exercisesAnchorPane;

  @FXML
  private Tab exerciseTab;

  @FXML
  private BarChart<String, Number> barChart;

  @FXML
  private CategoryAxis xAxis;

  @FXML
  private NumberAxis yAxis;

  private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd");


  @FXML
  void initialize() {
    yAxis.setLabel("Duration (minutes)");

    // DENNE KODEBLOKKEN BLE GENERERT VED HJELP AV CHATGPT

    exerciseDatabaseListView.setCellFactory(new Callback<ListView<Exercise>, ListCell<Exercise>>() {
      @Override
      public ListCell<Exercise> call(ListView<Exercise> listView) {
        return new ExerciseListCell<Exercise>(false);
      }
    });

    // SLUTT PÅ GENERERT KODEBLOKK

    HashMap<LocalDate, Integer> workoutData = GetUser.getUser().getLast30DaysWorkouts();
    XYChart.Series<String, Number> series = new XYChart.Series<>();

    for (int i = 29; i >= 0; i--) {
      LocalDate date = LocalDate.now().minusDays(i);

      String formattedDate = dateFormatter.format(date);

      int duration = workoutData.get(date);
      series.getData().add(new XYChart.Data<>(formattedDate, duration));
    }

    barChart.getData().clear();
    barChart.getData().add(series);
    exerciseDatabaseListView.setItems(getExerciseDatabase());

  }

  /**
   * Method to update chart whenever a workout session is completed.
   */
  public void updateChart() {
    HashMap<LocalDate, Integer> workoutData = GetUser.getUser().getLast30DaysWorkouts();
    XYChart.Series<String, Number> series = new XYChart.Series<>();

    for (int i = 29; i >= 0; i--) {
      LocalDateTime date = LocalDateTime.now().minusDays(i);

      String formattedDate = dateFormatter.format(date);


      int duration = workoutData.getOrDefault(date, 0);
      series.getData().add(new XYChart.Data<>(formattedDate, duration));
    }
    barChart.getData().clear();
    barChart.getData().add(series);
  }

  @FXML
  void handleHelpClick() {

  }

  /**
   * Button that allows the user to create a new workout.
   */

  @FXML
  void handleNewWorkoutButton() {
    LoadPage.loadPage("newWorkoutData");
  }

  /**
   * Button that opens a new instance where the user has the option of repeating a previously saved
   * exercise.
   */

  @FXML
  void handleRepeatWorkoutButton() {
    LoadPage.loadPage("workoutList");
  }

  /**
   * Method that converts a list to ObservableList so JavaFX understands how to add and remove from
   * the exercise ListView.

   * @return a list of exercises, that javafx can understand.
   */
  private ObservableList<Exercise> getExerciseDatabase() {
    return FXCollections.observableArrayList(GetUser.getUser().getExerciseDatabase());
  }

  /**
   * Button that opens list of exercises where the user has the option of adding exercise to workout
   * session.

   * @throws IOException if fxml file is not found.
   */
  @FXML
  private void handleAddNewExercise() throws IOException {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("newExercise.fxml"));
      Parent root = loader.load();

      NewExerciseController controller = loader.getController();
      controller.setMainPageController(this);


      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ListView<Exercise> getExerciseDatabaseListView() {
    return exerciseDatabaseListView;
  }
}
