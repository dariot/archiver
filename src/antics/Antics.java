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
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

import org.apache.commons.io.FileUtils;

import dto.Category;
import dto.CommonTableModel;
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
	
	// dettaglio oggetto
	private static JFrame frameDettaglioEntity;
	private static JButton salvaEntityBtn;
	private static JButton rimuoviEntityBtn;
	private static JButton picturesBtn;
	private static JButton documentsBtn;
	private static JPanel panelDocuments;
	private static JButton addDocumentBtn;
	private static JButton removeDocumentBtn;
	
	private static ArrayList<Category> listCategories = new ArrayList<Category>();
	private static ArrayList<Entity> listEntities = new ArrayList<Entity>();
	
	// autocomplete commit string
	private static final String COMMIT_ACTION = "commit";
	
	private static final String CD_BTN_AMMINISTRAZIONE = "Amministrazione";
	private static final String CD_BTN_CERCA = "Cerca";
	private static final String CD_BTN_AGGIUNGI_CATEGORIA = "Aggiungi categoria";
	private static final String CD_BTN_SALVA_CATEGORIA = "Salva categoria";
	private static final String CD_BTN_RIMUOVI_CATEGORIA = "Rimuovi categoria";
	
	private static final String CD_BTN_AGGIUNGI_ENTITY = "Aggiungi oggetto";
	private static final String CD_BTN_SALVA_ENTITY = "Salva oggetto";
	private static final String CD_BTN_RIMUOVI_ENTITY = "Rimuovi oggetto";
	
	private static final String CD_BTN_ADD_DOCUMENT = "Aggiungi documento";
	private static final String CD_BTN_REMOVE_DOCUMENT = "Rimuovi documento";
	
	private static final String CD_BTN_PICTURES = "Immagini";
	private static final String CD_BTN_DOCUMENTS = "Documenti";
	
	private static final String MSG_PROBLEM_LOAD_CATEGORIES = "Si e' verificato un problema nel caricamento delle categorie";
	private static final String MSG_PROBLEM_LOAD_ENTITIES = "Si e' verificato un problema nel caricamento degli oggetti";
	
	private static final String MSG_SALVA_CATEGORIA_OK = "Categoria salvata correttamente.";
	private static final String MSG_AGGIORNA_CATEGORIA_OK = "Categoria aggiornata correttamente.";
	private static final String MSG_RIMUOVI_CATEGORIA_OK = "Categoria rimossa correttamente.";
	private static final String MSG_PROBLEM_SAVE_CATEGORY = "Si e' verificato un problema nel salvataggio della categoria";
	
	private static final String MSG_SALVA_ENTITY_OK = "Oggetto salvato correttamente.";
	private static final String MSG_SALVA_ENTITY_KO = "Si e' verificato un problema nel salvataggio dell''oggetto.";
	private static final String MSG_AGGIORNA_ENTITY_OK = "Oggetto aggiornato correttamente.";
	private static final String MSG_RIMUOVI_ENTITY_OK = "Oggetto rimosso correttamente.";
	
	private static final String MSG_ADD_DOCUMENT_OK = "Documento salvato correttamente.";
	private static final String MSG_ADD_DOCUMENT_KO = "Si e' verificato un problema nel salvataggio del documento.";
	private static final String MSG_REMOVE_DOCUMENT_OK = "Documento rimosso correttamente.";
	private static final String MSG_REMOVE_DOCUMENT_KO = "Si e' verificato un problema nella rimozione del documento.";
	
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
		
		newEntityBtn = new JButton(CD_BTN_AGGIUNGI_ENTITY);
		newEntityBtn.setSize(100, 40);
		newEntityBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPanelDettaglioEntity();
			}
		});
		panelButtons.add(newEntityBtn);
        
		// pulsante "Amministrazione"
        adminBtn = new JButton(CD_BTN_AMMINISTRAZIONE);
        adminBtn.setSize(100, 40);
        adminBtn.addActionListener(ref);
        panelButtons.add(adminBtn);
        
        labelCategoria = new JLabel("Categoria");
        comboCategoria = new JComboBox<String>();
        for (int i = 0; i < listCategories.size(); i++) {
        	comboCategoria.insertItemAt(listCategories.get(i).getName(), i);
        }
        panelButtons.add(labelCategoria);
        panelButtons.add(comboCategoria);
        
        // combo autore
        labelAutore = new JLabel("Autore");
        ArrayList<String> listAuthors = db.getListAuthors();
        comboAutore = new JComboBox<String>();
        for (int i = 0; i < listAuthors.size(); i++) {
        	comboAutore.insertItemAt(listAuthors.get(i), i);
        }
        panelButtons.add(labelAutore);
        panelButtons.add(comboAutore);
        
        // combo localizzazione
        labelLocalizzazione = new JLabel("Localizzazione");
        ArrayList<String> listLocations = db.getListLocations();
        comboLocalizzazione = new JComboBox<String>();
        for (int i = 0; i < listLocations.size(); i++) {
        	comboLocalizzazione.insertItemAt(listLocations.get(i), i);
        }
        panelButtons.add(labelLocalizzazione);
        panelButtons.add(comboLocalizzazione);
        
        // combo provenienza
        labelProvenienza = new JLabel("Provenienza");
        ArrayList<String> listOriginalPlaces = db.getListOriginalPlaces();
        comboProvenienza = new JComboBox<String>();
        for (int i = 0; i < listOriginalPlaces.size(); i++) {
        	comboProvenienza.insertItemAt(listOriginalPlaces.get(i), i);
        }
        panelButtons.add(labelProvenienza);
        panelButtons.add(comboProvenienza);
        
        // pulsante "Cerca"
        cercaBtn = new JButton(CD_BTN_CERCA);
        cercaBtn.setSize(100, 40);
        cercaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String categoryName = (String) comboCategoria.getSelectedItem();
				Category c = findCategoryByName(categoryName);
				
				String author = (String) comboAutore.getSelectedItem();
				String location = (String) comboLocalizzazione.getSelectedItem();
				String originalPlace = (String) comboProvenienza.getSelectedItem();
				
				filterTableEntities(c, author, location, originalPlace);
			}
		});
        panelButtons.add(cercaBtn);
        
        panelTableEntities = new JPanel();
        showTableEntities(null);
        
        frame.getContentPane().add(panelButtons, BorderLayout.PAGE_START);
        frame.getContentPane().add(panelTableEntities, BorderLayout.CENTER);
		
		frame.setSize(1200, 650);
        frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static void startDB() {
		db = new Database();
		db.startServer();
		db.getConnection();
	}
	
	private static void stopDB() {
		db.shutdown();
	}
	
	private static void init() {
		startDB();
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
	
	private static void loadEntities() {
		listEntities = new ArrayList<Entity>();
		
		try {
			listEntities = db.getEntities();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, MSG_PROBLEM_LOAD_ENTITIES + ":\n\n" + e.getMessage());
			e.printStackTrace();
		}
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
		categoryLabel = new JLabel("Categoria dell'oggetto");
		frameDettaglioEntity.add(categoryLabel);
		comboCategoriaDettaglioEntity = new JComboBox<String>();
        for (int i = 0; i < listCategories.size(); i++) {
        	comboCategoriaDettaglioEntity.insertItemAt(listCategories.get(i).getName(), i);
        }
        String categoryName = findCategoryById(e.getCategoryId()).getName();
        comboCategoriaDettaglioEntity.setSelectedItem(categoryName);
		frameDettaglioEntity.add(comboCategoriaDettaglioEntity);
		
		authorLabel = new JLabel("Autore");
		frameDettaglioEntity.add(authorLabel);
		authorTF = new JTextField(e.getAuthor());
		frameDettaglioEntity.add(authorTF);
		
		titleLabel = new JLabel("Titolo o descrizione dell'oggetto");
		frameDettaglioEntity.add(titleLabel);
		titleTF = new JTextField(e.getTitle());
		frameDettaglioEntity.add(titleTF);
	
		techniqueLabel = new JLabel("Tecnica usata");
		frameDettaglioEntity.add(techniqueLabel);
		techniqueTF = new JTextField(e.getTechnique());
		frameDettaglioEntity.add(techniqueTF);
		
		measuresLabel = new JLabel("Misure");
		frameDettaglioEntity.add(measuresLabel);
		measuresTF = new JTextField(e.getMeasures());
		frameDettaglioEntity.add(measuresTF);
		
		buyYearLabel = new JLabel("Anno di acquisizione");
		frameDettaglioEntity.add(buyYearLabel);
		buyYearTF = new JTextField(e.getBuyYear());
		frameDettaglioEntity.add(buyYearTF);
		
		priceLabel = new JLabel("Importo originario pagato");
		frameDettaglioEntity.add(priceLabel);
		priceTF = new JTextField(e.getPrice());
		frameDettaglioEntity.add(priceTF);
		
		paymentTypeLabel = new JLabel("Modalita' di pagamento");
		frameDettaglioEntity.add(paymentTypeLabel);
		paymentTypeTA = new JTextArea(e.getPaymentType());
		paymentTypeTA.setSize(15, 20);
		JScrollPane scrollPanePaymentType = new JScrollPane(paymentTypeTA);
		frameDettaglioEntity.add(scrollPanePaymentType);
		
		originalPlaceLabel = new JLabel("Provenienza");
		frameDettaglioEntity.add(originalPlaceLabel);
		originalPlaceTF = new JTextField(e.getOriginalPlace());
		frameDettaglioEntity.add(originalPlaceTF);
		
		actualPlaceLabel = new JLabel("Localizzazione dell'oggetto");
		frameDettaglioEntity.add(actualPlaceLabel);
		actualPlaceTF = new JTextField(e.getActualPlace());
		frameDettaglioEntity.add(actualPlaceTF);
		
		currentValueLabel = new JLabel("Valore odierno");
		frameDettaglioEntity.add(currentValueLabel);
		currentValueTF = new JTextField(e.getCurrentValue());
		frameDettaglioEntity.add(currentValueTF);
		
		currentValueDateLabel = new JLabel("Data valore odierno");
		frameDettaglioEntity.add(currentValueDateLabel);
		currentValueDateTF = new JTextField(e.getCurrentValueDate());
		frameDettaglioEntity.add(currentValueDateTF);
		
		soldLabel = new JLabel("Oggetto venduto");
		frameDettaglioEntity.add(soldLabel);
		soldTF = new JTextField(e.getCurrentValueDate());
		frameDettaglioEntity.add(soldTF);
		
		notesLabel = new JLabel("Annotazioni varie");
		frameDettaglioEntity.add(notesLabel);
		notesTA = new JTextArea(e.getNotes());
		notesTA.setSize(15, 20);
		JScrollPane scrollPaneNotes = new JScrollPane(notesTA);
		frameDettaglioEntity.add(scrollPaneNotes);
	}
	
	private static void clearTableEntities() {
		int rowCount = dtmEntities.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			dtmEntities.removeRow(i);
		}
	}
	
	private static void filterTableEntities(Category c, String author, String location, String originalPlace) {
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
		ArrayList<Picture> pictures = db.getPicturesFromEntityId(id);
		ImageIcon icon = new ImageIcon();
		if (pictures.size() > 0) {
			Picture mainPic = db.getMainPictureFromEntityId(id);
			if (mainPic != null) {
				icon = new ImageIcon(mainPic.getData());
			} else {
				icon = new ImageIcon(pictures.get(0).getData());
			}
			icon = resizeIcon(icon, 80, 80);
		}
		return icon;
	}
	
	public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image resized = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resized);
	}
	
	public static void updateEntity(long id, Entity e) {
		db.updateEntity(e);
		
		// aggiorna l'oggetto nella lista degli oggetti
		for (int i = 0; i < listEntities.size(); i++) {
			if (listEntities.get(i).getId() == id) {
				listEntities.set(i, e);
				// aggiorna l'oggetto nel tableModel
				dtmEntities.removeRow(i);
				
				ImageIcon icon = getIconFromEntityId(id);
				Category category = findCategoryById(e.getCategoryId());
				dtmEntities.addRow(new Object[] {
						icon,
						e.getId(), category.getName(), e.getAuthor(),
    					e.getTitle(), e.getTechnique(), e.getMeasures(), e.getBuyYear(),
    					e.getPrice(), e.getOriginalPlace(), e.getActualPlace(),
    					e.getCurrentValue()
				});
				break;
			}
		}
	}
	
	public static void removeEntity(long id) {
		db.deleteEntity(id);
		
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
	
	public static Entity getEntityFromUI(long id) {
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
		
		return newEntity;
	}
	
	// la lista delle entita' riceve in input la categoria per cui devono essere filtrate
	private static void showTableEntities(Category c) {
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
    			dtmEntities.addRow(new Object[] {icon, current.getId(), category.getName(), current.getAuthor(),
    					current.getTitle(), current.getTechnique(), current.getMeasures(), current.getBuyYear(),
    					current.getPrice(), current.getOriginalPlace(), current.getActualPlace(),
    					current.getCurrentValue()});
    		}
    	}
    	dtmEntities.fireTableDataChanged();
    	
    	tableEntities.setPreferredScrollableViewportSize(new Dimension(1000, 400));
    	tableEntities.setFillsViewportHeight(true);
    	
    	tableEntities.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent event) {
    			if (event.getClickCount() == 2) {
    				
    				frameDettaglioEntity = new JFrame("Dettaglio");
    				frameDettaglioEntity.setLayout(new GridLayout(16, 2, 10, 10));
    				
    				int row = tableEntities.getSelectedRow();
    				int convertedRow = tableEntities.convertRowIndexToModel(row);
    				
    				final long id = (Long) tableEntities.getValueAt(convertedRow, 1);
    				long categoryId = (Long) tableEntities.getValueAt(convertedRow, 2);
    				String author = (String) tableEntities.getValueAt(row, 3);
    				String title = (String) tableEntities.getValueAt(row, 4);
    				String technique = (String) tableEntities.getValueAt(row, 5);
    				String measures = (String) tableEntities.getValueAt(row, 6);
    				String buyYear = (String) tableEntities.getValueAt(row, 7);
    				String price = (String) tableEntities.getValueAt(row, 8);
    				String originalPlace = (String) tableEntities.getValueAt(row, 9);
    				String actualPlace = (String) tableEntities.getValueAt(row, 10);
    				String currentValue = (String) tableEntities.getValueAt(row, 11);
    				
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
    				entity.setPaymentType(paymentType);
    				entity.setNotes(notes);
    				
    				createDettaglioEntity(entity);
    				
    				// pictures
    				picturesBtn = new JButton(CD_BTN_PICTURES);
    				picturesBtn.setSize(100, 40);
    				picturesBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						PictureFactory pic = new PictureFactory(db, id);
    					}
    				});
    				frameDettaglioEntity.getContentPane().add(picturesBtn);
    				
    				// documents
    				documentsBtn = new JButton(CD_BTN_DOCUMENTS);
    				documentsBtn.setSize(100, 40);
    				documentsBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						DocumentFactory doc = new DocumentFactory(db, id);
    					}
    				});
    				frameDettaglioEntity.getContentPane().add(documentsBtn);
    				
    				salvaEntityBtn = new JButton(CD_BTN_SALVA_ENTITY);
    				salvaEntityBtn.setSize(100, 40);
    				salvaEntityBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						updateEntity(id, getEntityFromUI(id));
    						onEntityChanged();
    						frameDettaglioEntity.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    						JOptionPane.showMessageDialog(frame, MSG_AGGIORNA_ENTITY_OK);
    					}
    				});
    				frameDettaglioEntity.getContentPane().add(salvaEntityBtn);
    				
    				rimuoviEntityBtn = new JButton(CD_BTN_RIMUOVI_ENTITY);
    				rimuoviEntityBtn.setSize(100, 40);
    				rimuoviEntityBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						removeEntity(id);
    						onEntityChanged();
    						frameDettaglioEntity.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    						JOptionPane.showMessageDialog(frame, MSG_RIMUOVI_ENTITY_OK);
    					}
    				});
    				frameDettaglioEntity.getContentPane().add(rimuoviEntityBtn);
    				
    				frameDettaglioEntity.setSize(600, 750);
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
    						updateCategory(c);
    						frameDettaglioCategoria.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    						JOptionPane.showMessageDialog(frame, MSG_AGGIORNA_CATEGORIA_OK);
    					}
    				});
    				frameDettaglioCategoria.getContentPane().add(salvaCategoriaBtn);
    				
    				rimuoviCategoriaBtn = new JButton(CD_BTN_RIMUOVI_CATEGORIA);
    				rimuoviCategoriaBtn.setSize(100, 40);
    				rimuoviCategoriaBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						deleteCategory(c);
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
	
	public static void addEntity(Entity e) {
		listEntities.add(e);
		db.insertEntity(e);
	}
	
	public static Category findCategoryByName(String n) {
		Category res = null;
		for (int i = 0; i < listCategories.size(); i++) {
			Category current = listCategories.get(i);
			if (current.getName().equals(n)) {
				res = current;
				break;
			}
		}
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
	
	private static void refreshComboAutore() {
        ArrayList<String> listAuthors = db.getListAuthors();
        comboAutore.removeAllItems();
        for (int i = 0; i < listAuthors.size(); i++) {
        	comboAutore.insertItemAt(listAuthors.get(i), i);
        }
	}
	
	private static void refreshComboLocalizzazione() {
        ArrayList<String> listLocations = db.getListLocations();
        comboLocalizzazione.removeAllItems();
        for (int i = 0; i < listLocations.size(); i++) {
        	comboLocalizzazione.insertItemAt(listLocations.get(i), i);
        }
	}
	
	private static void refreshComboProvenienza() {
        ArrayList<String> listOriginalPlaces = db.getListOriginalPlaces();
        comboProvenienza.removeAllItems();
        for (int i = 0; i < listOriginalPlaces.size(); i++) {
        	comboProvenienza.insertItemAt(listOriginalPlaces.get(i), i);
        }
	}
	
	private static void onEntityChanged() {
		refreshComboAutore();
		refreshComboLocalizzazione();
		refreshComboProvenienza();
	}
	
	public static void showPanelDettaglioEntity() {
		frameDettaglioEntity = new JFrame("DETTAGLIO");
		frameDettaglioEntity.setLayout(new GridLayout(17, 2, 10, 10));
		
		categoryLabel = new JLabel("Categoria dell'oggetto");
		frameDettaglioEntity.add(categoryLabel);
		comboCategoriaDettaglioEntity = new JComboBox<String>();
        for (int i = 0; i < listCategories.size(); i++) {
        	comboCategoriaDettaglioEntity.insertItemAt(listCategories.get(i).getName(), i);
        }
		frameDettaglioEntity.add(comboCategoriaDettaglioEntity);
		
		authorLabel = new JLabel("Autore");
		frameDettaglioEntity.add(authorLabel);
		authorTF = new JTextField();
		setAutocomplete(authorTF, "AUTHOR");
		frameDettaglioEntity.add(authorTF);
		
		titleLabel = new JLabel("Titolo o descrizione dell'oggetto");
		frameDettaglioEntity.add(titleLabel);
		titleTF = new JTextField();
		frameDettaglioEntity.add(titleTF);
	
		techniqueLabel = new JLabel("Tecnica usata");
		frameDettaglioEntity.add(techniqueLabel);
		techniqueTF = new JTextField();
		setAutocomplete(techniqueTF, "TECHNIQUE");
		frameDettaglioEntity.add(techniqueTF);
		
		measuresLabel = new JLabel("Misure");
		frameDettaglioEntity.add(measuresLabel);
		measuresTF = new JTextField();
		frameDettaglioEntity.add(measuresTF);
		
		buyYearLabel = new JLabel("Anno di acquisizione");
		frameDettaglioEntity.add(buyYearLabel);
		buyYearTF = new JTextField();
		frameDettaglioEntity.add(buyYearTF);
		
		priceLabel = new JLabel("Importo originario pagato");
		frameDettaglioEntity.add(priceLabel);
		priceTF = new JTextField();
		frameDettaglioEntity.add(priceTF);
		
		paymentTypeLabel = new JLabel("Modalita' di pagamento");
		frameDettaglioEntity.add(paymentTypeLabel);
		paymentTypeTA = new JTextArea(15, 20);
		//setAutocomplete(paymentTypeTA, "PAYMENT_TYPE");
		JScrollPane scrollPanePaymentType = new JScrollPane(paymentTypeTA);
		frameDettaglioEntity.add(scrollPanePaymentType);
		
		originalPlaceLabel = new JLabel("Provenienza");
		frameDettaglioEntity.add(originalPlaceLabel);
		originalPlaceTF = new JTextField();
		frameDettaglioEntity.add(originalPlaceTF);
		
		actualPlaceLabel = new JLabel("Localizzazione dell'oggetto");
		frameDettaglioEntity.add(actualPlaceLabel);
		actualPlaceTF = new JTextField();
		setAutocomplete(actualPlaceTF, "ACTUAL_PLACE");
		frameDettaglioEntity.add(actualPlaceTF);
		
		currentValueLabel = new JLabel("Valore odierno");
		frameDettaglioEntity.add(currentValueLabel);
		currentValueTF = new JTextField();
		frameDettaglioEntity.add(currentValueTF);
		
		currentValueDateLabel = new JLabel("Data valore odierno");
		frameDettaglioEntity.add(currentValueDateLabel);
		currentValueDateTF = new JTextField();
		frameDettaglioEntity.add(currentValueDateTF);
		
		soldLabel = new JLabel("Oggetto venduto");
		frameDettaglioEntity.add(soldLabel);
		soldTF = new JTextField();
		frameDettaglioEntity.add(soldTF);
		
		notesLabel = new JLabel("Annotazioni varie");
		frameDettaglioEntity.add(notesLabel);
		notesTA = new JTextArea(15, 20);
		JScrollPane scrollPaneNotes = new JScrollPane(notesTA);
		frameDettaglioEntity.add(scrollPaneNotes);
		
		final long id = getMaxIdEntities() + 1;
		
		// pictures
		picturesBtn = new JButton(CD_BTN_PICTURES);
		picturesBtn.setSize(100, 40);
		picturesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PictureFactory pic = new PictureFactory(db, id);
			}
		});
		frameDettaglioEntity.getContentPane().add(picturesBtn);
		
		// documents
		documentsBtn = new JButton(CD_BTN_DOCUMENTS);
		documentsBtn.setSize(100, 40);
		documentsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DocumentFactory doc = new DocumentFactory(db, id);
			}
		});
		frameDettaglioEntity.getContentPane().add(documentsBtn);
		
		salvaEntityBtn = new JButton(CD_BTN_SALVA_ENTITY);
		salvaEntityBtn.setSize(100, 40);
		salvaEntityBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
				
				addEntity(newEntity);
				
				ImageIcon icon = getIconFromEntityId(id);
				Category category = findCategoryById(newEntity.getCategoryId());
				dtmEntities.addRow(new Object[] {
						icon, newEntity.getId(), category.getName(), newEntity.getAuthor(),
						newEntity.getTitle(), newEntity.getTechnique(), newEntity.getMeasures(), newEntity.getBuyYear(),
						newEntity.getPrice(), newEntity.getOriginalPlace(), newEntity.getActualPlace(),
						newEntity.getCurrentValue(), newEntity.getCurrentValueDate(), newEntity.getSold()  
				});
				onEntityChanged();
				frameDettaglioEntity.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				JOptionPane.showMessageDialog(frame, MSG_SALVA_ENTITY_OK);
			}
		});
		frameDettaglioEntity.add(salvaEntityBtn);
		
		frameDettaglioEntity.setSize(600, 650);
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
		for (int i = 0; i < listCategories.size(); i++) {
			if (listCategories.get(i).getId() > max) {
				max = listCategories.get(i).getId();
			}
		}
		return max;
	}
	
	private static long getMaxIdEntities() {
		long max = 0;
		for (int i = 0; i < listEntities.size(); i++) {
			if (listEntities.get(i).getId() > max) {
				max = listEntities.get(i).getId();
			}
		}
		return max;
	}
	
	public static void deleteCategory(Category category) {
		for (int i = 0; i < listCategories.size(); i++) {
			Category current = listCategories.get(i);
			if (current.getId() == category.getId()) {
				listCategories.remove(i);
				dtmCategories.removeRow(i);
				break;
			}
		}
		db.deleteCategory(category.getId());
	}
	
	public static void updateCategory(Category category) {
		// aggiorna la categoria nella lista delle categorie
		for (int i = 0; i < listCategories.size(); i++) {
			Category current = listCategories.get(i);
			if (current.getId() == category.getId()) {
				listCategories.set(i, category);
				// aggiorna la categoria nel tableModel
				dtmCategories.removeRow(i);
				dtmCategories.addRow(new Object[] {category.getId(), category.getName()});
				break;
			}
		}
		db.updateCategory(category);
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
