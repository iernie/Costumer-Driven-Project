package com.kpro.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ConfigEditor extends Thread implements ActionListener{

	private JFrame frame;
	private JTextField inDBfileField;
	private JTextField outDBfileField;
	private JTextField inWeightsFileField;
	private JTextField p3pFileField;
	private JTextField newPolicyFileField;
	private JTextField confFileField;
	private JComboBox blankAccept_TrueFalse;
	private JComboBox userInitBox;
	private JComboBox cbrVerBox;
	private JComboBox dbTypeBox;
	private JComboBox logLvlBox;
	private JButton btnOk;
	private JButton btnSetInDBloc;
	private JButton btnSetOutDBloc;
	private JButton btnSetInWeightsLoc;
	private JButton btnSetP3Ploc;
	private JButton btnSetPolicyLoc;
	private JButton btnSetAltConfLoc;
	
	private Properties genProps;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ConfigEditor window = new ConfigEditor();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public ConfigEditor() {
//		initialize();
	}
	/**
	 * Default constructor called from PrivacyAdvisorGUI.
	 * @param genProps
	 */
	public ConfigEditor(Properties genProps){
		this.genProps = genProps;
//		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 382, 406);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewDb = new JLabel("New DB");
		lblNewDb.setBounds(6, 6, 61, 16);
		panel.add(lblNewDb);
		
		JLabel lblInDbLocation = new JLabel("In DB Location");
		lblInDbLocation.setBounds(6, 34, 166, 16);
		panel.add(lblInDbLocation);
		
		JLabel lblOutDbLocation = new JLabel("Out DB Location");
		lblOutDbLocation.setBounds(6, 62, 166, 16);
		panel.add(lblOutDbLocation);
		
		JLabel lblInWeightsLocation = new JLabel("In Weights Location");
		lblInWeightsLocation.setBounds(6, 90, 166, 16);
		panel.add(lblInWeightsLocation);
		
		JLabel lblPpLocation = new JLabel("P3P Location");
		lblPpLocation.setBounds(6, 118, 166, 16);
		panel.add(lblPpLocation);
		
		JLabel lblNewPolicyLocation = new JLabel("New Policy Location");
		lblNewPolicyLocation.setBounds(6, 146, 166, 16);
		panel.add(lblNewPolicyLocation);
		
		JLabel lblBlankAccept = new JLabel("Blank Accept");
		lblBlankAccept.setBounds(6, 174, 166, 16);
		panel.add(lblBlankAccept);
		
		JLabel lblUserInit = new JLabel("User Init");
		lblUserInit.setBounds(6, 202, 166, 16);
		panel.add(lblUserInit);
		
		JLabel lblCbrVersion = new JLabel("CBR Version");
		lblCbrVersion.setBounds(6, 230, 166, 16);
		panel.add(lblCbrVersion);
		
		JLabel lblAltConfig = new JLabel("Alt. Config Location");
		lblAltConfig.setBounds(6, 258, 166, 16);
		panel.add(lblAltConfig);
		
		JLabel lblDatabaseType = new JLabel("Database Type");
		lblDatabaseType.setBounds(6, 286, 166, 16);
		panel.add(lblDatabaseType);
		
		JLabel lblLogLevel = new JLabel("Log Level");
		lblLogLevel.setBounds(6, 314, 166, 16);
		panel.add(lblLogLevel);
		
		
		
		
		///// Textfields
		
		inDBfileField = new JTextField();
		inDBfileField.setText(genProps.getProperty("inDBLoc"));
		inDBfileField.setBounds(184, 28, 134, 28);
		panel.add(inDBfileField);
		inDBfileField.setColumns(10);
		
		outDBfileField = new JTextField();
		outDBfileField.setText(genProps.getProperty("outDBLoc"));
		outDBfileField.setBounds(184, 56, 134, 28);
		panel.add(outDBfileField);
		outDBfileField.setColumns(10);
		
		inWeightsFileField = new JTextField();
		inWeightsFileField.setText(genProps.getProperty("inWeightsLoc"));
		inWeightsFileField.setBounds(184, 84, 134, 28);
		panel.add(inWeightsFileField);
		inWeightsFileField.setColumns(10);
		
		p3pFileField = new JTextField();
		p3pFileField.setText(genProps.getProperty("p3pLocation"));
		p3pFileField.setBounds(184, 112, 134, 28);
		panel.add(p3pFileField);
		p3pFileField.setColumns(10);
		
		newPolicyFileField = new JTextField();
		newPolicyFileField.setText(genProps.getProperty("newPolicyLoc"));
		newPolicyFileField.setBounds(184, 140, 134, 28);
		panel.add(newPolicyFileField);
		newPolicyFileField.setColumns(10);
		
		
		
		
		//// Boxes
		
		JComboBox newDB_TrueFalse = new JComboBox();
		newDB_TrueFalse.setBounds(184, 2, 132, 27);
		panel.add(newDB_TrueFalse);
		
		blankAccept_TrueFalse = new JComboBox();
		blankAccept_TrueFalse.setBounds(184, 170, 132, 27);
		panel.add(blankAccept_TrueFalse);
		
		userInitBox = new JComboBox();
		userInitBox.setBounds(184, 198, 132, 27);
		panel.add(userInitBox);
		
		cbrVerBox = new JComboBox();
		cbrVerBox.setBounds(184, 226, 132, 27);
		panel.add(cbrVerBox);
		
		confFileField = new JTextField();
		confFileField.setBounds(184, 252, 134, 28);
		panel.add(confFileField);
		confFileField.setColumns(10);
		
		dbTypeBox = new JComboBox();
		dbTypeBox.setBounds(184, 282, 132, 27);
		panel.add(dbTypeBox);
		
		logLvlBox = new JComboBox();
		logLvlBox.setBounds(184, 310, 132, 27);
		panel.add(logLvlBox);
		
		
		
		////// Buttons
		
		btnOk = new JButton("OK");
		btnOk.setBounds(199, 349, 117, 29);
		panel.add(btnOk);
		
		btnSetInDBloc = new JButton("Set");
		btnSetInDBloc.setBounds(319, 29, 61, 29);
		panel.add(btnSetInDBloc);
		
		btnSetOutDBloc = new JButton("Set");
		btnSetOutDBloc.setBounds(319, 57, 61, 29);
		panel.add(btnSetOutDBloc);
		
		btnSetInWeightsLoc = new JButton("Set");
		btnSetInWeightsLoc.setBounds(319, 85, 61, 29);
		panel.add(btnSetInWeightsLoc);
		
		btnSetP3Ploc = new JButton("Set");
		btnSetP3Ploc.setBounds(319, 113, 61, 29);
		panel.add(btnSetP3Ploc);
		
		btnSetPolicyLoc = new JButton("Set");
		btnSetPolicyLoc.setBounds(319, 141, 61, 29);
		panel.add(btnSetPolicyLoc);
		
		btnSetAltConfLoc = new JButton("Set");
		btnSetAltConfLoc.setBounds(319, 253, 61, 29);
		panel.add(btnSetAltConfLoc);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String path;
		if (e.getSource() == btnOk) // close dialog
		{
			frame.setVisible(false);
		} else if (e.getSource() == btnSetInDBloc){
			setProp("inDBLoc", inDBfileField.getText());
		} else if (e.getSource() == btnSetOutDBloc){
			setProp("outDBLoc", outDBfileField.getText());
		} else if (e.getSource() == btnSetInWeightsLoc){
			setProp("inWeightsLoc", inWeightsFileField.getText());
		} else if (e.getSource() == btnSetP3Ploc){
			// Can take a directory as input as well
			JFileChooser jfc = new JFileChooser(p3pFileField.getText());
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				File f = jfc.getSelectedFile();
				if (f.isDirectory()) 
					genProps.setProperty("p3pDirLocation", f.getAbsolutePath());
				else 
					genProps.setProperty("p3pLocation", f.getAbsolutePath());
			}
		} else if (e.getSource() == btnSetPolicyLoc){
			setProp("newPolicyLoc",newPolicyFileField.getText());
		} else if (e.getSource() == btnSetAltConfLoc){
			setProp("genConfig",confFileField.getText());
		}		
	}
	/**
	 * Auxillary method in ConfigEditor. Opens a JFileChooser.
	 * Accepts and returns a file path as a string.
	 * @return path
	 */
	private void setProp(String prop,String path)
	{
		JFileChooser jfc = new JFileChooser(path);
		if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			path = jfc.getSelectedFile().getAbsolutePath();
			genProps.setProperty(prop, path);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		initialize();
		frame.setVisible(true);
	}
	
}
