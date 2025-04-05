// main/Main.java
package main;

import javax.swing.SwingUtilities;

import gui.EmployeeGUI;

public class main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new EmployeeGUI().setVisible(true);
		});
	}
}