package ui;

import java.util.ArrayList;

import client.UIManager;

/**
 * {@link Menu} in which options are given that the user can choose from.
 * <p>
 * Works in the form of printing the headings, and then giving the options 
 * in the form of a numbered list, where the user can select the option by 
 * inputting the number.
 * <p>
 * Use {@link #addHeading(String)} and {@link #addSubheading(String)} to 
 * add a string to be printed before the options are presented.
 * <p>
 * And use {@link #addOption(MenuOption)} to add an option to the menu.
 * If you want an option to be visible only at certain times, use 
 * {@link MenuOption#addVisibilityCondition(RunnableCheckCondition)}
 * <p>
 * When an option is selected {@link MenuOption#onSelect()} is ran.
 * Use {@link MenuOption#onSelect(RunnableSelectEvent)} to set the callback function
 * for when that specific option is selected.
 * <p>
 * See {@link OptionMenuWithResult} and {@link OptionMenuYesNo} for classes
 * similar to this one.
 * <p>
 * See {@link OptionListMenu} for an extension that specifically list quizzes
 * as options to select from, using a pagination format.
 * 
 * @author Isaac Fleetwood
 * @version 1.0.0
 * @see Menu
 * @see MenuOption
 */
public class OptionMenu extends Menu {

	UIManager uiManager;
	
	ArrayList<String> headings;
	ArrayList<MenuOption> options;
	Runnable callbackOnHeadingPrint;
	boolean checkLogin;

	public OptionMenu(UIManager uiManager) {
		this.uiManager = uiManager;
		options = new ArrayList<MenuOption>();
		headings = new ArrayList<String>();
		this.checkLogin = false;
	}

	public OptionMenu setCheckLogin(boolean checkLoginBool) {
		this.checkLogin = checkLoginBool;
		return this;
	}
	
	public OptionMenu addHeading(String heading) {
		headings.add("\n" + ANSICodes.BOLD + heading + ANSICodes.RESET);
		return this;
	}

	public OptionMenu setHeadings(ArrayList<String> headingsArr) {
		this.headings = headingsArr;
		return this;
	}

	public OptionMenu addSubheading(String subheading) {
		headings.add(subheading);
		return this;
	}

	public OptionMenu addOption(MenuOption option) {
		this.options.add(option);
		return this;
	}

	public OptionMenu onHeadingPrint(Runnable runnable) {
		this.callbackOnHeadingPrint = runnable;
		return this;
	}

	@Override
	public void runMenu() {
		if (this.checkLogin && uiManager.getCurrentUser() == null) {
			this.menuState = MenuState.CLOSE;
			return;
		}
		for (String heading : headings) {
			System.out.println(heading);
		}
		if (this.callbackOnHeadingPrint != null)
			this.callbackOnHeadingPrint.run();
		// Copy references, so that they can be safely filtered.
		ArrayList<MenuOption> visibleOptions = new ArrayList<MenuOption>(options);
		visibleOptions.removeIf((option) -> {
			return !option.isVisible();
		});

		for (int i = 0; i < visibleOptions.size(); i++) {
			MenuOption option = visibleOptions.get(i);
			int optionNumber = i + 1;
			System.out.println(
			    ANSICodes.CYAN + ANSICodes.BOLD + optionNumber + ": " + ANSICodes.RESET + option.getOptionString());
		}
		MenuOption option = null;
		while (option == null) {
			String input = uiManager.getScanner().nextLine();
			if (!isInteger(input)) {
				System.out.println("Please use a valid integer when selecting an option.");
				continue;
			}
			int i = Integer.parseInt(input);
			if (i <= 0 || i > visibleOptions.size()) {
				System.out.println("Please select a valid option.");
				continue;
			}
			option = visibleOptions.get(i - 1);
			break;
		}
		System.out.print(ANSICodes.CLEAR_SCREEN + ANSICodes.CURSOR_TO_HOME);
		menuState = option.onSelect();
	}
	
	public boolean isInteger(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}