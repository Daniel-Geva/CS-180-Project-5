# Testing
## Test 1: User Login
Steps:
1. User launches application.
2. User enters correct ip into ip text box
3. User selects connect
4. User selects the username textbox.
5. User enters username via the keyboard.
6. User selects the password textbox.
7. User enters password via keyboard
8. User selects the "Log in" button. 

Expected result: Application verifies the user's username and password and loads their homepage automatically. 

Test Status: Passed. 

## Test 2: User Login Unsuccesful
Steps:
1. User launches application
2. User enters correct ip into ip text box
3. User selects connect
4. User selects the username textbox.
5. User enters incorrect username via the keyboard.
6. User selects the password textbox.
7. User enters password via keyboard
8. User selects the "Log in" button. 


Expected result: Application shows an error message for invalid username/password

Test Status: Passed.

## Test 3: Create a new User
1. User launches application
2. User enters correct ip into ip text box
3. User selects connect
4. User selects create user
5. User selects and enters name, username, and password into respective text boxes
6. User selects Teacher for account type in the dropdown
7. User selects Create User
8. User selects the username textbox.
9. User enters incorrect username via the keyboard.
10. User selects the password textbox.
11. User enters password using keyboard
12. User selects the "Log in" button. 

Expected Result: User is succesfully created and logged in

Test Status: Passed

## Test 4: Changing account information

Steps:
1. User launches application.
2. User enters correct ip into ip text box
3. User selects connect
4. User selects the username textbox.
5. User enters username via the keyboard.
6. User selects the password textbox.
7. User enters password via keyboard
8. User selects the "Log in" button. 
9. User selects User Settings
10. User selects text boxes and alters name and username
11. User presses logout button
12. User selects username textbox and enters new username
13. User selects password textbox and enters password
14. User selects "log in"

Expected Result: User is logged in succesfully again and new nane is shown

Test Result: Passed

## Test 5: Creating a quiz
Steps:
1. User launches application
2. User enters correct ip into ip text box
3. User selects connect
4. User selects the username textbox.
5. User enters username for a teacher account via the keyboard.
6. User selects the password textbox.
7. User enters password for a teacher account via keyboard
8. User selects the "Log in" button. 
9. User selects "Create Quiz"
10. User selects course textbox and enters course name
11. User selects Quiz Name textbox and enters quiz name
12. User selects "Create Empty Quiz"
13. User selects "Add New Question"
14. User selects "Multiple Choice" from the dropdown
15. User enters a question into the question textbox
16. User selects "Add new answer"
17. User enters an answer and point value into the respective text boxes using keyboard
18 User selects "Add new answer"
19. User enters an answer and point value into the respective text boxes using keyboard
20. User selects "Add new answer"
21. User enters an answer and point value into the respective text boxes using keyboard
22. User selects "Add new question"
23. User selects "True/False" from the dropdown
24. User enters a question into the question text box
25. User writes two answers in the two answer text boxes
26. User selects "Add new question"
27. User selects "Dropdown" for question type
28. User enters a question into the question text box
29. User selects "Add new answer"
30. User enters an answer and point value into the respective text boxes using keyboard
31. User selects "Add new answer"
32. User enters an answer and point value into the respective text boxes using keyboard
33. User selects "Add new answer"
34. User enters an answer and point value into the respective text boxes using keyboard
35. User clicks save quiz

Expected Result: The quiz is created and viewable by all students and the teacher that created it

Test Result: Passed

## Test 6: Deleting a quiz
1. Run test case 5
2. Select Modify Quiz
3. Select the created quiz
4. Select "Delete Quiz"

Expected Result: The quiz is no longer viewable by any client

Test Result: Passed

## Test 7: Modify quiz
1. Run test case 5
2. Select modify quiz
3. Select the created quiz
4. Select modify quiz
5. Change the question
6. Press "Save quiz"
7. Press "Take Quiz"

Expected Result: Question reflects modification made

Test Result: Passed

## Test 8: Take a quiz
Steps:
1. User launches application.
2. User enters correct ip into ip text box
3. User selects connect
4. User selects the username textbox.
5. User enters student username via the keyboard.
6. User selects the password textbox.
7. User enters student password via keyboard
8. User selects the "Log in" button. 
9. User selects "Take quiz"
10. Select a quiz
11. Press "Take Quiz"
12. Click an answer for all questions
13. Press "Submit quiz"
14. Press "Yes" to see quiz grade

Expected Result: Student is able to see quiz grade, and teachers also have access to their submission

Test Result: passed

## Test 9: Concurrent Users
1. User 1 launches application.
2. User 1 enters correct ip into ip text box
3. User 1 selects connect
4. User 1 selects the username textbox.
5. User 1 enters teacher username via the keyboard.
6. User 1 selects the password textbox.
7. User 1 enters teacher password via keyboard
8. User 1 selects the "Log in" button. 
9. User 2 launches application.
10. User 2 enters correct ip into ip text box
11. User 2 selects connect
12. User 2 selects the username textbox.
13. User 2 enters student username via the keyboard.
14. User 2 selects the password textbox.
15. User 2 enters student password via keyboard
16. User 2 selects the "Log in" button. 
17. User 1 selects "Create Quiz"
18. User 1 selects course textbox and enters course name
19. User 1 selects Quiz Name textbox and enters quiz name
20. User 1 selects "Create Empty Quiz"
21. User 1 selects "Add New Question"
22. User 1 selects "Multiple Choice" from the dropdown
23. User 1 enters a question into the question textbox
24. User 1 selects "Add new answer"
25. User 1 enters an answer and point value into the respective text boxes using keyboard
26  User 1 selects "Add new answer"
27. User 1 enters an answer and point value into the respective text boxes using keyboard
28. User 1 selects "Add new answer"
29. User 1 enters an answer and point value into the respective text boxes using keyboard
30. User 1 selects "Add new question"
31. User 1 selects "True/False" from the dropdown
32. User 1 enters a question into the question text box
33. User 1 writes two answers in the two answer text boxes
34. User 1 selects "Add new question"
35. User 1 selects "Dropdown" for question type
36. User 1 enters a question into the question text box
37. User 1 selects "Add new answer"
38. User 1 enters an answer and point value into the respective text boxes using keyboard
39. User 1 selects "Add new answer"
40. User 1 enters an answer and point value into the respective text boxes using keyboard
41. User 1 selects "Add new answer"
42. User 1 enters an answer and point value into the respective text boxes using keyboard
43. User 1 clicks save quiz

Expected Result: The new quiz instantly appears to player 2 and is available to take

Test Result: Passed
