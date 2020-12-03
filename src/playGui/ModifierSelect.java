package playGui;


import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;

import main.Player;
import main.Team;

public class ModifierSelect extends JPanel {
	NoPartialFull coverPanel;
	NoPartialFull concealmentPanel;
	NoPartialFull rangePanel;

	JToggleButton charge;

	SlidingScale powerAttack;
	SlidingScale allOutAttack;
	
	public void setPlayer(Player player) {
		powerAttack.setBounds(player.preciseAttack ? -5 : -3, player.powerAttack ? 5 : 3);
		allOutAttack.setBounds(player.defensiveAttack ? -5 : -3, player.allOutAttack ? 5 : 3);
	}
	
	public ModifierSelect() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());

		JLabel label = new JLabel("Cover");
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		coverPanel = new NoPartialFull();
		add(coverPanel);
		add(Box.createVerticalStrut(5));
		
		label = new JLabel("Concealment");
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		concealmentPanel = new NoPartialFull();
		add(concealmentPanel);
		add(Box.createVerticalStrut(5));
		
		label = new JLabel("Range");
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		rangePanel = new NoPartialFull("Short", "Medium", "Long");
		add(rangePanel);
		add(Box.createVerticalStrut(15));
		
		JPanel chargePanel = new JPanel();
		charge = new JToggleButton("Charge");
		charge.setAlignmentX(CENTER_ALIGNMENT);
		add(charge);
		
		powerAttack = new SlidingScale("Precise Attack", "Power Attack");
		add(powerAttack);

		allOutAttack = new SlidingScale("Defensive Attack", "All-Out Attack");
		add(allOutAttack);
		
		add(Box.createVerticalGlue());
	}
	
	public class NoPartialFull extends JPanel {
		JToggleButton no;
		JToggleButton partial;
		JToggleButton full;

		public NoPartialFull(String noName, String partialName, String fullName) {
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			no = new JToggleButton(noName);
			partial = new JToggleButton(partialName);
			full = new JToggleButton(fullName);
			add(Box.createHorizontalGlue());
			add(no);
			//add(Box.createHorizontalGlue());
			//add(Box.createHorizontalGlue());
			add(partial);
			//add(Box.createHorizontalGlue());
			//add(Box.createHorizontalGlue());
			add(full);
			add(Box.createHorizontalGlue());
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(no);
			buttonGroup.add(partial);
			buttonGroup.add(full);
			no.doClick();
		}
		
		public NoPartialFull() {
			this("None", "Partial", "Full");
		}
		
		public int getValue() {
			if(partial.getModel().isPressed())
				return 2;
			if(full.getModel().isPressed())
				return 5;
			return 0;
		}
		
		public void setValue(int n) {
			switch(n) {
			case 0:
				no.doClick();
				return;
			case 2:
				partial.doClick();
				return;
			case 5:
				full.doClick();
				return;
			}
			throw new RuntimeException("Value " + n + " invalid for playGUI.ModifierSelect.NoPartialFull.setValue()");
		}
	}
	
	public class SlidingScale extends JPanel {
		private JSlider slider;
		
		public SlidingScale(String left, String right) {
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			JPanel labelPanel = new JPanel();
			labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
			labelPanel.add(Box.createHorizontalGlue());
			labelPanel.add(new JLabel(left, JLabel.CENTER));
			labelPanel.add(Box.createHorizontalGlue());
			labelPanel.add(Box.createHorizontalGlue());
			labelPanel.add(new JLabel(right, JLabel.CENTER));
			labelPanel.add(Box.createHorizontalGlue());
			add(labelPanel);
			slider = new JSlider(-2, 2);
			slider.setMajorTickSpacing(1);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			
			//Copied from Stack Overflow. This lets you click on the slider and have it move there immediately, instead of just a bit closer.
			slider.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mousePressed(MouseEvent e) {
			       JSlider sourceSlider=(JSlider)e.getSource();
			       BasicSliderUI ui = (BasicSliderUI)sourceSlider.getUI();
			       int value = ui.valueForXPosition( e.getX() );
			       slider.setValue(value);
			    }
			});
			
			add(slider);
		}
		
		public int getValue() {
			return slider.getValue();
		}
		
		public void setValue(int n) {
			slider.setValue(n);
		}
		
		public void setBounds(int min, int max) {
			slider.setMinimum(min);
			slider.setMaximum(max);
			revalidate();
		}
	}
}
