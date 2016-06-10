package antics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PictureFactory {
	
	public static JFrame mainFrame;
	
	private static JButton addPictureBtn;
	private static JButton removePictureBtn;
	private static JButton nextPictureBtn;
	private static JButton prevPictureBtn;
	
	private static final String CD_BTN_ADD_PICTURE = "Aggiungi immagine";
	private static final String CD_BTN_REMOVE_PICTURE = "Rimuovi immagine";
	private static final String CD_BTN_NEXT_PICTURE = ">";
	private static final String CD_BTN_PREV_PICTURE = "<";
	
	private static final String MSG_ADD_PICTURE_OK = "Immagine salvata correttamente.";
	private static final String MSG_ADD_PICTURE_KO = "Si e' verificato un problema nel salvataggio dell''immagine.";
	private static final String MSG_REMOVE_PICTURE_OK = "Immagine rimossa correttamente.";
	private static final String MSG_REMOVE_PICTURE_KO = "Si e' verificato un problema nella rimozione dell''immagine.";
	
	public PictureFactory() {
		mainFrame = new JFrame("Immagini");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(new BorderLayout());
		
		JPanel panelButtons = new JPanel();
		
		addPictureBtn = new JButton(CD_BTN_ADD_PICTURE);
		addPictureBtn.setSize(100, 40);
		addPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		panelButtons.add(addPictureBtn);
		
		removePictureBtn = new JButton(CD_BTN_REMOVE_PICTURE);
		removePictureBtn.setSize(100, 40);
		removePictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		panelButtons.add(removePictureBtn);
		
		prevPictureBtn = new JButton(CD_BTN_PREV_PICTURE);
		prevPictureBtn.setSize(100, 40);
		prevPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		panelButtons.add(prevPictureBtn);
		
		nextPictureBtn = new JButton(CD_BTN_NEXT_PICTURE);
		nextPictureBtn.setSize(100, 40);
		nextPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		panelButtons.add(nextPictureBtn);
		
		mainFrame.getContentPane().add(panelButtons, BorderLayout.PAGE_START);
		
		mainFrame.setSize(500, 500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

}
