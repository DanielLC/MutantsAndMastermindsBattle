package playGui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import main.Player;
import main.Stat;

public class ModifierSelect extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NoPartialFull coverPanel;
	NoPartialFull concealmentPanel;
	NoPartialFull rangePanel;

	JToggleButton charge;

	SlidingScale powerAttack;
	SlidingScale allOutAttack;
	
	public void setPlayer(Player player) {
		powerAttack.setBounds(player.preciseAttack ? -5 : -3, player.powerAttack ? 5 : 3);
		allOutAttack.setBounds(player.defensiveAttack ? -5 : -3, player.allOutAttack ? 5 : 3);
		coverPanel.setValue((int)player.cover.val);
		concealmentPanel.setValue((int)player.concealment.val);
		rangePanel.setValue((int)player.range.val);
		powerAttack.setValue((int)player.powerAttackValue.val);
		allOutAttack.setValue((int)player.allOutAttackValue.val);
	}
	
	public ModifierSelect() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());

		JLabel label = new JLabel("Cover");
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		coverPanel = new NoPartialFull();
		coverPanel.setter = new ValueSetter(Stat.COVER);
		add(coverPanel);
		add(Box.createVerticalStrut(5));
		
		label = new JLabel("Concealment");
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		concealmentPanel = new NoPartialFull();
		concealmentPanel.setter = new ValueSetter(Stat.CONCEALMENT);
		add(concealmentPanel);
		add(Box.createVerticalStrut(5));
		
		label = new JLabel("Range");
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		rangePanel = new NoPartialFull("Short", "Medium", "Long");
		rangePanel.setter = new ValueSetter(Stat.RANGE);
		add(rangePanel);
		add(Box.createVerticalStrut(15));
		
		/*JPanel chargePanel = new JPanel();
		charge = new JToggleButton("Charge");
		charge.setAlignmentX(CENTER_ALIGNMENT);
		add(charge);*/
		
		powerAttack = new SlidingScale("Precise Attack", "Power Attack");
		powerAttack.setter = new ValueSetter(Stat.POWER_ATTACK);
		add(powerAttack);

		allOutAttack = new SlidingScale("Defensive Attack", "All-Out Attack");
		allOutAttack.setter = new ValueSetter(Stat.ALL_OUT_ATTACK);
		add(allOutAttack);
		
		add(Box.createVerticalGlue());
	}
	
	private class ValueSetter {
		public byte stat;
		public ValueSetter(byte stat) {
			this.stat = stat;
		}
		public void setValue(int i) {
			GUI.gui.player.stats[stat].val = i;
		}
	}
	
	public class NoPartialFull extends JPanel implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JToggleButton no;
		private JToggleButton partial;
		private JToggleButton full;
		public ValueSetter setter;

		public NoPartialFull(String noName, String partialName, String fullName) {
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			no = new JToggleButton(noName);
			partial = new JToggleButton(partialName);
			full = new JToggleButton(fullName);
			no.addActionListener(this);
			partial.addActionListener(this);
			full.addActionListener(this);
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
			if(partial.getModel().isSelected())
				return 2;
			if(full.getModel().isSelected())
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

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(setter != null)
				setter.setValue(getValue());
		}
	}
	
	public class SlidingScale extends JPanel implements ChangeListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JSlider slider;
		public ValueSetter setter;
		
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
			slider.addChangeListener(this);
			
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

		@Override
		public void stateChanged(ChangeEvent arg0) {
			if(setter != null)
				setter.setValue(slider.getValue());
		}
	}
}
