package datastructures;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Stores one Question, including multiple answers
 * <p>
 * Contains an ArrayList of answers, along with the string of the question to be asked.
 * <p>
 *
 *
 * @author Liam Kelly
 * @version 11/14/21
 * @see Quiz
 */
public class Question implements Listable, Serializable {
	
	ArrayList<Answer> answers;
    String question;
    int id;
    String questionType;
    //private Object lockQuestionType = new Object();
    //private Object lockId = new Object();
    //private Object lockQuestion = new Object();
    //private Object lockAnswer = new Object();

    public Question(ArrayList<Answer> answers, String question, int id, String questionType) {
        this.answers = answers;
        this.question = question;
        this.id = id;
        this.questionType = questionType;
    }
    /**
     * Returns a unique id for an answer
     *
     * @return max - a new unique id
     */
    public int generateUniqueAnswerId() {
        //synchronized (lockId) {
            int max = 0;
            for (Answer a : answers) {
                if (a.getId() > max) {
                    max = a.getId();
                }
            }
            return max + 1;
        //}
    }
    /**
     * Returns the question string, with a limit of 20 characters
     *
     * @return retVal - question string, capped at 20 characters
     */
    public String getListName() {
        String retVal = question;
        if (retVal.length() > 50) {
            retVal = retVal.substring(0, 50);
        }
        return retVal;
    }
    /**
     * Returns the list of answers with the highest point value
     *
     * @return bestAnswers - the answers with point values matching the highest provided point values
     */
    public ArrayList<Answer> getBestAnswers() {
        int bestAnswerPointValue = 0;
        ArrayList<Answer> bestAnswers = new ArrayList<Answer>();
        for (Answer a: answers) {
            if (a.getPoints() > bestAnswerPointValue) {
                bestAnswerPointValue = a.getPoints();
            }
        }
        for (Answer a: answers) {
            if (a.getPoints() == bestAnswerPointValue) {
                bestAnswers.add(a);
            }
        }
        return bestAnswers;
    }
    /**
     * Returns the list of answers
     *
     * @return answers - the list of answers to this question
     */
    public ArrayList<Answer> getAnswers() {
            return answers;
    }
    /**
     * Returns the String representation of the question
     *
     * @return question - The question string
     */
    public String getQuestion() {
            return question;
    }
    /**
     * Changes the quiz ID to a new ID
     *
     * @param id - the new unique id for the quiz
     */
    public void setId(int id) {
        //synchronized (lockId) {
            this.id = id;
        //}
    }
    /**
     * Returns the unique quiz id
     *
     * @return id - the unique id for the quiz
     */
    public int getId() {
            return id;
    }
    /**
     * Returns the question type
     *
     * @return questionType - the type of question
     */
    public String getQuestionType() {
            return questionType;
    }
    /**
     * Returns the question type
     *
     * @param questionType - the type of question
     */
    public void setQuestionType(String questionType) {
        //synchronized (lockQuestionType) {
            this.questionType = questionType;
        //}
    }
    /**
     * Returns the question as it will be displayed to the user
     *
     * @return s - A string of the question, followed by the list of each possible answer
     */
    public String toString() {
        String s = question;
        for (Answer answer : answers) {
            s += answer.toString() + "\n";
        }
        return s;
    }
    
    // TODO Comment
    public void setQuestion(String question) {
    	this.question = question;
    }
    
}
