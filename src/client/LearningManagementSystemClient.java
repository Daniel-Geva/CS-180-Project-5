package client;

import client.UIManager;

/**
 * 
 * Main class that ties all the systems together.
 * Initializes each manager and provides access to them.
 * When the UI has exited, it safely exits all managers
 *   to clean up any loose ends.
 * 
 * @author Isaac Fleetwood
 * @version 1.0.0
 */
public class LearningManagementSystemClient {

	private UIManager uiManager;
	
	/**
	 * Main method. Initializes the LMS instance
	 * and runs the program.
	 */
	public static void main(String[] args) {
		LearningManagementSystemClient lms = new LearningManagementSystemClient();
		lms.init();
		lms.run();
		lms.exit();
	}
	
	/**
	 * Instantiates each manager and passes them
	 * an instance of LMS to be able to access 
	 * all other managers.
	 */
	public LearningManagementSystemClient() {
		uiManager = new UIManager(this);
	}

	/**
	 * Initializes each manager before the user can
	 * interact with the program.
	 */
	public void init() {
		uiManager.init();
	}
	
	/**
	 * Runs the program. UIManager will
	 * run the UI loop here until the UI
	 * exits.
	 */
	public void run() {
		uiManager.run();
	}
	
	/**
	 * Notifies all the managers that
	 * the program is exiting.
	 */
	public void exit() {
		uiManager.exit();
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}
		
}
