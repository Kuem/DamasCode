package br.com.thiagomv.damasCode;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.thiagomv.damasCode.view.damas.UIDamas;

public class Main {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		/* Set the Nimbus look and feel */
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			Logger.getLogger(UIDamas.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new UIDamas().setVisible(true);
			}
		});
	}
}
