package packets.request;

import datastructures.*;
import packets.response.DeleteQuizResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

import java.util.ArrayList;

public class DeleteQuizRequestPacket extends RequestPacket{
    int id;

    public DeleteQuizRequestPacket(int id) {
        this.id = id;
    }


    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        if (lms.getQuizManager().searchQuizByID(id) != null) {
            lms.getQuizManager().removeQuiz(id);
            ArrayList<GradedQuiz> gradedQuizList = lms.getGradedQuizManager().getGradedQuizList();
            for (int i = 0; i < gradedQuizList.size(); i++) {
                if (id == gradedQuizList.get(i).getQuizID()) {
                    gradedQuizList.remove(i);
                }
            }
            lms.getGradedQuizManager().setGradedQuiz(gradedQuizList);
            return new DeleteQuizResponsePacket(true);
        } else {
            return new DeleteQuizResponsePacket(false);
        }
    }
}
