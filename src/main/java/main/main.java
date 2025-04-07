package main;

import javax.swing.SwingUtilities;

import gui.LoginGUI;

public class main {
	public static void main(String[] args) {
		System.out.println("Starting application...");
		SwingUtilities.invokeLater(() -> {
			new LoginGUI().setVisible(true);
		});
	}
}
