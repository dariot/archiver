package antics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import dto.Category;
import dto.CommonTableModel;
import dto.Document;
import dto.Entity;
import dto.Picture;

public class Antics implements ActionListener {
	
	private static Antics ref = new Antics();
	
	private static Database db;
	
	private static JFrame frame;
	private static JFrame frameAmministrazione;
	private static JButton adminBtn;
	private static JButton newEntityBtn;
	private static JButton cercaBtn;
	private static JButton cancellaBtn;
	private static JButton stampaRicercaBtn;
	private static JButton stampaDettaglioBtn;
	private static JLabel labelCategoria;
	private static JLabel labelAutore;
	private static JLabel labelLocalizzazione;
	private static JLabel labelProvenienza;
	private static JComboBox<String> comboCategoria;
	private static JComboBox<String> comboAutore;
	private static JComboBox<String> comboLocalizzazione;
	private static JComboBox<String> comboProvenienza;
	private static JComboBox<String> comboCategoriaDettaglioEntity;
	private static JTextField nuovaCategoriaTF;
	private static JPanel panelTableEntities;
	
	// tabelle
	private static JTable tableCategories;
	private static String[] categoriesColumns = {"ID", "Categoria"};
	private static JTable tableEntities;
	private static String[] entitiesColumns = {"Immagine", "ID", "Categoria", "Autore", "Titolo",
			"Tecnica usata", "Misure", "Anno di acquisizione", "Importo pagato",
			"Provenienza", "Localizzazione", "Valore odierno"};
	private static CommonTableModel dtmCategories;
	private static CommonTableModel dtmEntities;
	
	// dettaglio categoria
	private static JFrame frameDettaglioCategoria;
	private static JTextField categoriaTF;
	private static JButton salvaCategoriaBtn;
	private static JButton rimuoviCategoriaBtn;
	private static JLabel categoryLabel;
	private static JLabel authorLabel;
	private static JLabel titleLabel;
	private static JLabel techniqueLabel;
	private static JLabel measuresLabel;
	private static JLabel buyYearLabel;
	private static JLabel priceLabel;
	private static JLabel paymentTypeLabel;
	private static JLabel originalPlaceLabel;
	private static JLabel actualPlaceLabel;
	private static JLabel currentValueLabel;
	private static JLabel currentValueDateLabel;
	private static JLabel soldLabel;
	private static JLabel notesLabel;
	
	private static JTextField authorTF;
	private static JTextField titleTF;
	private static JTextField techniqueTF;
	private static JTextField measuresTF;
	private static JTextField buyYearTF;
	private static JTextField priceTF;
	private static JTextArea paymentTypeTA;
	private static JTextField originalPlaceTF;
	private static JTextField actualPlaceTF;
	private static JTextField currentValueTF;
	private static JTextField currentValueDateTF;
	private static JTextField soldTF;
	private static JTextArea notesTA;
	
	private static ImageIcon stampa = new ImageIcon ("resources/print_icon.png");
	
	// dettaglio oggetto
	private static JFrame frameDettaglioEntity;

	private static JPanel panelDettaglioEntity;
	private static JButton salvaEntityBtn;
	private static JButton rimuoviEntityBtn;

	
	private static JPanel panelDocumentsAndPictures;

	
	private static ArrayList<Category> listCategories = new ArrayList<Category>();
	private static ArrayList<Entity> listEntities = new ArrayList<Entity>();
	
	// autocomplete commit string
	private static final String COMMIT_ACTION = "commit";
	
	public static final String FILE_PATH = "NON_TOCCARE/tempFiles/";
	
	private static final String defaultCategoryName = "Default";
	
	private static final String CD_BTN_AMMINISTRAZIONE = "Categorie";
	private static final String CD_BTN_CERCA = "Cerca";
	private static final String CD_BTN_CANCELLA = "Cancella";
	private static final String CD_BTN_AGGIUNGI_CATEGORIA = "Aggiungi categoria";
	private static final String CD_BTN_SALVA_CATEGORIA = "Salva categoria";
	private static final String CD_BTN_RIMUOVI_CATEGORIA = "Rimuovi categoria";
	
	private static final String CD_BTN_AGGIUNGI_ENTITY = "Aggiungi oggetto";
	private static final String CD_BTN_SALVA_ENTITY = "Salva oggetto";
	private static final String CD_BTN_RIMUOVI_ENTITY = "Rimuovi oggetto";
	
	private static final String CD_BTN_PRINT_DETAIL = "Stampa dettaglio";
	
	private static final String MSG_PROBLEM_LOAD_CATEGORIES = "Si e' verificato un problema nel caricamento delle categorie";
	private static final String MSG_PROBLEM_LOAD_ENTITIES = "Si e' verificato un problema nel caricamento degli oggetti";
	private static final String MSG_PROBLEM_CREATING_DEFAULT_CAT = "Si e' verificato un problema nella creazione della categoria di default";
	
	private static final String MSG_RIMUOVI_CATEGORIA_KO = "Si e' verificato un problema durante la rimozione della categoria";
	private static final String MSG_PROBLEM_SAVE_CATEGORY = "Si e' verificato un problema nel salvataggio della categoria";
	private static final String MSG_RIMOZIONE_CATEGORIA_DEF = "Non e' possibile rimuovere la categoria di default";
	
	private static final String MSG_SALVA_ENTITY_OK = "Oggetto salvato correttamente.";
	private static final String MSG_SALVA_ENTITY_KO = "Si e' verificato un problema nel salvataggio dell''oggetto.";
	private static final String MSG_AGGIORNA_ENTITY_OK = "Oggetto aggiornato correttamente.";
	private static final String MSG_RIMUOVI_ENTITY_OK = "Oggetto rimosso correttamente.";
	private static final String MSG_RIMUOVI_ENTITY_KO = "Si e' verificato un problema durante la rimozione dell''oggetto";
	private static final String MSG_ACCORPA_ENTITY_OK = "Le due entità sono state correttamente accorpate in un unico oggetto";
	
	private static final boolean showPrintDialog = true;
	private static final boolean interactivePrinting = true;
	private static final boolean fitWidthPrint = true;
	private static final boolean headerOption = false;
	private static final boolean footerOption = false;

	private static final String insert = "INSERT_NEW_ENTITY";
	private static final String update = "UPDATE_ENTITY";

	protected static final int LINK = 0;
	
