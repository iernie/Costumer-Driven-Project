package com.kpro.ui;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JButton;

import sun.awt.windows.ThemeReader;

import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.Action;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.main.Gio;
import com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter;
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
	
	// a gio object
	private Gio gio;

	// textarea to hold output  
	private JTextArea outputArea; 
	
	// buttons
	private JButton btnLoadDatabase;
	private JButton btnLoadPpFile; 
	private JButton btnLoadConfigFile;
	private JButton btnLoadWeightsFile;
	private JButton btnRun;
	
	// Properties object that is passed to GIO
	private Properties props;
	
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
	}

	/**
	 * GUI initializer:
	 * Initialize the contents of the frame.
	 * @author ulfnore
	 */
	private void initialize() {
		
		props = new Properties();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		outputArea = new JTextArea();
		outputArea.setBounds(385, 21, 443, 281);
		panel.add(outputArea);
		
		btnLoadDatabase = new JButton("Load Database");
		btnLoadDatabase.addActionListener(new pAdvisorButtonListener());
		btnLoadDatabase.setBounds(6, 6, 136, 29);
		panel.add(btnLoadDatabase);
		
		btnLoadPpFile = new JButton("Load P3P File");
		btnLoadPpFile.addActionListener(new pAdvisorButtonListener());
		btnLoadPpFile.setBounds(6, 31, 136, 29);
		panel.add(btnLoadPpFile);
		
		btnLoadConfigFile = new JButton("Load Config File");
		btnLoadConfigFile.addActionListener(new pAdvisorButtonListener());
		btnLoadConfigFile.setBounds(6, 59, 136, 29);
		panel.add(btnLoadConfigFile);
		
		btnLoadWeightsFile = new JButton("Load Weights File");
		btnLoadWeightsFile.addActionListener(new pAdvisorButtonListener());
		btnLoadWeightsFile.setBounds(6, 88, 136, 29);
		panel.add(btnLoadWeightsFile);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new pAdvisorButtonListener());
		btnRun.setBounds(6, 114, 136, 29);
		panel.add(btnRun);
	}

	@Override
	public Properties user_init(Properties genProps) {
		/*
		 * create the gui
		for i in genProps
			new field(i)
		once close by user after editing
		for i in fields
			genprops.set(i)
		return genprops
		*/
		
		System.out.println(genProps);
//		genProps.setProperty("genConfig", configPath);
		return genProps;
	}

	/**
	 * Output database to textArea
	 */
	@Override
	public void showDatabase(PolicyDatabase pdb) {
		System.out.println("Printing pdb:");
		System.err.println(pdb == null);
		for(PolicyObject po : pdb) System.out.println(po);
		System.out.println(pdb.toString());
		outputArea.setText(pdb.toString());
	}

	@Override
	public ArrayList<PolicyObject> loadHistory() {
		// TODO Auto-generated method stub
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
		String recommendation = n.getAction().getAccepted() == true ? "Accept" : "Reject";
		int response = 2;
		while(response == 2)
			response = JOptionPane.showConfirmDialog(null, "Privacy Advisor recommends: "+recommendation+
						". Accept recommendation?");
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
		// TODO Auto-generated method stub
		gio.shutdown();
		
	}
	
	class pAdvisorButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnRun) run();
			else
			{ 
				if(e.getSource() == btnLoadConfigFile)
				{
					loadFile(configPath);
					initTest();
				}
				else if (e.getSource() == btnLoadDatabase)
				{
//					loadFile(dbPath);
					loadDB_test();
				}
				else if (e.getSource() == btnLoadPpFile)
					loadFile(p3pPolicyPath);
				else if (e.getSource() == btnLoadWeightsFile)
					loadFile(weightsPath);
//				updateProperties();
			}				
		}
	}
	
	
	private void initTest()
	{
		gio = new Gio(null, this);
	}
	
	private void loadDB_test()
	{
		gio = new Gio(null, this);
		System.err.println("gio.getPDB() == null:" + gio.getPDB() == null);
		gio.loadDB();
		showDatabase(gio.getPDB());
	}
	
	private void loadFile(String targetPath)
	{
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(null);
		try{ 
			targetPath = jfc.getSelectedFile().getAbsolutePath();
			System.out.println(targetPath);
			}
		catch(NullPointerException e) { 
			System.err.println("An exception was caught.");
			e.printStackTrace(); 
		}
		catch(Exception e) {
			System.err.println("An exception was caught.");
			e.printStackTrace(); 
		}
	}
	
	private void updateProperties()
	{
		try
		{
			//in/out DB set to the same file 
			props.setProperty("inDBLoc", dbPath);
			props.setProperty("outDBLoc", dbPath);
			
			props.setProperty("p3pLocation", p3pPolicyPath);
			props.setProperty("inWeightsLoc", weightsPath);
			props.setProperty("outWeightsLoc", weightsPath);
			
			// TODO: ???
			props.setProperty("userIO", "PrivacyAdvisorGUI");
		} catch(Exception e){
			//TODO: something useful
			e.printStackTrace();
		}
		// TODO: update gio object
		
	}
	
	private void run()
	{
		try
		{
			//TODO: something more relevant perhaps
			System.out.println("Run very much successful.");
		}catch(NullPointerException e)
		{
			//TODO: something more relevant perhaps
			System.err.println("An exception was caught.");
			e.printStackTrace();
		}catch (Exception e) 
		{
			//TODO: something more relevant perhaps
			System.err.println("An exception was caught.");
			e.printStackTrace();
		}

	}
}
