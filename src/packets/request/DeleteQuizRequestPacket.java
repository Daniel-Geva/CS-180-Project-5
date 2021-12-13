package packets.request;

import datastructures.*;
import packets.response.DeleteQuizResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

import java.util.ArrayList;

/**
 * Deletes a quiz and associated graded quizzes, specified by quiz id
 *
 * @author Liam Kelly
 *
 * @version December 12, 2021
 *
 **/

public class DeleteQuizRequestPacket extends RequestPacket{
    int id;

    public DeleteQuizRequestPacket(int id) {
        this.id = id;
    }

    /** Removes a quiz and all associated graded quizzes
     * Also creates a response packet and returns it
     *
     * @param lms
     * @return ResponsePacket - packet containing success, push, and id variables
     */
    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        if (lms.getQuizManager().searchQuizByID(id) != null) {
            lms.getQuizManager().removeQuiz(id);
            ArrayList<GradedQuiz> gradedQuizList = lms.getGradedQuizManager().getGradedQuizList();
            for (int i = 0; i < gradedQuizList.size(); i++) {
                if (id == gradedQuizList.get(i).getQuizID()) {
                    gradedQuizList.remove(i);
                    i--;
                }
            }
            lms.getGradedQuizManager().setGradedQuiz(gradedQuizList);
            
            lms.getGradedQuizFileManager().save();
            lms.getQuizFileManager().save();
            return new DeleteQuizResponsePacket(true, id);
        } else {
            return new DeleteQuizResponsePacket(false, id);
        }
    }
}
