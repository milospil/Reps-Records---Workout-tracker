**Reps&Records API**

**Base URI**
All requests are sent to the base URI:
http://localhost:8080/

**Endpoints**
1. getUserManager
- Description: Retrieves the UserManager instance from the server.
- URI: http://localhost:8080/usermanager
- Parameters: none
- Method: GET
- Endpoint: /usermanager
- Request Headers:
- Accept: application/json
- Response:
  - Success (200): Returns a JSON representation of UserManager.
  - Failure: Throws an exception if the response cannot be parsed.

2. getAllUsernames
- Description: Retrieves a list of all usernames from the remote server.
- URI: http://localhost:8080/api/users
- Parameters: none
- Method: GET
- Endpoint: /api/users
- Request Headers:
- Accept: application/json
- Response:
  - Success (200): Returns a JSON array of usernames.
    ```json 
    ["username","name"]
    ```
  - Failure: Throws an exception if there is an error in the HTTP request or response parsing.

3. getUserByUsername
- Description: Fetches a specific user by username.
- URI: http://localhost:8080//api/users/{username}
- Parameters: String username
- Method: GET
- Endpoint: /api/users/{username}
- Replace {username} with the actual username of the user to retrieve.
- Request Headers:
- Accept: application/json
- Response:
  - Success (200): Returns a JSON representation of the User object.
    ```json
    {
      "username":"tester",
      "password":"Test123!",
      "eMail":"test@tester.com",
      "fullName":"tester tester",
      "workoutList":[],
      "exerciseDatabase":[{
        "exerciseName":"Push Up",
        "purpose":"ARMS",
        "mechanics":"COMPOUND",
        "force":"PUSH"
        },
        {"more exercises"}
    ]}
    ```
  - Failure: Throws an exception if the user is not found or there’s an error in response parsing.

4. Create User
- Description: Creates a new user and saves it to the server.
- URI: http://localhost:8080/api/users
- Parameters: User user
- Method: POST
- Endpoint: /api/users
- Request Headers:
- Content-Type: application/json
- Request Body: json
  ```json
    {"username":"tester",
    "password":"Test123!",
    "eMail":"test@tester.com",
    "fullName":"tester tester",
    "workoutList":[],
    "exerciseDatabase":[{
        "exerciseName":"Push Up",
        "purpose":"ARMS",
        "mechanics":"COMPOUND",
        "force":"PUSH"
        },
        {"more exercises"}
    ]}
  ```
- Response:
  - Success (201): Returns the created User object.
  - Failure: Throws an exception if there is an error in the HTTP request or response parsing.

5. Create Exercise for User
- Description: Adds a new exercise to a specified user.
- URI: http://localhost:8080/{username}/exercises
- Parameters: User user, Exercise exercise
- Method: POST
- Endpoint: /api/users/{username}/exercises
- Replace {username} with the username for which the exercise is to be added.
- Request Headers:
- Content-Type: application/json
- Request Body: json
    ```json
    {"exerciseName":"Push Up",
    "purpose":"ARMS",
    "mechanics":"COMPOUND",
    "force":"PUSH"},
    {"more exercises"}
    ```
- Response:
  - Success (201): Returns the created Exercise object.
  - Failure: Returns an error if the server responds with an unexpected status code.

6. setUserManager
- Description: Saves the current UserManager to the server.
- URI: http://localhost:8080/usermanager
- Parameters: UserManager userManager
- Method: PUT
- Endpoint: /usermanager
- Request Headers:
- Content-Type: application/json
- Request Body:
- "JSON representation of the UserManager object."(Large object)
- Response:
  - Success (200): No response body but a status indicating the request succeeded.
  - Failure: Throws an exception if there’s an error in the HTTP request.

7. getAllSessions()
- Description: Retrieves all workout sessions for a specific user.
- URI: http://localhost:8080/api/users/{username}/workoutSessions
- Parameters: String username 
- Method: GET
- Endpoint: {username}/workoutSessions
- Request Headers:
- Accept: application/json
- Response:
  - Success (200): Returns a JSON array of WorkoutSession objects for the specified user.
  - Failure (404): Returns a 404 Not Found error if the user is not found.

8. addWorkoutSession()
- Description: Adds a new workout session to a specific user’s account.
- URI: http://localhost:8080/workoutSessions/{username}
- Parameters: 
  - String username
  - WorkoutSession workoutSession
- Method: POST
- Endpoint: /workoutSessions/{username}
- Request Headers:
- Content-Type: application/json
- Request Body (JSON):
```json
{
  "name": "Workout Session Name",
  "type": "STRENGTH",
  "intensity": "HIGH",
  "date": "2024-11-14T12:36",
  "sessionDurationTime" : "00:00",
  "exerciseList": [
    {
      "exerciseName": "Milos Press",
      "purpose": "CHEST",
      "mechanics": "COMPOUND",
      "force": "PUSH",
      "workoutSets": [
        {
          "setNumber": 1,
          "kilos": 800,
          "reps": 10
        }
      ]
    }
  ]
}
```
- Response:
  - Success (201): Returns the created WorkoutSession object.
  - Failure (404): Returns a 404 Not Found error if the user or workout session is not found.
