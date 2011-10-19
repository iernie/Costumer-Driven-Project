package com.kpro.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class confEditor extends Thread implements ActionListener{

	private JFrame frame;
	private JTextField newDBfield;
	private JTextField inDBlocField;
	private JTextField outDBlocField;
	private JTextField inWeightsLocField;
	private JTextField outWeightsLocField;
	private JTextField p3pLocField;
	private JTextField newPolicyLocField;
	private JTextField blankAccField;
	private JTextField userInitField;
	private JTextField cbrVerField;
	private JTextField altConfLocField;
	private JTextField dbTypeField;
	private JTextField logLevField;
	private JButton btnOk;
	
	private Properties props;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					confEditor window = new confEditor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param Properties object to be modified.
	 * @wbp.parser.entryPoint
	 */
	public confEditor(Properties props) {
		this.props = props;
	}

	/**
	 * No-arg constructor
	 */
	public confEditor(){
		
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 363, 429);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblInDbLocation = new JLabel("In DB Location");
		lblInDbLocation.setBounds(6, 44, 147, 21);
		panel.add(lblInDbLocation);
		
		JLabel lblOutDbLocation = new JLabel("Out DB Location");
		lblOutDbLocation.setBounds(6, 77, 132, 16);
		panel.add(lblOutDbLocation);
		
		JLabel lblInWeightsLocation = new JLabel("In Weights Location");
		lblInWeightsLocation.setBounds(6, 105, 132, 16);
		panel.add(lblInWeightsLocation);
		
		JLabel lblOutWeightsLocation = new JLabel("Out Weights Location");
		lblOutWeightsLocation.setBounds(6, 133, 157, 16);
		panel.add(lblOutWeightsLocation);
		
		JLabel lblPpLocation = new JLabel("p3p Location");
		lblPpLocation.setBounds(6, 161, 132, 16);
		panel.add(lblPpLocation);
		
		JLabel lblNewDb = new JLabel("New DB");
		lblNewDb.setBounds(6, 16, 61, 16);
		panel.add(lblNewDb);
		
		JLabel lblNewPolicyLocation = new JLabel("New Policy Location");
		lblNewPolicyLocation.setBounds(6, 189, 132, 16);
		panel.add(lblNewPolicyLocation);
		
		JLabel lblLogLevel = new JLabel("Log level");
		lblLogLevel.setBounds(6, 343, 132, 16);
		panel.add(lblLogLevel);
		
		JLabel lblBlankAccept = new JLabel("Blank Accept");
		lblBlankAccept.setBounds(6, 215, 132, 16);
		panel.add(lblBlankAccept);
		
		JLabel lblUserInit = new JLabel("User Init");
		lblUserInit.setBounds(6, 243, 132, 10);
		panel.add(lblUserInit);
		
		JLabel lblCbrVersion = new JLabel("CBR Version");
		lblCbrVersion.setBounds(6, 264, 147, 16);
		panel.add(lblCbrVersion);
		
		JLabel lblAltConfigFile = new JLabel("Alt. Config File Location");
		lblAltConfigFile.setBounds(6, 292, 157, 16);
		panel.add(lblAltConfigFile);
		
		JLabel lblDatabaseType = new JLabel("Database Type");
		lblDatabaseType.setBounds(6, 315, 157, 16);
		panel.add(lblDatabaseType);
		
		
		// Text fields go here
		
		
		newDBfield = new JTextField(props.getProperty("newDB"));
		newDBfield.setBounds(184, 10, 134, 28);
		panel.add(newDBfield);
		newDBfield.setColumns(10);
		
		inDBlocField = new JTextField(props.getProperty("inDBloc"));
		inDBlocField.setBounds(184, 40, 134, 28);
		panel.add(inDBlocField);
		inDBlocField.setColumns(10);
		
		outDBlocField = new JTextField(props.getProperty("outDBloc"));
		outDBlocField.setBounds(184, 71, 134, 28);
		panel.add(outDBlocField);
		outDBlocField.setColumns(10);
		
		inWeightsLocField = new JTextField(props.getProperty("inWeightsLoc"));
		inWeightsLocField.setBounds(184, 99, 134, 28);
		panel.add(inWeightsLocField);
		inWeightsLocField.setColumns(10);
		
		outWeightsLocField = new JTextField(props.getProperty("outWeightsLoc"));
		outWeightsLocField.setBounds(184, 127, 134, 28);
		panel.add(outWeightsLocField);
		outWeightsLocField.setColumns(10);
		
		p3pLocField = new JTextField(props.getProperty("p3pLocation"));
		p3pLocField.setBounds(184, 155, 134, 28);
		panel.add(p3pLocField);
		p3pLocField.setColumns(10);
		
		newPolicyLocField = new JTextField(props.getProperty("newPolicyLoc"));
		newPolicyLocField.setBounds(184, 183, 134, 28);
		panel.add(newPolicyLocField);
		newPolicyLocField.setColumns(10);
		
		blankAccField = new JTextField(props.getProperty("blanketAccept"));
		blankAccField.setBounds(184, 209, 134, 28);
		panel.add(blankAccField);
		blankAccField.setColumns(10);
		
		userInitField = new JTextField(props.getProperty("userInit"));
		userInitField.setBounds(184, 234, 134, 28);
		panel.add(userInitField);
		userInitField.setColumns(10);
		
		cbrVerField = new JTextField(props.getProperty("cbrV"));
		cbrVerField.setBounds(184, 258, 134, 28);
		panel.add(cbrVerField);
		cbrVerField.setColumns(10);
		
		altConfLocField = new JTextField(props.getProperty("genConfig"));
		altConfLocField.setBounds(184, 286, 134, 28);
		panel.add(altConfLocField);
		altConfLocField.setColumns(10);
		
		dbTypeField = new JTextField(props.getProperty("policyDB"));
		dbTypeField.setBounds(184, 309, 134, 28);
		panel.add(dbTypeField);
		dbTypeField.setColumns(10);
		
		logLevField = new JTextField(props.getProperty("loglevel"));
		logLevField.setBounds(184, 337, 134, 28);
		panel.add(logLevField);
		logLevField.setColumns(10);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(this);
		btnOk.setBounds(201, 373, 117, 28);
		panel.add(btnOk);
		
		frame.setVisible(true);
	}

	@Override
	public void run() {
		
		initialize();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnOk)){ 
			frame.setVisible(false);
		}
	}
	
	
}
