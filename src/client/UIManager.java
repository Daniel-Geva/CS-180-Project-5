package client;

import java.awt.BorderLayout;
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

import datastructures.Manager;
import datastructures.Question;
import datastructures.Quiz;
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

public class UIManager implements Manager {

	LearningManagementSystemClient lms;
	User currentUser;
	
	public UIManager(LearningManagementSystemClient lms) {
		this.lms = lms;
	}

	Panel hostnameMenu;
	Panel errorMenu;
	Panel loginPanel;
	Panel mainPanel;
	Panel mainTabPanel;
	
	private List<String> getCourses(List<Quiz> quizzes) {
		ArrayList<String> courses = new ArrayList<String>();
		// TODO Get courses
		return courses;
	}
	
	private List<Quiz> getQuizzesFromCourse(List<Quiz> quizzes, String course) {
		// TODO Auto-generated method stub
		return quizzes;
	}
	
	@Override
	public void init() {
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
			.add((new Button("Quiz Submissions"))
				.onClick((Panel p) -> {
					mainTabPanel.openTabPanel("Quiz Submissions");
				}))
			.add((new Button("User Settings"))
				.onClick((Panel p) -> {
					mainTabPanel.openTabPanel("User Settings");
				}))
			.add((new Button("Logout"))
				.onClick((Panel p) -> {
					mainPanel.close();
					loginPanel.open();
				}))
		.setPanelSize(280, 720));
		
		mainPanel.add(mainTabPanel
			.setPanelSize(1000, 720)
			.setMargin(50, 50));
		//GridBagBuilder.start().left().stretchY().build()
		
		mainTabPanel.addTabPanel("Quiz List", (new Panel())
			.boxLayout(BoxLayout.Y_AXIS)
			.onOpen((Panel p) -> {
				List<Quiz> quizzes = new ArrayList<Quiz>();
				quizzes.add(new Quiz("Quiz Name", "Author", 0, new ArrayList<Question>(), false, "Course"));
				List<String> courses = new ArrayList<String>();
				courses.add("Course");
				
				for(String course: courses) {
					p.add(new Heading(course), GridBagBuilder.start().top().stretchX().build());
					JScrollPane scroll = new JScrollPane();
					List<Quiz> quizzesCourse = getQuizzesFromCourse(quizzes, course);
					Panel p2 = new Panel();
					p2.boxLayout(BoxLayout.Y_AXIS);
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
						//panel.setBackground(Color.BLACK);
						//panel.setPanelSize(400, 100);
						p.add(panel);
						
					}
					scroll.add(p2);
					//p.add(scroll);
				}
				
				p.revalidate();
			})
		.setPanelSize(1000, 720)
		.setMargin(64, 64));
		
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
						System.out.println("Test");
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
	}

	@Override
	public void exit() {
		
	}
	
	public void run() {
		this.loginPanel.open();
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
