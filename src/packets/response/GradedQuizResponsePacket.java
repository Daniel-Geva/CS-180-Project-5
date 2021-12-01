package packets.response;

import datastructures.GradedQuiz;
import datastructures.Quiz;
import server.LearningManagementSystemServer;

import java.io.Serializable;

public class GradedQuizResponsePacket implements Serializable {
    GradedQuiz gradedQuiz;

    public GradedQuizResponsePacket(LearningManagementSystemServer lms, int id) {
        //TODO
    }

    public GradedQuiz getQuizResponse() {
        return gradedQuiz;
    }
}
