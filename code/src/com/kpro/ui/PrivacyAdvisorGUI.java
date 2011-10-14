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
import java.util.Enumeration;
import java.util.Iterator;
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
import javax.swing.JScrollPane;
import javax.swing.JInternalFrame;
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
	private JButton btnLoadConfigFile;
	private JButton btnRun;
	
	// Properties object that is passed to GIO
	private Properties props;
	private JScrollPane scrollPane_1;
	
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
		frame.setResizable(false);
		frame.setBounds(100, 100, 714, 534);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(169, 11, 529, 486);
		panel.add(scrollPane_1);
		
		outputArea = new JTextArea();
		scrollPane_1.setViewportView(outputArea);
		
		btnLoadDatabase = new JButton("Load Database");
		btnLoadDatabase.addActionListener(new pAdvisorButtonListener());
		btnLoadDatabase.setBounds(6, 6, 136, 29);
		panel.add(btnLoadDatabase);
		
		btnLoadConfigFile = new JButton("Load Config File");
		btnLoadConfigFile.addActionListener(new pAdvisorButtonListener());
		btnLoadConfigFile.setBounds(6, 33, 136, 29);
		panel.add(btnLoadConfigFile);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new pAdvisorButtonListener());
		btnRun.setBounds(6, 61, 136, 29);
		panel.add(btnRun);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(385, 287, 4, 4);
		panel.add(scrollPane);
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
		


		Enumeration e = genProps.propertyNames();
		println("Properties file loaded.\n");
		while (e.hasMoreElements()) 
		{
			String key = (String)e.nextElement();
//			System.out.println(key+": "+ genProps.getProperty(key));
			println(key+": "+ genProps.getProperty(key));
			
		}

		
		
		
		JOptionPane.showMessageDialog(frame, "hello");
//		System.out.println(genProps);
		
		println("hepp");
		
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
			response = JOptionPane.showConfirmDialog(null, 
						"Privacy Advisor recommends: "+recommendation+
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
	
	/**
	 * Listens on GUI buttons.
	 * @author ulfnore
	 */
	class pAdvisorButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnRun) run();
			else
			{ 
				if(e.getSource() == btnLoadConfigFile)
				{
					loadConf();
				}
				else if (e.getSource() == btnLoadDatabase)
				{
					loadDB();
				}
			}				
		}
	}
	
	
	private void loadConf(){
		if (props == null){ // load default config file
			gio = new Gio(null, this);
		}else{
			String config  = outputArea.getText();
			
		}
	}
	
	private void loadDB()
	{
//		gio = new Gio(null, this);
		try
		{
			System.err.println("gio.getPDB() == null:" + gio.getPDB() == null);
			gio.loadDB();
			showDatabase(gio.getPDB());
		}
		catch(NullPointerException e)
		{
			JOptionPane.showMessageDialog(null, "No configuration file selected. Loading default.");
		}
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
			gio.getCBR().run(gio.getPO());

			
			
			
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
	
	
	private void loadDefault()
	{
		gio = new Gio(null,this);
	}
	
	
	/**
	 * Write to textarea
	 * @author ulfnore
	 */
	private void println(String str)
	{
		String str0 = outputArea.getText();
		str0 += "\n"+str;
		outputArea.setText(str0);
	}
}