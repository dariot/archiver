package antics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dto.Category;
import dto.Entity;

public class Antics implements ActionListener {
	
	private static String SEPARATOR = System.getProperty("line.separator");
	
	private static Antics ref = new Antics();
	
	private static Database db;
	
	private static JFrame frame;
	private static JFrame frameAmministrazione;
	private static JButton adminBtn;
	private static JButton newEntityBtn;
	private static JButton cercaBtn;
	private static JComboBox<String> comboCategoria;
	private static JComboBox<String> comboCategoriaDettaglioEntity;
	private static JTextField nuovaCategoriaTF;
	private static JPanel panelTableEntities;
	
	// tabelle
	private static JTable tableCategories;
	private static String[] categoriesColumns = {"ID", "Categoria"};
	private static JTable tableEntities;
	private static String[] entitiesColumns = {"ID", "Categoria", "Autore", "Titolo",
			"Tecnica usata", "Misure", "Anno di acquisizione", "Importo pagato",
			"Provenienza", "Localizzazione", "Valore odierno"};
	private static DefaultTableModel dtmCategories;
	private static DefaultTableModel dtmEntities;
	
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
	private static JTextField paymentTypeTF;
	private static JTextField originalPlaceTF;
	private static JTextField actualPlaceTF;
	private static JTextField currentValueTF;
	private static JTextField currentValueDateTF;
	private static JTextField soldTF;
	private static JTextField notesTF;
	
	// dettaglio oggetto
	private static JFrame frameDettaglioEntity;
	private static JButton salvaEntityBtn;
	private static JButton rimuoviEntityBtn;
	private static JButton picturesBtn;
	private static JPanel panelDocuments;
	private static JButton addDocumentBtn;
	private static JButton removeDocumentBtn;
	
	private static ArrayList<Category> listCategories = new ArrayList<Category>();
	private static ArrayList<Entity> listEntities = new ArrayList<Entity>();
	
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
        
        comboCategoria = new JComboBox<String>();
        for (int i = 0; i < listCategories.size(); i++) {
        	comboCategoria.insertItemAt(listCategories.get(i).getName(), i);
        }
        panelButtons.add(comboCategoria);
        
        // pulsante "Cerca"
        cercaBtn = new JButton(CD_BTN_CERCA);
        cercaBtn.setSize(100, 40);
        cercaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String categoryName = (String) comboCategoria.getSelectedItem();
				Category c = findCategoryByName(categoryName);
				filterTableEntities(c);
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
		dtmCategories = new DefaultTableModel(0, 0);
    	dtmCategories.setColumnIdentifiers(categoriesColumns);
    	tableCategories.setModel(dtmCategories);
    	
