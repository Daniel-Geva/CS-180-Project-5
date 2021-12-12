package client;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import client.NetworkManagerClient.NameSetter;
import datastructures.Answer;
import datastructures.GradedQuiz;
import datastructures.Manager;
import datastructures.PushPacketHandler;
import datastructures.Question;
import datastructures.Quiz;
import datastructures.Student;
import datastructures.Teacher;
import datastructures.User;
import gui.Aesthetics;
import gui.Button;
import gui.Dropdown;
import gui.GapComponent;
import gui.Heading;
import gui.Label;
import gui.Panel;
import gui.RadioButton;
import gui.TextField;
import packets.request.CreateUserRequestPacket;
import packets.request.DeleteQuizRequestPacket;
import packets.request.GradedQuizListRequestPacket;
import packets.request.GradedQuizRequestPacket;
import packets.request.LoginUserRequestPacket;
import packets.request.QuizListRequestPacket;
import packets.request.QuizRequestPacket;
import packets.request.UpdateUserRequestPacket;
import packets.response.DeleteQuizResponsePacket;
import packets.response.GradedQuizListResponsePacket;
import packets.response.GradedQuizResponsePacket;
import packets.response.NewUserResponsePacket;
import packets.response.QuizListResponsePacket;
import packets.response.QuizResponsePacket;
import packets.response.ResponsePacket;

/**
 *
 *
 * @author Aryan Jain
 * @author Isaac Fleetwood
 * @version 1.0.0
 *
 *
 */
public class UIManager implements Manager {

	LearningManagementSystemClient lms;
	
	User currentUser = new Teacher(49100, "Person 21", "username", "passa");
	
	public UIManager(LearningManagementSystemClient lms) {
		this.lms = lms;
	}

	Panel hostnamePanel;
	Panel connectionStatusPanel;
	Panel loginPanel;
	Panel mainPanel;
	Panel mainTabPanel;
	
	private List<String> getCourses(List<Quiz> quizzes) {
		ArrayList<String> courses = new ArrayList<String>();
		for(Quiz q: quizzes) {
			if(!courses.contains(q.getCourse()))
				courses.add(q.getCourse());
		}
		return courses;
	}
	
	private List<Quiz> getQuizzesFromCourse(List<Quiz> quizzes, String course) {
		return quizzes.stream().filter((Quiz q) -> (
			q.getCourse().equals(course)
		)).toList();
	}
	
	private Panel getTakeQuizPanel(Quiz quiz) {
		Panel overallPanel = new Panel();
		overallPanel.setPanelSize(500, 720);
		overallPanel.setMargin(0, 20);
		Panel panel = new Panel();
		panel.setMargin(64, 64);
		panel.boxLayout(BoxLayout.Y_AXIS);
		panel.alignLeft();
		
		panel.add(new Heading("Quiz Session").big().margin(30));
		panel.add(new Heading(quiz.getName()));

		ArrayList<Question> questions = quiz.getQuestions();
		if(quiz.isScrambled())
			Collections.shuffle(questions);
		
		int i = 1;
		for(Question question: questions) {
			addMargin(panel.getPreviousComponent(), new int[] {0, 0, 30, 0});
			panel.add(new Label("\nQuestion #" + i));
			panel.add(new Label(question.getQuestion()));
			switch(question.getQuestionType()) {
				case "True or False":
				case "Multiple Choice":
					ButtonGroup buttonGroup = new ButtonGroup();
					ArrayList<Answer> answers = question.getAnswers();
					if(quiz.isScrambled())
						Collections.shuffle(answers);
					for(Answer answer: answers) {
						RadioButton button = new RadioButton(answer.getAnswer(), answer.getId(), question.getId());
						panel.add(button);
						buttonGroup.add(button);
					}
					break;
				case "Dropdown":
					Dropdown dropdown = new Dropdown(
						Integer.toString(question.getId()), 
						question.getAnswers()
							.stream()
							.map((Answer a) -> a.getAnswer())
							.toList()
					);
					dropdown.setSize(300, 30);
					panel.add(new Panel()
						.add(dropdown)
						.setPanelSize(300, 30)
						.alignLeft()
					);
					break;
			}
			i += 1;
		}
		overallPanel.addModal("verify-cancel", new Panel()
			.add(new Heading("Are you sure?"))
			.add(new Label("If you cancel, you will"))
			.add(new Label("lose your progress."))
			.add(new Panel()
				.boxLayout(BoxLayout.X_AXIS)
				.add(new Button("Cancel")
					.onClick((Panel p) -> {
						overallPanel.closeModal();
					}))
				.add(new Button("Exit Quiz")
					.onClick((Panel p) -> {
						overallPanel.close();
						mainPanel.open();
					}))
			)
			.setPanelSize(350, 200)
		);
		
		overallPanel.addModal("submit-error", new Panel()
			.add(new Heading("Error"))
			.add(new Label("An error occurred in submitting."))
			.add(new Panel()
				.boxLayout(BoxLayout.X_AXIS)
				.add(new Button("Close")
					.onClick((Panel p) -> {
						overallPanel.closeModal();
					}))
				.add(new Button("Exit Quiz Without Submitting")
					.onClick((Panel p) -> {
						overallPanel.close();
						mainPanel.open();
					}))
			)
			.setPanelSize(350, 200)
		);
		
		panel.add(new Panel()
			.boxLayout(BoxLayout.X_AXIS)
			.alignLeft()
			.alignTop()
			.add(new Button("Cancel")
				.color(Aesthetics.BUTTON_WARNING)
				.onClick((Panel p) -> {
					overallPanel.openModal("verify-cancel");
				}))
			.add(new Button("Submit Quiz")
				.onClick((Panel p) -> {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					LocalDateTime now = LocalDateTime.now();  
					String time = dtf.format(now);
					
					Map<String, String> resultMap = p.getResultMap();
					HashMap<Integer, Integer> map = new HashMap<>();
					for(Question q: quiz.getQuestions()) {
						if(!resultMap.containsKey(Integer.toString(q.getId()))) {
							JOptionPane.showMessageDialog(
								null, 
								"Please answer all of the questions and try to submit again.", 
								"Error", 
								JOptionPane.ERROR_MESSAGE
							);
							return;
						}
						String answerId = resultMap.get(Integer.toString(q.getId()));
						for(Answer a: q.getAnswers()) {
							if(answerId.equals(Integer.toString(a.getId()))) {
								map.put(q.getId(), a.getId());
							}
						}
					}
					
					GradedQuiz submission = new GradedQuiz(quiz.getId(), this.getCurrentUser().getID(), map, time);

					lms.getNetworkManagerClient()
						.sendPacket(new GradedQuizRequestPacket(submission))
						.onReceiveResponse((ResponsePacket resp) -> {
							if(!resp.wasSuccess()) {
								overallPanel.openModal("submit-error");
								return;
							}
							overallPanel.addModal("submitted", new Panel()
								.add(new Heading("Submitted the quiz"))
								.add(new Label("Successfully submitted the quiz."))
								.add(new Label("Would you like to see your score?"))
								.add(new Panel()
									.boxLayout(BoxLayout.X_AXIS)
									.add(new Button("No")
										.onClick((Panel __) -> {
											overallPanel.close();
											mainPanel.open();
										}))
									.add(new Button("Yes")
										.onClick((Panel __) -> {
											overallPanel.close();
											getSubmissionPanel(submission, quiz, this.getCurrentUser()).open();
										}))
									.setPanelSize(250, 50)
									.setMargin(10, 0)
								)
								.setPanelSize(450, 230)
							);
							overallPanel.openModal("submitted");
						});
					
				})
			).setPanelSize(200, 50)
		);

		JScrollPane pane = new JScrollPane(panel.getMainPanel());
		pane.setBorder(null);
		//pane.setSize(1000, 720);
		overallPanel.add(pane);
		return overallPanel;
		
	}
	
