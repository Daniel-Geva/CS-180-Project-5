# CS-180-Project-5

# Submission Details
Project 4 Reflection Submitted by Liam Kelly

Project 4 submitted on Vocareum by Sean Lee

# Compilation Instructions
To compile and run the project, run the `compile_and_run.sh` script found in the root project directory. This script will compile all of the java classes, run the main class, and then clean away all of the .class files once the program has exited.

_Note: If the script is ran on Windows (or any system that doesn't support .sh scripts), you can also compile the project normally via the javac command or via an IDE._

## Voccheck instructions
Voccheck (the script used by Vocareum to check coding style) only runs on .java files in the root directory. As such, it does not run with our project structure that is organized inside of directories. Because of this, we have provided a handy script `run_voccheck.sh` that will run voccheck on all of our .java files in our source code. Run `run_voccheck.sh` in Vocareum to view the output of voccheck on our code. If you want, you can also view the source code of the script to verify it is properly checking our code.

## Importing File Instructions
The instructions for importing quizzes from a file can be found in `ImportExample.txt`. If you would like to see an example of a file formatted properly to be imported, view `TestImport.txt`.

## Javadoc Information
We have included Javadocs alongside our code which can be found in `/doc`. These javadocs include each of our classes, with documentation regarding what each class does and what many of the methods in each class also do. 

This documentation can be very useful for getting an overview of how certain parts of the code (such as the User Interface Menu System) are structured.

# Classes Summary

## Client Classes

### LearningManagementSystemClient

### NetworkManagerClient

### ResponsePacketHandler

### RunnableHandleResponsePacket

### UIManager

## Datastructure Classes

## GUI Classes

## Server Classes

## Packet Classes

### Request Packet Classes

### Response Packet Classes

## UI classes

## Manager Classses
Classes that each handle a vital function of the program.

### Manager
This interface is what blueprints all of other managers used in this application such as UIManager, QuizManager, and UserManager. There are 2 methods in this interface: `init()` and `exit()`. These methods help initialize and save data. 

### UIManager
The manager that is responsible for the User Interface (UI). It uses the User Interface Menu System to create menus that the user then interacts with. In `init()` it creates all of the menus, which sets up the structure of the UI, and then in `run()` it runs the start menu, which is used as the entry point to the rest of the UI.

### UserManager
The manager that is responsible for keeping track of users and providing functionalities such as `addUser()` or `authenticator()`. This manager performs actions that mainly require iterating through all the Users. For example, searching a user by his username or generating a unique id for each user is done through this class.

### QuizManager
The manager that holds the list of every created quiz.  It also provides methods for searching through the quizzes with various filters, such as name, author, etc.  It also is responsible for generating unique ids for new quizzes.

### GradedQuizManager
The manager that holds the the list of all graded quizzes. It also provides a method for searching for graded quizzes by course, and a method to delete all graded quizzes taken by a certain student.

### UserFileManager, QuizFileManager, and GradedQuizFileManager
The managers that read and write user, quiz, and graded quiz data to and from files. Each gives its respective manager the list of users, quizzes, or graded quizzes at the start of the program and receives the list of them at the end. The file paths in `readUsers()`, `readQuizzes()`, and `readGradedQuizzes` as well as `writeUsers()`, `writeQuizzes()`, and `writeGradedQuizzes()` are hard coded and should work as relative paths for the file that stores the desired information.

#### FileWrapper
The class used for reading and writing to file. It contains static methods that are used in any situation where interacting with files is necessary. `readFile()` and `writeFile()` are generic methods that are used for reading and writing files. `readImportFile()` is a special read method used for importing existing quizzes from a file. It is special so that the people creating the quiz on a new file will have an easier time writing it and won't have to write it in a way that makes it easy for the program to read.

## Datastructure Classes
The following are classes used for storing data that don't inherently do anything by themselves.

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

## User Interface Menu System:
Each of the following classes are part of the User Interface Menu System. They all work together to form an abstract menu system that is then used to make the User Interface in UIManager.
Much of the system uses lambdas to pass callback functions to the menus. This allows for defining code during object creation that will run only after certain condition happen.

### Menu
A basic menu. All menus extend this class. Contains an `menu.open()` that is ran to open a menu. Requires all subclasses to implement `menu.runMenu()` to run their respective menu.

### MenuState
An enum representing the state of the Menu. Is used in Menu to determine if the Menu should continue running (`MenuState.RESTART`) or close (`MenuState.CLOSE`).

### InputMenu
A menu that prompts the user to enter input. Once all required input is received, it will run the callback function `onInputFinish(()->{})`.

### OptionMenu
A menu that prompts the user to select an option. When an option is selected, it will run the callback function `onSelect(()->{})` for that respective option. (See MenuOption)

### MenuQuickInput
A smaller input menu that only allows for one input question. The user input from that one question is then stored in a field and is accessible to any method with an instance of this menu.

### InformationMenu
A menu that is solely used for outputting data to the user. It is used for presenting quiz information, question information, and other pieces of information that do not require interaction with the user. Can require the user to press Enter to continue if `requireEnter()` is ran on object creation.

### OptionMenuWithResult<T>
An OptionMenu that contains a result field (of parameterized type T) with accessor and mutator methods for it. Generally when the callback function `onSelect(()->{})` is ran (via OptionMenu running it), it will set the result. Then, once the menu exits, the surrounding code can access the result.
The reason this is desired is because it reduces the lambdas from nesting code too deeply.

### OptionMenuYesNo
An OptionMenuWithResult that is specifically a result of a boolean.
It automatically adds the options Yes and No, and sets the boolean result accordingly. (true for Yes, false for No)
It also adds functions for checking is the result was yes or no.

### OptionListMenu<T>
An OptionMenuWithResult that contains a list of options (of parameterized type T), and uses pages to seperate them out into easier to read options. Once an option in the list is selected, the RunnableSelectListItem.selectItem() callback function is ran with the item that is selected. The user is also given the choice to exit without making a selection.

### MenuInput
A basic input question that prompts the user to enter information. Used by InputMenu

### MenuInputInt
A more specific input question that only allows integer inputs. Extends MenuInput and is used by InputMenu.

### MenuInputOptions
A input that only allows you to select out of a list of specific options. Extends MenuInput and is used by InputMenu.

### MenuOption
A menu option that can be selected. When selected, will run the `onSelect(()->{})` callback function. Used by OptionMenu.

## Runnables
The following classes are used in the User Interface Menu System to define callback functions. They contain one method that is ran whenever the callback function is invoked.

### RunnableCheckCondition
Ran to check if a condition is true. Used by `MenuOption.setVisibilityCondition()`

### RunnableGetListItems
Ran when the `OptionListMenu` is opened to get the items in the menu (since they can be dynamic).

### RunnableInputFinish
Ran with a parameter of the map of the inputs when the inputs are finished being prompted. Used by InputMenu

### RunnableSelectListItem
Ran with a parameter of the selected item whenever a list item is selected in OptionListMenu.

### RunnableSelectOption
Ran whenever an option is selected in OptionMenu

## Utility Classes

### ANSICodes
This class contains ANSI codes that are used for modifying text (like making it bold, underlines, or a different color). Also, it contains some commands for clearing the terminal and moving the cursor. Then, it also contains a utility function for stripping the ANSI codes from a piece of text, which is used for comparing output in test cases.


