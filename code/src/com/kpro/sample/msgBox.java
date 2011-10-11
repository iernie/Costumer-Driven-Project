package com.kpro.sample;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class msgBox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		int tull= JOptionPane.showConfirmDialog(null, "Hei?");
//		System.out.println(tull);
		
		JFileChooser jfc = new JFileChooser();
		int tall = jfc.showOpenDialog(null);
		System.out.println(tall);
		System.out.println(jfc.getSelectedFile().getAbsolutePath());
		

	}

}
