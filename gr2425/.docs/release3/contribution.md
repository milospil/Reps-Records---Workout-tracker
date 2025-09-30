# Contributing members

  *Christoffer Paulsen*
  *Inga Haifa Kvernberg Dajani*
  *Mirjam Liodden*
  *Milos Pilipovic*

## Christoffer

  In this project I have mostly contributed to setting up our pom.xml dependencies through all releases. Specifically I added JaCoCo Coverage (aggregate reporting), SpringBoot, Maven Surefire and Failsafe (Integration Testing) and Jackson (JSON Serialization and Deserialization). I also set up the project module-structure. The greatest challenge here was version-matching all these plugins and opening different modules in the module-info.java to other modules to not get access errors.
  For the first release I made the .fxml files using Scenebuilder based on a design the group agreed to on beforehand, whilst for the second release my focus was on the controller logic and mostly tried to work on accessing data across controllers (Constructor Injection not possible with JavaFX) the solution was to use a Singleton Pattern for static references to a single object. For the third release I used CSS to sharpen our design to have a more modern look.
  I have also coded the JSON Serialization and some core logic in our CORE module. For release 3 I worked on some of the controllers and services in our RESTSERVER module.

## Inga Haifa Kvernberg Dajani

In this project, I focused on implementing and refining core functionality and user interface elements related to workout session tracking, user interaction, and data visualization. One of my contributions to the first release is Core Module setup, where I created the initial structure and foundational classes in the core module, implementing essential methods and functionality for managing usernames, sessions, session types, exercises, and intensity. This core framework provided the groundwork for further development by the team. For the second release, I worked more on the FXUI Module, where I implemented the option "Repeat Workout" on the main page, so that users can repeat the workouts they have saved. For the third release, I have developed client-side features to allow users to view and update their personal records. Christoffer Paulsen and I have also worked together on the Workout Progression Graph and on methods to add saved workouts to remote storage. These features, combined with additional documentation and refactoring, enhance user engagement and streamline workout management.

## Mirjam Liodden

My primary focus in this project has been on facilitating agile development and promoting effective teamwork in addition to contribute to the technical development. We believe that development is more than just programming, so I have concentrated on implementing Scrum practices, Git conventions, and organizing workshops to simplify the programming part.
For the first release, I established a workspace for the Reps&Records project on Slack and introduced Scrum practices, including daily standups, sprint planning and retrospectives. Since this is a school project, we held daily standups during designated working hours on Mondays and Fridays rather than every day. To enhance collaboration, I booked group rooms for meetings where we could share our "plans, progress, and problems" since the last meeting.
In the beginning, it was really important to make sure the whole team had the same understanding of what we wanted the application to do. To achieve this, I facilitated project planning sessions to define clear, specific goals with associated user stories with designated assignees to keep the group members accountable for development.
Additionally, I focused on promoting consistent Git commit messages and branch management, to maintain a clean codebase for minimizing technical debt. To help the team work better together, I also organized workshops and programming evenings to build stronger connections within the group and facilitate reflections around the project and development.

## Milos Pilipovic

Throughout this project, I focused on implementing key technical aspects and ensuring code quality across modules. In the first release, I implemented file saving to .csv files and established basic testing, providing a foundation for data persistence and quality assurance.
In the second release, I transitioned our data handling to JSON by setting up deserialization processes, allowing for seamless data retrieval across components. Additionally, I updated our FXML GUI, giving it a modern and user-friendly appearance. In the third release, my work centered on the REST-server module, where I developed controllers and services to handle HTTP requests and integrate data layers efficiently. This work enabled the app to handle HTTP requests and efficiently integrate data layers. A significant challenge was ensuring smooth interactions between the modules and maintaining efficient data handling across the system. I also expanded the unit tests for this new module, using Mockito for mocking and JaCoCo to track test coverage, and added tests to existing modules for comprehensive coverage. Additionally, I managed POM configuration for testing, coordinating dependencies across releases and configuring tools and libraries like Mockito and JaCoCo for robust testing. I also made our class, sequence, and package diagrams, ensuring they accurately represented our architecture.
