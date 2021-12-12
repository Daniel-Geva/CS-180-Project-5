package client;

import datastructures.Answer;
import datastructures.Question;
import datastructures.Quiz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class for importing a quiz from a file
 * <p>
 * Contains methods for reading a file and extracting the information as well as creating a quiz object with the information
 * <p>
 *
 *
 * @author Daniel Geva
 * @version 11/14/21
 */

public class ClientFileWrapper {

    ///A version of readFile that is specific for importing quizzes in a file
    public static ArrayList<String> readImportFile(File f) {
        ArrayList<String> contents = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                String readLine = "";
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("/") && !readLine.isBlank()) {
                        //if the line starts with a "/" (forward slash) and the buffer (readLine) is not empty,
                        // the buffer is added to the arraylist and reset to the line
                        contents.add(readLine);
                        readLine = line.substring(line.indexOf("/") + 1) + "\n";
                    } else if (!line.startsWith("#")) {
                        //ignores any lines starting with "#" (pound), adds a read line to the buffer
                        if (line.startsWith("/")) {
                            readLine += line.substring(line.indexOf("/") + 1) + "\n";
                        } else {
                            readLine += line + "\n";
                        }
                    }
                }
                if (!readLine.isBlank()) {
                    contents.add(readLine);
                }
            } catch (FileNotFoundException | NullPointerException e) {
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        return contents;
    }

    ///Creates and returns a quiz object generated from a file, with some values provided by UI
    public static Quiz importQuiz(LearningManagementSystemClient lmsc, File f, String name, String course) {
        String user = lmsc.getUIManager().getCurrentUser().getName();
        int quizId = 0;

        ArrayList<String> contents = readImportFile(f);

        if (contents == null) {
            return null;
        }

        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < contents.size(); i++) {
            String[] quizParts = contents.get(i).split("\n", -1);
            //A "\n" newline separates question from answers
            String[] questionParts = quizParts[0].split("/", 2);
            //A "/" forward slash separates the question form the type of question
            String question = questionParts[0];
            String questionType = questionParts[1];
            int questionId = i;
            ArrayList<Answer> answers = new ArrayList<>();
            for (int j = 1; j < quizParts.length - 1; j++) {
                String[] answerParts = quizParts[j].split(";;", 3);
                //Two ";;" semicolons separate the parts of the answer
                String answer = answerParts[0];
                boolean isCorrect = Boolean.parseBoolean(answerParts[1]);
                int numPoints = Integer.parseInt(answerParts[2]);
                int answerId = j - 1;
                answers.add(new Answer(answer, isCorrect, numPoints, answerId));
            }
            questions.add(new Question(answers, question, questionId, questionType));
        }

        Quiz quiz = new Quiz(name, user, quizId, questions, false, course);

        return quiz;
    }

}
