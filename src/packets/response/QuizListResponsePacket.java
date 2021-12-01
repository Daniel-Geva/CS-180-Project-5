package packets.response;

import server.LearningManagementSystemServer;
import datastructures.Quiz;
import java.util.ArrayList;

public class QuizListResponsePacket extends ResponsePacket {
    ArrayList<Quiz> quizzesToReturn = new ArrayList<Quiz>();
    String searchTerm;
    public QuizListResponsePacket(LearningManagementSystemServer lms,  String requestType, String searchTerm) {
        this.searchTerm = searchTerm;
        switch (requestType) {
            case "All":
                quizzesToReturn = lms.getQuizManager().getQuizList();
                break;
            case "Author":
                quizzesToReturn = lms.getQuizManager().searchQuizByAuthor(searchTerm);
                break;
            case "Name":
                quizzesToReturn = lms.getQuizManager().searchQuizByName(searchTerm);
                break;
            case "Course":
                quizzesToReturn = lms.getQuizManager().searchQuizByCourse(searchTerm);
                break;
            default:
                quizzesToReturn = null;
        }
    }

    public ArrayList<Quiz> getListOfQuizzesResponse() {
        return quizzesToReturn;
    }



}
