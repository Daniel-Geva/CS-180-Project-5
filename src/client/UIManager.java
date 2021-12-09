package client;

import java.awt.GridBagLayout;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

import datastructures.Manager;
import datastructures.Teacher;
import datastructures.User;
import gui.Button;
import gui.Dropdown;
import gui.GapComponent;
import gui.GridBagBuilder;
import gui.Heading;
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

	Panel loginPanel;
	Panel mainPanel;
	
	@Override
	public void init() {
		this.loginPanel = new Panel();
		this.mainPanel = new Panel(new GridBagLayout());

		mainPanel.add((new Panel())
			/*.add()*/ /* Icon */
			.add((new Button("Quiz List"))
				.onClick((Panel p) -> {
					p.openTabPanel("Quiz List");
				}))
			.add((new Button("Quiz Submissions"))
				.onClick((Panel p) -> {
					p.openTabPanel("Quiz Submissions");
				}))
			.add((new Button("User Settings"))
				.onClick((Panel p) -> {
					p.openTabPanel("User Settings");
				}))
			.add((new Button("Logout"))
				.onClick((Panel p) -> {
					mainPanel.close();
					loginPanel.open();
				}))
		, GridBagBuilder.start().left().stretchY().build());
		mainPanel.addTabPanel("Quiz List", (new Panel())
			.onOpen((Panel p) -> {
				
			})
		);
		
		loginPanel.addModal("Create User", (new Panel())
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
			).setPanelSize(300, 350)
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
		.setPanelSize(500, 400)
		.setMargin(100, 100);
	}

	@Override
	public void exit() {
		
	}
	
	public void run() {
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
