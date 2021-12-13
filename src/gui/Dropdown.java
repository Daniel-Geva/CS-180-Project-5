package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The dropdown menu for the dropdown question type
 *
 * @author Isaac Fleetwood
 * @version December 12, 2021
 */
public class Dropdown extends JPanel {

	private String resultKey;
	private JLabel label;
	private JComboBox<String> jComboBox;
	private List<String> options;
	private List<Integer> optionsIds;

	public Dropdown(String resultKey, List<String> options, List<Integer> optionsIds) {
		this.resultKey = resultKey;
		String[] arr = new String[options.size()];
		for (int i = 0; i < options.size(); i++)
			arr[i] = options.get(i);
		this.jComboBox = new JComboBox<String>(arr);
		this.options = options;
		this.optionsIds = optionsIds;

		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.add(jComboBox);

		this.jComboBox.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jComboBox.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);

		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public Dropdown(String resultKey, String[] options) {
		this.resultKey = resultKey;
		this.jComboBox = new JComboBox<String>(options);

		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.add(jComboBox);

		this.jComboBox.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jComboBox.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);

		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public Dropdown(String resultKey, List<String> options) {
		this.resultKey = resultKey;
		String[] arr = new String[options.size()];
		for (int i = 0; i < options.size(); i++)
			arr[i] = options.get(i);
		this.jComboBox = new JComboBox<String>(arr);

		this.setLayout(new GridLayout(1, 1));
		this.add(jComboBox);

		this.jComboBox.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jComboBox.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);

		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public Dropdown(String name, String resultKey, String[] options) {
		this.label = new JLabel(name);
		this.resultKey = resultKey;
		this.jComboBox = new JComboBox<String>(options);

		this.setLayout(new GridLayout(2, 1));
		this.add(label);
		this.add(jComboBox);

		this.label.setForeground(Aesthetics.GENERAL_FOREGROUND);
		this.jComboBox.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jComboBox.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);

		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	/**
	 * Sets the size of the dropdown box
	 *
	 * @param x - Width of the dropdown
	 * @param y - Height of the dropdown
	 */
	@Override
	public void setSize(int x, int y) {
		super.setPreferredSize(new Dimension(x, y));
		super.setMinimumSize(new Dimension(x, y));
		super.setMaximumSize(new Dimension(x, y));
		super.setSize(x, y);
		if (label == null) {
			this.jComboBox.setSize(x, y);
		} else {
			this.label.setSize(x, y / 2);
			this.jComboBox.setSize(x, y / 2);
		}
	}

	/**
	 * Gets the selected option
	 *
	 * @return item - The selected item
	 */
	public String getSelection() {
		String item = (String) this.jComboBox.getSelectedItem();
		if (this.optionsIds == null) {
			return item;
		}
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).equals(item))
				return Integer.toString(optionsIds.get(i));
		}
		return item;
	}

	/**
	 * Gets the key of the selected item
	 *
	 * @return key - String key for a HashMap
	 */
	public String getResultKey() {
		return this.resultKey;
	}

	/**
	 * Sets the margin of the dropdown
	 *
	 * @param margin - Length of the separation for the margin; Used on all sides
	 * @return dropdown - Returns the dropdown item
	 */
	public Dropdown margin(int margin) {
		this.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
		return this;
	}

	/**
	 * Runs a callback when the selection is changed
	 *
	 * @param consumer - The callback function
	 * @return dropdown - Returns the dropdown item
	 */
	public Dropdown onChange(Consumer<String> consumer) {
		this.jComboBox.addActionListener((ActionEvent e) -> {
			consumer.accept(getSelection());
		});
		return this;
	}

	/**
	 * Encloses the dropdown into a panel to help with sizing
	 *
	 * @param width - Width of the panel
	 * @param height - Height of the panel
	 * @param marginX - Margin length of the panel
	 * @param marginY - Margin height of the panel
	 * @return panel - Returns the panel with the dropdown enclosed
	 */
	public Panel panelize(int width, int height, int marginX, int marginY) {
		this.setSize(width - marginX, height - marginY);
		return new Panel().add(this).setPanelSize(width, height).alignLeft().setMargin(marginX, marginY);
	}

	/**
	 * Changes the selected answer choice
	 *
	 * @param selection - The selected answer choice
	 */
	public void select(String selection) {
		this.jComboBox.setSelectedItem(selection);
	}

}