	private Panel getSubmissionPanel(GradedQuiz submission, Quiz quiz, User user) {
		Panel overallPanel = new Panel();
		overallPanel.setPanelSize(500, 720);
		overallPanel.setMargin(0, 20);
		Panel panel = new Panel();
		panel.setMargin(64, 64);
		panel.boxLayout(BoxLayout.Y_AXIS);
		
		panel.add(new Heading("Quiz Submission")
			.big().margin(30));
		panel.add(new Label("Quiz Name: " + quiz.getName())
			.setFontSize(18));
		panel.add(new Label("Taken By: " + user.getName())
			.setFontSize(18));
		panel.add(new Label("Time: " + submission.getSubmissionTime())
			.setFontSize(18));
		panel.add(new Label("Score: " + submission.getScore(quiz))
			.setFontSize(18));

		ArrayList<Question> questions = quiz.getQuestions();
		
		int i = 1;
		for(Question question: questions) {
			addMargin(panel.getPreviousComponent(), new int[] {0, 0, 30, 0});
			panel.add(new Label("\nQuestion #" + i));
			panel.add(new Label(question.getQuestion()));
			Answer chosenAnswer = null;
			if(submission.getGradedQuizMap().containsKey(question.getId())) {
				int chosenAnswerId = submission.getGradedQuizMap().get(question.getId());
				int possibleAmt = 0;
				for(Answer answer: question.getAnswers()) {
					Label answerLabel = new Label(" - " + answer.getAnswer() + " - " + answer.getPoints() + " Point(s)");
					panel.add(answerLabel);
					if(chosenAnswerId == answer.getId()) {
						chosenAnswer = answer;
					}
					if(answer.getPoints() > possibleAmt) {
						possibleAmt = answer.getPoints();
					}
				}
				if(chosenAnswer != null) {
					panel.add(new Label("Chosen Answer: " + chosenAnswer.getAnswer() ));
					panel.add(new Label("Points Earned: " + chosenAnswer.getPoints() + " / " + possibleAmt));
				} else {
					panel.add(new Label("The answer the student chose was removed from the quiz."));
				}
			} else {
				panel.add(new Label("This question was added after the student took the quiz."));
				panel.add(new Label("Therefore, they do not have a score for it."));
			}
			i += 1;
		}
		
		panel.add(new Panel()
			.boxLayout(BoxLayout.X_AXIS)
			.alignLeft()
			.alignTop()
			.add(new Button("Exit")
				.onClick((Panel p) -> {
					overallPanel.close();
					mainPanel.open();
				}))
			.setPanelSize(300, 50)
		);

		JScrollPane pane = new JScrollPane(panel.getMainPanel());
		pane.setBorder(null);
		//pane.setSize(1000, 720);
		overallPanel.add(pane);
		return overallPanel;
	}

