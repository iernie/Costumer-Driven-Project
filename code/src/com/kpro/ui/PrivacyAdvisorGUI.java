package com.kpro.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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
	private static Object lock = new Object();
	
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
			loadDB();
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
		loadDBMenuItem= new JMenuItem("Reload Database", KeyEvent.VK_D);
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
	public void user_init(Properties genProps){
		final ConfigEditor2 ce = new ConfigEditor2(genProps);
		ce.run();
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
				caseNode = new DefaultMutableTreeNode(c.getDataType());
				
				purpose = new DefaultMutableTreeNode("Purpose");
				for (Purpose p : c.getPurposes())
					purpose.add(new DefaultMutableTreeNode(p.toString() + (p.isOptional() ? " - Optional" : "")));
				caseNode.add(purpose);
				
				recipient = new DefaultMutableTreeNode("Recipient");
				for (Recipient r : c.getRecipients())
					recipient.add(new DefaultMutableTreeNode(r.toString() + (r.isOptional() ? " - Optional" : "")));
				caseNode.add(recipient);
				
				retention = new DefaultMutableTreeNode("Retention");
				for(Retention r : c.getRetentions())
					retention.add(new DefaultMutableTreeNode(r.toString()));
				caseNode.add(retention);
				
				if (c.getCategories() != null) {
					category = new DefaultMutableTreeNode("Category");
					for (Category ca : c.getCategories())
						category.add(new DefaultMutableTreeNode(ca.toString()));
					caseNode.add(category);
				}
				
				policyObj.add(caseNode);
			}
			policyObj.add(new DefaultMutableTreeNode(po.getAction()));
			root.add(policyObj);
		}	
	}
	
	private void buildTree(DefaultMutableTreeNode root, PolicyObject po)
	{
		DefaultMutableTreeNode 	policyObj = null,
								caseNode = null,
								purpose = null,
								recipient = null, 
								retention = null,
								category = null;
		
		policyObj = new DefaultMutableTreeNode(po.getContextDomain());
		
		for (Case c : po){
			caseNode = new DefaultMutableTreeNode(c.getDataType());
			
			purpose = new DefaultMutableTreeNode("Purpose");
			for (Purpose p : c.getPurposes())
				purpose.add(new DefaultMutableTreeNode(p.toString() + (p.isOptional() ? " - Optional" : "")));
			caseNode.add(purpose);
			
			recipient = new DefaultMutableTreeNode("Recipient");
			for (Recipient r : c.getRecipients())
				recipient.add(new DefaultMutableTreeNode(r.toString() + (r.isOptional() ? " - Optional" : "")));
			caseNode.add(recipient);
			
			retention = new DefaultMutableTreeNode("Retention");
			for(Retention r : c.getRetentions())
				retention.add(new DefaultMutableTreeNode(r.toString()));
			caseNode.add(retention);
			
			if (c.getCategories() != null) {
				category = new DefaultMutableTreeNode("Category");
				for (Category ca : c.getCategories())
					category.add(new DefaultMutableTreeNode(ca.toString()));
				caseNode.add(category);
			}
			
			policyObj.add(caseNode);
		}
		policyObj.add(new DefaultMutableTreeNode(po.getAction()));
		root.add(policyObj);
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
	
	private class ConfigEditor2 {

		private JFrame frame;
		private JPanel panel;
		
		private JButton btnOk;
		
		private HashMap<String,String> fieldNames; // config field descriptions
		private HashMap<String, JButton> buttons; // mapping from config field names to buttons
		private HashMap<String, JTextField> textfields; // etc
		private HashMap<String, JComboBox> comboboxes;
		private HashMap<String, JCheckBox> checkboxes;	

		private Properties genProps;
		
		private String[] userInitModel 	= 	{"true", "false"};
		private String[] uiModel =			{"PrivacyAdvisorGUI","UserIO_Simple"};	 
		private String[] cbrVerModel 	= 	{"bitmapDistanceWisOne,Reduction_KNN:1,Conclusion_Simple,LearnAlgSimpler"};
		private String[] dbTypeModel 	= 	{"PDatabase"};
		private String[] logLvlModel	=  	{"INFO"};
		private String[] networkTypeModel = {"NRCouchdb"};
		
		
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
			comboboxes.put("policyDB", new JComboBox(dbTypeModel));
			
			fieldNames.put("p3pLocation", "Add new P3P to DB");
			textfields.put("p3pLocation", new JTextField());
			buttons.put("p3pLocation", new JButton("Set"));
			
			fieldNames.put("p3pDirLocation", "Add P3P Dir to DB");
			textfields.put("p3pDirLocation", new JTextField());
			buttons.put("p3pDirLocation", new JButton("Set"));
			
			fieldNames.put("newPolicyLoc", "P3P to Evaluate");
			textfields.put("newPolicyLoc", new JTextField());
			buttons.put("newPolicyLoc", new JButton("Set"));
			
			fieldNames.put("inDBLoc", "Input DB Location");
			textfields.put("inDBLoc", new JTextField());
			buttons.put("inDBLoc", new JButton("Set"));
			
			fieldNames.put("outDBLoc", "Output DB Location");
			textfields.put("outDBLoc", new JTextField());
			buttons.put("outDBLoc", new JButton("Set"));
			
			fieldNames.put("inWeightsLoc", "Input Weights Location");
			textfields.put("inWeightsLoc", new JTextField());
			buttons.put("inWeightsLoc", new JButton("Set"));
			
			fieldNames.put("outWeightsLoc", "Output Weights Location");
			textfields.put("outWeightsLoc", new JTextField());
			buttons.put("outWeightsLoc", new JButton("Set"));
			
			fieldNames.put("cbrV", "CBR Version");
			textfields.put("cbrV", new JTextField());
			
			fieldNames.put("userIO", "User Interface");
			comboboxes.put("userIO", new JComboBox(uiModel));
			
			fieldNames.put("userInit", "User Init");
			comboboxes.put("userInit", new JComboBox(userInitModel));
			
			fieldNames.put("NetworkRType", "Network Type");
			comboboxes.put("NetworkRType", new JComboBox(networkTypeModel));
			
			fieldNames.put("NetworkROptions", "Networking Options");
			textfields.put("NetworkROptions", new JTextField());
			
			fieldNames.put("useNet", "Use Networking");
			checkboxes.put("useNet", new JCheckBox());
			
			// Add action listeners to all components
			for(JCheckBox c : checkboxes.values())
				c.addActionListener(new ConfEditorActionListener());
			for(JTextField f : textfields.values())
				f.setEditable(false);
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
		public ConfigEditor2(Properties genProps)
		{
			this.genProps = genProps;

		}
		
		/**
		 * Zero-arg constructor for testing.
		 */
		public ConfigEditor2(){}
		
		
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
			panel.setBorder(new EmptyBorder(10, 10, 10, 10));
			panel.setLayout(new GridLayout(18, 3));
			frame.add(panel);		
			
			
			
			for (String str : checkboxes.keySet())
			{
				panel.add(new JLabel(fieldNames.get(str)));
				panel.add(checkboxes.get(str));
				panel.add(new JLabel(""));
			}
			for (String str : comboboxes.keySet())
			{
				panel.add(new JLabel(fieldNames.get(str)));
				panel.add(comboboxes.get(str));
				panel.add(new JLabel(""));
			}
			for (String str : textfields.keySet())
			{
				panel.add(new JLabel(fieldNames.get(str)));
				panel.add(textfields.get(str));
				if(buttons.containsKey(str))
					panel.add(buttons.get(str));
				else 
					panel.add(new JLabel(""));
			}

			panel.add(btnOk = new JButton("Ok"));
			btnOk.addActionListener(new ConfEditorActionListener());
			frame.setVisible(true);
		}
		
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
		private void updateProps(){
			if(genProps == null)
				genProps = new Properties();
			
			for (String key : textfields.keySet())
				genProps.setProperty(key, textfields.get(key).getText());
			for (String key : comboboxes.keySet()) 
				genProps.setProperty(key, (String)comboboxes.get(key).getSelectedItem());
			for (String key : checkboxes.keySet())
				genProps.setProperty(key, checkboxes.get(key).isSelected() ? "true" : "false");
			
			gio.setGenProps(genProps);
		}
		
		/**
		 * Finds key corresponding to a value in 
		 * an injective Map.
		 * @param buttons
		 * @param object
		 * @return key
		 */
		public String getKeyByValue(HashMap<String, JButton> buttons, JButton btn) {
		    for (String s : buttons.keySet())
		        if (buttons.get(s).equals(btn))
		            return s;
		    return null;
		}

		public JFrame getFrame() {
			return frame;
		}

		public Properties getGenProps() {
			return genProps;
		}
		
	}
}