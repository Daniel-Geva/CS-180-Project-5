package packets.request;

import java.util.ArrayList;
import java.util.List;

import datastructures.GradedQuiz;
import datastructures.User;
import packets.response.GradedQuizListResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

/**
 * Request packet for a list of graded quizzes
 *
 * @author Issac Fleetwood
 * @version December 9, 2021
 */
public class GradedQuizListRequestPacket extends RequestPacket {
	
	User user;
	
	public GradedQuizListRequestPacket(User user) {
		this.user = user;
	}
	
	public GradedQuizListRequestPacket() {
		this.user = null;
	}

	@Override
	public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
		List<GradedQuiz> list = new ArrayList<>();
		if (user == null)
			list = lms.getGradedQuizManager().getGradedQuizList();
		else 
			list = lms
				.getGradedQuizManager()
				.getGradedQuizList()
				.stream()
				.filter((GradedQuiz quiz) -> (
					quiz.getStudentID() == user.getID()
				))
				.toList();
		
		return new GradedQuizListResponsePacket(list, lms, false);
	}
	
}
