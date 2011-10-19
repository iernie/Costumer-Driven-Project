package com.kpro.sample;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class msgBox {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Desktop desktop = Desktop.getDesktop();
		System.out.println(Desktop.isDesktopSupported());
		File f = new File("/Users/ulfnore/Desktop/test.txt");
		desktop.edit(f);


	}

}
