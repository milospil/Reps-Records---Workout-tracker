# Group gr2425 repository

[Open in Eclipse Che](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2024/gr2425/gr2425?new)

This project is for group 25 in the course IT1901 fall 2024. The project is an application named **Reps&Records** with a purpose of keeping track of progression of gym sessions.

**How to run the remote application**

To run this application, please ensure that Java (version 17) and Maven are installed on your system. After cloning the repository to your local machine, run the following commands in your terminal to start the server:

`cd app`
`mvn clean install -Pheadless` (optional to run with headless)
`cd restserver`
`mvn spring-boot:run`

Open a new terminal and open the remote application with following commands:
`cd app/fxui`
`mvn -Premoteapp javafx:run`

Now, Reps&Records will open. To view stored data, navigate to [this URL](http://localhost:8080/api/users) in your browser. You’ll see one user, "Testing." When you create a new user in the interface, it will be added to this list.

To view details for a specific user, change the endpoint to api/users/{yourUsername}, which will display your login details and the default exercises associated with all new users. These exercises can also be found under the "Exercises" tab in the app.

If you add or remove exercises within the app, these changes will be updated on both the server and the user interface. New users start with an empty "workoutList." After creating a workout session with exercises, sets, repetitions, and weights, refresh the server page to see the updated data.

You can also repeat a workout session by selecting "Repeat Workout Session" and modifying it as desired. When saved, it will appear as a new workout session linked to your user.

**How to run the local application**
`mvn clean install`
`cd app/fxui`
`mvn javafx:run`

**How to run the tests**

When starting the application with `mvn clean install`, the project tests will run automatically. If they don’t, navigate to the app directory and use `mvn test` in the terminal to run them manually.

We found that sometimes it’s necessary to run the `mvn clean install` command twice, as the project occasionally doesn’t locate the aggregate report on the first attempt.

## Project setup

**Java Version**

This project requires Java version 17 or later.

**Maven Version**

This project uses Maven version 3.9.9 or later.

**Dependencies**

This project requires the following dependencies:

- JUnit version 4.11 for testing

- org.junit.jupiter version 5.7.0 for JUnit testing

- org.openjfx version 21 for JavaFX

## AI usage

This project is an opportunity to learn how to develop an application from scratch, and we think that it is better to have simple code made by our own, than heavy functionality made with AI. if we still have used artificial intelligence, mainly focused on language models as ChatGPT and Claude, this will be commented in the documentation for the current part of the project.

## Functionality

As a [user](app/core/src/main/java/com/core/User.java), you can either create a new account with a name, email, username, and password, or log into an existing account. Upon successful login, you are directed to the Reps&Records homepage. Here, you’ll see a graph showing the time spent on workouts over the last 30 days. Each workout session you log adds a red line on the graph, indicating the minutes tracked for each date.

Underneath the graph, there are two buttons for tracking a new workout session. On the left side you can create a new [workoutSession](app/core/src/main/java/com/core/WorkoutSession.java) of [exersices](app/core/src/main/java/com/core/Exercise.java) containing [sets](app/core/src/main/java/com/core/WorkoutSet.java), and on the right side you can repeat workout sessions you have tracked earlier. 

The navigation bar at the bottom links to the “Exercises” page. Here, users start with a set of basic exercises but can add or remove exercises freely. You can remove exercises you don´t need, and add own exercises to the database. These exercises can easily be used in the workout sessions. 

When logging a workout, you can add exercises from the database or create new ones directly within the session, which will then be saved for future use. For each exercise, you can add multiple sets and log repetitions and weight as you go. After completing a workout, a line indicating time spent is added to the homepage graph, and the session becomes available for future repetition. To save the session you clock on "Cancel", and then "Finish and save". Repeated sessions include the same exercises, but sets and repetitions will be blank for new input.

Exercises are organized by attributes like purpose (for example legs or arms), mechanics (compound or isolation), and force type (push or pull), making it easier to select exercises that align with specific workout goals. In future versions of the application, it would be exciting to add a feature that generates complete workout templates, using these attributes to create personalized sessions.

## Documentation

More documentation for the project and development process is to be found in [release1](.docs/release1/release1.md), [release2](.docs/release2/release2.md) and [release3](.docs/release3/release3.md)