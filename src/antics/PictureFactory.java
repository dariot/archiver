package antics;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang3.ArrayUtils;

import dto.Picture;

public class PictureFactory {
	
	public static JFrame mainFrame;
	public static JPanel mainPanel;
	
	private static JButton addPictureBtn;
	private static JButton removePictureBtn;
	private static JButton nextPictureBtn;
	private static JButton prevPictureBtn;
	private static JCheckBox checkbox;
	private static JLabel cboxLabel;
	
	private static JPanel panelPictures;
	private static JLabel labelPictures;
	
	private static FileDialog chooser;
	
	private static final String insert = "INSERT_NEW_ENTITY";
	private static final String update = "UPDATE_ENTITY";
	
	public static final String FILE_PATH = "NON_TOCCARE/tempFiles/";
	
	private static final String CD_BTN_ADD_PICTURE = "Aggiungi immagine";
	private static final String CD_BTN_REMOVE_PICTURE = "Rimuovi immagine";
	private static final String CD_BTN_NEXT_PICTURE = ">";
	private static final String CD_BTN_PREV_PICTURE = "<";
	
	private static final String MSG_ADD_PICTURE_OK = "Immagine salvata correttamente.";
	private static final String MSG_ADD_PICTURE_KO = "Si e' verificato un problema nel salvataggio dell''immagine.";
	private static final String MSG_REMOVE_PICTURE_OK = "Rimozione avvenuta correttamente: eliminata l'immagine ";
	private static final String MSG_REMOVE_PICTURE_KO = "Si e' verificato un problema nella rimozione dell''immagine.";
	
	private static final int MAX_WIDTH = 400;
	private static final int MAX_HEIGHT = 300;
	
	private static final String[] imageTypes = {"bmp", "cdp", "djvu", "eps", "gif", "gpd", 
		     "jpd", "jpg", "jpeg", "pict", "png", "tga", "tiff", "pcx", "psd", "webp", 
		     "ai", "cdr", "drv", "dgn", "dds", "dwg", "dxf", "dwf", "edrw", "flt", 
		     "fla", "igs", "lfp", "par", "plt", "prt", "sat", "stl", "svg", "pln", 
		     "pla", "shp"};
	private static final String[] supportedImgTypes = {"jpg", "jpeg", "png"};
	
	private static ArrayList<Picture> pictures = new ArrayList<Picture>();
	
	static int currentPictureIdx = 0;
	
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
	
	private long getMaxPictureId(Database db) {
		return db.getMaxPicturesId();
	}
	
	private ArrayList<Picture> loadPictures(Database db, long entityId) {
		ArrayList<Picture> pictures = db.getPicturesFromEntityId(entityId);
		return pictures;
	}
	
