package com.kpro.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

abstract class ConfigEditor {

	private JFrame frame;
	private JPanel panel;
	
	private JButton btnOk;
	
	private HashMap<String,String> fieldNames; // config field descriptions
	private HashMap<String, JButton> buttons; // mapping from config field names to buttons
	private HashMap<String, JTextField> textfields; // etc
	private HashMap<String, JComboBox> comboboxes;
	private HashMap<String, JCheckBox> checkboxes;	

	protected Properties genProps;
	
	private String[] logLvlModel	=  	{"OFF", "FATAL", "ERROR", "WARN", 
										 "INFO", "DEBUG", "TRACE"};
	
	/**
	 * Initialize SWING components.
	 */
	private void InitComponents(){
		// INitialize components
		fieldNames = new HashMap<String, String>();
		buttons = new HashMap<String, JButton>();
		textfields = new HashMap<String, JTextField>();
		comboboxes = new HashMap<String, JComboBox>();
		checkboxes = new HashMap<String, JCheckBox>();
			
		fieldNames.put("loglevel", "Log level");
		comboboxes.put("loglevel", new JComboBox(logLvlModel));
		
		fieldNames.put("newDB", "Build new DB");
		checkboxes.put("newDB", new JCheckBox());
		
		fieldNames.put("policyDB", "DB Type");
		textfields.put("policyDB", new JTextField());
		
		fieldNames.put("p3pLocation", "Add new P3P to DB");
		textfields.put("p3pLocation", new JTextField());
		buttons.put("p3pLocation", new JButton("Choose file"));
		
		fieldNames.put("p3pDirLocation", "Add P3P Dir to DB");
		textfields.put("p3pDirLocation", new JTextField());
		buttons.put("p3pDirLocation", new JButton("Choose file"));
		
		fieldNames.put("newPolicyLoc", "P3P to Evaluate");
		textfields.put("newPolicyLoc", new JTextField());
		buttons.put("newPolicyLoc", new JButton("Choose file"));
		
		fieldNames.put("inDBLoc", "Input DB Location");
		textfields.put("inDBLoc", new JTextField());
		buttons.put("inDBLoc", new JButton("Choose file"));
		
		fieldNames.put("outDBLoc", "Output DB Location");
		textfields.put("outDBLoc", new JTextField());
		buttons.put("outDBLoc", new JButton("Choose file"));
		
		fieldNames.put("inWeightsLoc", "Input Weights Location");
		textfields.put("inWeightsLoc", new JTextField());
		buttons.put("inWeightsLoc", new JButton("Choose file"));
		
		fieldNames.put("outWeightsLoc", "Output Weights Location");
		textfields.put("outWeightsLoc", new JTextField());
		buttons.put("outWeightsLoc", new JButton("Choose file"));
		
		fieldNames.put("cbrV", "CBR Version");
		textfields.put("cbrV", new JTextField());
		
		fieldNames.put("NetworkRType", "Network Type");
		textfields.put("NetworkRType", new JTextField());
		
		fieldNames.put("NetworkROptions", "Networking Options");
		textfields.put("NetworkROptions", new JTextField());
		
		fieldNames.put("useNet", "Use Networking");
		checkboxes.put("useNet", new JCheckBox());
		
		// Add action listeners to all components
		for(JCheckBox c : checkboxes.values())
			c.addActionListener(new ConfEditorActionListener());
		for(JButton b : buttons.values())
			b.addActionListener(new ConfEditorActionListener());
		for(JComboBox b : comboboxes.values())
			b.addActionListener(new ConfEditorActionListener());
		
		// Set SWING component values according to config file
		if(genProps != null){
			for (String str : textfields.keySet())
				textfields.get(str).setText(genProps.getProperty(str));
			for (String str : comboboxes.keySet())
				comboboxes.get(str).setSelectedItem(genProps.getProperty(str));
			for (String str : checkboxes.keySet())
				checkboxes.get(str).setSelected(genProps.getProperty(str).equals("true"));
		}
		
	}
	
	
	
	/**
	 * Default constructor.
	 * @param genProps
	 */
	public ConfigEditor(Properties genProps)
	{
		this.genProps = genProps;

	}
	
