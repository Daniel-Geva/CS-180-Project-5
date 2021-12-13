package server;

import java.util.ArrayList;

import datastructures.Answer;
import datastructures.Manager;
import datastructures.Question;
import datastructures.Quiz;

/**
 * Reads and writes files with quiz information for initialization and storage
 * <p>
 * Contains an ArrayList of quizzes from storage that will be passed to QuizManager.
 * <p>
 *
 * @author Daniel Geva
 * @version 11/14/21
 * @see QuizManager
 * @see FileWrapper
 */
public class QuizFileManager implements Manager {

    LearningManagementSystemServer lms;
    private ArrayList<Quiz> quizzes;

    public QuizFileManager(LearningManagementSystemServer lms) {
        this.lms = lms;
        this.quizzes = this.readQuizzes();
    }

    /**
     * Sets the QuizManager's arraylist of quizzes after reading the saved data
     */
    @Override
    public void init() {
        lms.getQuizManager().setQuizList(quizzes);
    }

    @Override
    public void exit() {
        this.save();
    }

    /**
     * Gets the altered list of quizzes and writes them to a file
     */
    public synchronized void save() {
        quizzes = lms.getQuizManager().getQuizList();
        this.writeQuizzes();
    }

    /**
     * Reads the file that stores the quiz data and constructs an arraylist of quizzes out of it
     *
     * @return tempQuizzes - ArrayList of Quizzes to be passed to QuizManager
     */
    public ArrayList<Quiz> readQuizzes() {
        ArrayList<Quiz> tempQuizzes = new ArrayList<>();
        String path = "./data/quizzes.txt";
        ArrayList<String> contents = FileWrapper.readFile(path);

        if (contents == null) {
            return tempQuizzes;
        }

        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).isEmpty() || contents.get(i).isBlank()) {
                continue;
            }
            String[] components = contents.get(i).split(";;", 8);
            //Two ";;" (semicolons) are used to separate the parts of a quiz
            String name = components[0];
            String author = components[1];
            ArrayList<Question> questions = this.readQuestions(components[2]);
            //format the list of questions
            int id = Integer.parseInt(components[3]);
            boolean scrambled = Boolean.parseBoolean(components[4]);
            String course = components[5];
            tempQuizzes.add(new Quiz(name, author, id, questions, scrambled, course));
        }
        return tempQuizzes;
    }

    /**
     * Used to create the arraylist of questions that a quiz takes in its constructor
     *
     * @param questionList - String containing all the information required to construct a question
     *
     * @return questions - ArrayList of Questions containing the questions for the quiz
     */
    public ArrayList<Question> readQuestions(String questionList) {
        ArrayList<Question> questions = new ArrayList<>();

        if (questionList.isEmpty() || questionList.isBlank()) {
            return questions;
        }

        String[] list = questionList.split("::", -1);
        //Two "::" (colons) are used to separate the different questions in a quiz

        for (int i = 0; i < list.length; i++) {
            String[] questionParts = list[i].split("//", 4);
            //Two "//" (forward slashes) are used to separate the answers from the question asked
            ArrayList<Answer> answers = this.readAnswers(questionParts[0]);
            String question = questionParts[1];
            int id = Integer.parseInt(questionParts[2]);
            String questionType = questionParts[3];
            questions.add(new Question(answers, question, id, questionType));
        }

        return questions;
    }

    /**
     * Used to create the arraylist of answers that a question takes in its constructor
     *
     * @param answerList - String containing all the information required to construct the answers to a question
     *
     * @return answers - ArrayList of Answers containing the answers for the question
     */
    public ArrayList<Answer> readAnswers(String answerList) {
        ArrayList<Answer> answers = new ArrayList<>();

        if (answerList.isBlank() || answerList.isEmpty()) {
            return answers;
        }

        String[] list = answerList.split("--");
        //Two "--" (dashes) are used to separate the answers from each other

        for (int i = 0; i < list.length; i++) {
            String[] answerParts = list[i].split("__", 4);
            //Two "__" (underscores) are used to separate the parts of an answer
            String answer = answerParts[0];
            boolean correct = Boolean.parseBoolean(answerParts[1]);
            int points = Integer.parseInt(answerParts[2]);
            int id = Integer.parseInt(answerParts[3]);
            answers.add(new Answer(answer, correct, points, id));
        }
        return answers;
    }

    /**
     * Writes the arraylist of quizzes "quizzes" to a file for storage
     *
     * @return success - whether the writing succeeded
     */
    private boolean writeQuizzes() {
        ArrayList<String> writableQuizzes = new ArrayList<>();
        String path = "./data/quizzes.txt";

        for (int i = 0; i < quizzes.size(); i++) {
            String name = quizzes.get(i).getName();
            String author = quizzes.get(i).getAuthor();
            String questions = formatQuestions(quizzes.get(i).getQuestions());
            int id = quizzes.get(i).getId();
            boolean scrambled = quizzes.get(i).isScrambled();
            String course = quizzes.get(i).getCourse();
            String addon = String.format("%s;;%s;;%s;;%s;;%s;;%s", name, author, questions, id, scrambled, course);
            //formats the quiz to be written
            writableQuizzes.add(addon);
            //add a string that is formatted for storage to the arraylist that will be written to the file
        }
        return FileWrapper.writeFile(path, writableQuizzes);
    }

    /**
     * Used to format the arraylist of questions to be written
     *
     * @param questions - An ArrayList of Questions from the Quiz
     *
     * @return retVal - A String containing all the information
     * from the questions and answers in a format to be written to a file
     *
     */
    public String formatQuestions(ArrayList<Question> questions) {
        String retVal = "";
        for (int i = 0; i < questions.size(); i++) {
            String answers = formatAnswers(questions.get(i).getAnswers());
            String question = questions.get(i).getQuestion();
            String id = Integer.toString(questions.get(i).getId());
            String questionType = questions.get(i).getQuestionType();
            if (i == questions.size() - 1) { //format for final question
                retVal += String.format("%s//%s//%s//%s", answers, question, id, questionType);
            } else { //format with characters to separate questions
                retVal += String.format("%s//%s//%s//%s::", answers, question, id, questionType);
            }
        }
        return retVal;
    }

    /**
     * Used to format the arraylist of answers to be written
     *
     * @param answers - ArrayList of answers form the questions
     *
     * @return retVal - A String of all the information from the answers formatted to be written to a file
     */
    public String formatAnswers(ArrayList<Answer> answers) {
        String retVal = "";
        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i).getAnswer();
            boolean correct = answers.get(i).isCorrect();
            int points = answers.get(i).getPoints();
            String id = Integer.toString(answers.get(i).getId());
            if (i == answers.size() - 1) { //format for final answer
                retVal += String.format("%s__%s__%s__%s", answer, correct, points, id);
            } else { //format with characters to separate answers
                retVal += String.format("%s__%s__%s__%s--", answer, correct, points, id);
            }
        }
        return retVal;
    }

    /**
     * Directly sets the value of quizzes, the ArrayList of quizzes. Used for getting
     * an updated version of the quizzes when saving information
     *
     * @param quizzes - An ArrayList of quizzes
     */
    public void setQuizzes(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

}
