package antics;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import dto.Document;
import dto.Picture;

public class DocumentFactory {
	
	public static JFrame mainFrame;
	
	private static JButton addDocumentBtn;
	private static JButton removeDocumentBtn;
	private static JButton nextDocumentBtn;
	private static JButton prevDocumentBtn;
	
	private static JPanel panelDocuments;
	private static JLabel labelDocuments;
	
	private static JFileChooser chooser;
	
	private static final String CD_BTN_ADD_DOCUMENT = "Aggiungi documento";
	private static final String CD_BTN_REMOVE_DOCUMENT = "Rimuovi documento";
	
	private static final String MSG_ADD_DOCUMENT_OK = "Documento salvato correttamente.";
	private static final String MSG_ADD_DOCUMENT_KO = "Si e' verificato un problema nel salvataggio del documento.";
	private static final String MSG_REMOVE_DOCUMENT_OK = "Documento rimosso correttamente.";
	private static final String MSG_REMOVE_DOCUMENT_KO = "Si e' verificato un problema nella rimozione del documento.";
	
	private ArrayList<Document> documents = new ArrayList<Document>();
	
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
	
	private long getMaxDocumentId(Database db, long entityId) {
		ArrayList<Document> documents = db.getDocumentsFromEntityId(entityId);
		long max = 0;
		for (int i = 0; i < documents.size(); i++) {
			Document d = documents.get(i);
			if (d.getId() > max) {
				max = d.getId();
			}
		}
		return max;
	}
	
	private ArrayList<Document> loadDocuments(Database db, long entityId) {
		ArrayList<Document> documents = db.getDocumentsFromEntityId(entityId);
		return documents;
	}
	
	public DocumentFactory(Database db, long entityId) {
		mainFrame = new JFrame("Documenti");
		mainFrame.setLayout(new BorderLayout());
		
		final Database thisDb = db;
		final long thisEntityId = entityId;
		
		chooser = new JFileChooser();
		
		panelDocuments = new JPanel();
		panelDocuments.setSize(200, 200);
		
		JPanel panelButtons = new JPanel();
		
		final ArrayList<Picture> pictures = loadDocuments(thisDb, thisEntityId);
		
		addDocumentBtn = new JButton(CD_BTN_ADD_DOCUMENT);
		addDocumentBtn.setSize(100, 40);
		addDocumentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					byte[] bytes = getBytesFromFile(file);
					
					long id = getMaxDocumentId(thisDb, thisEntityId) + 1;
					
					Picture p = new Picture();
					p.setId(id);
					p.setEntityId(thisEntityId);
					p.setData(bytes);
					thisDb.insertPicture(p);
				}
			}
		});
		panelButtons.add(addDocumentBtn);
		
		removePictureBtn = new JButton(CD_BTN_REMOVE_PICTURE);
		removePictureBtn.setSize(100, 40);
		removePictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pictures.size() > 0) {
					thisDb.deletePicture(pictures.get(currentPictureIdx).getId());
				}
			}
		});
		panelButtons.add(removePictureBtn);
		
		prevPictureBtn = new JButton(CD_BTN_PREV_PICTURE);
		prevPictureBtn.setSize(100, 40);
		prevPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pictures.size() > 0) {
					currentPictureIdx = (currentPictureIdx - 1) % pictures.size();
					showPicture(pictures.get(currentPictureIdx).getData());
				}
			}
		});
		
		nextPictureBtn = new JButton(CD_BTN_NEXT_PICTURE);
		nextPictureBtn.setSize(100, 40);
		nextPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pictures.size() > 0) {
					currentPictureIdx = (currentPictureIdx + 1) % pictures.size();
					showPicture(pictures.get(currentPictureIdx).getData());
				}
			}
		});
		
		mainFrame.getContentPane().add(prevPictureBtn, BorderLayout.LINE_START);
		mainFrame.getContentPane().add(panelPictures, BorderLayout.CENTER);
		mainFrame.getContentPane().add(nextPictureBtn, BorderLayout.LINE_END);
		mainFrame.getContentPane().add(panelButtons, BorderLayout.PAGE_END);
		
		mainFrame.setSize(500, 500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		if (pictures.size() > 0) {
			Picture firstPicture = pictures.get(0);
			showPicture(firstPicture.getData());
		}
	}

}
