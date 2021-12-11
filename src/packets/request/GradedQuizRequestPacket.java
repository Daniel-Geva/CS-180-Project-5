package packets.request;
import java.io.Serializable;

import datastructures.GradedQuiz;
import datastructures.Quiz;
import datastructures.User;
import packets.response.GradedQuizResponsePacket;
import server.LearningManagementSystemServer;

/**
 * Graded Quiz Request Packet
 *
 * Contains the method that handles a handle request by the server.
 *
 * @author Sean Lee
 * @version 12/10/21
 * @see GradedQuizResponsePacket
 * @see datastructures.GradedQuiz
 */
public class GradedQuizRequestPacket extends RequestPacket implements Serializable {
    
	GradedQuiz gradedQuiz;
	String id;

    /**
     * Constructor for when the user creates a new Graded Quiz or changes an existing Graded Quiz
     * @param gradedQuiz
     */
    public GradedQuizRequestPacket(GradedQuiz gradedQuiz) {
        this.gradedQuiz = gradedQuiz;
        this.id = gradedQuiz.getID();
    }

    /**
     * Constructor for when client is requesting a specific Graded Quiz
     * @param id
     */
    public GradedQuizRequestPacket(String id) {
        this.id = id;
        gradedQuiz = null;
    }

    /**
     * Handles the request packet based on client needs
     * @param lms
     * @return
     */
    @Override
    public GradedQuizResponsePacket serverHandle(LearningManagementSystemServer lms) {
        // if the Graded Quiz is null the server needs to send the client the Graded Quiz it is looking for
        if (gradedQuiz == null) {
            gradedQuiz = lms.getGradedQuizManager().searchGradedQuizByID(id);
            return new GradedQuizResponsePacket(false, gradedQuiz);
        } else {
            // if the Graded Quiz exists the new information is replaced
            if (lms.getGradedQuizManager().searchGradedQuizByID(id) != null) {
                lms.getGradedQuizManager().removeQuiz(id);
                lms.getGradedQuizManager().addGradedQuiz(gradedQuiz);
            } else {
                // if the Graded Quiz doesn't exist a new Graded Quiz is added to the LMS
                lms.getGradedQuizManager().addGradedQuiz(gradedQuiz);
            }
            lms.getGradedQuizFileManager().save();
            // client needs to figure out if its a new Graded Quiz or preexisting Graded Quiz that needs to be updated
            return new GradedQuizResponsePacket(true, gradedQuiz);
        }
    }
}
