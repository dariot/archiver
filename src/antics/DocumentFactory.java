package antics;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dto.CommonTableModel;
import dto.Document;

public class DocumentFactory {
	
	public static JFrame mainFrame;
	
	public final Database thisDb;
	
	public static final String FILE_PATH = "NON_TOCCARE/tempFiles/";
	
	private static JButton addDocumentBtn;
	private static JButton removeDocumentBtn;
	
	private static JPanel panelDocuments;
	private static JLabel labelDocuments;
	
	private static CommonTableModel dtmDocuments;
	private static JTable tableDocuments;
	private static String[] documentsColumns = {"ID", "Documento"};
	
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
	
	private long getMaxDocumentId(Database db) {
		return db.getMaxDocumentsId();
	}
	
	private ArrayList<Document> loadDocuments(Database db, long entityId) {
		ArrayList<Document> documents = db.getDocumentsFromEntityId(entityId);
		return documents;
	}
	
	private void openDocumentFromByteArray(byte[] data, String name) throws IOException {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			
			FileOutputStream fos = new FileOutputStream(FILE_PATH + name);
			fos.write(data);
			fos.close();
			
			File file = new File(FILE_PATH + name);
			desktop.open(file);
		}
	}
	
	private void showTableDocuments() {
    	for (int i = 0; i < documents.size(); i++) {
    		Document current = documents.get(i);
    		dtmDocuments.addRow(new Object[] {current.getId(), current.getName()});
    	}
    	
    	tableDocuments.setPreferredScrollableViewportSize(new Dimension(500, 130));
    	tableDocuments.setFillsViewportHeight(true);
    	
    	tableDocuments.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent event) {
    			if (event.getClickCount() == 2) {
    				int row = tableDocuments.getSelectedRow();
    				if (row >= 0) {
	    				int convertedRow = tableDocuments.convertRowIndexToModel(row);
	    				final long id = (Long) tableDocuments.getValueAt(convertedRow, 0);
	    				String name = (String) tableDocuments.getValueAt(convertedRow, 1);
	    				
	    				Document d = thisDb.getDocument(id);
	    				try {
							openDocumentFromByteArray(d.getData(), name);
						} catch (IOException e) {
							e.printStackTrace();
						}
    				}
    			}
    		}
    	});
    	
    	JScrollPane scrollPane = new JScrollPane(tableDocuments);
    	panelDocuments.add(scrollPane);
    	
//    	mainFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	public static String getFileNameFromPath(String path) {
		String name = "";
		String[] splitted = path.split("\\" + File.separator);
		name = splitted[splitted.length - 1];
		return name;
	}
	
	public JPanel getPanel() {
		return panelDocuments;
	}
	
	public DocumentFactory(Database db, long entityId) {
		mainFrame = new JFrame("Documenti");
		mainFrame.setLayout(new BorderLayout());
		
		thisDb = db;
		final long thisEntityId = entityId;
		
		chooser = new JFileChooser();
		
		panelDocuments = new JPanel();
		
		JPanel panelButtons = new JPanel();
		
		tableDocuments = new JTable();
		dtmDocuments = new CommonTableModel();
		dtmDocuments.setColumnIdentifiers(documentsColumns);
		tableDocuments.setModel(dtmDocuments);
		
		final ArrayList<Document> documents = loadDocuments(thisDb, thisEntityId);
		
		addDocumentBtn = new JButton(CD_BTN_ADD_DOCUMENT);
		addDocumentBtn.setSize(100, 40);
		addDocumentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					byte[] bytes = getBytesFromFile(file);
					
					long id = getMaxDocumentId(thisDb) + 1;
					
					String name = getFileNameFromPath(file.getPath());
					
					Document d = new Document();
					d.setId(id);
					d.setEntityId(thisEntityId);
					d.setName(name);
					d.setData(bytes);
					thisDb.insertDocument(d);
					
					dtmDocuments.addRow(new Object[] {d.getId(), d.getName()});
				}
			}
		});
		panelButtons.add(addDocumentBtn);
		
		removeDocumentBtn = new JButton(CD_BTN_REMOVE_DOCUMENT);
		removeDocumentBtn.setSize(100, 40);
		removeDocumentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (documents.size() > 0) {
					int row = tableDocuments.getSelectedRow();
    				if (row >= 0 && row <= documents.size() - 1) {
	    				int convertedRow = tableDocuments.convertRowIndexToModel(row);
	    				final long id = (Long) tableDocuments.getValueAt(convertedRow, 0);
	    				
	    				thisDb.deleteDocument(id);
	    				
	    				dtmDocuments.removeRow(row);
    				}
				}
			}
		});
		panelButtons.add(removeDocumentBtn);
		
		panelDocuments.setPreferredSize(new Dimension(550, 300));

		showTableDocuments();
		panelDocuments.add(panelButtons);
		
//		mainFrame.getContentPane().add(panelButtons, BorderLayout.PAGE_END);
//		
//		mainFrame.setSize(500, 500);
//		mainFrame.setLocationRelativeTo(null);
//		mainFrame.setVisible(true);
	}

}
