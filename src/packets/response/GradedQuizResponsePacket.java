package packets.response;

import datastructures.GradedQuiz;
import server.LearningManagementSystemServer;

import java.io.Serializable;

public class GradedQuizResponsePacket implements Serializable {
    GradedQuiz gradedQuiz;

    public GradedQuizResponsePacket(LearningManagementSystemServer lms, int id) {
        // if gradedQuiz is null the entered id is not valid
        gradedQuiz = lms.getGradedQuizManager().searchGradedQuizByID(id);
    }

    public GradedQuiz getQuizResponse() {
        return gradedQuiz;
    }
}
