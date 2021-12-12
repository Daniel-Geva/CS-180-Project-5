package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * 
 * The main class for the GUI framework.
 * Is used to contain GUI elements and encapsulate them into
 * searchable and identifiable pieces.
 * 
 * Contains modals and tabs to allow for a dynamic structure.
 * 
 * Also contains 
 * 
 * @author Isaac Fleetwood
 * @author Aryan Jain
 * 
 * @version 1.0.0
 */
public class Panel extends JLayeredPane {

	private JFrame frame;
	
	private JComponent prevComponent;

	public int scrollHeight;
	
	private int prefWidth;
	private int prefHeight;

	private List<DynamicLabel> dynamicLabels;
	private List<TextField> textFields;
	private List<Button> buttons;
	private List<Dropdown> dropdowns;
	private List<RadioButton> radioButtons;

	private JPanel mainPanel;
	private Map<String, Panel> tabPanels;
	private String currentTabId;
	private Map<String, Panel> modals;
	private String currentModalId;

	private PanelRunnable onOpenRunnable;

	public Panel(LayoutManager layout) {
		this();
		mainPanel.setLayout(layout);
	}

	public Panel() {
		this.mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		this.alignLeft();
		this.alignTop();
		
		super.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
		
		this.dynamicLabels = new ArrayList<DynamicLabel>();
		this.textFields = new ArrayList<TextField>();
		this.buttons = new ArrayList<Button>();
		this.dropdowns = new ArrayList<Dropdown>();
		this.radioButtons = new ArrayList<RadioButton>();

		this.tabPanels = new HashMap<String, Panel>();
		this.modals = new HashMap<String, Panel>();
		
		this.scrollHeight = 0;
		
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
		mainPanel.setBackground(Aesthetics.GENERAL_BACKGROUND);
		mainPanel.setForeground(Aesthetics.GENERAL_FOREGROUND);
	}

	public JPanel getMainPanel() {
		return this.mainPanel;
	}

	public List<DynamicLabel> getDynamicLabels() {
		return this.dynamicLabels;
	}

	public List<Button> getButtons() {
		return this.buttons;
	}

	public List<TextField> getTextFields() {
		return this.textFields;
	}

	public List<Dropdown> getDropdowns() {
		return this.dropdowns;
	}

	private List<RadioButton> getRadioButtons() {
		return this.radioButtons;
	}
	
	public Panel setPanelSize(int wid, int hei) {
		Dimension d = new Dimension(wid, hei);
		this.prefWidth = wid;
		this.prefHeight = hei;
		super.setSize(d);
		super.setPreferredSize(d);
		super.setMaximumSize(d);
		super.setMinimumSize(d);
		return this;
	}

	public void searchContainer(Container component) {
		if (component instanceof Button) {
			this.buttons.add((Button) component);
		} else if (component instanceof TextField) {
			this.textFields.add((TextField) component);
		} else if (component instanceof Dropdown) {
			this.dropdowns.add((Dropdown) component);
		} else if (component instanceof RadioButton) {
			this.radioButtons.add((RadioButton) component);
		} else if (component instanceof DynamicLabel) {
			this.dynamicLabels.add((DynamicLabel) component);
		} else {
			for(Component c: component.getComponents()) {
				if(c instanceof Container) {
					searchContainer((Container) c);
				}
			}
		}
	}
	
	public Panel add(JComponent component) {
		prevComponent = component;
		searchContainer(component);
		mainPanel.add(component);
		return this;
	}
	
