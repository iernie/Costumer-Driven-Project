package com.kpro.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfEditor extends Thread implements ActionListener
{
	private JFrame frame;
	private JPanel panel;
	
	private JTextField inDBfileField;
	private JTextField outDBfileField;
	private JTextField inWeightsFileField;
	private JTextField outWeightsFileField;
	private JTextField p3pFileField;
	private JTextField p3pDirField;
	private JTextField newPolicyFileField;
	private JTextField confFileField;
	
	private JCheckBox newDBBox;
	private JCheckBox blankAcceptBox;
	private JComboBox userInitBox;
	private JTextField cbrVerField;
	private JComboBox dbTypeBox;
	private JComboBox logLvlBox;
	private JComboBox networkingBox;
	
	private JButton btnOk;
	private JButton btnSetInDBloc;
	private JButton btnSetOutDBloc;
	private JButton btnSetInWeightsLoc;
	private JButton btnSetOutWeightsLoc;
	private JButton btnSetP3PFileLoc;
	private JButton btnSetP3PDirLoc;
	private JButton btnSetPolicyLoc;
	private JButton btnSetAltConfLoc;
	
	private String[] userInitModel 	= {"default"};
	private String[] cbrVerModel 	= {"default"};
	private String[] dbTypeModel 	= {"default"};
	private String[] logLvlModel	= {"default"};
	private String[] networkModel	= {"none"};
	
	private Properties genProps;
	
	
	
	public ConfEditor(Properties genProps)
	{
		this.genProps = genProps;
	}
	public ConfEditor(){
		InitFrame();
	}
	
	
	/**
	 * Initalize frame window.
	 */
	private void InitFrame()
	{
		frame = new JFrame("Configuration Editor");
		frame.setSize(400,600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(new GridLayout(15, 3));
		frame.add(panel);		
		
		panel.add(new JLabel("Build New DB"));
		panel.add(newDBBox = new JCheckBox());
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("Blanket Accept"));
		panel.add(blankAcceptBox = new JCheckBox());
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("In DB Location"));
		panel.add(inDBfileField = new JTextField());
		panel.add(btnSetInDBloc = new JButton("Set"));
		
		panel.add(new JLabel("Out DB Location"));
		panel.add(outDBfileField = new JTextField());
		panel.add(btnSetOutDBloc = new JButton("Set"));
		
		panel.add(new JLabel("In Weights Location"));
		panel.add(inWeightsFileField = new JTextField());
		panel.add(btnSetInWeightsLoc = new JButton("Set"));
		
		panel.add(new JLabel("Out Weights Location"));
		panel.add(outWeightsFileField = new JTextField());
		panel.add(btnSetOutWeightsLoc = new JButton("Set"));
		
		panel.add(new JLabel("P3P InFile Location"));
		panel.add(p3pFileField = new JTextField());
		panel.add(btnSetP3PFileLoc = new JButton("Set"));
		
		panel.add(new JLabel("P3P InDirectory Location"));
		panel.add(p3pDirField = new JTextField());
		panel.add(btnSetP3PDirLoc = new JButton("Set"));
		
		panel.add(new JLabel("User Init"));
		panel.add(userInitBox = new JComboBox(userInitModel));
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("CBR Version"));
		panel.add(cbrVerField = new JTextField());
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("Alt Config File Location"));
		panel.add(confFileField = new JTextField());
		panel.add(btnSetAltConfLoc = new JButton("Set"));
		
		panel.add(new JLabel("DB Type"));
		panel.add(dbTypeBox = new JComboBox(dbTypeModel));
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("Networking"));
		panel.add(networkingBox = new JComboBox(networkModel));
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("Log Level"));
		panel.add(logLvlBox = new JComboBox(logLvlModel));
		panel.add(new JLabel(""));
		
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(btnOk = new JButton("Run"));
		btnOk.addActionListener(this);
		
		frame.setVisible(true);
	}
	
	/**
	 * Fill properties data into forms.
	 */
	private void FillPropertiesData()
	{
		newDBBox.setSelected(genProps.getProperty("inDBLoc") == "true");
		blankAcceptBox.setSelected(genProps.getProperty("blanketAccept") == "true");
		
		// text fields
		inDBfileField.setText(genProps.getProperty("inDBLoc"));
		outDBfileField.setText(genProps.getProperty("outDBLoc"));
		inWeightsFileField.setText(genProps.getProperty("inWeightsLoc"));
		outWeightsFileField.setText(genProps.getProperty("outWeightsLoc"));
		p3pFileField.setText(genProps.getProperty("p3pLocation"));
		p3pDirField.setText(genProps.getProperty("p3pDirLocation"));
		newPolicyFileField.setText(genProps.getProperty("newPolicyLoc"));
		confFileField.setText(genProps.getProperty("genConfig"));
		
		
	}
	/** 
	 * Update properties object whenever
	 * a GUI field is changed. Called by
	 * actionPerformed.
	 */
	private void updateProps()
	{
		//TODO: write this method
	}
	
	public void run()
	{
		InitFrame();
		FillPropertiesData();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() ==  btnOk)
		{
			frame.setVisible(false);			
			
		}
		else
		{
			updateProps();
		}
	}
	
	
	public static void main(String[] args) {
		new ConfEditor().run();
	}
	
}
