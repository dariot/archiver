package antics;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Clob;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialClob;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import dto.Picture;

public class PictureFactory {
	
	public static JFrame mainFrame;
	
	private static JButton addPictureBtn;
	private static JButton removePictureBtn;
	private static JButton nextPictureBtn;
	private static JButton prevPictureBtn;
	
	private static JPanel panelPictures;
	private static JLabel labelPictures;
	
	private static JFileChooser chooser;
	
	private static final String CD_BTN_ADD_PICTURE = "Aggiungi immagine";
	private static final String CD_BTN_REMOVE_PICTURE = "Rimuovi immagine";
	private static final String CD_BTN_NEXT_PICTURE = ">";
	private static final String CD_BTN_PREV_PICTURE = "<";
	
	private static final String MSG_ADD_PICTURE_OK = "Immagine salvata correttamente.";
	private static final String MSG_ADD_PICTURE_KO = "Si e' verificato un problema nel salvataggio dell''immagine.";
	private static final String MSG_REMOVE_PICTURE_OK = "Immagine rimossa correttamente.";
	private static final String MSG_REMOVE_PICTURE_KO = "Si e' verificato un problema nella rimozione dell''immagine.";
	
	private ArrayList<Picture> pictures = new ArrayList<Picture>();
	
	private byte[] getBytesFromFile(File f) {
		byte[] bFile = new byte[(int) f.length()];
        
        try {
		    FileInputStream fileInputStream = new FileInputStream(f);
		    fileInputStream.read(bFile);
		    fileInputStream.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        return bFile;
	}
	
	private long getMaxPictureId(Database db, long entityId) {
		ArrayList<Picture> pictures = db.getPicturesFromEntityId(entityId);
		long max = 0;
		for (int i = 0; i < pictures.size(); i++) {
			Picture p = pictures.get(i);
			if (p.getId() > max) {
				max = p.getId();
			}
		}
		return max;
	}
	
	private ArrayList<Picture> loadPictures(Database db, long entityId) {
		ArrayList<Picture> pictures = db.getPicturesFromEntityId(entityId);
		return pictures;
	}
	
	public PictureFactory(Database db, long entityId) {
		mainFrame = new JFrame("Immagini");
		mainFrame.setLayout(new BorderLayout());
		
		final Database thisDb = db;
		final long thisEntityId = entityId;
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg", "pdf");
		chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		
		panelPictures = new JPanel();
		panelPictures.setSize(200, 200);
		
		JPanel panelButtons = new JPanel();
		
		addPictureBtn = new JButton(CD_BTN_ADD_PICTURE);
		addPictureBtn.setSize(100, 40);
		addPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					byte[] bytes = getBytesFromFile(file);
					
					long id = getMaxPictureId(thisDb, thisEntityId) + 1;
					
					Picture p = new Picture();
					p.setId(id);
					p.setEntityId(thisEntityId);
					p.setData(bytes);
					thisDb.insertPicture(p);
					
					ImageIcon imageIcon = new ImageIcon(bytes);
					Image image = imageIcon.getImage();
					image = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
					imageIcon = new ImageIcon(image);
					labelPictures = new JLabel(imageIcon);
					
					panelPictures.add(labelPictures);
					panelPictures.revalidate();
				}
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
		
		nextPictureBtn = new JButton(CD_BTN_NEXT_PICTURE);
		nextPictureBtn.setSize(100, 40);
		nextPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		
		ArrayList<Picture> pictures = loadPictures(thisDb, thisEntityId);
		
		mainFrame.getContentPane().add(prevPictureBtn, BorderLayout.LINE_START);
		mainFrame.getContentPane().add(panelPictures, BorderLayout.CENTER);
		mainFrame.getContentPane().add(nextPictureBtn, BorderLayout.LINE_END);
		mainFrame.getContentPane().add(panelButtons, BorderLayout.PAGE_END);
		
		mainFrame.setSize(500, 500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

}
