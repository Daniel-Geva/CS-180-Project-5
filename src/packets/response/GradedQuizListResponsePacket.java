package packets.response;

import java.util.List;

import datastructures.GradedQuiz;
import datastructures.Quiz;
import datastructures.User;
import server.LearningManagementSystemServer;

/**
 * Gets the requested list of the graded quizzes to be returned to the client
 *
 * @author Issac Fleetwod
 * @version December 9, 2021
 */
public class GradedQuizListResponsePacket extends ResponsePacket {
	
	// Need all 3 lists to cross reference IDs to their respective lists.
	List<GradedQuiz> gradedQuizzes;
	List<User> users;
	List<Quiz> quizzes;

    public GradedQuizListResponsePacket(List<GradedQuiz> gradedQuizzes, LearningManagementSystemServer lms, boolean push) {
        super(true, push);
        this.gradedQuizzes = gradedQuizzes;
        this.users = lms.getUserManager().getUsers();
        this.quizzes = lms.getQuizManager().getQuizList();
    }

	public List<GradedQuiz> getGradedQuizzes() {
		return gradedQuizzes;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Quiz> getQuizzes() {
		return quizzes;
	}


}