	/**
	 * Zero-arg constructor for testing.
	 */
	public ConfigEditor(){}
	
	
	/**
	 * Initalize frame window.
	 */
	private void InitFrame()
	{
		frame = new JFrame("Configure");
		frame.setSize(700,600);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setLayout(new GridBagLayout());
		frame.add(panel);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		
		for (String str : checkboxes.keySet())
		{
			c.gridx = 0;
			panel.add(new JLabel(fieldNames.get(str)), c);
			
			c.gridx = 1;
			panel.add(checkboxes.get(str), c);
			
			c.gridy += 1;
		}
		
		for (String str : comboboxes.keySet())
		{
			c.gridx = 0;
			c.gridwidth = 1;
			panel.add(new JLabel(fieldNames.get(str)), c);
			
			c.gridx = 1;
			c.gridwidth = 2;
			panel.add(comboboxes.get(str), c);
			
			c.gridy += 1;
		}
		
		for (String str : textfields.keySet())
		{
			c.gridx = 0;
			c.gridwidth = 1;
			panel.add(new JLabel(fieldNames.get(str)), c);
			
			c.gridx = 1;
			c.gridwidth = 2;
			if(buttons.containsKey(str)) {
				panel.add(textfields.get(str), c);
				
				c.gridx = 2;
				panel.add(buttons.get(str), c);
			} else {
				c.gridwidth = 3;
				panel.add(textfields.get(str), c);
			}
			
			c.gridy += 1;
		}

		c.gridx = 2;
		c.gridwidth = 2;
		panel.add(btnOk = new JButton("Save"), c);
		btnOk.addActionListener(new ConfEditorActionListener());
		frame.setVisible(true);
	}
	
	/**
	 * To build and run the ConfigEditor
	 */
	public void run()
	{
		InitComponents();
		InitFrame();
	}
	
	/**
	 * Opens a JFileChooser to various input/output
	 * paths for configuration parameters.
	 * @param directory
	 * @return path
	 */
	private String openFile(boolean directory)
	{
		String path = null;
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		if(directory)
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		else
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int retVal = jfc.showOpenDialog(null);
		if (retVal == JFileChooser.APPROVE_OPTION)
			path = jfc.getSelectedFile().getAbsolutePath();
		return path;
	}
	
	/**
	 * A standard {@link ActionListener} for {@link ConfigEditor}.
	 * @author ulfnore
	 */
	private class ConfEditorActionListener implements ActionListener
	{
		/**
		 * A standard ActionPerformed method.
		 * See {@link ActionListener}.
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() instanceof JButton)
			{
				String key = getKeyByValue(buttons, (JButton)e.getSource());
				if (key != null){
					String path = null;
					path = openFile(key.equals("p3pDirLocation") ? 
									true : false);
					textfields.get(key).setText(path);
					updateProps();
				}else if (e.getSource() ==  btnOk){
					updateProps();
					frame.setVisible(false);
				}
			}
			else updateProps();
			
		}
		
	}
	
	/** 
	 * Update local properties object according to
	 * values in SWING components.
	 */
	protected void updateProps(){
		if(genProps == null)
			genProps = new Properties();
		
		for (String key : textfields.keySet())
			genProps.setProperty(key, textfields.get(key).getText());
		for (String key : comboboxes.keySet()) 
			genProps.setProperty(key, (String)comboboxes.get(key).getSelectedItem());
		for (String key : checkboxes.keySet())
			genProps.setProperty(key, checkboxes.get(key).isSelected() ? "true" : "false");
	}
	
	/**
	 * Finds key corresponding to a value in 
	 * an injective Map.
	 * @param buttons
	 * @param object
	 * @return key
	 */
	public static String getKeyByValue(HashMap<String, JButton> buttons, JButton btn) {
	    for (String s : buttons.keySet())
	        if (buttons.get(s).equals(btn))
	            return s;
	    return null;
	}

	/**
	 * Returns the properties from the config editor
	 * @return Properties genProps
	 */
	public Properties getGenProps() {
		return genProps;
	}
	
}