	public Panel add(JComponent component, Object constraints) {
		prevComponent = component;
		searchContainer(component);
		mainPanel.add(component, constraints);
		return this;
	}
	
	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
		this.mainPanel.setBackground(c);
	}

	@Override
	public void setLayout(LayoutManager layout) {
		if(mainPanel != null)
			mainPanel.setLayout(layout);
	}
	
	@Override
	public void paint(Graphics g) {
		this.updateBounds();
		super.paint(g);
	}
	
	public Panel boxLayout(int boxlayout) {
		//this.setLayout(new BoxLayout(this, boxlayout));
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, boxlayout));
		return this;
	}

	public boolean containsComponent(Container container, Component component) {
		for(Component c: container.getComponents()) {
			if(c == component)
				return true;
			if(c instanceof Container)
				if(containsComponent((Container) c, component))
					return true;
		}
		return false;
	}
	
	private boolean isPanelOpen(Panel panel) {
		if(containsComponent(mainPanel, panel)) {
			return this.currentModalId == null;
		}
		if(this.currentModalId != null) {
			Panel p = this.modals.get(this.currentModalId);
			if(p != null && containsComponent(p, panel))
				return true;
		}
		if(this.currentTabId != null) {
			Panel p = this.tabPanels.get(this.currentTabId);
			if(p != null && containsComponent(p, panel))
				return true;
		}
		return false;
	}

	public void updateBounds() {
		int wid = this.getWidth();
		int hei = this.getHeight();
		
		if(this.prefWidth != 0) {
			int x = (int) ((wid-this.prefWidth)*this.getAlignmentX());
			int y = (int) ((hei-this.prefHeight)*this.getAlignmentY()) - this.scrollHeight;
			mainPanel.setBounds(x, y, prefWidth, prefHeight + this.scrollHeight);
		} else {
			mainPanel.setBounds(0, 0, wid, hei);
		}
		
		for (Panel p : tabPanels.values()) {
			p.setBounds(0, 0, wid, hei);
			p.updateBounds();
		}
		
		for (Panel p : modals.values()) {
			p.setBounds(0, 0, wid, hei);
			p.updateBounds();
		}
		
		for (Component c : mainPanel.getComponents()) {
			if (c instanceof Panel) {
				Panel p = (Panel) c;
				p.updateBounds();
			}
		}
		
	}
	
	public void openTabPanel(String id) {
		if (this.currentTabId != null) {
			Panel p = tabPanels.get(this.currentTabId);
			if(p != null)
				tabPanels.get(this.currentTabId).setVisible(false);
		}
		this.currentTabId = id;
		Panel p = tabPanels.get(this.currentTabId);
		if(p == null) {
			System.out.println("Tab " + this.currentTabId + " does not exist.");
			return;
		}
		p.setVisible(true);
	}

	public void openModal(String id) {
		if (this.currentTabId != null) {
			this.modals.get(this.currentModalId).setVisible(false);
		}
		this.currentModalId = id;
		Panel p = this.modals.get(this.currentModalId);
		if(p == null) {
			System.out.println("Modal " + currentModalId + " does not exist.");
			return;
		}
		p.setBounds(0, 0, this.getWidth(), this.getHeight());
		p.updateBounds();
		p.setVisible(true);
		this.revalidate();
	}

	public void closeModal() {
		if (this.currentModalId != null) {
			this.modals.get(this.currentModalId).setVisible(false);
			this.currentModalId = null;
		}
	}

	public Panel addModal(String id, Panel panel) {
		if(this.modals.containsKey(id)) {
			Panel p = this.modals.get(id);
			this.modals.remove(id);
			super.remove(p);
		}
		panel.setVisible(true);
		panel.mainPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.BLACK, 2),
			BorderFactory.createEmptyBorder(50, 50, 50, 50)
		));

		Panel p = new Panel(new GridBagLayout());
		p.setBackground(Aesthetics.MODAL_BACKGROUND);
		p.add(panel, new GridBagConstraints());
		p.setVisible(false);
		p.registerListeners();
		
		super.add(p, JLayeredPane.MODAL_LAYER);
		this.modals.put(id, p);

		return this;
	}

	public Panel addTabPanel(String id, Panel panel) {
		if(this.tabPanels.containsKey(id)) {
			Panel p = this.tabPanels.get(id);
			this.tabPanels.remove(id);
			super.remove(p);
		}
		this.tabPanels.put(id, panel);
		panel.setVisible(false);
		this.add(panel);
		return this;
	}
	
	public Panel setMargin(int x, int y) {
		mainPanel.setBorder(BorderFactory.createEmptyBorder(y>>1, x>>1, y>>1, x>>1));
		return this;
	}

	public void registerListeners() {
		final Panel panel = this;
		for (Button b : this.buttons) {
			if(b.getActionListeners().length == 0) {
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						b.getClickRunnable().run(panel);
					}
				});
			}
		}
	}
	
	@Override
	public void revalidate() {
		super.revalidate();
		this.registerListeners();
	}
	
	public void registerDebug(Container c) {
		for(Component c2: c.getComponents()) {
			if(c2 instanceof Container) {
				this.registerDebug((Container) c2);
			}
			c2.addMouseListener(DebugListener.INST);
		}
	}

	public void open() {
		this.runOnOpen();
		this.updateBounds();
		this.registerListeners();
		
		JFrame f = new JFrame();
		f.setSize(prefWidth, prefHeight);
		f.setLocationRelativeTo(null);
		f.setBackground(Color.DARK_GRAY);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new GridLayout(1,1));
		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
		f.setName("Darkspace");
		this.frame = f;
		
		this.setBounds(0, 0, prefWidth, prefHeight);
		registerDebug(this);
	}
	
	public void close() {
		this.frame.setVisible(false);
	}

	public Map<String, String> getResultMap() {
		Map<String, String> resultMap = new HashMap<String, String>();
		for (TextField textField : this.getTextFields()) {
			resultMap.put(textField.getResultKey(), textField.getText());
		}
		for (Dropdown dropdown : this.getDropdowns()) {
			resultMap.put(dropdown.getResultKey(), dropdown.getSelection());
		}
		for (RadioButton radioButton : this.getRadioButtons()) {
			if(radioButton.isSelected()) {
				resultMap.put(radioButton.getResultKey(), radioButton.getSelectionId());
			}
		}
		return resultMap;
	}

	public Panel compSetSize(int i, int j) {
		this.prevComponent.setMaximumSize(new Dimension(i, j));
		this.prevComponent.setMinimumSize(new Dimension(i, j));
		this.prevComponent.setPreferredSize(new Dimension(i, j));
		this.prevComponent.setSize(i, j);
		return this;
	}

	public Panel onOpen(PanelRunnable panelRunnable) {
		this.onOpenRunnable = panelRunnable;
		return this;
	}

	public void searchOnOpen(Container container, int i) {
		for(Component c: container.getComponents()) {
			if(c instanceof Panel) {
				Panel panel = (Panel) c;
				panel.runOnOpen();
				continue;
			}
			if(c instanceof Container) {
				searchOnOpen((Container) c, i+1);
			}
		}
	}
	
	public void runOnOpen() {
		if (this.onOpenRunnable != null)
			this.onOpenRunnable.run(this);
		searchOnOpen(this, 0);
	}

	public String getInput(String key) {
		for (TextField textField : this.getTextFields()) {
			if(textField.getResultKey().equals(key)) {
				return textField.getText();
			}
		}
		for (Dropdown dropdown : this.getDropdowns()) {
			if(dropdown.getResultKey().equals(key)) {
				return dropdown.getSelection();
			}
		}
		for (RadioButton radioButton : this.getRadioButtons()) {
			if(radioButton.getResultKey().equals(key) && radioButton.isSelected()) {
				return radioButton.getSelectionId();
			}
		}
		return "";
	}

	public void setInput(String key, String value) {
		for (TextField textField : this.getTextFields()) {
			if(textField.getResultKey().equals(key)) {
				textField.setText(value);
			}
		}
	}
	
	public void registerClick(Container c, MouseListener l) {
		for(Component c2: c.getComponents()) {
			if(c2 instanceof Container) {
				registerClick((Container) c2, l);
			}
			c2.addMouseListener(l);
		}
	}
	
	public Panel onClick(Panel parent, PanelRunnable runnable) {
		final Panel panel = this;
		registerClick(this, new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mainPanel.setBackground(Aesthetics.CLICKABLE_HOVER_BG_COLOR);
				mainPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				mainPanel.setBackground(Aesthetics.GENERAL_BACKGROUND);
				mainPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!parent.isPanelOpen(panel)) return;
				runnable.run(panel);
			}
		});
		return this;
	}

	public void clear() {
		this.getMainPanel().removeAll();
		this.dropdowns.clear();
		this.radioButtons.clear();
		this.buttons.clear();
		this.textFields.clear();
	}

	public void refreshComponents() {
		this.dropdowns.clear();
		this.radioButtons.clear();
		this.buttons.clear();
		this.textFields.clear();
		searchContainer(this.getMainPanel());
	}

	public void setDynamicLabel(String id, String text) {
		for(DynamicLabel label: this.getDynamicLabels()) {
			if(label.getId().equals(id)) {
				label.setText(text);
			}
		}
	}

	public JComponent getPreviousComponent() {
		return this.prevComponent;
	}

	public Panel alignLeft() {
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		return this;
	}

	public Panel alignTop() {
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		return this;
	}
	
	public Panel alignRight() {
		this.setAlignmentX(1.0f);
		return this;
	}

	public Panel alignBottom() {
		this.setAlignmentY(1.0f);
		return this;
	}

	public Panel scrollize() {
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				scrollHeight += e.getUnitsToScroll()*7;
				if(scrollHeight < 0)
					scrollHeight = 0;
				updateBounds();
				revalidate();
			}
			
		});
		return this;
	}
	
}