package com.kpro.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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

	
	//private String[][][] filesfields = {{"inDBLoc","outDBLoc",""},{"Location of previous database", "Location to save database to",""}}; //varnames,prettynames,values 
	
	
	
	
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
		panel.setLayout(new GridLayout(16, 3));
		frame.add(panel);		
		
		
		// ADD BUTTONS
		panel.add(new JLabel("Build New DB"));
		panel.add(newDBBox = new JCheckBox());
		panel.add(new JLabel(""));
		newDBBox.addActionListener(this);
		
		panel.add(new JLabel("Blanket Accept"));
		panel.add(blankAcceptBox = new JCheckBox());
		panel.add(new JLabel(""));
		blankAcceptBox.addActionListener(this);
		
		panel.add(new JLabel("In DB Location"));
		panel.add(inDBfileField = new JTextField());
		panel.add(btnSetInDBloc = new JButton("Set"));
		btnSetInDBloc.addActionListener(this);
		
		panel.add(new JLabel("Out DB Location"));
		panel.add(outDBfileField = new JTextField());
		panel.add(btnSetOutDBloc = new JButton("Set"));
		btnSetOutDBloc.addActionListener(this);
		
		panel.add(new JLabel("In Weights Location"));
		panel.add(inWeightsFileField = new JTextField());
		panel.add(btnSetInWeightsLoc = new JButton("Set"));
		btnSetInWeightsLoc.addActionListener(this);
		
		panel.add(new JLabel("Out Weights Location"));
		panel.add(outWeightsFileField = new JTextField());
		panel.add(btnSetOutWeightsLoc = new JButton("Set"));
		btnSetOutWeightsLoc.addActionListener(this);
		
		panel.add(new JLabel("P3P InFile Location"));
		panel.add(p3pFileField = new JTextField());
		panel.add(btnSetP3PFileLoc = new JButton("Set"));
		btnSetP3PFileLoc.addActionListener(this);
		
		panel.add(new JLabel("P3P InDirectory Location"));
		panel.add(p3pDirField = new JTextField());
		panel.add(btnSetP3PDirLoc = new JButton("Set"));
		btnSetP3PDirLoc.addActionListener(this);
		
		panel.add(new JLabel("User Init"));
		panel.add(userInitBox = new JComboBox(userInitModel));
		panel.add(new JLabel(""));
		userInitBox.addActionListener(this);
		
		panel.add(new JLabel("CBR Version"));
		panel.add(cbrVerField = new JTextField());
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("Alt Config File Location"));
		panel.add(confFileField = new JTextField());
		panel.add(btnSetAltConfLoc = new JButton("Set"));
		btnSetAltConfLoc.addActionListener(this);
		
		panel.add(new JLabel("New Policy File Loc"));
		panel.add(newPolicyFileField = new JTextField());
		panel.add(btnSetPolicyLoc = new JButton("Set"));
		btnSetPolicyLoc.addActionListener(this);
		
		panel.add(new JLabel("DB Type"));
		panel.add(dbTypeBox = new JComboBox(dbTypeModel));
		panel.add(new JLabel(""));
		dbTypeBox.addActionListener(this);
		
		panel.add(new JLabel("Networking"));
		panel.add(networkingBox = new JComboBox(networkModel));
		panel.add(new JLabel(""));
		networkingBox.addActionListener(this);
		
		panel.add(new JLabel("Log Level"));
		panel.add(logLvlBox = new JComboBox(logLvlModel));
		panel.add(new JLabel(""));
		logLvlBox.addActionListener(this);
		
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(btnOk = new JButton("Ok"));
		btnOk.addActionListener(this);
		
		frame.setVisible(true);
	}
	
	/**
	 * Fill properties data into forms.
	 */
	private void FillPropertiesData()
	{
		if(genProps != null){
			// combo boxes
			newDBBox.setSelected(genProps.getProperty("inDBLoc") == "true");
			blankAcceptBox.setSelected(genProps.getProperty("blanketAccept") == "true");
			
			// text fields
			inDBfileField.setText(genProps.getProperty("inDBLoc"));
			outDBfileField.setText(genProps.getProperty("outDBLoc"));
			inWeightsFileField.setText(genProps.getProperty("inWeightsLoc"));
			outWeightsFileField.setText(genProps.getProperty("outWeightsLoc"));
			p3pFileField.setText(genProps.getProperty("p3pLocation"));
			p3pDirField.setText(genProps.getProperty("p3pDirLocation"));
			confFileField.setText(genProps.getProperty("genConfig"));
			newPolicyFileField.setText(genProps.getProperty("newPolicyLoc"));
			
			// make all fields ineditable
			inDBfileField.setEditable(false);
			outDBfileField.setEditable(false);
			inWeightsFileField.setEditable(false);
			outWeightsFileField.setEditable(false);
			p3pDirField.setEditable(false);
			p3pFileField.setEditable(false);
			newPolicyFileField.setEditable(false);
			confFileField.setEditable(false);
		}
		
	}
	
	public void run()
	{
		InitFrame();
		FillPropertiesData();
	}
	
	
	/**
	 * Standard actionPerformed method. See
	 * {@link java.awt.event.ActionListener}.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() ==  btnOk)
		{
			frame.setVisible(false);
			
		}else{
			// Setting file paths
			if ( e.getSource() ==  btnSetAltConfLoc){
				setPropsFile("genConfig", false);
			}else if ( e.getSource() == btnSetInDBloc){
				setPropsFile("inDBLoc", false);
			}else if( e.getSource() == btnSetInWeightsLoc){
				setPropsFile("inWeightsLoc", false);
			}else if (e.getSource() == btnSetOutDBloc){
				setPropsFile("outDBLoc", false);
			}else if (e.getSource() == btnSetOutWeightsLoc){
				setPropsFile("outWeightsLoc", false);
			}else if (e.getSource() == btnSetP3PDirLoc){
				setPropsFile("p3pDirLocation", true);
			}else if (e.getSource() == btnSetP3PFileLoc){
				setPropsFile("p3pLocation", false);
			}else if (e.getSource() == btnSetPolicyLoc){
				setPropsFile("newPolicyLoc", false);
			}
			
			// checkboxes 
			else if(e.getSource() == blankAcceptBox){
				genProps.setProperty("newDB", blankAcceptBox.isSelected() ? 
											  "true" : "false");
			}else if(e.getSource() == newDBBox){
				genProps.setProperty("newDB", newDBBox.isSelected() ? 
											  "true" : "false");
			}
			
			// comboboxes
			else if (e.getSource() == userInitBox){
				genProps.setProperty("userInit", userInitBox.getSelectedItem().toString());
			}else if (e.getSource() == networkingBox){
				genProps.setProperty("networkRType", networkingBox.getSelectedItem().toString());
			}else if (e.getSource() == logLvlBox){
				genProps.setProperty("loglevel", logLvlBox.getSelectedItem().toString());
			}else if (e.getSource() == dbTypeBox){
				genProps.setProperty("policyDB", dbTypeBox.getSelectedItem().toString());
			}
			
		}
	}
	
	
	/**
	 * Updates file paths in properties file and
	 * confEditor text fields based on choices
	 * made using set buttons.
	 *  
	 * @param propsField
	 * @param directory
	 */
	private void setPropsFile(String propsField, boolean directory)
	{
		JFileChooser jfc = new JFileChooser(genProps.getProperty(propsField));
		jfc.setFileSelectionMode(directory ? 
				JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		int retVal = jfc.showOpenDialog(null);
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			String path = jfc.getSelectedFile().getAbsolutePath();
			genProps.setProperty(propsField, path);
			
			//update textfields to hold file path

			if(propsField == "inDBLoc")  
				inDBfileField.setText(path);
			else if (propsField=="outDBLoc") 
				outDBfileField.setText(path);
			else if (propsField ==  "inWeightsLoc")
				inWeightsFileField.setText(path);
			else if (propsField == "outWeightsLoc")
				outWeightsFileField.setText(path);
			else if (propsField == "p3pLocation")
				p3pFileField.setText(path);
			else if (propsField == "p3pDirLocation")
				p3pDirField.setText(path);
			else if (propsField == "genConfig")
				confFileField.setText(path);
			else if (propsField == "newPolicyLoc") 
				newPolicyFileField.setText(path);

		}
		
	}
	
	
	
	
	public static void main(String[] args) 
	{
		new ConfEditor().run();
	}
	
}
