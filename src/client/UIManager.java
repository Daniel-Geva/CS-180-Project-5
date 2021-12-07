package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import datastructures.Manager;
import datastructures.Teacher;
import datastructures.User;
import gui.Button;
import gui.Dropdown;
import gui.GapComponent;
import gui.GridBagPosition;
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

	@Override
	public void init() {
		
	}

	@Override
	public void exit() {
		
	}
	
	public void run() {

		JFrame mainFrame = new JFrame();
		JFrame loginFrame = new JFrame();
		
		Panel mainPanel = new Panel();
		
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
					mainFrame.setVisible(false);
					loginFrame.setVisible(true);
				}))
		);
		mainPanel.addTabPanel("Quiz List", (new Panel())
			.onOpen((Panel p) -> {
				
			})
		);
		
		Panel panel = (new Panel());
		panel.addModal("Create User", (new Panel())
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
						panel.closeModal();
					}), GridBagPosition.LEFT.get())
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
								panel.closeModal();
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
				)
			)
		);
		
		panel
			.add(new TextField("Username"))
			.add(new TextField("Password"))
			.add(new GapComponent(20))
				.compSetSize(100, 20)
			.add((new Panel(new GridBagLayout()))
				.add((new Button("Create User"))
						.onClick((Panel p) -> {
							// Open Create User menu.
							panel.openModal("Create User");
						}), GridBagPosition.LEFT.get())
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
								loginFrame.setVisible(false);
								mainFrame.setVisible(true);
							}
						});
					}), GridBagPosition.RIGHT.get())
				.setPanelSize(600, 20)
			)
			.setPanelSize(400, 300);
		
		loginFrame.setLayout(new GridBagLayout());
		loginFrame.add(panel, new GridBagConstraints());
		
		loginFrame.setSize(600, 400);
		loginFrame.setVisible(true);

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