	public static boolean isPictureFile(String ext)	{
		if (ArrayUtils.contains(imageTypes, ext)){
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isPictureFileSupported(String ext){
		if (ArrayUtils.contains(supportedImgTypes, ext)){
			return true;
		}else {
			return false;
		}
	}
	
	public static int[] getScaledMeasures(Image image) {
		int[] scaled = new int[2];
		int height = image.getHeight(null);
		int width = image.getWidth(null);
		if (width > height) {
			double scaling = (double) MAX_WIDTH / (double) width;
			width = (int) ((double) width * scaling);
			height = (int) ((double) height * scaling);
		} else {
			double scaling = (double) MAX_HEIGHT / (double) height;
			height = (int) ((double) height * scaling);
			width = (int) ((double) width * scaling);
		}
		scaled[0] = width;
		scaled[1] = height;
		return scaled;
	}
	
	public static void showPicture(byte[] bytes) {
		ImageIcon imageIcon = new ImageIcon();
		final ImageIcon startImageIcon = new ImageIcon(bytes);
		Image image = startImageIcon.getImage();
		int[] scaledMeasures = getScaledMeasures(image);
		image = image.getScaledInstance(scaledMeasures[0], scaledMeasures[1], Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		labelPictures = new JLabel(imageIcon);
		labelPictures.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					ImageIcon i = (ImageIcon)labelPictures.getIcon();
					try {
						openPictureFromImage(startImageIcon.getImage(), i.getDescription());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
    			}
				
			}
		});
		
		panelPictures.removeAll();
		panelPictures.updateUI();
		panelPictures.add(labelPictures);
		panelPictures.revalidate();

	}
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	protected static void openPictureFromImage(Image image, String name) throws IOException{
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			FileOutputStream fos = new FileOutputStream(FILE_PATH + name);
			ImageIO.write(toBufferedImage(image), "png", fos);
			fos.close();
			
			File file = new File(FILE_PATH + name);
			desktop.open(file);
		}
		
	}

	public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image resized = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resized);
	}
	
	public static BufferedImage getRenderedImage(Image in) {
        int w = in.getWidth(null);
        int h = in.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage out = new BufferedImage(w, h, type);
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(in, 0, 0, null);
        g2.dispose();
        return out;
    }
	
	private static void movePrev() {
		if (pictures.size() > 0) {
			currentPictureIdx--;
			if (currentPictureIdx < 0) {
				currentPictureIdx = pictures.size() - Math.abs(currentPictureIdx);
			}
			Picture current = pictures.get(currentPictureIdx);
			checkbox.setSelected("S".equals(current.getIsMainPic()));
			showPicture(current.getData());
		}
	}
	
	private static void moveNext() {
		if (pictures.size() > 0) {
			currentPictureIdx = (currentPictureIdx + 1) % pictures.size();
			Picture current = pictures.get(currentPictureIdx);
			checkbox.setSelected("S".equals(current.getIsMainPic()));
			showPicture(current.getData());
		}
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
	public PictureFactory(Database db, long entityId, String mode) {
		mainFrame = new JFrame("Immagini");
		mainFrame.setLayout(new BorderLayout());
		
		final Database thisDb = db;
		final long thisEntityId = entityId;
		
		panelPictures = new JPanel();
		
		JPanel panelButtons = new JPanel();
		
		if(mode.equalsIgnoreCase(update)) {
			pictures = loadPictures(thisDb, thisEntityId);
		}else if(mode.equalsIgnoreCase(insert)){
			pictures = new ArrayList<Picture>();
		}
		
		addPictureBtn = new JButton(CD_BTN_ADD_PICTURE);
		addPictureBtn.setSize(100, 40);
		addPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new FileDialog(mainFrame);
				chooser.setDirectory("C:\\");
				chooser.setFile("*.*");
				chooser.setVisible(true);
				String directory = chooser.getDirectory();
				String filename = chooser.getFile();
				if (directory != null && filename != null) {
					
					File file = new File(directory + filename);
					byte[] bytes = getBytesFromFile(file);
					
					long id = getMaxPictureId(thisDb) + 1;
					String name = Antics.getFileNameFromPath(file.getAbsolutePath());
					String extension = Antics.getFileExtension(name);
					
					if(isPictureFile(extension)) {
						if(isPictureFileSupported(extension)) {
							Picture p = new Picture();
							p.setId(id);
							p.setEntityId(thisEntityId);
							p.setData(bytes);
							p.setName(name);
							
							try {
								thisDb.insertPicture(p);
		
								ImageIcon icon = new ImageIcon(bytes);
								icon = resizeIcon(icon, 80, 80);
								Picture thumbnail = new Picture();
								thumbnail.setId(id);
								thumbnail.setEntityId(thisEntityId);
								
								ByteArrayOutputStream os = new ByteArrayOutputStream();
								
								try {
									ImageIO.write(getRenderedImage(icon.getImage()), extension, os);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								thumbnail.setData(os.toByteArray());
								thisDb.insertPictureThumbnail(thumbnail);
								
								pictures.add(p);
								showPicture(bytes);
							
							} catch (SQLException ex) {
								String errore = MSG_ADD_PICTURE_KO + " Errore: " + ex.getMessage();
								JOptionPane.showMessageDialog(mainFrame, errore, errore, JOptionPane.ERROR_MESSAGE);
							}
							
							JOptionPane.showMessageDialog(mainFrame, MSG_ADD_PICTURE_OK);
						} else {
							JOptionPane.showMessageDialog(null, "Il formato dell'immagine non e' supportato", "Formato non corretto", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Il file inserito non e' un immagine", "Formato non corretto", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		panelButtons.add(addPictureBtn);
		
		checkbox = new JCheckBox();
		checkbox.addItemListener(new ItemListener() {
		    @Override
		    public void itemStateChanged(ItemEvent e) {
		    	try {
			    	if (pictures.size() > 0) {
				        if (e.getStateChange() == ItemEvent.SELECTED) {
				        	for (int i = 0; i < pictures.size(); i++) {
				        		Picture current = pictures.get(i);
				        		current.setIsMainPic("N");
				        		thisDb.updatePicture(current);
				        		thisDb.updatePictureThumbnail(current);
				        	}
				        	
				            Picture p = pictures.get(currentPictureIdx);
				            p.setIsMainPic("S");
				            thisDb.updatePicture(p);
				            thisDb.updatePictureThumbnail(p);
				            checkbox.setSelected(true);
				        } else {
				        	Picture p = pictures.get(currentPictureIdx);
				            p.setIsMainPic("N");
				            thisDb.updatePicture(p);
				            thisDb.updatePictureThumbnail(p);
				            checkbox.setSelected(false);
				        }
			    	}
		    	} catch (SQLException ex) {
					String errore = MSG_ADD_PICTURE_KO + " Errore: " + ex.getMessage();
					JOptionPane.showMessageDialog(mainFrame, errore, errore, JOptionPane.ERROR_MESSAGE);
				}
		    }
		});
		
		
		cboxLabel = new JLabel("Immagine principale");
		panelButtons.add(checkbox);
		panelButtons.add(cboxLabel);
		
		removePictureBtn = new JButton(CD_BTN_REMOVE_PICTURE);
		removePictureBtn.setSize(100, 40);
		removePictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Picture p = pictures.get(currentPictureIdx);
					thisDb.deletePicture(p.getId());
					thisDb.deletePictureThumbnail(p.getId());
					
					pictures = loadPictures(thisDb, thisEntityId);
					if (pictures.size() > 0) {
						moveNext();
					}
					JOptionPane.showMessageDialog(mainFrame, MSG_REMOVE_PICTURE_OK + p.getName());
				} catch (SQLException ex) {
					String errore =  MSG_REMOVE_PICTURE_KO + " Errore: " + ex.getMessage();
					JOptionPane.showMessageDialog(mainFrame, errore, errore, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelButtons.add(removePictureBtn);
		
		prevPictureBtn = new JButton(CD_BTN_PREV_PICTURE);
		prevPictureBtn.setSize(100, 40);
		prevPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movePrev();
			}
		});
		
		nextPictureBtn = new JButton(CD_BTN_NEXT_PICTURE);
		nextPictureBtn.setSize(100, 40);
		nextPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveNext();
			}
		});
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(prevPictureBtn, BorderLayout.LINE_START);
		mainPanel.add(panelPictures, BorderLayout.CENTER);
		mainPanel.add(nextPictureBtn, BorderLayout.LINE_END);
		mainPanel.add(panelButtons, BorderLayout.PAGE_END);
		mainPanel.setPreferredSize(new Dimension(550, 350));
		
//		mainFrame.getContentPane().add(mainPanel);
//		
//		mainFrame.setSize(600, 500);
//		mainFrame.setLocationRelativeTo(null);
//		mainFrame.setVisible(true);
		
		if (pictures.size() > 0) {
			Picture firstPicture = pictures.get(0);
			showPicture(firstPicture.getData());
		}
	}

}
