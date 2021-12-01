package packets.response;

import server.LearningManagementSystemServer;
import datastructures.Quiz;

public class QuizResponsePacket extends ResponsePacket{
    Quiz quizToReturn;

    public QuizResponsePacket(LearningManagementSystemServer lms, int id) {
        quizToReturn = lms.getQuizManager().searchQuizByID(id);
    }

    public Quiz getQuizResponse() {
        return quizToReturn;
    }

}
