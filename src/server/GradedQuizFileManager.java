package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import datastructures.GradedQuiz;
import datastructures.Manager;

/**
 * Reads and writes files with graded quiz information for initialization and storage
 * <p>
 * Contains an ArrayList of graded quizzes from storage that will be passed to GradedQuizManager.
 * <p>
 *
 * @author Daniel Geva
 * @version 11/14/21
 * @see GradedQuizManager
 * @see FileWrapper
 */
public class GradedQuizFileManager implements Manager {

    LearningManagementSystemServer lms;
    private ArrayList<GradedQuiz> gradedQuizzes;

    private static Object writeLock = new Object();

    public GradedQuizFileManager(LearningManagementSystemServer lms) {
        this.lms = lms;
        this.gradedQuizzes = this.readGradedQuizzes();
    }

    ///Sets the GradedQuizManger's arraylist of graded quizzes
    @Override
    public void init() {
        lms.getGradedQuizManager().setGradedQuiz(gradedQuizzes);
    }

    @Override
    public void exit() {
        this.save();
    }

    ///gets the altered arraylist of graded quizzes from GradedQuizManger and writes it to a file
    public synchronized void save() {
        gradedQuizzes = lms.getGradedQuizManager().getGradedQuizList();
        this.writeGradedQuizzes();
    }

    ///Reads the file that stores the graded quiz data and creates an arraylist of graded quizzes
    public ArrayList<GradedQuiz> readGradedQuizzes() {
        ArrayList<GradedQuiz> tempGradQuiz = new ArrayList<>();
        String path = "./data/gradedQuizzes.txt";
        ArrayList<String> contents = FileWrapper.readFile(path);

        if (contents == null) {
            return tempGradQuiz;
        }

        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).isBlank() || contents.get(i).isEmpty()) {
                continue;
            }
            String[] list = contents.get(i).split(";;", 4);
            //Two ";;" semicolons are used to separate the parts of a graded quiz
            int quizID = Integer.parseInt(list[0]);
            int studentID = Integer.parseInt(list[1]);
            HashMap<Integer, Integer> map = createHashmap(list[2]); //handles creating the Hashmap
            String submissionTime = list[3];
            tempGradQuiz.add(new GradedQuiz(quizID, studentID, map, submissionTime));
        }
        return tempGradQuiz;
    }

    ///Used to create the Hashmap
    public HashMap<Integer, Integer> createHashmap(String contents) {
        HashMap<Integer, Integer> map = new HashMap<>();
        String[] list = contents.split("//", -1);
        //Two "//" forward slashes are used to separate the key/value pairs from each other

        for (int i = 0; i < list.length; i++) {
            String[] parts = list[i].split(",,", 2); //Two ",," commas are used to separate the key and value
            Integer questionId = Integer.parseInt(parts[0]);
            Integer answerId = Integer.parseInt(parts[1]);
            map.put(questionId, answerId);
        }
        return map;
    }

    ///Writes the arraylist of graded quizzes to a file for storage
    public boolean writeGradedQuizzes() {
        ArrayList<String> writableGradedQuizzes = new ArrayList<>();
        String path = "./data/gradedQuizzes.txt";

        synchronized (writeLock) {
            for (int i = 0; i < gradedQuizzes.size(); i++) {
                int quizId = gradedQuizzes.get(i).getQuizID();
                int studentId = gradedQuizzes.get(i).getStudentID();
                String mapList = this.formatHashmap(gradedQuizzes.get(i).getGradedQuizMap());
                String submissionTime = gradedQuizzes.get(i).getSubmissionTime();
                writableGradedQuizzes.add(String.format("%d;;%d;;%s;;%s", quizId, studentId, mapList, submissionTime));
                //formats the graded quiz to written and adds it to the arraylist of strings to written
            }

            return FileWrapper.writeFile(path, writableGradedQuizzes);
        }
    }

    ///Used to format the Hashmap to be written
    public String formatHashmap(HashMap<Integer, Integer> map) {
        StringJoiner joiner = new StringJoiner("//");

        for (Integer key : map.keySet()) {
            String question = Integer.toString(key);
            String answer = Integer.toString(map.get(key));
            joiner.add(String.format("%s,,%s", question, answer));
        }
        return joiner.toString();
    }

}
