package demo;

import java.awt.EventQueue;

import javax.swing.UIManager;

import view.ElgamalDigitalSignatureView;

public class ElgamalDigitalSignature {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ElgamalDigitalSignatureView frame = new ElgamalDigitalSignatureView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
}
