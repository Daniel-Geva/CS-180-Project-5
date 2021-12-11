package datastructures;

import java.io.Serializable;
import java.util.HashMap;

import server.GradedQuizManager;

/**
 *
 * Graded Quiz that contains the information of a quiz that has been graded
 *
 * @author Sean Lee
 * @version 11/14/21
 * @see GradedQuizManager
 */
public class GradedQuiz implements Serializable { // should students be able to take quiz multiple times

	private String submissionTime;
    private int quizID;
    private int studentID;
    private HashMap<Integer, Integer> map = new HashMap<>();
    private static Object obj = new Object();

    public GradedQuiz(int quizID, int studentID) {
        this.quizID = quizID;
        this.studentID = studentID;
    }

    public GradedQuiz(int quizID, int studentID, HashMap<Integer, Integer> map, String submissionTime) {
        this.quizID = quizID;
        this.studentID = studentID;
        this.map = map;
        this.submissionTime = submissionTime;
    }

    /**
     * @return Hash Map of Graded Quizzes
     */
    public HashMap<Integer, Integer> getGradedQuizMap() {
        synchronized (map) {
            return this.map;
        }
    }

    /**
     * Returns the ID of the quiz that got graded
     * @return Quiz ID
     */
    public int getQuizID() {
        synchronized (obj) {
            return this.quizID;
        }
    }

    /**
    * Returns the ID of the student that took the quiz
    * @return Student ID
     */
    public int getStudentID() {
        synchronized (obj) {
            return this.studentID;
        }
    }

    /**
     * Adds a question to the Hash Map of questions and answers
     * @param question Question that will be added
     * @param answer Answer that will be added
     */
    public void addQuestion(Question question, Answer answer) {
        synchronized (map) {
            map.put(question.getId(), answer.getId());
        }
    }

    /**
     * Returns string of ID of GradedQuiz
     * @return String of Graded Quiz ID
     */
    public String getID() {
        synchronized (obj) {
            return String.format("G%d", quizID);
        }
    }

    /**
     * Adds a question to the Hash Map of questions and answers
     * @param questionID ID of question that will be added to hash map
     * @param answerID ID of answer that will be added to hash map
     */
    public void addQuestion(int questionID, int answerID) {
        synchronized (map) {
            map.put(questionID, answerID);
        }
    }

    /**
     * Returns submission time of quiz
     * @return Submission time of quiz
     */
    public String getSubmissionTime() {
        synchronized (obj) {
            return submissionTime;
        }
    }

    /**
     * Sets the submission time of quiz
     * @param submissionTime Submission time of quiz
     */
    public void setSubmissionTime(String submissionTime) {
        synchronized (obj) {
            this.submissionTime = submissionTime;
        }
    }

    public String getScore(Quiz quiz) {
        int earnedPoints = 0;
        int possiblePoints = 0;
        for (Question question: quiz.getQuestions()) {
            int chosenAnswerId = map.get(question.getId());
            Answer chosen = null;
            Answer best = question.getAnswers().get(0);
            for (Answer answer : question.getAnswers()) {
                if (answer.getId() == chosenAnswerId) {
                    chosen = answer;
                }
                if (answer.getPoints() > best.getPoints()) {
                    best = answer;
                }
            }
        }
        return String.format("%s/%s", earnedPoints, possiblePoints);
    }
}
