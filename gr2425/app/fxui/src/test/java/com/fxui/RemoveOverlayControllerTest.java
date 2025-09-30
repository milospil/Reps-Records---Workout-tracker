package com.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.fxui.util.GetUser;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;


/**
 * Test class for Remove Overlay.
 * RemoveOverlayControllerTest
 */
public class RemoveOverlayControllerTest extends ApplicationTest {

  private RemoveOverlayController controller;

  @Mock
  private User mockUser;

  @Mock
  private Exercise mockExercise;

  @Mock
  private WorkoutSession mockWorkoutSession;

  @Mock
  private WorkoutSessionExercise mockWorkoutSessionExercise;

  @Mock
  private ListView<Exercise> mockExerciseListView;

  @Mock
  private ListView<WorkoutSession> mockWorkoutSessionListView;

  @Mock
  private UserManagerAccess mockUserManagerAccess;

  @Mock
  private WorkoutSession mockCurrentWorkoutSession;

  @Mock
  private Button mockCloseButton;

  @Mock
  private Scene mockScene;

  @Mock
  private Stage mockStage;

  private MockedStatic<GetUser> getUserMockedStatic;
  private MockedStatic<UserManagerAccessProvider> userManagerAccessProviderMockedStatic;


  /**
   * Test setup environment.
   */
  @BeforeEach
  public void setUp() {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Initialize the controller
    controller = new RemoveOverlayController();

    // Mock UserManager and User setup
    UserManager mockUserManager = mock(UserManager.class);
    when(mockUserManagerAccess.getUserManager()).thenReturn(mockUserManager);
    when(mockUserManager.getUsers()).thenReturn(List.of(mockUser));

    // Mock static methods
    userManagerAccessProviderMockedStatic = mockStatic(UserManagerAccessProvider.class);
    userManagerAccessProviderMockedStatic.when(UserManagerAccessProvider::getInstance)
        .thenReturn(mockUserManagerAccess);

    getUserMockedStatic = mockStatic(GetUser.class);
    getUserMockedStatic.when(GetUser::getUser).thenReturn(mockUser);

    controller.setCloseButton(new Button());
    controller.setExerciseListView(mockExerciseListView);
    controller.setWorkoutSessionListView(mockWorkoutSessionListView);
    when(mockUserManagerAccess.getCurrentWorkoutSession()).thenReturn(mockCurrentWorkoutSession);

    // Mock Scene and Stage for close button
    when(mockCloseButton.getScene()).thenReturn(mockScene);
    when(mockScene.getWindow()).thenReturn(mockStage);
    controller.setCloseButton(mockCloseButton);

  }

  /**
   * Test teardown.
   */
  @AfterEach
  public void tearDown() {
    // Close the static mocks
    getUserMockedStatic.close();
    userManagerAccessProviderMockedStatic.close();
  }

  @Test
  public void testSetCloseButton() {
    controller.setCloseButton(mockCloseButton);
    assertEquals(mockCloseButton, controller.getCloseButton());
  }

  @Test
  public void testHandleAcceptButton_RemoveExercise() {
    // Setup
    when(mockExercise.getExerciseName()).thenReturn("TestExercise");
    when(mockUser.getExerciseDatabase()).thenReturn(List.of(mockExercise));

    // Mock ListView items
    @SuppressWarnings("unchecked")
    ObservableList<Exercise> mockItems = mock(ObservableList.class);
    when(mockExerciseListView.getItems()).thenReturn(mockItems);

    // Mock Scene and Stage for close button
    when(mockCloseButton.getScene()).thenReturn(mockScene);
    when(mockScene.getWindow()).thenReturn(mockStage);
    controller.setCloseButton(mockCloseButton);

    controller.setExercise(mockExercise);
    controller.setExerciseListView(mockExerciseListView);

    controller.handleAcceptButton();

    verify(mockUser).setExercises(anyList());
    verify(mockItems).remove(mockExercise);
    verify(mockUserManagerAccess).autoSaveUserManager();
    verify(mockStage).close();
  }

  @Test
  public void testHandleAcceptButton_RemoveWorkoutSessionExercise() {
    // Setup
    WorkoutSessionExercise workoutExercise = mock(WorkoutSessionExercise.class);
    when(workoutExercise.getExerciseName()).thenReturn("TestWorkoutExercise");
    when(mockCurrentWorkoutSession.getExerciseList()).thenReturn(List.of(workoutExercise));

    // Mock ListView items
    @SuppressWarnings("unchecked")
    ObservableList<Exercise> mockItems = mock(ObservableList.class);
    when(mockExerciseListView.getItems()).thenReturn(mockItems);

    controller.setExercise(workoutExercise);
    controller.setExerciseListView(mockExerciseListView);

    controller.handleAcceptButton();

    verify(mockCurrentWorkoutSession).setExerciseList(anyList());
    verify(mockItems).remove(workoutExercise);
    verify(mockUserManagerAccess).autoSaveUserManager();
  }

  @Test
  public void testHandleAcceptButton_RemoveWorkoutSession() {
    // Mock ListView items
    @SuppressWarnings("unchecked")
    ObservableList<WorkoutSession> mockItems = mock(ObservableList.class);
    when(mockWorkoutSessionListView.getItems()).thenReturn(mockItems);

    controller.setWorkoutSession(mockWorkoutSession);
    controller.setWorkoutSessionListView(mockWorkoutSessionListView);

    controller.handleAcceptButton();

    verify(mockUser).removeWorkout(mockWorkoutSession);
    verify(mockItems).remove(mockWorkoutSession);
    verify(mockUserManagerAccess).autoSaveUserManager();
  }

}
