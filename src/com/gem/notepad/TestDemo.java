package com.gem.notepad;

import javax.swing.SwingUtilities;

public class TestDemo {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				TextDAO textDAO = new FileTextDAO();
				NotePad notePad = new NotePad(textDAO);
				notePad.setVisible(true);
			}
		});
	}
}
