package server;

import java.util.ArrayList;

import datastructures.GradedQuiz;
import datastructures.Manager;

/**
 * Manager for the Graded Quizzes
 *
 * @author Sean Lee
 * @version 11/14/21
 * @see Manager
 */
public class GradedQuizManager implements Manager {

    private LearningManagementSystemServer lms;
    private ArrayList<GradedQuiz> gradedQuizList = new ArrayList<>();
    private static Object obj = new Object();

    public GradedQuizManager(LearningManagementSystemServer lms) {
        this.lms = lms;
    }

    /**
     * Initializes information when the program starts
     * Empty because no information needs to be initialized
     */
    public void init() {

    }

    /**
     * Saves information for long-term as program closes
     * Empty because this class does not need to do that
     */
    public void exit() {

    }

    /**
     * Adds a graded quiz to the list of graded quizzes
     * @param gradedQuiz The graded quiz that needs to be added
     */
    public void addGradedQuiz(GradedQuiz gradedQuiz) {
        synchronized (obj) {
            gradedQuizList.add(gradedQuiz);
        }
    }

    public void deleteAllByStudentID(int studentID) {
        for (int i = gradedQuizList.size() - 1; i >= 0; i--) {  // iterates backwards to prevent
            // array out of bounds exception
            synchronized (obj) {
                if (gradedQuizList.get(i).getStudentID() == studentID) {
                    gradedQuizList.remove(gradedQuizList.get(i));
                }
            }
        }
    }

    /**
     * Removes a graded quiz from the list of graded quiz
     * @param id The ID of the Graded Quiz that is to be removed
     */
    public void removeQuiz(int id) {
        synchronized (obj) {
            int startingListLength = gradedQuizList.size();
            for (int i = 0; i < gradedQuizList.size(); i++) {
                if (gradedQuizList.get(i).getID().equals(Integer.toString(id))) {
                    gradedQuizList.remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * Sets the graded quiz list with input
     * @param inputGradedQuizList The list of graded quizzes
     */
    public void setGradedQuiz(ArrayList<GradedQuiz> inputGradedQuizList) {
        synchronized (obj) {
            this.gradedQuizList = inputGradedQuizList;
        }
    }

    /**
     * Lists the list of graded quizzes
     * @return List of graded quizzes
     */
    public String listGradedQuizzes() {
        StringBuilder quizDescriptions = new StringBuilder();
        synchronized (obj) {
            for (GradedQuiz q : gradedQuizList) {
                quizDescriptions.append(q.toString()).append("\n");
            }
            if (gradedQuizList.size() == 0) {
                quizDescriptions.append("No quizzes have been created");
            }
            return quizDescriptions.toString();
        }
    }

    /**
     * Searches the list of the quizzes by course
     * @param course Course to search by
     * @return List of quizzes that matches the inputted course
     */
    public ArrayList<GradedQuiz> searchGradedQuizzesByCourse(String course) {
        ArrayList<GradedQuiz> matchingQuizzes = new ArrayList<>();
        synchronized (obj) {
            for (GradedQuiz gradedQuiz : gradedQuizList) {
                if (lms.getQuizManager().searchQuizByID(Integer.getInteger(
                        gradedQuiz.getID().substring(1))).getCourse().equals(course)) {
                    matchingQuizzes.add(gradedQuiz);
                }
            }
            return matchingQuizzes;
        }
    }

    /**
     * Returns the list of graded quizzes
     * @return ArrayList of graded quizzes
     */
    public ArrayList<GradedQuiz> getGradedQuizList() {
        synchronized (obj) {
            return gradedQuizList;
        }
    }
}