	private JComponent addMargin(JComponent component, int... margin) {
		component.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(margin[0], margin[1], margin[2], margin[3]),
				component.getBorder()
			)
		);
		return component;
	}

	public Panel getModifyQuizPanel(Quiz quiz) {
		Panel panel = new Panel();
		
		lms.getNetworkManagerClient()
		.addPushHandler(new PushPacketHandler(DeleteQuizResponsePacket.class) {
			@Override
			public void handlePacket(ResponsePacket resp) {
				DeleteQuizResponsePacket respDelQuiz = (DeleteQuizResponsePacket) resp;
				if(quiz.getId() == respDelQuiz.getQuizId()) {
					panel.close();
					JOptionPane.showMessageDialog(
						null, 
						"The quiz was deleted by another user.", 
						"Error", 
						JOptionPane.ERROR_MESSAGE
					);
					mainPanel.open();
				}
			}
		});
		
		panel.setPanelSize(1000, 720);
		panel.setMargin(96, 96);
		panel.boxLayout(BoxLayout.Y_AXIS);
		panel.alignLeft();
		panel.onOpen((Panel __) -> {
			if(panel.getMainPanel().getComponents().length > 1) {
				Map<String, String> map = panel.getResultMap();
				List<Question> questions = quiz.getQuestions();
				for(Question question: questions) {
					int qid = question.getId();
					// Scrambled is taken care of.
					if(map.containsKey("QT-" + qid))
						question.setQuestionType(map.get("QT-" + qid));
					question.setQuestion(map.get("Q-" + qid));
					for(Answer answer: question.getAnswers()) {
						int aid = answer.getId();
						String prefix = "Q-" + qid + "-" + aid;
						answer.setAnswer(map.get(prefix));
						try {
							if(map.containsKey(prefix + "-P"))
								answer.setPointValue(Integer.parseInt(map.get(prefix + "-P")));
						} catch(NumberFormatException e) {
							JOptionPane.showMessageDialog(
								null,
								"Invalid point values. " +
								"Please enter a valid input when entering the point values of a question. " +
								"The point values must be an integer.", 
								"Error", 
								JOptionPane.ERROR_MESSAGE
							);
						}
					}
				}
			}
			
			panel.getMainPanel().removeAll();
			
			panel.add(new Heading("Modify Quiz").big().margin(30));
			panel.add(new TextField("Quiz Name", "Quiz Name", quiz.getName()).panelize(300, 90, 0, 30));

			addMargin(panel.getPreviousComponent(), new int[] {0, 0, 30, 0});
			Label scrambledLabel = new Label("Questions Scrambled: " + (quiz.isScrambled() ? "Yes" : "No"));
			
			panel.add(scrambledLabel);
			panel.add(new Button("Toggle Scrambled")
				.onClick((Panel ___) -> {
					quiz.setScrambled(!quiz.isScrambled());
					scrambledLabel.setText("Questions Scrambled: " + (quiz.isScrambled() ? "Yes" : "No"));
				}));
			panel.add(new GapComponent(10));
			
			int i = 1;
			for(Question question: quiz.getQuestions()) {
				addMargin(panel.getPreviousComponent(), new int[] {0, 0, 60, 0});
				panel.add(new Label("\nQuestion #" + i));
				Dropdown questionTypeDropdown = new Dropdown("QT-"+question.getId(), new String[] {
						"Multiple Choice", "True or False", "Dropdown"
					})
					.onChange((String choice) -> {
						if(question.getQuestionType().equalsIgnoreCase(choice))
							return;
						question.setQuestionType(choice);
						if(choice.equalsIgnoreCase("True or False")) {
							ArrayList<Answer> answers = question.getAnswers();
							answers.clear();
							answers.add(new Answer("True", true, 1, 0));
							answers.add(new Answer("False", false, 0, 1));
							panel.runOnOpen();
						}
					});
				questionTypeDropdown.select(question.getQuestionType());
				panel.add(questionTypeDropdown.panelize(300, 35, 0, 5));
				TextField questionField = new TextField("Question", "Q-" + question.getId(), question.getQuestion());
				panel.add(questionField.panelize(300, 45, 0, 5));
				ArrayList<Answer> answers = question.getAnswers();
				int answerIndex = 1;
				for(Answer answer: answers) {
					String id = "Q-" + question.getId() + "-" + answer.getId();
					String idPoints = "Q-" + question.getId() + "-" + answer.getId() + "-P";
					panel.add(new Panel(new FlowLayout(FlowLayout.LEADING))
						.add(new TextField("Answer #" + answerIndex, id, answer.getAnswer())
							.panelize(300, 45, 0, 5))
						.add(new TextField("Points", idPoints, Integer.toString(answer.getPoints()))
								.panelize(100, 45, 0, 5))
						.add(new Button("Remove Answer")
							.onClick((Panel ___) -> {
								answers.remove(answer);
								panel.runOnOpen();
							}))
						.setPanelSize(1000, 50)
					);
					answerIndex += 1;
				}
				panel.add(new Panel(new FlowLayout(FlowLayout.LEADING))
					.add(new Button("Add New Answer")
						.onClick((Panel ___) -> {
							answers.add(new Answer("", false, 0, question.generateUniqueAnswerId()));
							panel.runOnOpen();
						}))
					.add(new Button("Remove Question")
						.onClick((Panel ___) -> {
							quiz.getQuestions().remove(question);
							panel.runOnOpen();
						}))
					.setPanelSize(1000, 50)
				);
				panel.add(new GapComponent(10));
				i += 1;
			}
			panel.addModal("verify-cancel", new Panel()
				.add(new Heading("Are you sure?"))
				.add(new Label("If you cancel, you will"))
				.add(new Label("lose all changes."))
				.add(new Panel()
					.boxLayout(BoxLayout.X_AXIS)
					.add(new Button("Cancel")
						.onClick((Panel p) -> {
							panel.closeModal();
						}))
					.add(new Button("Discard Changes & Exit")
						.onClick((Panel p) -> {
							panel.close();
							mainPanel.open();
						}))
				)
				.setPanelSize(350, 200)
			);
			panel.addModal("verify-delete", new Panel()
					.add(new Heading("Are you sure?"))
					.add(new Label("If you delete this quiz, "))
					.add(new Label("it cannot be recovered."))
					.add(new Panel()
						.boxLayout(BoxLayout.X_AXIS)
						.add(new Button("Cancel")
							.onClick((Panel p) -> {
								panel.closeModal();
							}))
						.add(new Button("Delete Quiz")
							.color(Aesthetics.BUTTON_WARNING)
							.onClick((Panel p) -> {
								lms.getNetworkManagerClient()
									.sendPacket(new DeleteQuizRequestPacket(quiz.getId()))
									.onReceiveResponse((ResponsePacket resp) -> {
										if(resp.wasSuccess()) {
											JOptionPane.showMessageDialog(
												null, 
												"Successfully deleted the quiz.", 
												"Success", 
												JOptionPane.INFORMATION_MESSAGE
											);
											panel.close();
											mainPanel.open();
										} else {
											JOptionPane.showMessageDialog(
												null, 
												"Unable to delete the quiz.", 
												"Error", 
												JOptionPane.ERROR_MESSAGE
											);
											panel.close();
											mainPanel.open();
										}
									});
							}))
					)
					.setPanelSize(350, 200)
				);

			panel.add(new Button("Add New Question")
				.onClick((Panel ___) -> {
					quiz.getQuestions().add(
						new Question(
							new ArrayList<Answer>(),
							"", 
							quiz.generateUniqueQuestionId(), 
							"Multiple Choice"
						)
					);
					panel.runOnOpen();
				}));
			
			panel.add(new Panel()
				.boxLayout(BoxLayout.X_AXIS)
				.alignLeft()
				.alignTop()
				.add(new Button("Cancel")
					.color(Aesthetics.BUTTON_WARNING)
					.onClick((Panel p) -> {
						panel.openModal("verify-cancel");
					}))
				.add(new Button("Delete Quiz")
					.color(Aesthetics.BUTTON_WARNING)
					.onClick((Panel p) -> {
						panel.openModal("verify-delete");
					}))
				.add(new Button("Save Quiz")
					.onClick((Panel p) -> {
						Map<String, String> map = p.getResultMap();
						List<Question> questions = quiz.getQuestions();
						for(Question question: questions) {
							int qid = question.getId();
							// Scrambled is taken care of.
							question.setQuestionType(map.get("QT-" + qid));
							question.setQuestion(map.get("Q-" + qid));
							for(Answer answer: question.getAnswers()) {
								int aid = answer.getId();
								String prefix = "Q-" + qid + "-" + aid;
								answer.setAnswer(map.get(prefix));
								try {
									answer.setPointValue(Integer.parseInt(map.get(prefix + "-P")));
								} catch(NumberFormatException e) {
									JOptionPane.showMessageDialog(
										null,
										"Invalid point values. " +
										"Please enter a valid input when entering the point values of a question. " +
										"The point values must be an integer.", 
										"Error", 
										JOptionPane.ERROR_MESSAGE
									);
								}
							}
						}
						
						lms.getNetworkManagerClient()
							.sendPacket(new QuizRequestPacket(quiz))
							.onReceiveResponse((ResponsePacket resp) -> {
								JOptionPane.showMessageDialog(
									null, 
									"Successfully saved the quiz. Going back to the main menu.",
									"Success",
									JOptionPane.INFORMATION_MESSAGE
								);
								panel.close();
								mainPanel.open();
							});
						
					})
				).setPanelSize(400, 50)
			);
			
			panel.revalidate();
			panel.updateBounds();
		});
		return panel.scrollize();
	}
	
	@Override
	public void init() {
		this.hostnamePanel = new Panel(new GridLayout(3, 1));
		this.connectionStatusPanel = new Panel(new GridLayout(2, 1));
		this.loginPanel = new Panel(new GridLayout(5, 1));
		this.mainPanel = new Panel(new GridBagLayout());
		this.mainTabPanel = new Panel();
		
		mainPanel.setPanelSize(1280+64, 720+64);
		mainPanel.setMargin(64, 64);
		
		mainPanel.addModal("create-quiz", new Panel(new GridLayout(5, 1))
			.add(new Heading("Create Quiz"))
			.add(new TextField("Course"))
			.add(new TextField("Quiz Name"))
			.add(new Panel(new FlowLayout())
				.add(new Button("Cancel")
					.onClick((Panel __) -> {
						mainPanel.closeModal();
					}))
				.add(new Button("Import From File")
					.onClick((Panel panel) -> {
						Map<String, String> map = panel.getResultMap();
						String name = map.get("Quiz Name");
						String course = map.get("Course");
						JFileChooser fileChooser = new JFileChooser();
						
						fileChooser.showOpenDialog(null);
						File f = fileChooser.getSelectedFile();
						Quiz quiz;
						try {
							quiz = ClientFileWrapper.importQuiz(lms, f, name, course);
						} catch(Exception e) {
							JOptionPane.showMessageDialog(
								null, 
								"An error occurred when attempting to import the quiz. Invalid file format.", 
								"Error", 
								JOptionPane.ERROR_MESSAGE
							);
							return;
						}
						if(quiz == null) {
							JOptionPane.showMessageDialog(
								null, 
								"Invalid file. Please select a valid one to import.", 
								"Error",
								JOptionPane.ERROR_MESSAGE
							);
							return;
						}
						lms.getNetworkManagerClient()
							.sendPacket(new QuizRequestPacket(quiz))
							.onReceiveResponse((ResponsePacket resp) -> {
								JOptionPane.showMessageDialog(null, "Successfully imported the quiz.");
								mainPanel.closeModal();
								mainPanel.close();
								QuizResponsePacket quizResp = (QuizResponsePacket) resp;
								getModifyQuizPanel(quizResp.getQuizResponse()).open();
							});
					}))
				.add(new Button("Create Empty Quiz")
					.onClick((Panel panel) -> {
						Map<String, String> map = panel.getResultMap();
						String name = map.get("Quiz Name");
						String course = map.get("Course");
						ArrayList<Question> questions = new ArrayList<Question>();
						Quiz quiz = new Quiz(name, this.getCurrentUser().getName(), -1, questions, false, course);
						
						lms.getNetworkManagerClient()
							.sendPacket(new QuizRequestPacket(quiz))
							.onReceiveResponse((ResponsePacket resp) -> {
								JOptionPane.showMessageDialog(null, "Successfully created the quiz.");
								mainPanel.closeModal();
								mainPanel.close();
								QuizResponsePacket quizResp = (QuizResponsePacket) resp;
								getModifyQuizPanel(quizResp.getQuizResponse()).open();
							});
					}))
			)
			.setPanelSize(500, 350)
		);
		
		mainPanel.add(new Panel(new FlowLayout(FlowLayout.CENTER))
			.onOpen((Panel panel) -> {
				panel.clear();
				panel
					.add(new JLabel(new ImageIcon("FinalLogo.png")))
					.add(new Heading("Darkspace").center())
					.compSetSize(280, 30)
					.add(new Label("Logged in as " + this.getCurrentUser().getName()).center())
					.compSetSize(280, 50)
					.add((new Button("Take Quiz"))
						.onClick((Panel __) -> {
							mainTabPanel.openTabPanel("Take Quiz");
						}))
					.compSetSize(280, 35);
				if(this.getCurrentUser() instanceof Teacher) {
					panel
						.add((new Button("Modify Quiz"))
							.onClick((Panel __) -> {
								mainTabPanel.openTabPanel("Modify Quiz");
							}))
						.compSetSize(280, 35)
						.add((new Button("Quiz Submissions"))
							.onClick((Panel __) -> {
								mainTabPanel.openTabPanel("Quiz Submissions");
							}))
						.compSetSize(280, 35)
						.add(new Button("Create Quiz")
							.onClick((Panel __) -> {
								mainPanel.openModal("create-quiz");
							}))
						.compSetSize(280, 35);
				} else {
					panel
						.add((new Button("My Quiz Submissions"))
							.onClick((Panel __) -> {
								mainTabPanel.openTabPanel("My Quiz Submissions");
							}))
						.compSetSize(280, 35);
				}
				panel
					.add((new Button("User Settings"))
						.onClick((Panel __) -> {
							mainTabPanel.openTabPanel("User Settings");
						}))
					.compSetSize(280, 35)
					.add((new Button("Logout"))
						.onClick((Panel __) -> {
							mainPanel.close();
							loginPanel.open();
						}))
					.compSetSize(280, 35);
				mainPanel.refreshComponents();
			})
		.setPanelSize(280, 720));
		
		mainPanel.add(mainTabPanel
			.setPanelSize(1000, 720));
		
		mainPanel.addModal("user-settings-invalid", new Panel()
			.add(new Label("No fields can be empty."))
			.add(new Label("Verify all fields have values in them."))
			.add(new Button("Okay")
				.onClick((Panel __) -> {
					mainPanel.closeModal();
				}))
			.setPanelSize(400, 200));
		
		mainPanel.addModal("user-settings-error", new Panel()
			.add(new Label("A user with that username already exists."))
			.add(new Label("Please use a different one."))
			.add(new Button("Okay")
				.onClick((Panel __) -> {
					mainPanel.closeModal();
				}))
			.setPanelSize(400, 200));
		
		mainPanel.addModal("user-settings-success", new Panel()
			.add(new Label("Successfully updated your details."))
			.add(new Button("Okay")
				.onClick((Panel __) -> {
					mainPanel.closeModal();
				}))
			.setPanelSize(400, 200));
		
		mainTabPanel.addTabPanel("User Settings", (new Panel(new GridLayout(6, 1)))
			.onOpen((Panel p) -> {
				User user = this.getCurrentUser();
				p.setInput("Name", user.getName());
				p.setInput("Username", user.getUsername());
				p.setInput("Password", user.getPassword());
			})
			.add(new Heading("User Settings"))
			.add(new TextField("Name"))
			.add(new TextField("Username"))
			.add(new TextField("Password"))
			.add(new GapComponent())
			.add(new Button("Save Changes")
				.onClick((Panel p) -> {
					Map<String, String> results = p.getResultMap();
					String name = results.get("Name");
					String username = results.get("Username");
					String password = results.get("Password");
					
					if(name.isBlank() || username.isBlank() || password.isBlank()) {
						mainPanel.openModal("user-settings-invalid");
						return;
					}
					
					User user = this.getCurrentUser();
					user.setName(name);
					user.setUsername(username);
					user.setPassword(password);
					
					lms.getNetworkManagerClient()
					.sendPacket(new UpdateUserRequestPacket(user))
					.onReceiveResponse((ResponsePacket packet) -> {
						if(!packet.wasSuccess()) {
							mainPanel.openModal("user-settings-error");
							return;
						}
						mainPanel.openModal("user-settings-success");
					});
				}).panelize()
			)
			.setMargin(64, 0)
			.setPanelSize(300, 350)
		);
		
		
		
		mainTabPanel.addTabPanel("Take Quiz", (new Panel(new FlowLayout(FlowLayout.LEFT)))
			.onOpen((Panel p) -> {
				lms.getNetworkManagerClient()
				.addPushHandler(new PushPacketHandler(QuizResponsePacket.class) {
					@Override
					public void handlePacket(ResponsePacket resp) {
						lms.getNetworkManagerClient().removePushHandler(this);
						SwingUtilities.invokeLater(() -> {
							p.runOnOpen();
						});
					}
				});
				p.getMainPanel().removeAll();
				p.add((new Heading("Take Quiz")).big());
				lms.getNetworkManagerClient()
					.sendPacket(new QuizListRequestPacket("All", "", false))
					.onReceiveResponse((ResponsePacket resp) -> {
						QuizListResponsePacket listResp = (QuizListResponsePacket) resp;
						List<Quiz> quizzes = listResp.getListOfQuizzesResponse();
						if(quizzes == null) {
							p.add(new Heading("Unable to get a list of quizzes. Please verify the server is up or try again later."));
							p.revalidate();
							return;
						}
						List<String> courses = getCourses(quizzes);
						for(String course: courses) {
							p.add(new Heading(course));
							p.compSetSize(1000, 80);
							
							
							List<Quiz> quizzesCourse = getQuizzesFromCourse(quizzes, course);
							
							for(Quiz quiz: quizzesCourse) {
								Panel panel = (new Panel())
									.add(new Label(quiz.getName()))
									.add(new Label("Author: " + quiz.getAuthor()))
									.add(new Label("Questions: " + quiz.getQuestions().size()))
									.onClick(mainPanel, (Panel __) -> {
										mainPanel.addModal("Quiz-" + quiz.getId(), 
											(new Panel())
											.boxLayout(BoxLayout.Y_AXIS)
											.alignLeft()
											.add(new Heading(quiz.getName()))
											.add(new Label("Course: " + quiz.getCourse()))
											.add(new Label("Author: " + quiz.getAuthor()))
											.add(new Label("Questions: " + quiz.getQuestions().size()))
											.add(new Panel()
												.boxLayout(BoxLayout.X_AXIS)
												.add((new Button("Take Quiz"))
													.onClick((Panel __2) -> {
														mainPanel.closeModal();
														mainPanel.close();
														getTakeQuizPanel(quiz).open();
													}))
												.add((new Button("Cancel"))
													.onClick((Panel __2) -> {
														mainPanel.closeModal();
													}))
												.setPanelSize(299, 48)
											)
											.setPanelSize(400, 300)
										);
										mainPanel.revalidate();
										mainPanel.openModal("Quiz-"+quiz.getId());
									});
								panel.getMainPanel().setBorder(
									BorderFactory.createCompoundBorder(
										BorderFactory.createLineBorder(Aesthetics.BUTTON_BORDER, 1),
										BorderFactory.createEmptyBorder(20, 20, 20, 20)
									)
								);
								panel.setPanelSize(200, 100);
								panel.setSize(200, 100);
								p.add(panel);
								
							}
						}
						
						p.revalidate();
						p.updateBounds();
					});
			})
		.setPanelSize(1000, 720)
		.setMargin(64, 0)
		.scrollize());
		
		mainTabPanel.addTabPanel("Modify Quiz", (new Panel(new FlowLayout(FlowLayout.LEFT)))
				.onOpen((Panel p) -> {
					lms.getNetworkManagerClient()
					.addPushHandler(new PushPacketHandler(QuizResponsePacket.class) {
						@Override
						public void handlePacket(ResponsePacket resp) {
							lms.getNetworkManagerClient().removePushHandler(this);
							SwingUtilities.invokeLater(() -> {
								p.runOnOpen();
							});
						}
					});
					p.getMainPanel().removeAll();
					p.add((new Heading("Modify Quiz")).big());
					lms.getNetworkManagerClient()
						.sendPacket(new QuizListRequestPacket("All", "", false))
						.onReceiveResponse((ResponsePacket resp) -> {
							QuizListResponsePacket listResp = (QuizListResponsePacket) resp;
							List<Quiz> quizzes = listResp.getListOfQuizzesResponse();
							if(quizzes == null) {
								p.add(new Heading("Unable to get a list of quizzes. Please verify the server is up or try again later."));
								p.revalidate();
								return;
							}
							List<String> courses = getCourses(quizzes);
							for(String course: courses) {
								p.add(new Heading(course));
								p.compSetSize(1000, 80);
								
								
								List<Quiz> quizzesCourse = getQuizzesFromCourse(quizzes, course);
								
								for(Quiz quiz: quizzesCourse) {
									Panel panel = (new Panel())
										.add(new Label(quiz.getName()))
										.add(new Label("Author: " + quiz.getAuthor()))
										.add(new Label("Questions: " + quiz.getQuestions().size()))
										.onClick(mainPanel, (Panel __) -> {
											mainPanel.addModal("Quiz-" + quiz.getId(), 
												(new Panel())
												.boxLayout(BoxLayout.Y_AXIS)
												.add(new Heading(quiz.getName()))
												.add(new Label("Course: " + quiz.getCourse()))
												.add(new Label("Author: " + quiz.getAuthor()))
												.add(new Label("Questions: " + quiz.getQuestions().size()))
												.add((new Panel())
													.boxLayout(BoxLayout.X_AXIS) 
													.add((new Button("Modify Quiz"))
														.onClick((Panel __2) -> {
															mainPanel.closeModal();
															mainPanel.close();
															getModifyQuizPanel(quiz).open();
														}))
													.add((new Button("Cancel"))
														.onClick((Panel __2) -> {
															mainPanel.closeModal();
														}))
													.setPanelSize(300, 48)
												)
												.setPanelSize(400, 300)
											);
											mainPanel.revalidate();
											mainPanel.openModal("Quiz-"+quiz.getId());
										});
									panel.getMainPanel().setBorder(
										BorderFactory.createCompoundBorder(
											BorderFactory.createLineBorder(Aesthetics.BUTTON_BORDER, 1),
											BorderFactory.createEmptyBorder(20, 20, 20, 20)
										)
									);
									panel.setPanelSize(200, 100);
									panel.setSize(200, 100);
									p.add(panel);
									
								}
							}
							
							p.revalidate();
							p.updateBounds();
						});
					
					p.revalidate();
					p.updateBounds();
				})
			.setPanelSize(1000, 720)
			.setMargin(64, 0)
			.scrollize());
		
		mainTabPanel.addTabPanel("Quiz Submissions", new Panel(new FlowLayout(FlowLayout.LEFT))
			.onOpen((Panel p) -> {
				lms.getNetworkManagerClient()
					.addPushHandler(new PushPacketHandler(GradedQuizResponsePacket.class) {
						@Override
						public void handlePacket(ResponsePacket resp) {
							lms.getNetworkManagerClient().removePushHandler(this);
							SwingUtilities.invokeLater(() -> {
								p.runOnOpen();
							});
						}
					});
				p.getMainPanel().removeAll();
				p.add((new Heading("Submission List")).big());
				lms.getNetworkManagerClient()
					.sendPacket(new GradedQuizListRequestPacket())
					.onReceiveResponse((ResponsePacket resp) -> {
						GradedQuizListResponsePacket listResp = (GradedQuizListResponsePacket) resp;
						List<GradedQuiz> gradedQuizzes = listResp.getGradedQuizzes();
						if(gradedQuizzes == null) {
							p.add(new Heading("Unable to get a list of quizzes. Please verify the server is up or try again later."));
							p.revalidate();
							return;
						}
						List<User> users  = getUsers(gradedQuizzes, listResp.getUsers());
						for(User user: users) {
							p.add(new Heading(user.getName()));
							p.compSetSize(1000, 80);
							
							List<GradedQuiz> userSubmissions = gradedQuizzes
								.stream()
								.filter((GradedQuiz submission) -> (
									submission.getStudentID() == user.getID()
								))
								.toList();
							
							for(GradedQuiz gradedQuiz: userSubmissions) {
								Quiz quiz = getQuiz(listResp.getQuizzes(), gradedQuiz.getQuizID());
								
								Panel panel = (new Panel())
									.add(new Label(quiz.getCourse()))
									.add(new Label(quiz.getName()))
									.add(new Label("Score: " + gradedQuiz.getScore(quiz)))
									.add(new Label(gradedQuiz.getSubmissionTime()))
									.onClick(mainPanel, (Panel __) -> {
										mainPanel.addModal("Quiz-" + quiz.getId(), 
											(new Panel())
											.boxLayout(BoxLayout.Y_AXIS)
											.add(new Heading(quiz.getName()))
											.add(new Label("Course: " + quiz.getCourse()))
											.add(new Label("Author: " + quiz.getAuthor()))
											.add(new Label("Questions: " + quiz.getQuestions().size()))
											.add((new Panel())
												.boxLayout(BoxLayout.X_AXIS) 
												.add((new Button("View Submission"))
													.onClick((Panel __2) -> {
														mainPanel.closeModal();
														mainPanel.close();
														getSubmissionPanel(gradedQuiz, quiz, user).open();
													}))
												.add((new Button("Cancel"))
													.onClick((Panel __2) -> {
														mainPanel.closeModal();
													}))
												.setPanelSize(300, 48)
											)
											.setPanelSize(400, 300)
										);
										mainPanel.revalidate();
										mainPanel.openModal("Quiz-"+quiz.getId());
									});
								panel.getMainPanel().setBorder(
									BorderFactory.createCompoundBorder(
										BorderFactory.createLineBorder(Aesthetics.BUTTON_BORDER, 1),
										BorderFactory.createEmptyBorder(20, 20, 20, 20)
									)
								);
								panel.setPanelSize(200, 100);
								panel.setSize(200, 100);
								p.add(panel);
								
							}
						}
						
						p.revalidate();
						p.repaint();
						//mainTabPanel.revalidate();
						p.updateBounds();
					});
			})
			.setPanelSize(1000, 720)
			.setMargin(64, 0)
			.scrollize());
		
		mainTabPanel.addTabPanel("My Quiz Submissions", new Panel(new FlowLayout(FlowLayout.LEFT))
				.onOpen((Panel p) -> {
					lms.getNetworkManagerClient()
						.addPushHandler(new PushPacketHandler(GradedQuizResponsePacket.class) {
							@Override
							public void handlePacket(ResponsePacket resp) {
								lms.getNetworkManagerClient().removePushHandler(this);
								SwingUtilities.invokeLater(() -> {
									p.runOnOpen();
								});
							}
						});
					p.getMainPanel().removeAll();
					p.add((new Heading("My Quiz Submission List")).big());
					lms.getNetworkManagerClient()
						.sendPacket(new GradedQuizListRequestPacket(this.getCurrentUser()))
						.onReceiveResponse((ResponsePacket resp) -> {
							GradedQuizListResponsePacket listResp = (GradedQuizListResponsePacket) resp;
							List<GradedQuiz> gradedQuizzes = listResp.getGradedQuizzes();
							if(gradedQuizzes == null) {
								p.add(new Heading("Unable to get a list of quizzes. Please verify the server is up or try again later."));
								p.revalidate();
								return;
							}
							User user = this.getCurrentUser();
							p.add(new Heading(user.getName()));
							p.compSetSize(1000, 80);
							
							List<GradedQuiz> userSubmissions = gradedQuizzes
								.stream()
								.filter((GradedQuiz submission) -> (
									submission.getStudentID() == user.getID()
								))
								.toList();
							
							for(GradedQuiz gradedQuiz: userSubmissions) {
								Quiz quiz = getQuiz(listResp.getQuizzes(), gradedQuiz.getQuizID());
								
								Panel panel = (new Panel())
									.add(new Label(quiz.getCourse()))
									.add(new Label(quiz.getName()))
									.add(new Label("Score: " + gradedQuiz.getScore(quiz)))
									.add(new Label(gradedQuiz.getSubmissionTime()))
									.onClick(mainPanel, (Panel __) -> {
										mainPanel.addModal("Quiz-" + quiz.getId(), 
											(new Panel())
											.boxLayout(BoxLayout.Y_AXIS)
											.add(new Heading(quiz.getName()))
											.add(new Label("Course: " + quiz.getCourse()))
											.add(new Label("Author: " + quiz.getAuthor()))
											.add(new Label("Questions: " + quiz.getQuestions().size()))
											.add((new Panel())
												.boxLayout(BoxLayout.X_AXIS) 
												.add((new Button("View Submission"))
													.onClick((Panel __2) -> {
														mainPanel.closeModal();
														mainPanel.close();
														getSubmissionPanel(gradedQuiz, quiz, user).open();
													}))
												.add((new Button("Cancel"))
													.onClick((Panel __2) -> {
														mainPanel.closeModal();
													}))
												.setPanelSize(300, 48)
											)
											.setPanelSize(400, 300)
										);
										mainPanel.revalidate();
										mainPanel.openModal("Quiz-"+quiz.getId());
									});
								panel.getMainPanel().setBorder(
									BorderFactory.createCompoundBorder(
										BorderFactory.createLineBorder(Aesthetics.BUTTON_BORDER, 1),
										BorderFactory.createEmptyBorder(20, 20, 20, 20)
									)
								);
								panel.setPanelSize(200, 100);
								panel.setSize(200, 100);
								p.add(panel);
								
							}
							
							p.revalidate();
							p.repaint();
							//mainTabPanel.revalidate();
							p.updateBounds();
						});
				})
				.setPanelSize(1000, 720)
				.setMargin(64, 0)
				.scrollize());
			
		
		mainTabPanel.openTabPanel("Take Quiz");
		
		loginPanel.addModal("Create User", (new Panel(new GridLayout(6, 1)))
			.add(new Heading("Create User"))
			.add(new TextField("Name"))
			.add(new TextField("Username"))
			.add(new TextField("Password"))
			.add(new Dropdown("User Type", "User Type", new String[] {
				"Student", "Teacher"
			}))
			.add((new Panel(new FlowLayout()))
				.add((new Button("Cancel"))
					.onClick((Panel p) -> {
						loginPanel.closeModal();
					}))
				.add((new Button("Create User"))
					.onClick((Panel p) -> {
						Map<String, String> result = p.getResultMap();
						String name = result.get("Name");
						String username = result.get("Username");
						String password = result.get("Password");
						String userType = result.get("User Type");
						User user = null;
						switch(userType) {
							case "Student":
								user = new Student(0, name, username, password);
								break;
							case "Teacher":
								user = new Teacher(0, name, username, password);
								break;
						}
						lms.getNetworkManagerClient().sendPacket(
							new CreateUserRequestPacket(user)	
						).onReceiveResponse((ResponsePacket response) -> {
							if(response.wasSuccess()) {
								JOptionPane.showMessageDialog(
									null,
									"Successfully created the user.",
									"Create User",
									JOptionPane.INFORMATION_MESSAGE
								);
								loginPanel.closeModal();
							} else {
								JOptionPane.showMessageDialog(
									null, 
									"That username is already taken. Please try a different one.",
									"Error",
									JOptionPane.ERROR_MESSAGE
								);
							}
						});
					}))
			).setPanelSize(350, 400)
		);
		
		loginPanel
		.add(new JLabel(new ImageIcon("FinalLogo.png")))
		.add(new Heading("Darkspace").center())
		.add(new TextField("Username"))
		.add(new TextField("Password"))
		.add((new Panel())
			.boxLayout(BoxLayout.X_AXIS)
			.add((new Button("Create User"))
					.onClick((Panel p) -> {
						// Open Create User menu.
						loginPanel.openModal("Create User");
					}))
			.add((new Button("Login"))
				.onClick((Panel p) -> {
					Map<String, String> result = p.getResultMap();
					String username = result.get("Username");
					String password = result.get("Password");
					lms.getNetworkManagerClient().sendPacket(
						new LoginUserRequestPacket(username, password)	
					).onReceiveResponse((ResponsePacket response) -> {
						if(!response.wasSuccess()) {
							JOptionPane.showMessageDialog(
								null, 
								"Invalid Username or Password.\nPlease try again or create a new user.",
								"Error",
								JOptionPane.ERROR_MESSAGE
							);
						} else {
							NewUserResponsePacket resp = (NewUserResponsePacket) response;
							this.setCurrentUser(resp.getUser());
							JOptionPane.showMessageDialog(
									null,
									"You have successfully logged into Darkspace.",
									"Login In Validation",
									JOptionPane.INFORMATION_MESSAGE
							);
							loginPanel.close();
							mainPanel.open();
						}
					});
				}))
		)
		.setPanelSize(500, 500)
		.setMargin(100, 100);

		connectionStatusPanel
		.add(new Label("Connecting to the server..."))
		.add(new Label("Please Wait..."))
		.setPanelSize(400, 200)
		.setMargin(100, 100);
		
		hostnamePanel
		.add(new Label("Please enter the ip address of the server."))
		.add(new TextField("IP Address", "ip"))
		.add((new Button("Connect"))
			.onClick((Panel p) -> {
				String ip = p.getInput("ip");
				NameSetter nameSetter = lms.getNetworkManagerClient().nameSetter;
				hostnamePanel.close();
				connectionStatusPanel.open();
				nameSetter.setErrorRunnable(() -> {
					connectionStatusPanel.getMainPanel().removeAll();
					connectionStatusPanel
						.add(new Label("Unable to connect to the server."))
						.add((new Button("Try Again"))
							.onClick((Panel __) -> {
								connectionStatusPanel.close();
								connectionStatusPanel.getMainPanel().removeAll();
								connectionStatusPanel
									.add(new Label("Connecting to the server..."))
									.add(new Label("Please Wait..."));
								hostnamePanel.open();
							})
						);
					connectionStatusPanel.revalidate();
				});
				
				nameSetter.setSuccessRunnable(() -> {
					connectionStatusPanel.close();
					loginPanel.open();
				});
				
				synchronized(nameSetter) {
					nameSetter.setName(ip);
					nameSetter.notify();
				}
			}).panelize().setPanelSize(400, 100))
		.setPanelSize(500, 300)
		.setMargin(100, 100);
		
	}

	private User getUser(List<User> users, int id) {
		for(User u: users) {
			if(u.getID() == id) {
				return u;
			}
		}
		return null;
	}

	private Quiz getQuiz(List<Quiz> quizzes, int id) {
		for(Quiz q: quizzes) {
			if(q.getId() == id) {
				return q;
			}
		}
		return null;
	}
	
	private List<User> getUsers(List<GradedQuiz> quizzes, List<User> allUsers) {
		List<User> users = new ArrayList<User>();
		for(GradedQuiz submission: quizzes) {
			User user = getUser(allUsers, submission.getStudentID());
			if(user != null && !users.contains(user))
				users.add(user);
		}
		return users;
	}

	@Override
	public void exit() {
		
	}
	
	public void run() {
		this.hostnamePanel.open();
	}
	
	public Scanner getScanner() {
		return null;
	}
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
}
