// main/Main.java
package main;

import javax.swing.SwingUtilities;
import gui.LoginGUI;

public class main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new LoginGUI().setVisible(true);
		});
	}
}
