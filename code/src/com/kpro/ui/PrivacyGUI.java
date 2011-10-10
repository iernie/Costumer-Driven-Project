package com.kpro.ui;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
//import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JList;

import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.main.Gio;
//import javax.swing.JScrollPane;
//import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The Privacy Advisor GUI
 * 
 * Runs on top of command line PA.
 * 
 * @author ulfnore
 * @version 101011
 */
public class PrivacyGUI extends UserIO{
	
	private String weightsPath;
	private String dbPath;
	private String p3pPath;
	private Gio gio;
	
	JTextArea textArea;
	JList list ;
	
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
		
		textArea = new JTextArea();
		textArea.setBounds(314, 6, 386, 438);
		panel.add(textArea);
		
		list = new JList();
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
			String path = "";
			loadDatabase();
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
	
	
	void loadDatabase(){
		String[] args = 
			{
				"FFFFFUUU",
			};
		gio = new Gio(args);
		
		gio.loadDB();
	}

	@Override
	public Properties user_init(Properties genProps) {
		// TODO Auto-generated method stub
		
		return null;
	}

	/**
	 * Writes policy database to onscreen text area
	 * @author ulfnore
	 */
	@Override
	public void showDatabase(PolicyDatabase pdb) {

		String str = "";
		for(PolicyObject po : pdb)
			str += po.toString();
		textArea.setText(str);			
	}

	@Override
	public ArrayList<PolicyObject> loadHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * get user response based on a recommendation
	 * @author ulfnore
	 */
	@Override
	public PolicyObject userResponse(PolicyObject n) {
		String response = "Privacy Advisor advises: "+ n.getAction().getAccepted()+ "\n";
		String reason ="";
		int override = 2;
		
		for(String str : n.getAction().getReasons())
			response += str + "\n";
		
		while(override == 2)
			override = JOptionPane.showConfirmDialog(null, response);
				
		if (override == 0) // override
		{
			System.out.println("Action overridden.");
			n.getAction().setOverride(true);
		}
		
		return n;
	}

	@Override
	public void closeResources() {
		// TODO Auto-generated method stub
		
	}
}
