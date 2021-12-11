package client;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

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
import gui.FileInput;
import gui.GapComponent;
import gui.Heading;
import gui.Label;
import gui.Panel;
import gui.RadioButton;
import gui.TextField;
import packets.request.CreateUserRequestPacket;
import packets.request.GradedQuizListRequestPacket;
import packets.request.GradedQuizRequestPacket;
import packets.request.LoginUserRequestPacket;
import packets.request.QuizListRequestPacket;
import packets.request.QuizRequestPacket;
import packets.request.UpdateUserRequestPacket;
import packets.response.GradedQuizListResponsePacket;
import packets.response.NewUserResponsePacket;
import packets.response.QuizListResponsePacket;
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
	
	User currentUser = new Teacher(49100, "Person 1", "username", "passa");
	
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

		panel.add(new Heading("Quiz Session").big().margin(30));
		panel.add(new Heading(quiz.getName()));

		ArrayList<Question> questions = quiz.getQuestions();
		if(quiz.isScrambled())
			Collections.shuffle(questions);
		
		int i = 1;
		for(Question question: questions) {
			panel.add(new Panel().setPanelSize(50, 50));
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
					panel.add(dropdown);
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
			.add(new Label(""))
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
							System.out.println("Submitted");
							if(!resp.wasSuccess()) {
								overallPanel.openModal("submit-error");
								return;
							}
							overallPanel.addModal("submitted", new Panel()
								.add(new Heading("Submit Quiz"))
								.add(new Label("Successfully submitted quiz."))
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
								.setPanelSize(350, 230)
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
		overallPanel.setPanelSize(1000, 720);
		overallPanel.setMargin(0, 20);
		Panel panel = new Panel();
		panel.setMargin(64, 64);
		panel.boxLayout(BoxLayout.Y_AXIS);
		
		panel.add(new Heading("Quiz Submission").big().margin(30));
		panel.add(new Heading("Quiz Name: " + quiz.getName()));
		panel.add(new Heading("Taken By: " + user.getName()));
		panel.add(new Heading("Time: " + submission.getSubmissionTime()));

		ArrayList<Question> questions = quiz.getQuestions();
		
		int i = 1;
		for(Question question: questions) {
			panel.add(new Panel().setPanelSize(50, 50));
			panel.add(new Label("\nQuestion #" + i));
			panel.add(new Label(question.getQuestion()));
			Answer chosenAnswer = null;
			if(submission.getGradedQuizMap().containsKey(question.getId())) {
				int chosenAnswerId = submission.getGradedQuizMap().get(question.getId());
				int possibleAmt = 0;
				for(Answer answer: question.getAnswers()) {
					Label answerLabel = new Label(" - " + answer.getAnswer() + " - " + answer.getPoints() + " Points");
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
			}
			i += 1;
		}
		
		panel.add(new Panel()
			.boxLayout(BoxLayout.X_AXIS)
			.add(new Button("Exit")
				.color(Aesthetics.BUTTON_WARNING)
				.onClick((Panel p) -> {
					overallPanel.close();
					mainPanel.open();
				}))
			.setPanelSize(200, 50)
		);

		JScrollPane pane = new JScrollPane(panel.getMainPanel());
		pane.setBorder(null);
		//pane.setSize(1000, 720);
		overallPanel.add(pane);
		return overallPanel;
	}

	public Panel getModifyQuizPanel(Quiz quiz) {
		Panel overallPanel = new Panel();
		overallPanel.setPanelSize(1000, 720);
		overallPanel.setMargin(0, 20);
		Panel panel = new Panel();
		panel.setMargin(64, 64);
		//panel.disableBounding();
		panel.boxLayout(BoxLayout.Y_AXIS);

		panel.add(new Heading("Modify Quiz").big().margin(30));
		panel.add(new Heading(quiz.getName()));

		JScrollPane pane = new JScrollPane(panel.getMainPanel());
		pane.setBorder(null);
		//pane.setSize(1000, 720);
		overallPanel.add(pane);
		return overallPanel;
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
			.add(new FileInput("Quiz File (Optional)", "Select Quiz File", "quiz-file"))
			.add(new Panel(new FlowLayout())
				.add(new Button("Cancel")
					.onClick((Panel __) -> {
						mainPanel.closeModal();
					}))
				.add(new Button("Create Quiz")
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
								//mainPanel.close();
							});
					}))
			)
			.setPanelSize(500, 500)
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
					.add((new Button("Quiz List"))
						.onClick((Panel __) -> {
							mainTabPanel.openTabPanel("Quiz List");
						}))
					.compSetSize(280, 35)
					.add((new Button("Quiz Submissions"))
						.onClick((Panel __) -> {
							mainTabPanel.openTabPanel("Quiz Submissions");
						}))
					.compSetSize(280, 35);
				if(this.getCurrentUser() instanceof Teacher) {
					panel
						.add(new Button("Create Quiz")
							.onClick((Panel __) -> {
								mainPanel.openModal("create-quiz");
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
			.setPanelSize(300, 300)
		);
		
		
		
		mainTabPanel.addTabPanel("Quiz List", (new Panel(new FlowLayout(FlowLayout.LEFT)))
			.onOpen((Panel p) -> {
				p.getMainPanel().removeAll();
				p.add((new Heading("Quiz List")).big());
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
			})
		.setPanelSize(1000, 720)
		.setMargin(64, 0));
		
		mainTabPanel.addTabPanel("Quiz Submissions", new Panel(new FlowLayout(FlowLayout.LEFT))
			.onOpen((Panel p) -> {
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
									.add(new Label(quiz.getName()))
									.add(new Label(user.getName()))
									//.add(new Label(gradedQuiz.getScore(quiz)))
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
						p.updateBounds();
					});
			})
			.setPanelSize(1000, 720)
			.setMargin(64, 0));
		
		mainTabPanel.openTabPanel("Quiz List");
		
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
		.add((new Panel(new FlowLayout()))
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
			}).panelize().setPanelSize(500, 100))
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
		lms.getNetworkManagerClient()
			.addPushHandler(new PushPacketHandler(ResponsePacket.class) {
				@Override
				public void handlePacket(ResponsePacket resp) {
					System.out.println("Push " + resp);
				}
			});
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
