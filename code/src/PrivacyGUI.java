import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
//import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JList;
//import javax.swing.JScrollPane;
//import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The Privacy Advisor GUI
 * @author ulfnore
 *
 */
public class PrivacyGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrivacyGUI window = new PrivacyGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PrivacyGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 718, 484);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Privacy Advisor");
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 706, 450);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Load Policies");
		btnNewButton.addActionListener(new LoadActionListener());
		btnNewButton.setBounds(6, 6, 117, 29);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Run Model");
		btnNewButton_1.addActionListener(new RunActionListener());
		btnNewButton_1.setBounds(6, 38, 117, 29);
		panel.add(btnNewButton_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(314, 6, 386, 438);
		panel.add(textArea);
		
		JList list = new JList();
		list.setBounds(6, 66, 296, 378);
		panel.add(list);
		
		JButton btnConfigureWeights = new JButton("Configure Weights");
		btnConfigureWeights.addActionListener(new configWeightsListener());
		btnConfigureWeights.setBounds(135, 6, 153, 29);
		panel.add(btnConfigureWeights);
		
		JButton btnConfigureMetrics = new JButton("Configure Metrics");
		btnConfigureMetrics.addActionListener(new configMetricsListener());
		btnConfigureMetrics.setBounds(135, 38, 153, 29);
		panel.add(btnConfigureMetrics);
	}
	
	
	class RunActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Run successful.");
			
		}
	}
	
	class LoadActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,"Load successful.");
		}
	}
	class configMetricsListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null,"Metrics configured.");
			
		}
		
	}
	class configWeightsListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,"Weights configured.");
		}
		
	}
}
