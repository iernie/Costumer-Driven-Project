package com.kpro.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JButton;

import com.kpro.database.PolicyDatabase;
import com.kpro.dataobjects.PolicyObject;
import com.kpro.main.Gio;
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
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		outputArea = new JTextArea();
		outputArea.setBounds(385, 21, 443, 281);
		panel.add(outputArea);
		
		JButton btnLoadDatabase = new JButton("Load Database");
		btnLoadDatabase.setBounds(6, 6, 136, 29);
		panel.add(btnLoadDatabase);
		
		JButton btnLoadPpFile = new JButton("Load P3P File");
		btnLoadPpFile.setBounds(6, 31, 136, 29);
		panel.add(btnLoadPpFile);
		
		JButton btnLoadConfigFile = new JButton("Load Config File");
		btnLoadConfigFile.setBounds(6, 59, 136, 29);
		panel.add(btnLoadConfigFile);
		
		JButton btnLoadWeightsFile = new JButton("Load Weights File");
		btnLoadWeightsFile.setBounds(6, 88, 136, 29);
		panel.add(btnLoadWeightsFile);
		
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(6, 114, 136, 29);
		panel.add(btnRun);
	}

	@Override
	public Properties user_init(Properties genProps) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Output database to textArea
	 */
	@Override
	public void showDatabase(PolicyDatabase pdb) {
		outputArea.setText(pdb.toString());
	}

	@Override
	public ArrayList<PolicyObject> loadHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyObject userResponse(PolicyObject n) {
		String recommendation = n.getAction().getAccepted() == true ? "Accept" : "Reject";
		int response = 2;
		while(response == 2)
			response = JOptionPane.showConfirmDialog(null, "Privacy Advisor recommends: "+recommendation);
		if(response == 0) // update
			
			
		return n;
	}

	@Override
	public void closeResources() {
		// TODO Auto-generated method stub
		
	}
}
