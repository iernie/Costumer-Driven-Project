package com.kpro.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JTextArea;

//import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.Case;
import com.kpro.dataobjects.Category;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.dataobjects.Purpose;
import com.kpro.dataobjects.Recipient;
import com.kpro.dataobjects.Retention;
import com.kpro.datastorage.PolicyDatabase;
import com.kpro.main.Gio;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 * Privacy Advisor GUI to run on top of 
 * @author ulfnore
 *
 */
public class PrivacyAdvisorGUI extends UserIO{

	private JFrame frame;
	private String weightsPath;
	private String configPath;
	private String dbPath;
	private String p3pPolicyPath;
	private Gio gio;

	// textarea to hold output and 
	// JTree to visualize pDatabase
	private JTextArea outputArea;
	private JTree dataBaseTree, policyTree;
	private DefaultMutableTreeNode dataBaseTreeRoot, policyTreeRoot;
	
	// menu bar
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem loadConfigMenuItem;
	private JMenuItem loadDBMenuItem;
	private JMenuItem runMenuItem;
	private JMenuItem exitMenuItem;
	
	
	// Properties object that is passed to GIO
	private Properties props;
	
	private JScrollPane outputAreaScrollPane, dataBaseTreeScrollPane, policyTreeScrollPane;
	
	/**
	 * Launch the application.
	 * @author ulfnore
	 */
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrivacyAdvisorGUI window = new PrivacyAdvisorGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Default no-arg constructor
	 * @author ulfnore
	 */
	public PrivacyAdvisorGUI() {
		initialize();
		try {
			gio = new Gio(null,this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * GUI initializer:
	 * Initialize the contents of the frame.
	 * @author ulfnore, ernie
	 */
	private void initialize() {
		
		GridBagConstraints c = new GridBagConstraints();
		
		frame = new JFrame();
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
		frame.add(panel);
		
		// Add text area + tree view
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		dataBaseTreeRoot = new DefaultMutableTreeNode("Policy Database");
		dataBaseTree = new JTree(dataBaseTreeRoot);
		dataBaseTree.addTreeSelectionListener(new TListener());
		dataBaseTreeScrollPane = new JScrollPane(dataBaseTree);
		panel.add(dataBaseTreeScrollPane, c);
	
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		policyTreeRoot = new DefaultMutableTreeNode("New Policy");
		policyTree = new JTree(policyTreeRoot);
		policyTree.addTreeSelectionListener(new TListener());
		policyTreeScrollPane = new JScrollPane(policyTree);
		panel.add(policyTreeScrollPane, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		outputAreaScrollPane = new JScrollPane();
		outputArea = new JTextArea();
		outputAreaScrollPane.setViewportView(outputArea);
		panel.add(outputAreaScrollPane, c);
		
		
		// Build menu
		menuBar = new JMenuBar();
		menu = new JMenu("Privacy Advisor");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		frame.setJMenuBar(menuBar);
		menuBar.add(menu);
		
		loadConfigMenuItem = new JMenuItem("Configuration", KeyEvent.VK_C);
		loadDBMenuItem= new JMenuItem("Load Database", KeyEvent.VK_D);
		runMenuItem = new JMenuItem("Run",KeyEvent.VK_R);
		exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_Q);
		
		loadConfigMenuItem.addActionListener(new pAdvisorButtonListener());
		loadDBMenuItem.addActionListener(new pAdvisorButtonListener());
		runMenuItem.addActionListener(new pAdvisorButtonListener());
		exitMenuItem.addActionListener(new pAdvisorButtonListener());
		
		menu.add(loadConfigMenuItem); menu.add(loadDBMenuItem);
		menu.add(runMenuItem); menu.add(exitMenuItem);
	}

	/**
	 * Called from GIO. Takes default properties file as argument.
	 * @author ulfnore
	 */
	@Override
	public Properties user_init(Properties genProps){
		final ConfEditor ce = new ConfEditor(genProps);
		Thread t = new Thread() {
			public void run() {
				ce.run();
				while(ce.isVisible()) {
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};
		t.start();
		System.out.println("Thread finished?");
		return ce.getGenProps();
	}
	
	private void loadConfig(){
		gio.configUI();
	}
	

	@Override
	public ArrayList<PolicyObject> loadHistory() {

		return null;
	}

	/**
	 * Shows recommendation and prompts for user action
	 * 
	 * Needs improvement to allow for giving reasons as for why  
	 * recommendation is not accepted.
	 * @author ulfnore
	 */
	@Override
	public PolicyObject userResponse(PolicyObject n) {
		String description = n.getContextDomain();
		String recommendation = n.getAction().getAccepted() == true ? "Accept." : "Reject.";
		String reason = "\nReason:\n";
		for (String str : n.getAction().getReasons()) reason += str + "\n";
		reason += "\n";
		String confidence = "\nWith Confidence: " + String.valueOf(n.getAction().getConfidence());
		
		
		int response = 2;
		while(response == 2)
			response = JOptionPane.showConfirmDialog(null, 
						"For the policy: \n"+description+
						"\nPrivacy Advisor recommends: "
						+recommendation + reason + confidence + 
						". \nAccept recommendation?",
						"Privacy Advisor",
						JOptionPane.YES_NO_OPTION);
		
		if(response == 1)// update
		{ 
			Action a = n.getAction();
			a.setAccepted(!a.getAccepted());
			a.setOverride(true);
		}
		return n;
	}

	@Override
	public void closeResources() {

		gio.shutdown();
		
	}
	
	/**
	 * Listens on GUI buttons.
	 * @author ulfnore
	 */
	class pAdvisorButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == runMenuItem) run();
			else if (e.getSource() == loadConfigMenuItem) loadConfig();
			else if (e.getSource() == loadDBMenuItem) loadDB();
			else if (e.getSource() == exitMenuItem){
				frame.setVisible(false);
				System.exit(0);
			}
		}
	}
	

		
	private void loadDB()
	{
		try
		{
			//System.err.println("gio.getPDB() == null:" + gio.getPDB() == null);
			gio.loadDB();
			showDatabase(gio.getPDB());
		}
		catch(NullPointerException e)
		{
			JOptionPane.showMessageDialog(null, "No configuration file selected. Loading default.");
		}
	}



	/**
	 * Runs the CBR algorithm to classify the input P3P.
	 * @author ulfnore
	 */
	private void run()
	{		
		try
		{
			gio.getCBR().run(gio.loadPO());
			buildTree(policyTreeRoot, gio.getPO());

		}catch(NullPointerException e)
		{
			//TODO: something more relevant perhaps
			println("Error: No configuration is loaded.");
			System.err.println("An exception was caught.");
			e.printStackTrace();
		}catch (Exception e) 
		{
			//TODO: something more relevant perhaps
			println("Error: No configuration is loaded.");
			System.err.println("An exception was caught.");
			e.printStackTrace();
		}

	}
	
	/**
	 * Write to PrivacyAdvisorGUI output textarea.
	 * @author ulfnore
	 */
	private void println(String str)
	{
		String str0 = outputArea.getText();
		str0 += "\n"+str;
		outputArea.setText(str0);
	}

	
	@Override
	public void showDatabase(com.kpro.datastorage.PolicyDatabase pdb) {
		//System.out.println("Printing pdb:");
		//System.err.println(pdb == null);
		//for(PolicyObject po : pdb) System.out.println(po);
		//System.out.println(pdb.toString());
		//println(pdb.toString());
		buildTree(dataBaseTreeRoot, pdb);
		println("Database successfully loaded.\n");
	}


	
	private void buildTree(DefaultMutableTreeNode root, PolicyDatabase pdb)
	{
		DefaultMutableTreeNode 	policyObj = null,
								caseNode = null,
								purpose = null,
								recipient = null, 
								retention = null,
								category = null;
		
		for (PolicyObject po : pdb){
			policyObj = new DefaultMutableTreeNode(po.getContextDomain());
			
			for (Case c : po){
				policyObj.add(caseNode = 
						new DefaultMutableTreeNode(c.getDataType()));
				
				caseNode.add(purpose = 
						new DefaultMutableTreeNode("Purpose"));
				caseNode.add(recipient = 
						new DefaultMutableTreeNode("Recipient"));
				caseNode.add(retention = 
						new DefaultMutableTreeNode("Retention"));
				
				if (c.getCategories() != null) {
					caseNode.add(category = 
							new DefaultMutableTreeNode("Category"));
					for (Category ca : c.getCategories())
						category.add(new DefaultMutableTreeNode(ca.toString()));
				}
				
				for (Purpose p : c.getPurposes())
					purpose.add(new DefaultMutableTreeNode(p.toString() + (p.isOptional() ? " - Optional" : "")));
				
				for (Recipient r : c.getRecipients())
					recipient.add(new DefaultMutableTreeNode(r.toString()));
				
				for(Retention r : c.getRetentions())
					retention.add(new DefaultMutableTreeNode(r.toString()));		
			}
			policyObj.add(
					new DefaultMutableTreeNode(po.getAction()));
		}	
		root.add(policyObj);
	}
	
	private void buildTree(DefaultMutableTreeNode root, PolicyObject po)
	{
		DefaultMutableTreeNode 	policyObj = null,
								caseNode = null,
								purpose = null,
								recipient = null, 
								retention = null;
		
		policyObj = new DefaultMutableTreeNode(po.getContextDomain());
		root.add(policyObj);
		int case_id = 1;
		
		for (Case c : po){
			policyObj.add(caseNode = 
					new DefaultMutableTreeNode("Case " + String.valueOf(case_id++)));
			
			caseNode.add(purpose = 
					new DefaultMutableTreeNode("Purpose"));
			caseNode.add(recipient = 
					new DefaultMutableTreeNode("Recipient"));
			caseNode.add(retention = 
					new DefaultMutableTreeNode("Retention"));
			
			for (Purpose p : c.getPurposes())
				purpose.add(new DefaultMutableTreeNode(p.toString()));				
			for (Recipient r : c.getRecipients())
				recipient.add(new DefaultMutableTreeNode(r.toString()));
			for(Retention r : c.getRetentions())
				retention.add(new DefaultMutableTreeNode(r.toString()));		
		}
		policyObj.add(
				new DefaultMutableTreeNode(po.getAction()));
	}
	
	
	/**
	 * Implements the TreeSelectionListener interface.
	 * 
	 * See {@link TreeSelectionListener}
	 * @author ulfnore
	 */
	private class TListener implements TreeSelectionListener{	
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			println(e.getPath().toString());
			for(Object o: e.getPath().getPath())
				System.out.println(o);
		}
	}
}