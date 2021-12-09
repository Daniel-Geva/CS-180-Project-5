package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Panel extends JLayeredPane {

	private static final long serialVersionUID = -5199098153231935544L;

	JComponent prevComponent;

	int prefWidth;
	int prefHeight;
	
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

	@Override
	public void setLayout(LayoutManager layout) {
		super.setLayout(layout);
		if(mainPanel != null)
			mainPanel.setLayout(layout);
	}
	
	@Override
	public void paint(Graphics g) {
		if(this.currentModalId != null) {
			Panel p = this.modals.get(this.currentModalId);
			p.setBounds(0, 0, this.getWidth(), this.getHeight());
		}
		this.updateBounds();
		super.paint(g);
	}

	public void updateBounds() {
		/*
		System.out.println(this.getPreferredSize() + " " + this.getBounds());
		mainPanel.setPreferredSize(this.getPreferredSize());
		if(this.prefWidth != 0)
			mainPanel.setSize(this.getPreferredSize());
		System.out.println(this.getPreferredSize() + " " + this.getBounds());
		*/
		/*
		if(this.getPreferredSize() != null) {
			mainPanel.setPreferredSize(this.getPreferredSize());
			mainPanel.setBounds(0, 0, (int) this.getPreferredSize().getWidth(), (int) this.getPreferredSize().getHeight());
		} else {
			*/
		//}
		int wid = this.getWidth();
		int hei = this.getHeight();
		if(this.prefWidth != 0) {
			mainPanel.setBounds(wid/2 - this.prefWidth/2, hei/2 - this.prefHeight/2, prefWidth, prefHeight);
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

	public Panel() {
		this.mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0,1));
		//this.setLayout(new GridLayout(1,1));
		super.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
		this.textFields = new ArrayList<TextField>();
		this.buttons = new ArrayList<Button>();
		this.dropdowns = new ArrayList<Dropdown>();

		this.tabPanels = new HashMap<String, Panel>();
		this.modals = new HashMap<String, Panel>();
		
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
		
	}

	public void selectPanel(String id) {
		if (this.currentTabId != null) {
			tabPanels.get(this.currentTabId).setVisible(false);
		}
		this.currentTabId = id;
		this.tabPanels.get(this.currentTabId).setVisible(true);
	}

	public void openTabPanel(String string) {
		this.selectPanel(string);
	}

	public void openModal(String id) {
		if (this.currentTabId != null) {
			this.modals.get(this.currentModalId).setVisible(false);
		}
		this.currentModalId = id;
		Panel p = this.modals.get(this.currentModalId);
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

	public Panel add(JComponent component) {
		prevComponent = component;
		if (component instanceof Panel) {
			Panel p = (Panel) component;
			this.buttons.addAll(p.getButtons());
			this.textFields.addAll(p.getTextFields());
			this.dropdowns.addAll(p.getDropdowns());
		} else if (component instanceof Button) {
			this.buttons.add((Button) component);
		} else if (component instanceof TextField) {
			this.textFields.add((TextField) component);
		} else if (component instanceof Dropdown) {
			this.dropdowns.add((Dropdown) component);
		}
		mainPanel.add(component);
		return this;
	}

	public Panel add(JComponent component, Object constraints) {
		prevComponent = component;
		if (component instanceof Panel) {
			Panel p = (Panel) component;
			this.buttons.addAll(p.getButtons());
			this.textFields.addAll(p.getTextFields());
			this.dropdowns.addAll(p.getDropdowns());
		} else if (component instanceof Button) {
			this.buttons.add((Button) component);
		} else if (component instanceof TextField) {
			this.textFields.add((TextField) component);
		} else if (component instanceof Dropdown) {
			this.dropdowns.add((Dropdown) component);
		}
		mainPanel.add(component, constraints);
		return this;
	}
	
	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
		this.mainPanel.setBackground(c);
	}

	public Panel addModal(String id, Panel panel) {
		panel.setVisible(true);
		panel.mainPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
			BorderFactory.createEmptyBorder(50, 50, 50, 50)
		));

		Panel p = new Panel(new GridBagLayout());
		p.setBackground(Aesthetics.MODAL_BACKGROUND);
		//p.mainPanel.setLayout(new GridBagLayout());
		p.add(panel, new GridBagConstraints());
		p.setVisible(false);
		p.registerListeners();
		
		super.add(p, JLayeredPane.MODAL_LAYER);
		this.modals.put(id, p);

		return this;
	}

	public Panel addTabPanel(String id, Panel panel) {
		this.tabPanels.put(id, panel);
		panel.setVisible(false);
		this.add(panel);
		return this;
	}
	
	public Panel setMargin(int x, int y) {
		mainPanel.setBorder(BorderFactory.createEmptyBorder(x>>1, y>>1, x>>1, y>>1));
		return this;
	}

	public void registerListeners() {
		final Panel panel = this;
		for (Button b : this.buttons) {
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					b.getClickRunnable().run(panel);
				}
			});
		}
	}
	
	public void registerDebug(Container c) {
		for(Component c2: c.getComponents()) {
			if(c2 instanceof Container) {
				this.registerDebug((Container) c2);
			}
			//c2.addMouseListener(DebugListener.INST);
			c2.setForeground(Color.WHITE);
			c2.setBackground(Color.DARK_GRAY);
		}
	}

	public void open() {
		this.runOnOpen();
		this.updateBounds();
		
		Frame f = new Frame();
		f.setLayout(new GridLayout(0,1));
		this.registerListeners();
		f.setSize(prefWidth, prefHeight);
		f.setBackground(Color.DARK_GRAY);
		f.add(this); // , new GridBagConstraints()
		f.setVisible(true);
		//f.setBounds(0, 0, prefWidth+10, prefHeight+10);
		this.setBounds(0, 0, prefWidth, prefHeight);
		//registerDebug(this);
	}

	public Map<String, String> getResultMap() {
		Map<String, String> resultMap = new HashMap<String, String>();
		for (TextField textField : this.getTextFields()) {
			resultMap.put(textField.getResultKey(), textField.getText());
		}
		for (Dropdown dropdown : this.getDropdowns()) {
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
		this.prefWidth = wid;
		this.prefHeight = hei;
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
		if (this.onOpenRunnable != null)
			this.onOpenRunnable.run(this);
		for (Component comp : this.getComponents()) {
			if (comp instanceof Panel) {
				Panel p = (Panel) comp;
				p.runOnOpen();
			}
		}
	}

	// TODO Click Event for Panel (clickable card)

}