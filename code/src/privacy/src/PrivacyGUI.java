import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * A GUI for loading P3P policies from a local file
 * and testing the classifier algorithm.
 * 
 * @author ulfnore
 *
 */
public class PrivacyGUI extends JPanel{
	
	private JList policies; // list of policies to be displayed
	private DefaultListModel policyModel;
	
	private JButton loadDataButton; // load database
	private JButton runButton; // run the classifier algo
	private JTextArea outputArea; // output from algorithm
	
	private String path;
	private FlowLayout layout;
	private ArrayList<PolicyObject> database; 
	
	
	
	/**
	 * Default no-arg constructor
	 * @author ulfnore
	 */
	public PrivacyGUI()
	{
		policyModel = new DefaultListModel();
		policies = new JList(policyModel);

		runButton = new JButton("Run");
		loadDataButton = new JButton("Load data");
		runButton.addActionListener(new RunButtonListener());
		loadDataButton.addActionListener(new LoadDataButtonListener());
		
		
		outputArea = new JTextArea(40,40);
		outputArea.setText("Nothing here yet..");
				
		JScrollPane policyScrollPane = new JScrollPane(policies);
		policyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
				
		layout = new FlowLayout();
		add(policyScrollPane);
		
		add(loadDataButton);
		add(runButton);
		add(outputArea);


	}
	
	/**
	 * System call to load database from disk
	 * @author ulfnore 
	 */
	public void loadDatabase()
	{
		
	}
	
	/**
	 * Populate the "policies" list once a dataset is loaded.
	 * @author ulfnore
	 */
	public void populateList()
	{
		for(PolicyObject po : database)
			policyModel.addElement(po);		
	}
	
	/**
	 * Run the algo on the dataset
	 * @author ulfnore
	 */
	public void run()
	{
		// TODO call run on the data added
	}
	
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame();
		frame.add(new PrivacyGUI());
		frame.setTitle("Privacy Advisor - Testing Framework");
		frame.setSize(1024,960);
		frame.setLocation(30,30);
		frame.setVisible(true);
	}
	
	
	class RunButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Running.");
			run();
		}
	}
	class LoadDataButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Loading.");
			loadDatabase();
			populateList();
		}
	}
	

}
