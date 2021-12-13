# CS-180-Project-5

# Submission Details
Project 5 Reflection submitted on Brightspace by Liam Kelly

Project 5 Presentation submitted on Brightspace by Sean Lee

Project 5 submitted on Vocareum by Liam Kelly

# Compilation Instructions
To compile and run the server, run the `server.sh` script found in the root project directory. This script will compile all of the java classes, and run the main client class.
To compile and run the client, run the `client.sh` script found in the root project directory. This script will compile all of the java classes, and run the main server class.

_Note: If the script is ran on Windows (or any system that doesn't support .sh scripts), you can also compile the project normally via the javac command or via an IDE._

## Voccheck instructions
Voccheck (the script used by Vocareum to check coding style) only runs on .java files in the root directory. As such, it does not run with our project structure that is organized inside of directories. Because of this, we have provided a handy script `run_voccheck.sh` that will run voccheck on all of our .java files in our source code. Run `run_voccheck.sh` in Vocareum to view the output of voccheck on our code. If you want, you can also view the source code of the script to verify it is properly checking our code.

## Importing File Instructions
The instructions for importing quizzes from a file can be found in `ImportExample.txt`. 

## Javadoc Information
We have included Javadocs alongside our code which can be found in `/doc`. These javadocs include each of our classes, with documentation regarding what each class does and what many of the methods in each class also do. 

This documentation can be very useful for getting an overview of how certain parts of the code (such as the User Interface Menu System) are structured.

# Classes Summary

## Client Classes

### LearningManagementSystemClient
The main class for the client. Contains the NetworkManagerClient and the UIManager. Intializes the managers and starts the UI.

### NetworkManagerClient
Responsible for establishing a connection to the server and creating threads to send information back and forth. `init()` creates two threads, one for receiving objects and one for sending objects, so that the NetworkManagerClient can handle normal request and response packets as well as push packets sent to update the client when necessary.
### PushPacketHandler
Is used for catching push packets and running their associated callback functions.

### ResponsePacketHandler
Interprets incoming packets in context provided by a RunnableHandleResponsePacket and makes sure that the recieved packet is not null.

### RunnableHandleResponsePacket
An interface used to assign context to the received packets. Implemented and used by many other classes in order to propertly execute the `handlePacket()` method.
### UIManager
The manager that is responsible for the User Interface (UI). It uses the User Interface System to create menus that the user then interacts with. In `init()` it creates all of the menus, which sets up the structure of the UI, and then in `run()` it runs the start menu, which is used as the entry point to the rest of the UI.
### ClientFileWrapper
Contains methods for importing a quiz from a file. `readImportFile()` is a special read method used for importing existing quizzes from a file. It is special so that the people creating the quiz on a new file will have an easier time writing it and won't have to write it in a way that makes it easy for the program to read.
### Manager
The interface that all managers extend. Includs a `init` method and an `exit` method.

## Datastructure Classes

### Quiz
Implements Listable.  Class used for storing data about each quiz, including a toString which displays relevant information about the quiz.  It also is able to generate unique IDs for each question which haven't been used before.

### Question
Implements Listable.  Class used for storing data about each question.  It also has a toString which is used for displaying the question to the user.  Each question has a type, which can be multiple choice, true/false, or dropdown.  Each question also includes an array of answers.

### Answer
Class used for storing data about each answer.  Each answer is assigned a point value by the teacher, and is either correct or incorerct.

### User
A super class that contains the details of a particular user.

#### Teacher
Extends User. Contains the details of a particular teacher.

#### Student
Extends User. Contains the details of a particular student.

#### UserPermission
A class used for representing a user's permissions. This allows the permissions to be extended further in case we want more permissions than just 'Teacher' or 'Student' in the future.

### GradedQuiz
Class used for storing data about each graded quiz. Contains questions, student responses, the quiz's ID, and the student's ID.

### Listable
All classes that can be used in a list menu must implement this interface. It adds a function for how to represent the object in the list (`getListName()`).

### ListableGradedQuiz
This is a wrapper around GradedQuiz that includes fetching the names of the quiz and user so that it can be displayed to the user. The reason that it is not included in GradedQuiz itself is because GradedQuiz is for data storage, while this wrapper class is for data presentation. This wrapper class is used to contain redundant, slower to process, user-friendly data that is accessed via the other managers through LMS.

## GUI Classes

### Aesthetics
A class containing all of the constants for the GUI. Is used for easily changing the aesthetics of the GUI.

### Button
A class that extends JButton to encapsulate the functionality of a button and extend it to work with the GUI framework.
Allows registering of an `onClick` function that runs whenever the button is clicked.

### DebugListener
A class used for identifying the placement and size of panels when debugging. This class is not used in the final project.
However, in the future, if we wished to debug, this class would be very important for GUI debugging.

### Dropdown
A class that representing a dropdown in a panel. It contains a JComboBox. The class allows for intertwining with the rest of the GUI.

### GapComponent
An empty component used for spacing.

### Heading
A JLabel that has a big font size and is bold.

### Label
A JLabel with some additional functionality, such as setting margins and font size.

### Panel
The base class for the UI. All menus are built of this class. It contains the ability for panels to be dynamically visible (via the "addTabPanel"), or the ability to add modals (overlaying panels) via the "addModal".

### PanelRunnable
Am interface that is used for defining a callback function. The given argument is the panel that the callback function is ran from.

### RadioButton
An extension of JRadioButton that can communicate with additional parts of the UI.

### TextField
An extension of JTextField that adds a label and allows for additional communication with the rest of the UI.

## Server Classes

### FileWrapper
The class used for reading and writing to file. It contains static methods that are used in any situation where interacting with files is necessary. `readFile()` and `writeFile()` are generic methods that are used for reading and writing files.


### GradedQuizManager
The manager that holds the list of all graded quizzes. It also provides a method for searching for graded quizzes by course, and a method to delete all graded quizzes taken by a certain student.

### LearningManagementSystemServer
This class contains all the managers that are stored on the server - quizManager, gradedQuizManeger, UserManager, and all of those file managers.  When the server is started those are all initialized.  This is also used for the client managers to retrieve information from one another.

### NetworkManagerServer
This handles requests for information from the client, including sending push packets to all clients if something needs to be changed.  It also starts the server on initialization.  It utilizes multithreading in order to simultaneously be able to send and receive packets while also always listening for push packets.

### UserFileManager, QuizFileManager, and GradedQuizFileManager
The managers that read and write user, quiz, and graded quiz data to and from files. Each gives its respective manager the list of users, quizzes, or graded quizzes at the start of the program and receives the list of them at the end. The file paths in `readUsers()`, `readQuizzes()`, and `readGradedQuizzes` as well as `writeUsers()`, `writeQuizzes()`, and `writeGradedQuizzes()` are hard coded and should work as relative paths for the file that stores the desired information.

### QuizManager
The manager that holds the list of every created quiz.  It also provides methods for searching through the quizzes with various filters, such as name, author, etc.  It also is responsible for generating unique ids for new quizzes.

### UserManager
The manager that is responsible for keeping track of users and providing functionalities such as `addUser()` or `authenticator()`. This manager performs actions that mainly require iterating through all the Users. For example, searching a user by his username or generating a unique id for each user is done through this class.

## Packet Classes

### Request Packet Classes

#### CreateUserRequestPacket
This packet serves as a bridge for communication between the client side to server communication. Information is sent from the client to the server once the user chooses the option to create a user in the GUI. This class provides the server class with the information to create a new user based on user inputs.

#### DeleteUserRequestPacket
This communicates between the client side and the server side. Information is sent from the client to the server once the user chooses the option to delete their account in the GUI. This class provides the server class with the information to delete their account.

#### GradedQuizListRequestPacket
Is used for requesting the list of quiz submissions by students. Can specify for a list of submissions by a specific user.

#### GradedQuizRequestPacket
Is used for requesting a specific graded quiz.

#### LoginUserRequestPacket
Once the user enters the username and password and clicks on the submit button to log in to the system, the client side connects with the server to send the information, process that information, and authenticates the identity of the user based on their inputs.
#### QuizListRequestPacket
Enables the client to request a list of quizzes.  The list returned can be filtered by name, author, course, and all quizzes.
#### QuizRequestPacket
Requests a specific quiz.  Can also be used when creating a new quiz to create that quiz.
#### DeleteQuizRequestPacket
If a quiz exists with the specified ID, deletes that quiz and all associated quiz submissions.
#### RequestPacket
The super class for all response packets.
#### UpdateUserRequestPacket
Sends a packet from client to server so that user information can be updated in real-time on the GUI

## Response Packet Classes

#### GradedQuizResponsePacket
Returns a requested graded quiz to the user. If any changes were made to the graded quizzes or a new graded quiz was created, pushes the change to all users connected.

#### NewUserResponsePacket
Stores the user that has logged in to the program as an object.
#### ChangeUserResponsePacket
Sends a packet from server to client to inform the client the user information has been changed. This packet is used for recognizing type of change in user information
#### QuizListResponsePacket
Sends the requested list of quizzes back to the client
#### QuizResponsePacket
Returns a requested quiz to the user.  If any changes were made to the quizzes or a new quiz was created, pushes the change to all users connected.
#### DeleteQuizResponsePacket
Tells the client if a quiz was succesfully deleted, and if it was pushes the updated quiz list to all clients.
#### DeleteUserResponsePacket
This connects the server with the client and tells the client side that account deletion for the current user has been done successfully and the interface should go back to the login screen.
#### GradedQuizListResponsePacket
A response packet containing the list of graded quizzes requested from GradedQuizRequestPacket

#### GradedQuizResponsePacket
A response packet containing the specific graded quiz that was requested.

#### ResponsePacket
The super class for all response packets.