	private static void createWindow() {
		frame = new JFrame("ANTICS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// files saved in the temporary folder are deleted when the program is exiting
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		    	try {
					FileUtils.cleanDirectory(new File(DocumentFactory.FILE_PATH));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}
		    }
		});
		
		frame.setLayout(new BorderLayout());
		
		JPanel panelButtons = new JPanel();

		//combo categoria
        labelCategoria = new JLabel("Categoria");
        comboCategoria = new JComboBox<String>();
        //setto primo elemento sempre a stringa vuota
        comboCategoria.insertItemAt("", 0);
        for (int i = 0; i < listCategories.size(); i++) {
        	if(!listCategories.get(i).getName().equalsIgnoreCase(""))
        		comboCategoria.insertItemAt(listCategories.get(i).getName(), i+1);
        }
        panelButtons.add(labelCategoria);
        panelButtons.add(comboCategoria);
        
        // combo autore
        labelAutore = new JLabel("Autore");
        ArrayList<String> listAuthors = db.getListAuthors();
        comboAutore = new JComboBox<String>();
        //setto primo elemento sempre a stringa vuota
        comboAutore.insertItemAt("", 0);
        for (int i = 0; i < listAuthors.size(); i++) {
        	if(!listAuthors.get(i).equalsIgnoreCase(""))
        		comboAutore.insertItemAt(listAuthors.get(i), i+1);
        }
        panelButtons.add(labelAutore);
        panelButtons.add(comboAutore);
        
        // combo localizzazione
        labelLocalizzazione = new JLabel("Localizzazione");
        ArrayList<String> listLocations = db.getListLocations();
        comboLocalizzazione = new JComboBox<String>();
        //setto primo elemento sempre a stringa vuota
        comboLocalizzazione.insertItemAt("", 0);
        for (int i = 0; i < listLocations.size(); i++) {
        	if(!listLocations.get(i).equalsIgnoreCase(""))
        		comboLocalizzazione.insertItemAt(listLocations.get(i), i+1);
        }
        panelButtons.add(labelLocalizzazione);
        panelButtons.add(comboLocalizzazione);
        
        // combo provenienza
        labelProvenienza = new JLabel("Provenienza");
        ArrayList<String> listOriginalPlaces = db.getListOriginalPlaces();
        comboProvenienza = new JComboBox<String>();
        //setto primo elemento sempre a stringa vuota
        comboProvenienza.insertItemAt("", 0);
        for (int i = 0; i < listOriginalPlaces.size(); i++) {
        	if(!listOriginalPlaces.get(i).equalsIgnoreCase(""))
        		comboProvenienza.insertItemAt(listOriginalPlaces.get(i), i+1);
        }
        panelButtons.add(labelProvenienza);
        panelButtons.add(comboProvenienza);
        
        
        // pulsante "Cerca"
        cercaBtn = new JButton(CD_BTN_CERCA);
        cercaBtn.setSize(100, 40);
        cercaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String categoryName = (String) comboCategoria.getSelectedItem();
				Category c = new Category();
				try {
					c = findCategoryByName(categoryName);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				String author = (String) comboAutore.getSelectedItem();
				String location = (String) comboLocalizzazione.getSelectedItem();
				String originalPlace = (String) comboProvenienza.getSelectedItem();
				
				filterTableEntities(c, author, location, originalPlace);
			}
		});
        panelButtons.add(cercaBtn);
        
        //pulsante cancella
        cancellaBtn = new JButton(CD_BTN_CANCELLA);
        cancellaBtn.setSize(100, 40);
        cancellaBtn.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
		        comboCategoria.setSelectedIndex(0);
		        comboAutore.setSelectedIndex(0);
		        comboLocalizzazione.setSelectedIndex(0);
		        comboProvenienza.setSelectedIndex(0);
			}
		});
        panelButtons.add(cancellaBtn);
        
        //pulsante "Stampa ricerca"
        Image imageStampa = stampa.getImage(); // trasforma l'icona in immagine affinchè possa essere scalata 
        Image newimg = imageStampa.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        stampa = new ImageIcon(newimg);  
        stampaRicercaBtn = new JButton();
        stampaRicercaBtn.setSize(20, 20);
        stampaRicercaBtn.setIcon(stampa);
        stampaRicercaBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		printResultTable();
        	}
        });
        panelButtons.add(stampaRicercaBtn);
		
        JPanel panelFooterButtons = new JPanel();
        newEntityBtn = new JButton(CD_BTN_AGGIUNGI_ENTITY);
		newEntityBtn.setSize(100, 40);
		newEntityBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPanelDettaglioEntity();
			}
		});
		panelFooterButtons.add(newEntityBtn);
        
		// pulsante "Amministrazione"
        adminBtn = new JButton(CD_BTN_AMMINISTRAZIONE);
        adminBtn.setSize(100, 40);
        adminBtn.addActionListener(ref);
        panelFooterButtons.add(adminBtn);
        
        panelTableEntities = new JPanel();
        showTableEntities(null);
        
        frame.getContentPane().add(panelButtons, BorderLayout.PAGE_START);
        frame.getContentPane().add(panelTableEntities, BorderLayout.CENTER);
        frame.getContentPane().add(panelFooterButtons, BorderLayout.PAGE_END);
		
		frame.setSize(1200, 650);
        frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static void printResultTable() {
		/* header: può essere abilitato o disattivato */
		 MessageFormat header = null;
		 if(headerOption) {
			 header = new MessageFormat("Prova");
		 }		
		/* footer: può essere abilitato o disattivato */
		 MessageFormat footer = null;
		 if(footerOption) {
			 footer = new MessageFormat("Pagina");
		 }
		/* determina l'adattamento della pagina */
        JTable.PrintMode mode = fitWidthPrint ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            /* stampa la tabella con la ricerca effettuata */
            boolean complete = tableEntities.print(mode, header, footer, showPrintDialog, null, interactivePrinting, null);
 
            /* se la stampa è completata*/
            if (complete) {
                /* mostro un pannello di dialogo con successo */
                JOptionPane.showMessageDialog(null, "Stampa completata", "Risultato stampa: ", JOptionPane.INFORMATION_MESSAGE);
            } else {
                /* mostro un pannello di avviso per stampa annullata */
                JOptionPane.showMessageDialog(null, "Stampa cancellata", "Risultato stampa: ", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            /* Printing failed, report to the user */
            JOptionPane.showMessageDialog(null, "Errore nella stampa: " + pe.getMessage(), "Risultato stampa: ", JOptionPane.ERROR_MESSAGE);
        }

	}
	
	private static void printDettaglioEntity (long id) throws IOException, PrinterException {
	    
		Entity entityFromDb = db.getEntity(id);
		
		//Se trovo l'entità da stampare inizio a scrivere
		if(entityFromDb!= null && entityFromDb.getId()>0) {
		
			String tempPath = FILE_PATH + "temp" + Long.toString(id)+".pdf";
			PDFMergerUtility PDFmerger = new PDFMergerUtility(); 
			PDFmerger.setDestinationFileName(tempPath);
			
			// Create a document and add a page to it
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			// Start a new content stream which will "hold" the to be created content
			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			// PARTE RALATIVA AL DETTAGLIO. TESTO. PAG 1
			String[][] entita = PdfFactory.getStringArrayFromEntity(entityFromDb);
			PdfFactory.drawTable(page, contentStream, 700, 72, entita);
			contentStream.close();
			
			// PARTE RELATIVA ALLE IMMAGINI. PAG 2 - pictures.size()+1
			ArrayList<Picture> pictures = db.getPicturesFromEntityId(id);
			for(int i=0; i<pictures.size(); i++) {
				page = new PDPage();
				document.addPage(page);
				contentStream = new PDPageContentStream(document,page);

				PDImageXObject pdImage = PdfFactory.getPDImageFromPicture(pictures.get(i), document);
				contentStream.drawImage(pdImage, 80, 700-pdImage.getHeight());

				contentStream.close();
			}
			String pathDoc1 = FILE_PATH + "doc1_" + Long.toString(id)+".pdf";
			document.save(pathDoc1);
		    
		    File file1 = new File(pathDoc1);
		    PDFmerger.addSource(file1);
		    document.close();
		    
			// PARTE RELATIVA AI DOCUMENTI. PAG pictures.size()+1 in poi
			ArrayList<Document> documents = db.getDocumentsFromEntityId(id);
			for(int i=0; i<documents.size(); i++) {
				Document d = documents.get(i);
				String path = PdfFactory.getPDFFromDocument(d.getData(), d.getName());
				File file2 = new File(path);
			    PDDocument doc2 = PDDocument.load(file2);
			    PDFmerger.addSource(file2);
			    doc2.close();
			}
			
			//Unisco tutti i pdf prodotti
			PDFmerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

			PdfFactory.printPDFFile(tempPath);
		}
	}


	private static void startDB() {
		db = new Database();
		db.startServer();
		db.getConnection();
	}
	
	private static void stopDB(){
		db.shutdown();
	}
	
	private static void init() {
		startDB();
		defaultCategoryInit();
		loadCategories();
		loadEntities();
		defineTables();
	}
	
	public static void defineTables() {
		// tabella categorie
		tableCategories = new JTable();
		dtmCategories = new CommonTableModel();
    	dtmCategories.setColumnIdentifiers(categoriesColumns);
    	tableCategories.setModel(dtmCategories);
    	
    	// tabella entita'
    	tableEntities = new JTable();
		dtmEntities = new CommonTableModel();
    	dtmEntities.setColumnIdentifiers(entitiesColumns);
    	tableEntities.setModel(dtmEntities);
    	tableEntities.setRowHeight(80);
	}
	
	public static void loadEntities() {
		listEntities = new ArrayList<Entity>();
		try {
			listEntities = db.getEntities();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, MSG_PROBLEM_LOAD_ENTITIES + ":\n\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void defaultCategoryInit() {
		try {
			db.insert_update_default_category();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, MSG_PROBLEM_CREATING_DEFAULT_CAT + ":\n\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static String getFileExtension(String filename) {
		String ext = "";
		String[] splitted = filename.split("\\.");
		if (splitted.length > 0) {
			ext = splitted[splitted.length - 1];
		}
		return ext;
	}
	
	private static void loadCategories() {
		listCategories = new ArrayList<Category>();
		
		try {
			listCategories = db.getCategories();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, MSG_PROBLEM_LOAD_CATEGORIES + ":\n\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void createDettaglioEntity(Entity e) {
		
		//Per ogni elemento del pannello pre-setto i valori solo se e non è nullo
		//Se e è nullo mi trovo infatti in modalità di inserimento e non di modifica
		
		categoryLabel = new JLabel("Categoria dell'oggetto");
		panelDettaglioEntity.add(categoryLabel);
		comboCategoriaDettaglioEntity = new JComboBox<String>();
        for (int i = 0; i < listCategories.size(); i++) {
        	comboCategoriaDettaglioEntity.insertItemAt(listCategories.get(i).getName(), i);
        }
       
        if(e!=null){
        	String categoryName = findCategoryById(e.getCategoryId()).getName();
        	comboCategoriaDettaglioEntity.setSelectedItem(categoryName);
        }
		panelDettaglioEntity.add(comboCategoriaDettaglioEntity);
		
		authorLabel = new JLabel("Autore");
		panelDettaglioEntity.add(authorLabel);
		if(e!=null){
			authorTF = new JTextField(e.getAuthor());
		}else{
			authorTF = new JTextField();
			setAutocomplete(authorTF, "AUTHOR");
		}
		panelDettaglioEntity.add(authorTF);
		
		titleLabel = new JLabel("Titolo o descrizione dell'oggetto");
		panelDettaglioEntity.add(titleLabel);
		if(e!=null){
			titleTF = new JTextField(e.getTitle());
		}else{
			titleTF = new JTextField();
		}
		panelDettaglioEntity.add(titleTF);
	
		techniqueLabel = new JLabel("Tecnica usata");
		panelDettaglioEntity.add(techniqueLabel);
		if(e!=null){
			techniqueTF = new JTextField(e.getTechnique());
		}else{
			techniqueTF = new JTextField();
			setAutocomplete(techniqueTF, "TECHNIQUE");
		}
		panelDettaglioEntity.add(techniqueTF);
		
		measuresLabel = new JLabel("Misure");
		panelDettaglioEntity.add(measuresLabel);
		if(e!=null){
			measuresTF = new JTextField(e.getMeasures());
		}else{
			measuresTF = new JTextField();
		}
		panelDettaglioEntity.add(measuresTF);
		
		buyYearLabel = new JLabel("Anno di acquisizione");
		panelDettaglioEntity.add(buyYearLabel);
		if(e!=null){
			buyYearTF = new JTextField(e.getBuyYear());
		}else{
			buyYearTF = new JTextField();
		}
		panelDettaglioEntity.add(buyYearTF);
		
		priceLabel = new JLabel("Importo originario pagato");
		panelDettaglioEntity.add(priceLabel);
		if(e!=null){
			priceTF = new JTextField(e.getPrice());
		}else{
			priceTF = new JTextField();
		}
		panelDettaglioEntity.add(priceTF);
		
		paymentTypeLabel = new JLabel("Modalita' di pagamento");
		panelDettaglioEntity.add(paymentTypeLabel);
		if(e!=null){
			paymentTypeTA = new JTextArea(e.getPaymentType());
			paymentTypeTA.setSize(15, 20);
		}else{
			paymentTypeTA = new JTextArea(15, 20);
			//setAutocomplete(paymentTypeTA, "PAYMENT_TYPE");
		}
		JScrollPane scrollPanePaymentType = new JScrollPane(paymentTypeTA);
		panelDettaglioEntity.add(scrollPanePaymentType);
		
		originalPlaceLabel = new JLabel("Provenienza");
		panelDettaglioEntity.add(originalPlaceLabel);
		if(e!=null){
			originalPlaceTF = new JTextField(e.getOriginalPlace());
		}else{
			originalPlaceTF = new JTextField();
		}
		panelDettaglioEntity.add(originalPlaceTF);
		
		actualPlaceLabel = new JLabel("Localizzazione dell'oggetto");
		panelDettaglioEntity.add(actualPlaceLabel);
		if(e!=null){
			actualPlaceTF = new JTextField(e.getActualPlace());
		}else{
			actualPlaceTF = new JTextField();
			setAutocomplete(actualPlaceTF, "ACTUAL_PLACE");
		}
		panelDettaglioEntity.add(actualPlaceTF);
		
		currentValueLabel = new JLabel("Valore odierno");
		panelDettaglioEntity.add(currentValueLabel);
		if(e!=null){
			currentValueTF = new JTextField(e.getCurrentValue());
		}else{
			currentValueTF = new JTextField();
		}
		panelDettaglioEntity.add(currentValueTF);
		
		currentValueDateLabel = new JLabel("Data valore odierno");
		panelDettaglioEntity.add(currentValueDateLabel);
		if(e!=null){
			currentValueDateTF = new JTextField(e.getCurrentValueDate());
		}else{
			currentValueDateTF = new JTextField();
		}
		panelDettaglioEntity.add(currentValueDateTF);
		
		soldLabel = new JLabel("Oggetto venduto");
		panelDettaglioEntity.add(soldLabel);
		if(e!=null){
			soldTF = new JTextField(e.getSold());
		}else{
			soldTF = new JTextField();
		}
		panelDettaglioEntity.add(soldTF);
		
		notesLabel = new JLabel("Annotazioni varie");
		panelDettaglioEntity.add(notesLabel);
		if(e!=null){
			notesTA = new JTextArea(e.getNotes());
			notesTA.setSize(15, 20);
		}else{
			notesTA = new JTextArea(15, 20);
		}
		JScrollPane scrollPaneNotes = new JScrollPane(notesTA);
		panelDettaglioEntity.add(scrollPaneNotes);
	}
	
	private static void clearTableEntities() {
		int rowCount = dtmEntities.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			dtmEntities.removeRow(i);
		}
	}
	
	public static void filterTableEntities(Category c, String author, String location, String originalPlace) {
		clearTableEntities();
		dtmEntities.fireTableDataChanged();
		
		ArrayList<Entity> filteredEntities = db.searchEntities(c, author, location, originalPlace);
		for (int i = 0; i < filteredEntities.size(); i++) {
			Entity current = filteredEntities.get(i);
			ImageIcon icon = getIconFromEntityId(current.getId());
			Category category = findCategoryById(current.getCategoryId());
			dtmEntities.addRow(new Object[] {icon, current.getId(), category.getName(), current.getAuthor(),
					current.getTitle(), current.getTechnique(), current.getMeasures(), current.getBuyYear(),
					current.getPrice(), current.getOriginalPlace(), current.getActualPlace(),
					current.getCurrentValue()});
		}
		
    	dtmEntities.fireTableDataChanged();
	}
	
	public static Category findCategoryById(long id) {
		Category res = null;
		for (int i = 0; i < listCategories.size(); i++) {
			Category current = listCategories.get(i);
			if (current.getId() == id) {
				res = current;
				break;
			}
		}
		
		return res;
	}
	
	public static Entity findEntityById(long id) {
		Entity res = null;
		for (int i = 0; i < listEntities.size(); i++) {
			Entity current = listEntities.get(i);
			if (current.getId() == id) {
				res = current;
				break;
			}
		}
		return res;
	}
	
	public static ImageIcon getIconFromEntityId(long id) {
		ArrayList<Picture> pictures = db.getThumbnailsFromEntityId(id);
		ImageIcon icon = new ImageIcon();
		if (pictures.size() > 0) {
			Picture mainPic = db.getMainPictureThumbnailFromEntityId(id);
			if (mainPic != null) {
				icon = new ImageIcon(mainPic.getData());
			} else {
				icon = new ImageIcon(pictures.get(0).getData());
			}
		}
		return icon;
	}
	
	public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image resized = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resized);
	}
	
	public static void updateEntity(long id, Entity e) throws Exception{

		try {
			db.updateEntity(e);
		} catch (SQLException exception) {
			throw new Exception(MSG_SALVA_ENTITY_KO + "Errore: " + exception.getMessage());
		}
		
		// aggiorna l'oggetto nella lista degli oggetti
		for (int i = 0; i < listEntities.size(); i++) {
			if (listEntities.get(i).getId() == id) {
				listEntities.set(i, e);
				break;
			}
		}
		for (int i = 0; i < dtmEntities.getRowCount(); i++) {
			if (dtmEntities.getValueAt(i, 1).equals(id)) {
				// aggiorna l'oggetto nel tableModel
				ImageIcon icon = getIconFromEntityId(id);
				Category category = findCategoryById(e.getCategoryId());
				Object[] o = new Object[] {
						icon,
						e.getId(), category.getName(), e.getAuthor(),
    					e.getTitle(), e.getTechnique(), e.getMeasures(), e.getBuyYear(),
    					e.getPrice(), e.getOriginalPlace(), e.getActualPlace(),
    					e.getCurrentValue()
				};
				
				for (int j = 0; j < o.length; j++) {
					dtmEntities.setValueAt(o[j], i, j);
				}
				break;
			}
		}
	}
	
	public static void removeEntity(long id) throws Exception {
		try {
			ArrayList<Picture> picturesToRemove = db.getPicturesFromEntityId(id);
			for(int i=0; i<picturesToRemove.size(); i++) {
				Picture p = picturesToRemove.get(i);
				db.deletePicture(p.getId());
				db.deletePictureThumbnail(p.getId());
			}
			ArrayList<Document> documentsToRemove = db.getDocumentsFromEntityId(id);
			for(int i=0; i<documentsToRemove.size(); i++) {
				Document d = documentsToRemove.get(i);
				db.deleteDocument(d.getId());
			}

			db.deleteEntity(id);
			
		}catch (SQLException exception) {
			throw new Exception(MSG_RIMUOVI_ENTITY_KO + " Errore: " + exception.getMessage());
		}
		
		// rimuovi l'oggetto dalla lista degli oggetti
		for (int i = 0; i < listEntities.size(); i++) {
			if (listEntities.get(i).getId() == id) {
				listEntities.remove(i);
				// rimuovi l'oggetto dal tableModel
				dtmEntities.removeRow(i);
				break;
			}
		}
	}
	
	public static Entity getEntityFromUI(long id) throws SQLException {
		String categoryName = (String) comboCategoriaDettaglioEntity.getSelectedItem();
		String author = authorTF.getText();
		String title = titleTF.getText();
		String technique = techniqueTF.getText();
		String measures = measuresTF.getText();
		String buyYear = buyYearTF.getText();
		String price = priceTF.getText();
		String paymentType = paymentTypeTA.getText();
		String originalPlace = originalPlaceTF.getText();
		String actualPlace = actualPlaceTF.getText();
		String currentValue = currentValueTF.getText();
		String currentValueDate = currentValueDateTF.getText();
		String sold = soldTF.getText();
		String notes = notesTA.getText();
		
		Entity newEntity = new Entity();
		newEntity.setActualPlace(actualPlace);
		newEntity.setAuthor(author);
		newEntity.setBuyYear(buyYear);
		
		Category c = findCategoryByName(categoryName);
		newEntity.setCategoryId(c.getId());
		newEntity.setCurrentValue(currentValue);
		newEntity.setCurrentValueDate(currentValueDate);
		newEntity.setId(id);
		newEntity.setMeasures(measures);
		newEntity.setNotes(notes);
		newEntity.setOriginalPlace(originalPlace);
		newEntity.setPaymentType(paymentType);
		newEntity.setPrice(price);
		newEntity.setTechnique(technique);
		newEntity.setTitle(title);
		newEntity.setSold(sold);
		newEntity.setNotes(notes);
		
		return newEntity;
	}
	
	private static long getCategoryIdFromName(String name) {
		long id = -1;
		for (int i = 0; i < listCategories.size(); i++) {
			if (name.equals(listCategories.get(i).getName())) {
				id = listCategories.get(i).getId();
				break;
			}
		}
		return id;
	}
	
	private static JPanel createPanelDocumentsAndPictures(long id, String mode) {
		JPanel panel = new JPanel();
		
		// pictures
		PictureFactory pic = new PictureFactory(db, id, mode);
		JPanel panelPictures = pic.getPanel();
		panel.add(panelPictures, BorderLayout.CENTER);
		
		//separatore
		JPanel separator = new JPanel();
		separator.setPreferredSize(new Dimension(600, 10));
		panel.add(separator, BorderLayout.CENTER);
		
		// documents
		DocumentFactory doc = new DocumentFactory(db, id);
		JPanel panelDocuments = doc.getPanel();
		panel.add(panelDocuments, BorderLayout.CENTER);
		
		//stampa dettaglio
		JPanel stampaDettagio = new JPanel();
		final long entityid = id;
		stampaDettaglioBtn	= new JButton(CD_BTN_PRINT_DETAIL);
		stampaDettaglioBtn.setSize(100, 40);
		stampaDettaglioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					printDettaglioEntity(entityid);
				} catch (IOException | PrinterException e1) {
					e1.printStackTrace();
				}
			}
		});
		stampaDettagio.add(stampaDettaglioBtn);
		stampaDettagio.setPreferredSize(new Dimension(550, 60));
		panel.add(stampaDettagio, BorderLayout.NORTH);
		
		return panel;
	}
	
	// la lista delle entita' riceve in input la categoria per cui devono essere filtrate
	public static void showTableEntities(Category c) {
    	for (int i = 0; i < listEntities.size(); i++) {
    		Entity current = listEntities.get(i);
    		
    		ImageIcon icon = getIconFromEntityId(current.getId());
    		if (c != null) {
    			if (current.getCategoryId() == c.getId()) {
        			dtmEntities.addRow(new Object[] {icon, current.getId(), c.getName(), current.getAuthor(),
        					current.getTitle(), current.getTechnique(), current.getMeasures(), current.getBuyYear(),
        					current.getPrice(), current.getOriginalPlace(), current.getActualPlace(),
        					current.getCurrentValue()});
    			}
    		} else {
    			Category category = findCategoryById(current.getCategoryId());
    			if (category != null) {
	    			dtmEntities.addRow(new Object[] {icon, current.getId(), category.getName(), current.getAuthor(),
	    					current.getTitle(), current.getTechnique(), current.getMeasures(), current.getBuyYear(),
	    					current.getPrice(), current.getOriginalPlace(), current.getActualPlace(),
	    					current.getCurrentValue()});
    			}
    		}
    	}
    	dtmEntities.fireTableDataChanged();
    	
    	tableEntities.setPreferredScrollableViewportSize(new Dimension(1000, 400));
    	tableEntities.setFillsViewportHeight(true);
    	tableEntities.setDropMode(DropMode.INSERT_ROWS);
    	
    	tableEntities.setTransferHandler(new FileListTransferHandler(db));
    	
    	tableEntities.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent event) {
    			if (event.getClickCount() == 2) {
    				frameDettaglioEntity = new JFrame("Dettaglio");
    				
    				panelDettaglioEntity = new JPanel();
//    				panelDettaglioEntity.setSize(1200, 750);
    				panelDettaglioEntity.setLayout(new GridLayout(16, 2, 10, 10));
    				
    				frameDettaglioEntity.setLayout(new BoxLayout(frameDettaglioEntity.getContentPane(), BoxLayout.LINE_AXIS));
    				
    				int row = tableEntities.getSelectedRow();
    				int convertedRow = tableEntities.convertRowIndexToModel(row);
    				
    				final long id = (Long) tableEntities.getValueAt(convertedRow, 1);
    				long categoryId = getCategoryIdFromName((String) tableEntities.getValueAt(convertedRow, 2));
    				String author = (String) tableEntities.getValueAt(row, 3);
    				String title = (String) tableEntities.getValueAt(row, 4);
    				String technique = (String) tableEntities.getValueAt(row, 5);
    				String measures = (String) tableEntities.getValueAt(row, 6);
    				String buyYear = (String) tableEntities.getValueAt(row, 7);
    				String price = (String) tableEntities.getValueAt(row, 8);
    				String originalPlace = (String) tableEntities.getValueAt(row, 9);
    				String actualPlace = (String) tableEntities.getValueAt(row, 10);
    				String currentValue = (String) tableEntities.getValueAt(row, 11);
    				
    				Entity entityFromDb = db.getEntity(id);
    				String currentValueDate = entityFromDb.getCurrentValueDate();
    				String sold = entityFromDb.getSold();
    				
    				Entity originalEntity = findEntityById(id);
    				String paymentType = originalEntity.getPaymentType();
    				String notes = originalEntity.getNotes();
    				
    				Entity entity = new Entity();
    				entity.setId(id);
    				entity.setCategoryId(categoryId);
    				entity.setAuthor(author);
    				entity.setTitle(title);
    				entity.setTechnique(technique);
    				entity.setMeasures(measures);
    				entity.setBuyYear(buyYear);
    				entity.setPrice(price);
    				entity.setOriginalPlace(originalPlace);
    				entity.setActualPlace(actualPlace);
    				entity.setCurrentValue(currentValue);
    				entity.setCurrentValueDate(currentValueDate);
    				entity.setSold(sold);
    				entity.setPaymentType(paymentType);
    				entity.setNotes(notes);
    				
    				createDettaglioEntity(entity);
    				
    				salvaEntityBtn = new JButton(CD_BTN_SALVA_ENTITY);
    				salvaEntityBtn.setSize(100, 40);
    				salvaEntityBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						try {
    							Entity updatedEntity = getEntityFromUI(id);
    							Entity temp = new Entity();
    							temp.setTitle(updatedEntity.getTitle());
    							temp.setAuthor(updatedEntity.getAuthor());
    							long oldId = db.getEntityIdFromDescription(temp);
    							// Se esisteva già un entity faccio il merge tra quella esistente e quella appena creata 
    							updateEntity(id, updatedEntity);
    							if (oldId != -1 && oldId != id) {	
    								regroupEntity(oldId, id);
    							}
    						} catch (Exception ex) {
    							JOptionPane.showMessageDialog(frameDettaglioEntity, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    						}
    						onEntityChanged();
    						loadEntities();
    						filterTableEntities(null,"","","");
    						JOptionPane.showMessageDialog(frameDettaglioEntity, MSG_AGGIORNA_ENTITY_OK);
    						frameDettaglioEntity.dispatchEvent(new WindowEvent(frameDettaglioEntity, WindowEvent.WINDOW_CLOSING));
    					}
    				});
    				panelDettaglioEntity.add(salvaEntityBtn);
    				
    				rimuoviEntityBtn = new JButton(CD_BTN_RIMUOVI_ENTITY);
    				rimuoviEntityBtn.setSize(100, 40);
    				rimuoviEntityBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						try {
    							removeEntity(id);
    						} catch (Exception ex) {
    							JOptionPane.showMessageDialog(frameDettaglioEntity, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    						}
    						onEntityChanged();
    						JOptionPane.showMessageDialog(frame, MSG_RIMUOVI_ENTITY_OK);
    						frameDettaglioEntity.dispatchEvent(new WindowEvent(frameDettaglioEntity, WindowEvent.WINDOW_CLOSING));
    					}
    				});
    				panelDettaglioEntity.add(rimuoviEntityBtn);
    				
    				frameDettaglioEntity.add(panelDettaglioEntity);
    				
    				panelDocumentsAndPictures = createPanelDocumentsAndPictures(id, update);
    				panelDocumentsAndPictures.setPreferredSize(new Dimension(600, 750));
    				
    				frameDettaglioEntity.add(panelDocumentsAndPictures);
    				   				
    				frameDettaglioEntity.setSize(1100, 750);
    				frameDettaglioEntity.setLocationRelativeTo(null);
    				frameDettaglioEntity.setVisible(true);
    			}
    		}
    	});
    	
    	JScrollPane scrollPane = new JScrollPane(tableEntities);
    	panelTableEntities.add(scrollPane);
	}
	
	private void showTableCategories() {
		dtmCategories.setRowCount(0);
    	for (int i = 0; i < listCategories.size(); i++) {
    		Category current = listCategories.get(i);
    		dtmCategories.addRow(new Object[] {current.getId(), current.getName()});
    	}
    	
    	tableCategories.setPreferredScrollableViewportSize(new Dimension(500, 130));
    	tableCategories.setFillsViewportHeight(true);
    	
    	tableCategories.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent event) {
    			if (event.getClickCount() == 1) {
    				FlowLayout layout = new FlowLayout();
    				layout.setVgap(30);
    				
    				frameDettaglioCategoria = new JFrame("Dettaglio categoria");
    				frameDettaglioCategoria.setLayout(layout);
    				
    				int row = tableCategories.getSelectedRow();
    				int convertedRow = tableCategories.convertRowIndexToModel(row);
    				
    				long id = (long) tableCategories.getValueAt(convertedRow, 0);
    				String categoria = (String) tableCategories.getValueAt(convertedRow, 1);
    				final Category c = new Category(id, categoria);
    				
    				categoriaTF = new JTextField(categoria);
    				categoriaTF.setColumns(25);
    				frameDettaglioCategoria.getContentPane().add(categoriaTF);
    				
    				salvaCategoriaBtn = new JButton(CD_BTN_SALVA_CATEGORIA);
    				salvaCategoriaBtn.setSize(100, 40);
    				salvaCategoriaBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						Category newCat = new Category(c.getId(), categoriaTF.getText());
    						try {
    							updateCategory(newCat);
    						} catch (Exception ex) {
    							JOptionPane.showMessageDialog(frameDettaglioCategoria, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    						}
    						frameDettaglioCategoria.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    					}
    				});
    				frameDettaglioCategoria.getContentPane().add(salvaCategoriaBtn);
    				
    				rimuoviCategoriaBtn = new JButton(CD_BTN_RIMUOVI_CATEGORIA);
    				rimuoviCategoriaBtn.setSize(100, 40);
    				rimuoviCategoriaBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						if(!c.getName().equalsIgnoreCase(defaultCategoryName)) {
    							try {
    								deleteCategory(c);
	    						} catch (Exception ex) {
	    							JOptionPane.showMessageDialog(frameDettaglioCategoria, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
	    						}
    						}else {
    							JOptionPane.showMessageDialog(frameDettaglioCategoria, MSG_RIMOZIONE_CATEGORIA_DEF, "Attenzione", JOptionPane.INFORMATION_MESSAGE);
    						}
    						frameDettaglioCategoria.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    					}
    				});
    				frameDettaglioCategoria.getContentPane().add(rimuoviCategoriaBtn);
    				
    				frameDettaglioCategoria.setSize(400, 180);
    				frameDettaglioCategoria.setLocationRelativeTo(null);
    		        frameDettaglioCategoria.setVisible(true);
    			}
    		}
    	});
    	
    	JScrollPane scrollPane = new JScrollPane(tableCategories);
    	frameAmministrazione.getContentPane().add(scrollPane);
	}
	
	public static void addEntity(Entity e) throws Exception{
		listEntities.add(e);
		try {
			db.insertEntity(e);
		} catch (SQLException ex) {
			throw new Exception(MSG_SALVA_ENTITY_KO + " Errore: " + ex.getMessage());
		}
	}
	
	public static void insertNewEntity(long id) throws Exception{
		try {
			String categoryName = (String) comboCategoriaDettaglioEntity.getSelectedItem();
			String author = authorTF.getText();
			String title = titleTF.getText();
			String technique = techniqueTF.getText();
			String measures = measuresTF.getText();
			String buyYear = buyYearTF.getText();
			String price = priceTF.getText();
			String paymentType = paymentTypeTA.getText();
			String originalPlace = originalPlaceTF.getText();
			String actualPlace = actualPlaceTF.getText();
			String currentValue = currentValueTF.getText();
			String currentValueDate = currentValueDateTF.getText();
			String notes = notesTA.getText();
			String sold = soldTF.getText();
			
			Entity newEntity = new Entity();
			
			newEntity.setTitle(title);
			newEntity.setAuthor(author);
			
			long oldId = db.getEntityIdFromDescription(newEntity);
			
			Category c;
			if(!categoryName.equalsIgnoreCase("")) {
				c = findCategoryByName(categoryName);
				newEntity.setCategoryId(c.getId());
			}else{
				c = findCategoryByName(defaultCategoryName);
				newEntity.setCategoryId(c.getId());
			}
			
			newEntity.setBuyYear(buyYear);
			newEntity.setCurrentValue(currentValue);
			newEntity.setCurrentValueDate(currentValueDate);
			newEntity.setId(id);
			newEntity.setMeasures(measures);
			newEntity.setNotes(notes);
			newEntity.setOriginalPlace(originalPlace);
			newEntity.setPaymentType(paymentType);
			newEntity.setPrice(price);
			newEntity.setTechnique(technique);
			newEntity.setActualPlace(actualPlace);
			newEntity.setSold(sold);

			Entity entityPresente = db.getEntity(id);
			if(entityPresente.getId()>0) {
				updateEntity(id, newEntity);
			}else{
				addEntity(newEntity);
			}
			ImageIcon icon = getIconFromEntityId(id);
			Category category = findCategoryById(newEntity.getCategoryId());
			dtmEntities.addRow(new Object[] {
					icon, newEntity.getId(), category.getName(), newEntity.getAuthor(),
					newEntity.getTitle(), newEntity.getTechnique(), newEntity.getMeasures(), newEntity.getBuyYear(),
					newEntity.getPrice(), newEntity.getOriginalPlace(), newEntity.getActualPlace(),
					newEntity.getCurrentValue(), newEntity.getCurrentValueDate(), newEntity.getSold()  
			});
			
			// Se esisteva già un entity faccio il merge tra quella esistente e quella appena creata
			if (oldId != -1) {	
				regroupEntity(oldId, id);
			} 
			
		} catch (Exception ex) {
			throw ex;
		}
		
	}
	
	private static void regroupEntity(long oldId, long newId) throws SQLException {
		Entity entityOld = db.getEntity(oldId);
		Entity entityNew = db.getEntity(newId);
		mergeEntityData(entityOld, entityNew);
		
		mergeEntityPictures(oldId, newId);
		
		mergeEntityDocuments(oldId, newId);
		
		db.deleteEntity(newId);

		JOptionPane.showMessageDialog(frame, MSG_ACCORPA_ENTITY_OK);
		
	}

	private static void mergeEntityDocuments(long oldId, long newId) throws SQLException {
		ArrayList<Document> documentsOld = db.getDocumentsFromEntityId(oldId);
		ArrayList<Document> documentsNew = db.getDocumentsFromEntityId(newId);
		
		
		for(int i=0; i<documentsNew.size(); i++) {
			Document newTemp = documentsNew.get(i);
			boolean trovato = false;
			for(int j=0; j<documentsOld.size(); j++) {
				Document oldTemp = documentsOld.get(j);
				if(Arrays.equals(newTemp.getData(),oldTemp.getData())) {
					trovato = true;
					if(!oldTemp.getName().equalsIgnoreCase(newTemp.getName())) {
						oldTemp.setName(newTemp.getName());
						db.updateDocument(oldTemp);
					}
					break;					
				}
			}
			if(!trovato) {
				long docId = db.getMaxDocumentsId() + 1;
				Document d = new Document();
				d.setId(docId);
				d.setName(newTemp.getName());
				d.setEntityId(oldId);
				d.setData(newTemp.getData());
				db.insertDocument(d);
			}
			db.deleteDocument(newTemp.getId());
		}
	}

	private static void mergeEntityPictures(long oldId, long newId) throws SQLException {
		ArrayList<Picture> picturesOld = db.getPicturesFromEntityId(oldId);
		ArrayList<Picture> picturesNew = db.getPicturesFromEntityId(newId);

		for(int i=0; i<picturesNew.size(); i++) {
			Picture newTemp = picturesNew.get(i);
			boolean trovata = false;
			for(int j=0; j<picturesOld.size(); j++) {
				Picture oldTemp = picturesOld.get(j);
				if(Arrays.equals(newTemp.getData(),oldTemp.getData())) {
					trovata = true;
					if(!oldTemp.getIsMainPic().equalsIgnoreCase(newTemp.getIsMainPic())
						||!oldTemp.getName().equalsIgnoreCase(newTemp.getName())) {
						oldTemp.setIsMainPic(newTemp.getIsMainPic());
						oldTemp.setName(newTemp.getName());
						db.updatePicture(oldTemp);
					}
					break;					
				}
			}
			if(!trovata) {
				long imageId = db.getMaxPicturesId() + 1;
				Picture p = new Picture();
				p.setId(imageId);
				p.setData(newTemp.getData());
				p.setEntityId(oldId);
				p.setIsMainPic(newTemp.getIsMainPic());
				p.setName(newTemp.getName());
				db.insertPicture(p);
			}
			db.deletePicture(newTemp.getId());
		}
		
	}

	private static void mergeEntityData(Entity entityOld, Entity entityNew) throws SQLException {
		Entity updatedEntity = new Entity();
		updatedEntity.setId(entityOld.getId());
		updatedEntity.setAuthor(entityNew.getAuthor());
		updatedEntity.setTitle(entityNew.getTitle());
		
		Category c = findCategoryByName(defaultCategoryName);
		
		if(entityNew.getCategoryId()!=0 && entityNew.getCategoryId()!= c.getId()) {
			updatedEntity.setCategoryId(entityNew.getCategoryId());
		}else {
			updatedEntity.setCategoryId(entityOld.getCategoryId());
		}
		
		if(!entityNew.getTechnique().equalsIgnoreCase("")) {
			updatedEntity.setTechnique(entityNew.getTechnique());
		}else {
			updatedEntity.setTechnique(entityOld.getTechnique());
		}
		
		if(!entityNew.getMeasures().equalsIgnoreCase("")) {
			updatedEntity.setMeasures(entityNew.getMeasures());
		}else {
			updatedEntity.setMeasures(entityOld.getMeasures());
		}
		
		if(!entityNew.getBuyYear().equalsIgnoreCase("")) {
			updatedEntity.setBuyYear(entityNew.getBuyYear());
		}else {
			updatedEntity.setBuyYear(entityOld.getBuyYear());
		}
		
		if(!entityNew.getPrice().equalsIgnoreCase("")) {
			updatedEntity.setPrice(entityNew.getPrice());
		}else {
			updatedEntity.setPrice(entityOld.getPrice());
		}
		
		if(!entityNew.getPaymentType().equalsIgnoreCase("")) {
			updatedEntity.setPaymentType(entityNew.getPaymentType());
		}else {
			updatedEntity.setPaymentType(entityOld.getPaymentType());
		}
		
		if(!entityNew.getOriginalPlace().equalsIgnoreCase("")) {
			updatedEntity.setOriginalPlace(entityNew.getOriginalPlace());
		}else {
			updatedEntity.setOriginalPlace(entityOld.getOriginalPlace());
		}
		
		if(!entityNew.getActualPlace().equalsIgnoreCase("")) {
			updatedEntity.setActualPlace(entityNew.getActualPlace());
		}else {
			updatedEntity.setActualPlace(entityOld.getActualPlace());
		}
		
		if(!entityNew.getCurrentValue().equalsIgnoreCase("")) {
			updatedEntity.setCurrentValue(entityNew.getCurrentValue());
		}else {
			updatedEntity.setCurrentValue(entityOld.getCurrentValue());
		}
		
		if(!entityNew.getCurrentValueDate().equalsIgnoreCase("")) {
			updatedEntity.setCurrentValueDate(entityNew.getCurrentValueDate());
		}else {
			updatedEntity.setCurrentValueDate(entityOld.getCurrentValueDate());
		}
		
		if(!entityNew.getNotes().equalsIgnoreCase("")) {
			updatedEntity.setNotes(entityNew.getNotes());
		}else {
			updatedEntity.setNotes(entityOld.getNotes());
		}
		
		if(!entityNew.getSold().equalsIgnoreCase("")) {
			updatedEntity.setSold(entityNew.getSold());
		}else {
			updatedEntity.setSold(entityOld.getSold());
		}
		
		db.updateEntity(updatedEntity);		
	}

	public static Category findCategoryByName(String n) throws SQLException {
		Category res = null;
		/*for (int i = 0; i < listCategories.size(); i++) {
			Category current = listCategories.get(i);
			if (current.getName().equals(n)) {
				res = current;
				break;
			}
		}*/		
		res = db.getCategoryByName(n);
		return res;
	}
	
	private static void setAutocomplete(JTextField tf, String column) {
		ArrayList<String> keywords = db.getKeywords(column);
		tf.setFocusTraversalKeysEnabled(false);
		Autocomplete autoComplete = new Autocomplete(tf, keywords);
		tf.getDocument().addDocumentListener(autoComplete);
		tf.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
		tf.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
	}
	
	private static void refreshComboCategoria() {
        ArrayList<Category> listCategories = db.getListCategories();
        String selectedCategory = (String) comboCategoria.getSelectedItem();

        comboCategoria.removeAllItems();
        for (int i = 0; i < listCategories.size(); i++) {
        	comboCategoria.insertItemAt(listCategories.get(i).getName(), i);
        	if (listCategories.get(i).equals(selectedCategory)) {
        		comboCategoria.setSelectedIndex(i);
        	}
        }
	}
	
	private static void refreshComboAutore() {
        ArrayList<String> listAuthors = db.getListAuthors();
        String selectedAuthor = (String) comboAutore.getSelectedItem();

        comboAutore.removeAllItems();
        for (int i = 0; i < listAuthors.size(); i++) {
        	comboAutore.insertItemAt(listAuthors.get(i), i);
        	if (listAuthors.get(i).equals(selectedAuthor)) {
        		comboAutore.setSelectedIndex(i);
        	}
        }
	}
	
	private static void refreshComboLocalizzazione() {
        ArrayList<String> listLocations = db.getListLocations();
        String selectedLocation = (String) comboLocalizzazione.getSelectedItem();
        
        comboLocalizzazione.removeAllItems();
        for (int i = 0; i < listLocations.size(); i++) {
        	comboLocalizzazione.insertItemAt(listLocations.get(i), i);
        	if (listLocations.get(i).equals(selectedLocation)) {
        		comboLocalizzazione.setSelectedIndex(i);
        	}
        }
	}
	
	private static void refreshComboProvenienza() {
        ArrayList<String> listOriginalPlaces = db.getListOriginalPlaces();
        String selectedOriginalPlace = (String) comboProvenienza.getSelectedItem();
        
        comboProvenienza.removeAllItems();
        for (int i = 0; i < listOriginalPlaces.size(); i++) {
        	comboProvenienza.insertItemAt(listOriginalPlaces.get(i), i);
        	if (listOriginalPlaces.get(i).equals(selectedOriginalPlace)) {
        		comboProvenienza.setSelectedIndex(i);
        	}
        }
	}
	
	private static void onEntityChanged() {
		refreshComboAutore();
		refreshComboLocalizzazione();
		refreshComboProvenienza();
	}
	
	public static void showPanelDettaglioEntity() {
		frameDettaglioEntity = new JFrame("Inserimento nuovo elemento");

		panelDettaglioEntity = new JPanel();
		panelDettaglioEntity.setLayout(new GridLayout(16, 2, 10, 10));
		
		frameDettaglioEntity.setLayout(new BoxLayout(frameDettaglioEntity.getContentPane(), BoxLayout.LINE_AXIS));
		
		final long id = getMaxIdEntities() + 1;
		
		createDettaglioEntity(null);
		
		salvaEntityBtn = new JButton(CD_BTN_SALVA_ENTITY);
		salvaEntityBtn.setSize(100, 40);
		salvaEntityBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					insertNewEntity(id);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frameDettaglioEntity, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
				}
				onEntityChanged();
				JOptionPane.showMessageDialog(frameDettaglioEntity, MSG_SALVA_ENTITY_OK);
				panelDettaglioEntity.dispatchEvent(new WindowEvent(frameDettaglioEntity, WindowEvent.WINDOW_CLOSING));
				
			}
		});
		panelDettaglioEntity.add(salvaEntityBtn);
		frameDettaglioEntity.getContentPane().add(panelDettaglioEntity);

		
		panelDocumentsAndPictures = createPanelDocumentsAndPictures(id, insert);
		panelDocumentsAndPictures.setPreferredSize(new Dimension(600, 750));
		
		frameDettaglioEntity.add(panelDocumentsAndPictures);

		frameDettaglioEntity.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frameDettaglioEntity.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int op = JOptionPane.showConfirmDialog(frameDettaglioEntity, "Sei sicuro di voler chiudere la finestra senza salvare?");
				if (op == 0) {
					frameDettaglioEntity.setVisible(false);
				}
			};
		});
		
		frameDettaglioEntity.setSize(1100, 750);
		frameDettaglioEntity.setLocationRelativeTo(null);
		frameDettaglioEntity.setVisible(true);
	}
	
	public void showPanelAmministrazione() {
		frameAmministrazione = new JFrame("AMMINISTRAZIONE");
		frameAmministrazione.setLayout(new FlowLayout());
		
		JLabel nuovaCategoriaLabel = new JLabel("Nuova categoria:");
		frameAmministrazione.getContentPane().add(nuovaCategoriaLabel);
		
		nuovaCategoriaTF = new JTextField();
		nuovaCategoriaTF.setColumns(20);
		frameAmministrazione.getContentPane().add(nuovaCategoriaTF);
		
		JButton nuovaCategoriaBtn = new JButton(CD_BTN_AGGIUNGI_CATEGORIA);
		nuovaCategoriaBtn.setSize(100, 40);
		nuovaCategoriaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String categoryName = nuovaCategoriaTF.getText();
				if (categoryName.trim().isEmpty()) {
					JOptionPane.showMessageDialog(frameAmministrazione, "Non è possibile inserire una categoria vuota.");
					return;
				}
				long newId = getMaxIdCategories() + 1;
				
				Category newCategory = new Category(newId, categoryName);
				
				addCategory(categoryName);
				CommonTableModel dtm = (CommonTableModel) tableCategories.getModel();
				dtm.addRow(new Object[] {newCategory.getId(), newCategory.getName()});
				
				nuovaCategoriaTF.setText("");
			}
		});
        frameAmministrazione.getContentPane().add(nuovaCategoriaBtn);
        
        showTableCategories();
		
		frameAmministrazione.setSize(600, 400);
        frameAmministrazione.setLocationRelativeTo(null);
		frameAmministrazione.setVisible(true);
	}
	
	private static long getMaxIdCategories() {
		long max = 0;
		/*
		for (int i = 0; i < listCategories.size(); i++) {
			if (listCategories.get(i).getId() > max) {
				max = listCategories.get(i).getId();
			}
		}*/
		max = db.getMaxCategoryId();
		return max;
		
	}
	
	private static long getMaxIdEntities() {
		long max = 0;
		/*for (int i = 0; i < listEntities.size(); i++) {
			if (listEntities.get(i).getId() > max) {
				max = listEntities.get(i).getId();
			}
		}*/
		max = db.getMaxEntityId();
		return max;
	}
	
	public static boolean isUsedCategory(Category c) {
		boolean isUsedCategory = db.isUsedCategory(c);
		return isUsedCategory;
	}
	
	public static String getFileNameFromPath(String path) {
		String name = "";
		String[] splitted = path.split("\\" + File.separator);
		name = splitted[splitted.length - 1];
		return name;
	}
	
	public static void deleteCategory(Category category) throws Exception{
		if (isUsedCategory(category)) {
			JOptionPane.showMessageDialog(frameAmministrazione, "Categoria in uso, impossibile eliminarla.");
		} else {
			try {
				db.deleteCategory(category.getId());
				
				for (int i = 0; i < listCategories.size(); i++) {
					Category current = listCategories.get(i);
					if (current.getId() == category.getId()) {
						listCategories.remove(i);
						dtmCategories.removeRow(i);
						break;
					}
				}
				
				refreshComboCategoria();
			} catch (SQLException ex) {
				throw new Exception(MSG_RIMUOVI_CATEGORIA_KO + " Errore: " + ex.getMessage());
			}
		}
	}
	
	public static void updateCategory(Category category) throws Exception{
		try {
			db.updateCategory(category);
			
			long id = category.getId();
			String name = category.getName();
			String oldCategoryName = "";
			
			// aggiorna la categoria nella lista delle categorie
			for (int i = 0; i < listCategories.size(); i++) {
				Category current = listCategories.get(i);
				if (current.getId() == id) {
					oldCategoryName = current.getName();
					listCategories.set(i, category);
					break;
				}
			}
			
			// aggiorna la categoria nel tableModel
			for (int i = 0; i < dtmCategories.getRowCount(); i++) {
				if (dtmCategories.getValueAt(i, 0).equals(id)) {
					Object[] o = new Object[] {id, name};
					for (int j = 0; j < o.length; j++) {
						dtmCategories.setValueAt(o[j], i, j);
					}
					break;
				}
			}
					
			// aggiorna nel tableModel delle entita' la categoria per tutte le
			// entita' che la possiedono
			for (int i = 0; i < dtmEntities.getRowCount(); i++) {
				String entityCatName = (String) dtmEntities.getValueAt(i, 2);
				if (entityCatName.equals(oldCategoryName)) {
					dtmEntities.setValueAt(name, i, 2);
				}
			}
			
			refreshComboCategoria();
		} catch (SQLException ex) {
			throw new Exception( MSG_PROBLEM_SAVE_CATEGORY + " Errore: " + ex.getMessage());
		}
	}
	
	public static void addCategory(String c) {
		long id = getMaxIdCategories() + 1;
		
		Category newCategory = new Category(id, c);
		listCategories.add(newCategory);
		
		try {
			db.insertCategory(newCategory);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton source = (JButton) e.getSource();
			String text = source.getText();
			
			if (CD_BTN_AMMINISTRAZIONE.equals(text)) {
				showPanelAmministrazione();
			}
		}
	}
	
	public static void main(String[] args) {
		init();
		createWindow();
	}

}