    	// tabella entit�
    	tableEntities = new JTable();
		dtmEntities = new DefaultTableModel(0, 0);
    	dtmEntities.setColumnIdentifiers(entitiesColumns);
    	tableEntities.setModel(dtmEntities);
	}
	
	private static void loadEntities() {
		listEntities = new ArrayList<Entity>();
		
		try {
			// TODO
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
		paymentTypeTF = new JTextField(e.getPaymentType());
		frameDettaglioEntity.add(paymentTypeTF);
		
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
		notesTF = new JTextField(e.getNotes());
		frameDettaglioEntity.add(notesTF);
	}
	
	private static void clearTableEntities() {
		int rowCount = dtmEntities.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			dtmEntities.removeRow(i);
		}
	}
	
	private static void filterTableEntities(Category c) {
		clearTableEntities();
		dtmEntities.fireTableDataChanged();

    	for (int i = 0; i < listEntities.size(); i++) {
    		Entity current = listEntities.get(i);
    		if (c != null) {
    			if (current.getCategoryId() == c.getId()) {
        			dtmEntities.addRow(new Object[] {current.getId(), current.getCategoryId(), current.getAuthor(),
        					current.getTitle(), current.getTechnique(), current.getMeasures(), current.getBuyYear(),
        					current.getPrice(), current.getOriginalPlace(), current.getActualPlace(),
        					current.getCurrentValue()});
    			}
    		} else {
    			dtmEntities.addRow(new Object[] {current.getId(), current.getCategoryId(), current.getAuthor(),
    					current.getTitle(), current.getTechnique(), current.getMeasures(), current.getBuyYear(),
    					current.getPrice(), current.getOriginalPlace(), current.getActualPlace(),
    					current.getCurrentValue()});
    		}
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
	
	public static void updateEntity(long id, Entity e) {
		// aggiorna l'oggetto nella lista degli oggetti
		for (int i = 0; i < listEntities.size(); i++) {
			if (listEntities.get(i).getId() == id) {
				listEntities.set(i, e);
				// aggiorna l'oggetto nel tableModel
				dtmEntities.removeRow(i);
				dtmEntities.addRow(new Object[] {
						e.getId(), e.getCategoryId(), e.getAuthor(),
    					e.getTitle(), e.getTechnique(), e.getMeasures(), e.getBuyYear(),
    					e.getPrice(), e.getOriginalPlace(), e.getActualPlace(),
    					e.getCurrentValue()
				});
				break;
			}
		}
		// salva le modifiche su disco
		writeEntitiesToFile();
	}
	
	public static void removeEntity(long id) {
		// rimuovi l'oggetto dalla lista degli oggetti
		for (int i = 0; i < listEntities.size(); i++) {
			if (listEntities.get(i).getId() == id) {
				listEntities.remove(i);
				// rimuovi l'oggetto dal tableModel
				dtmEntities.removeRow(i);
				break;
			}
		}
		// salva le modifiche su disco
		writeEntitiesToFile();
	}
	
	public static Entity getEntityFromUI(long id) {
		String categoryName = (String) comboCategoriaDettaglioEntity.getSelectedItem();
		String author = authorTF.getText();
		String title = titleTF.getText();
		String technique = techniqueTF.getText();
		String measures = measuresTF.getText();
		String buyYear = buyYearTF.getText();
		String price = priceTF.getText();
		String paymentType = paymentTypeTF.getText();
		String originalPlace = originalPlaceTF.getText();
		String actualPlace = actualPlaceTF.getText();
		String currentValue = currentValueTF.getText();
		String currentValueDate = currentValueDateTF.getText();
		String sold = soldTF.getText();
		String notes = notesTF.getText();
		
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
	
	// la lista delle entit� riceve in input la categoria per cui devono essere filtrate
	private static void showTableEntities(Category c) {
    	for (int i = 0; i < listEntities.size(); i++) {
    		Entity current = listEntities.get(i);
    		if (c != null) {
    			if (current.getCategoryId() == c.getId()) {
        			dtmEntities.addRow(new Object[] {current.getId(), current.getCategoryId(), current.getAuthor(),
        					current.getTitle(), current.getTechnique(), current.getMeasures(), current.getBuyYear(),
        					current.getPrice(), current.getOriginalPlace(), current.getActualPlace(),
        					current.getCurrentValue()});
    			}
    		} else {
    			dtmEntities.addRow(new Object[] {current.getId(), current.getCategoryId(), current.getAuthor(),
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
    			if (event.getClickCount() == 1) {
    				
    				frameDettaglioEntity = new JFrame("Dettaglio");
    				frameDettaglioEntity.setLayout(new GridLayout(14, 2, 10, 10));
    				
    				int row = tableEntities.getSelectedRow();
    				int convertedRow = tableEntities.convertRowIndexToModel(row);
    				
    				final long id = (Long) tableEntities.getValueAt(convertedRow, 0);
    				long categoryId = (Long) tableEntities.getValueAt(convertedRow, 1);
    				String author = (String) tableEntities.getValueAt(row, 2);
    				String title = (String) tableEntities.getValueAt(row, 3);
    				String technique = (String) tableEntities.getValueAt(row, 4);
    				String measures = (String) tableEntities.getValueAt(row, 5);
    				String buyYear = (String) tableEntities.getValueAt(row, 6);
    				String price = (String) tableEntities.getValueAt(row, 7);
    				String originalPlace = (String) tableEntities.getValueAt(row, 8);
    				String actualPlace = (String) tableEntities.getValueAt(row, 9);
    				String currentValue = (String) tableEntities.getValueAt(row, 10);
    				
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
    				
    				salvaEntityBtn = new JButton(CD_BTN_SALVA_ENTITY);
    				salvaEntityBtn.setSize(100, 40);
    				salvaEntityBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						updateEntity(id, getEntityFromUI(id));
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
    						frameDettaglioEntity.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    						JOptionPane.showMessageDialog(frame, MSG_RIMUOVI_ENTITY_OK);
    					}
    				});
    				frameDettaglioEntity.getContentPane().add(rimuoviEntityBtn);
    				
    				picturesBtn = new JButton(CD_BTN_PICTURES);
    				picturesBtn.setSize(100, 40);
    				picturesBtn.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						PictureFactory pic = new PictureFactory(db, id);
    					}
    				});
    				frameDettaglioEntity.getContentPane().add(picturesBtn);
    				
    				// pictures
//    				panelPictures = new JPanel();
//    				
//    				addPictureBtn = new JButton(CD_BTN_ADD_PICTURE);
//    				addPictureBtn.setSize(100, 40);
//    				addPictureBtn.addActionListener(new ActionListener() {
//    					public void actionPerformed(ActionEvent e) {
//    						//framePictures.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//    						JOptionPane.showMessageDialog(frame, MSG_ADD_PICTURE_OK);
//    					}
//    				});
//    				panelPictures.add(addPictureBtn);
//    				
//    				removePictureBtn = new JButton(CD_BTN_REMOVE_PICTURE);
//    				removePictureBtn.setSize(100, 40);
//    				removePictureBtn.addActionListener(new ActionListener() {
//    					public void actionPerformed(ActionEvent e) {
//    						//framePictures.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//    						JOptionPane.showMessageDialog(frame, MSG_REMOVE_PICTURE_OK);
//    					}
//    				});
//    				panelPictures.add(removePictureBtn);
    				
    				frameDettaglioEntity.setSize(600, 600);
    				frameDettaglioEntity.setLocationRelativeTo(null);
    				frameDettaglioEntity.setVisible(true);
    			}
    		}
    	});
    	
    	JScrollPane scrollPane = new JScrollPane(tableEntities);
    	panelTableEntities.add(scrollPane);
	}
	
	private void showTableCategories() {
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
    						JOptionPane.showMessageDialog(frame, MSG_RIMUOVI_CATEGORIA_OK);
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
		writeEntitiesToFile();
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
	
	public static void showPanelDettaglioEntity() {
		frameDettaglioEntity = new JFrame("DETTAGLIO");
		frameDettaglioEntity.setLayout(new GridLayout(15, 2, 10, 10));
		
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
		frameDettaglioEntity.add(authorTF);
		
		titleLabel = new JLabel("Titolo o descrizione dell'oggetto");
		frameDettaglioEntity.add(titleLabel);
		titleTF = new JTextField();
		frameDettaglioEntity.add(titleTF);
	
		techniqueLabel = new JLabel("Tecnica usata");
		frameDettaglioEntity.add(techniqueLabel);
		techniqueTF = new JTextField();
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
		paymentTypeTF = new JTextField();
		frameDettaglioEntity.add(paymentTypeTF);
		
		originalPlaceLabel = new JLabel("Provenienza");
		frameDettaglioEntity.add(originalPlaceLabel);
		originalPlaceTF = new JTextField();
		frameDettaglioEntity.add(originalPlaceTF);
		
		actualPlaceLabel = new JLabel("Localizzazione dell'oggetto");
		frameDettaglioEntity.add(actualPlaceLabel);
		actualPlaceTF = new JTextField();
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
		notesTF = new JTextField();
		frameDettaglioEntity.add(notesTF);
		
		picturesBtn = new JButton(CD_BTN_PICTURES);
		picturesBtn.setSize(100, 40);
		picturesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PictureFactory pic = new PictureFactory(db, null);
			}
		});
		frameDettaglioEntity.getContentPane().add(picturesBtn);
		
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
				String paymentType = paymentTypeTF.getText();
				String originalPlace = originalPlaceTF.getText();
				String actualPlace = actualPlaceTF.getText();
				String currentValue = currentValueTF.getText();
				String currentValueDate = currentValueDateTF.getText();
				String notes = notesTF.getText();
				String sold = soldTF.getText();
				long id = getMaxIdEntities() + 1;
				
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
				dtmEntities.addRow(new Object[] {
						newEntity.getId(), newEntity.getCategoryId(), newEntity.getAuthor(),
						newEntity.getTitle(), newEntity.getTechnique(), newEntity.getMeasures(), newEntity.getBuyYear(),
						newEntity.getPrice(), newEntity.getOriginalPlace(), newEntity.getActualPlace(),
						newEntity.getCurrentValue(), newEntity.getCurrentValueDate(), newEntity.getSold()  
				});
				frameDettaglioEntity.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				JOptionPane.showMessageDialog(frame, MSG_SALVA_ENTITY_OK);
			}
		});
		frameDettaglioEntity.add(salvaEntityBtn);
		
		frameDettaglioEntity.setSize(600, 600);
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
				DefaultTableModel dtm = (DefaultTableModel) tableCategories.getModel();
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
	
	private static ArrayList<Category> readCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
		
		try {
			// TODO
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return categories;
	}
	
	private static long getMaxIdFromFile(String filename) {
		long max = 0;
		
		ArrayList<Category> categories = readCategories();
		for (int i = 0; i < categories.size(); i++) {
			long id = categories.get(i).getId();
			if (id > max) {
				max = id;
			}
		}
		
		return max;
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
		
		db.insertCategory(newCategory);
	}
	
	public static void writeEntitiesToFile() {
		try {
			// TODO
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, MSG_SALVA_ENTITY_KO);
		}
	}
	
	public static void writeCategoriesToFile() {
		try {
			// TODO
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, MSG_PROBLEM_SAVE_CATEGORY);
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
