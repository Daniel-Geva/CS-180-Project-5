package client;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import client.NetworkManagerClient.NameSetter;
import datastructures.Manager;
import datastructures.Question;
import datastructures.Quiz;
import datastructures.Student;
import datastructures.Teacher;
import datastructures.User;
import gui.Aesthetics;
import gui.Button;
import gui.Dropdown;
import gui.GapComponent;
import gui.GridBagBuilder;
import gui.Heading;
import gui.Label;
import gui.Panel;
import gui.TextField;
import packets.request.CreateUserRequestPacket;
import packets.request.LoginUserRequestPacket;
import packets.response.NewUserResponsePacket;
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
	User currentUser;
	
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
		// TODO Get courses
		return courses;
	}
	
	private List<Quiz> getQuizzesFromCourse(List<Quiz> quizzes, String course) {
		return quizzes.stream().filter((Quiz q) -> (
			q.getCourse().equals(course)
		)).toList();
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
		
		mainPanel.add((new Panel())
			/*.add()*/ /* Icon */
			.add((new Button("Quiz List"))
				.onClick((Panel p) -> {
					mainTabPanel.openTabPanel("Quiz List");
				}))
			.compSetSize(280, 35)
			.add((new Button("Quiz Submissions"))
				.onClick((Panel p) -> {
					mainTabPanel.openTabPanel("Quiz Submissions");
				}))
			.compSetSize(280, 35)
			.add((new Button("User Settings"))
				.onClick((Panel p) -> {
					mainTabPanel.openTabPanel("User Settings");
				}))
			.compSetSize(280, 35)
			.add((new Button("Logout"))
				.onClick((Panel p) -> {
					mainPanel.close();
					loginPanel.open();
				}))
			.compSetSize(280, 35)
		.setPanelSize(280, 720));
		
		mainPanel.add(mainTabPanel
			.setPanelSize(1000, 720)
			.setMargin(50, 50));
		//GridBagBuilder.start().left().stretchY().build()
		
		mainTabPanel.addTabPanel("Quiz List", (new Panel(new FlowLayout(FlowLayout.LEFT)))
			.onOpen((Panel p) -> {
				p.add((new Heading("Quiz List")).big());
				List<Quiz> quizzes = new ArrayList<Quiz>();
				quizzes.add(new Quiz("Quiz Name", "Author", 0, new ArrayList<Question>(), false, "Course"));
				quizzes.add(new Quiz("Quiz 2", "Author", 0, new ArrayList<Question>(), false, "Course"));
				quizzes.add(new Quiz("Quiz 3", "Author", 0, new ArrayList<Question>(), false, "Course"));
				quizzes.add(new Quiz("Quiz 4", "Author", 0, new ArrayList<Question>(), false, "Course"));
				
				quizzes.add(new Quiz("Quiz Name", "Author", 0, new ArrayList<Question>(), false, "CS 180"));
				quizzes.add(new Quiz("Quiz 2", "Author", 0, new ArrayList<Question>(), false, "CS 180"));
				quizzes.add(new Quiz("Quiz 3", "Author", 0, new ArrayList<Question>(), false, "CS 182"));
				quizzes.add(new Quiz("Quiz 4", "Author", 0, new ArrayList<Question>(), false, "CS 182"));
				
				List<String> courses = new ArrayList<String>();
				courses.add("Course");
				courses.add("CS 180");
				courses.add("CS 182");
				
				for(String course: courses) {
					p.add(new Heading(course));
					p.compSetSize(1000, 80);
					
					
					List<Quiz> quizzesCourse = getQuizzesFromCourse(quizzes, course);
					
					for(Quiz quiz: quizzesCourse) {
						Panel panel = (new Panel())
							.add(new Label(quiz.getName()))
							.add(new Label("Author: " + quiz.getAuthor()))
							.add(new Label("Questions: " + quiz.getQuestions().size()))
							.onClick((Panel __) -> {
								mainPanel.addModal("Quiz-" + quiz.getId(), 
									(new Panel())
									.boxLayout(BoxLayout.Y_AXIS)
									.add(new Heading(quiz.getName()))
									.add(new Label("Course: " + quiz.getCourse()))
									.add(new Label("Author: " + quiz.getAuthor()))
									.add((new Panel())
										.boxLayout(BoxLayout.X_AXIS) 
										.add((new Button("Take Quiz"))
											.onClick((Panel __2) -> {
												
											}))
										.add((new Button("Cancel"))
											.onClick((Panel __2) -> {
												mainPanel.closeModal();
											}))
										.setPanelSize(300, 48)
									)
									.setPanelSize(400, 500)
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
			})
		.setPanelSize(1000, 720)
		.setMargin(64, 0));
		
		mainTabPanel.openTabPanel("Quiz List");
		
		loginPanel.addModal("Create User", (new Panel(new GridLayout(6, 1)))
			.add(new Heading("Create User"))
			.add(new TextField("Name"))
			.add(new TextField("Username"))
			.add(new TextField("Password"))
			.add(new Dropdown("User Type", new String[] {
				"Student", "Teacher"
			}))
			.add((new Panel(new GridBagLayout()))
				.add((new Button("Cancel"))
					.onClick((Panel p) -> {
						loginPanel.closeModal();
					}), GridBagBuilder.start().left().build())
				.add((new Button("Create User"))
					.onClick((Panel p) -> {
						Map<String, String> result = p.getResultMap();
						String name = result.get("Name");
						String username = result.get("Username");
						String password = result.get("Password");
						String userType = result.get("User Type");
						// TODO Get ID or send raw data and create user on server with id.
						User user = null;
						switch(userType) {
						case "Student":
							user = new Student(0, name, username, password);
							break;
						case "Teacher":
							user = new Teacher(0, name, username, password);
							break;
						}
						System.out.println(user);
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
									"Unable to create the user. Please try again.",
									"Error",
									JOptionPane.ERROR_MESSAGE
								);
							}
						});
					})
				, GridBagBuilder.start().right().build())
			).setPanelSize(350, 400)
		);
		
		loginPanel
		.add(new Heading("Darkspace"))
		.add(new GapComponent())
		.add(new TextField("Username"))
		.add(new TextField("Password"))
		.add((new Panel(new GridBagLayout()))
			.add((new Button("Create User"))
					.onClick((Panel p) -> {
						// Open Create User menu.
						loginPanel.openModal("Create User");
					}), GridBagBuilder.start().left().build())
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
							// TODO Open main menu.
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
				}), GridBagBuilder.start().right().build())
		)
		.setPanelSize(500, 600)
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
				/*
				nameSetter.setSuccessRunnable(() -> {
					connectionStatusPanel.close();
					loginPanel.open();
				});
				*/
				synchronized(nameSetter) {
					nameSetter.setName(ip);
					nameSetter.notify();
				}
				System.out.println("Finish");
			}).panelize())
		.setPanelSize(500, 300)
		.setMargin(100, 100);
		
	}

	@Override
	public void exit() {
		
	}
	
	public void run() {
		// TODO hostnamePanel
		this.mainPanel.open();
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
