package packets.response;

import server.LearningManagementSystemServer;

public class QuizResponsePacket extends ResponsePacket{
    LearningManagementSystemServer lms;
    Quiz quizToReturn;

    public QuizResponsePacket(LearningManagementSystemServer lms, int id) {
        this.lms = lms;
        quizToReturn = lms.getQuizManager().searchQuizByID(id);
    }

    public Quiz getQuizResponse() {
        return quizToReturn;
    }

}
