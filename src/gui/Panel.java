package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Panel extends JLayeredPane {
	
	private static final long serialVersionUID = -5199098153231935544L;
	
	JComponent prevComponent;
	
	List<TextField> textFields;
	List<Button> buttons;
	List<Dropdown> dropdowns;
	
	JPanel mainPanel;
	Map<String, Panel> tabPanels;
	String currentTabId;
	Map<String, Panel> modals;
	String currentModalId;

	private PanelRunnable onOpenRunnable;
	
	public Panel(LayoutManager layout) {
		this();
		mainPanel.setLayout(layout);
	}
	
	public Panel() {
		this.mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 1));
		this.setLayout(new GridLayout(0, 1));
		super.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
		this.textFields = new ArrayList<TextField>();
		this.buttons = new ArrayList<Button>();
		this.dropdowns = new ArrayList<Dropdown>();
		
		this.tabPanels = new HashMap<String, Panel>();
		this.modals = new HashMap<String, Panel>();
	}
	
	public void selectPanel(String id) {
		if(this.currentTabId != null) {
			tabPanels.get(this.currentTabId).setVisible(false);
		}
		this.currentTabId = id;
		this.tabPanels.get(this.currentTabId).setVisible(true);
	}

	public void openTabPanel(String string) {
		this.selectPanel(string);
	}
	
	public void openModal(String id) {
		if(this.currentTabId != null) {
			this.modals.get(this.currentModalId).setVisible(false);
		}
		this.currentModalId = id;
		this.modals.get(this.currentModalId).setVisible(true);
	}
	
	public void closeModal() {
		if(this.currentTabId != null) {
			this.modals.get(this.currentModalId).setVisible(false);
			this.currentModalId = null;
		}
	}
	
	public Panel add(JComponent component) {
		prevComponent = component;
		if(component instanceof Panel) {
			Panel p = (Panel) component;
			this.buttons.addAll(p.getButtons());
			this.textFields.addAll(p.getTextFields());
			this.dropdowns.addAll(p.getDropdowns());
		} else if(component instanceof Button) {
			this.buttons.add((Button) component);
		} else if(component instanceof TextField) {
			this.textFields.add((TextField) component);
		} else if(component instanceof Dropdown) {
			this.dropdowns.add((Dropdown) component);
		}
		mainPanel.add(component);
		// TODO Debug
		component.addMouseListener(DebugListener.INST);
		return this;
	}
	
	public Panel add(JComponent component, Object constraints) {
		prevComponent = component;
		if(component instanceof Panel) {
			Panel p = (Panel) component;
			this.buttons.addAll(p.getButtons());
			this.textFields.addAll(p.getTextFields());
			this.dropdowns.addAll(p.getDropdowns());
		} else if(component instanceof Button) {
			this.buttons.add((Button) component);
		} else if(component instanceof TextField) {
			this.textFields.add((TextField) component);
		} else if(component instanceof Dropdown) {
			this.dropdowns.add((Dropdown) component);
		}
		mainPanel.add(component, constraints);
		// TODO Debug
		component.addMouseListener(DebugListener.INST);
		return this;
	}
	
	public Panel addModal(String id, Panel panel) {
		this.modals.put(id, panel);
		panel.setVisible(false);
		Panel p = new Panel();
		p.setBackground(Colors.MODAL_BACKGROUND);
		p.setLayout(new GridBagLayout());
		p.add(panel);
		this.add(p, JLayeredPane.MODAL_LAYER);
		return this;
	}
	
	public Panel addTabPanel(String id, Panel panel) {
		this.tabPanels.put(id, panel);
		panel.setVisible(false);
		this.add(panel);
		return this;
	}
	
	public void open() {
		Frame f = new Frame();
		final Panel panel = this;
		for(Button b: this.buttons) {
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					b.getClickRunnable().run(panel);
				}
			});
		}
		f.add(this);
		f.setVisible(true);
	}
	
	public Map<String, String> getResultMap() {
		Map<String, String> resultMap = new HashMap<String, String>();
		for(TextField textField: this.getTextFields()) {
			resultMap.put(textField.getResultKey(), textField.getText());
		}
		for(Dropdown dropdown: this.getDropdowns()) {
			resultMap.put(dropdown.getResultKey(), dropdown.getSelection());
		}
		return resultMap;
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

	public Panel compSetSize(int i, int j) {
		this.prevComponent.setPreferredSize(new Dimension(i, j));
		this.prevComponent.setSize(i, j);
		return this;
	}
	
	public Panel setPanelSize(int wid, int hei) {
		Dimension d = new Dimension(wid, hei);
		super.setSize(d);
		super.setPreferredSize(d);
		return this;
	}

	public void close() {
		this.setVisible(false);
	}

	public Panel onOpen(PanelRunnable panelRunnable) {
		this.onOpenRunnable = panelRunnable;
		return this;
	}

	public void runOnOpen() {
		this.onOpenRunnable.run(this);
		for(Component comp: this.getComponents()) {
			if(comp instanceof Panel) {
				Panel p = (Panel) comp;
				p.runOnOpen();
			}
		}
	}
	
	
	// TODO Click Event for Panel (clickable card)
	
}
