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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Panel extends JLayeredPane {

	private static final long serialVersionUID = -5199098153231935544L;

	JFrame frame;
	
	JComponent prevComponent;

	int prefWidth;
	int prefHeight;
	boolean boundingDisabled = false;
	
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
	
	protected boolean isPanelOpen(Panel panel) {
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
		if(this.boundingDisabled) {
			mainPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
			return;
		}
		
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
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		super.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
		this.textFields = new ArrayList<TextField>();
		this.buttons = new ArrayList<Button>();
		this.dropdowns = new ArrayList<Dropdown>();

		this.tabPanels = new HashMap<String, Panel>();
		this.modals = new HashMap<String, Panel>();
		
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
		mainPanel.setBackground(Aesthetics.GENERAL_BACKGROUND);
		mainPanel.setForeground(Aesthetics.GENERAL_FOREGROUND);
		
	}

	public void selectPanel(String id) {
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

	public void openTabPanel(String string) {
		this.selectPanel(string);
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

	public Panel add(JComponent component) {
		prevComponent = component;
		searchContainer(component);
		mainPanel.add(component);
		return this;
	}

	public void searchContainer(Container component) {
		if (component instanceof Button) {
			this.buttons.add((Button) component);
		} else if (component instanceof TextField) {
			this.textFields.add((TextField) component);
		} else if (component instanceof Dropdown) {
			this.dropdowns.add((Dropdown) component);
		} else {
			for(Component c: component.getComponents()) {
				if(c instanceof Container) {
					searchContainer((Container) c);
				}
			}
		}
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
			//c2.setForeground(Color.WHITE);
			//c2.setBackground(Color.DARK_GRAY);
		}
	}

	public void open() {
		this.runOnOpen();
		this.updateBounds();
		
		JFrame f = new JFrame();
		this.frame = f;
		f.setLayout(new GridLayout(1,1));
		this.registerListeners();
		f.setSize(prefWidth, prefHeight);
		f.setLocationRelativeTo(null);
		f.setBackground(Color.DARK_GRAY);
		f.add(this); // , new GridBagConstraints()
		f.setResizable(false);
		f.setVisible(true);
		//f.setBounds(0, 0, prefWidth+10, prefHeight+10);
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
		this.prevComponent.setMaximumSize(new Dimension(i, j));
		this.prevComponent.setMinimumSize(new Dimension(i, j));
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
		for (Component comp : mainPanel.getComponents()) {
			if (comp instanceof Panel) {
				Panel p = (Panel) comp;
				p.runOnOpen();
			}
		}
	}

	public JPanel getMainPanel() {
		return this.mainPanel;
	}
	
	public Panel boxLayout(int boxlayout) {
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, boxlayout));
		return this;
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
		return "";
	}

	public JComponent prevComp() {
		return this.prevComponent;
	}

	public void disableBounding() {
		this.boundingDisabled = true;
	}

	public Panel alignLeft() {
		this.setAlignmentX(0.0f);
		return this;
	}

	// TODO Click Event for Panel (clickable card)

}