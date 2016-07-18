package antics;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	private static JCheckBox checkbox;
	private static JLabel cboxLabel;
	
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
	
	private static ArrayList<Picture> pictures = new ArrayList<Picture>();
	
	int currentPictureIdx = 0;
	
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
	
	private void showPicture(byte[] bytes) {
		ImageIcon imageIcon = new ImageIcon(bytes);
		Image image = imageIcon.getImage();
		image = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		labelPictures = new JLabel(imageIcon);
		
		panelPictures.removeAll();
		panelPictures.add(labelPictures);
		panelPictures.revalidate();
	}
	
	public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image resized = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resized);
	}
	
	private static BufferedImage getRenderedImage(Image in) {
        int w = in.getWidth(null);
        int h = in.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage out = new BufferedImage(w, h, type);
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(in, 0, 0, null);
        g2.dispose();
        return out;
    }
	
	public static String getFileNameFromPath(String path) {
		String name = "";
		String[] splitted = path.split("\\" + File.separator);
		name = splitted[splitted.length - 1];
		return name;
	}
	
	private static String getFileExtension(String filename) {
		String ext = "";
		String[] splitted = filename.split("\\.");
		if (splitted.length > 0) {
			ext = splitted[splitted.length - 1];
		}
		return ext;
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
		
		pictures = loadPictures(thisDb, thisEntityId);
		
		addPictureBtn = new JButton(CD_BTN_ADD_PICTURE);
		addPictureBtn.setSize(100, 40);
		addPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					byte[] bytes = getBytesFromFile(file);
					
					long id = getMaxPictureId(thisDb) + 1;
					
					Picture p = new Picture();
					p.setId(id);
					p.setEntityId(thisEntityId);
					p.setData(bytes);
					thisDb.insertPicture(p);
					
					ImageIcon icon = new ImageIcon(bytes);
					icon = resizeIcon(icon, 80, 80);
					Picture thumbnail = new Picture();
					thumbnail.setId(id);
					thumbnail.setEntityId(thisEntityId);
					
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					String extension = getFileExtension(getFileNameFromPath(file.getAbsolutePath()));
					try {
						ImageIO.write(getRenderedImage(icon.getImage()), extension, os);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					thumbnail.setData(os.toByteArray());
					thisDb.insertPictureThumbnail(thumbnail);
					
					pictures.add(p);
					
					showPicture(bytes);
				}
			}
		});
		panelButtons.add(addPictureBtn);
		
		checkbox = new JCheckBox();
		checkbox.addItemListener(new ItemListener() {
		    @Override
		    public void itemStateChanged(ItemEvent e) {
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
			        } else {
			        	Picture p = pictures.get(currentPictureIdx);
			            p.setIsMainPic("N");
			            thisDb.updatePicture(p);
			            thisDb.updatePictureThumbnail(p);
			        }
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
				if (pictures.size() > 0) {
					Picture p = pictures.get(currentPictureIdx);
					thisDb.deletePicture(p.getId());
					thisDb.deletePictureThumbnail(p.getId());
				}
			}
		});
		panelButtons.add(removePictureBtn);
		
		prevPictureBtn = new JButton(CD_BTN_PREV_PICTURE);
		prevPictureBtn.setSize(100, 40);
		prevPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		});
		
		nextPictureBtn = new JButton(CD_BTN_NEXT_PICTURE);
		nextPictureBtn.setSize(100, 40);
		nextPictureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pictures.size() > 0) {
					currentPictureIdx = (currentPictureIdx + 1) % pictures.size();
					Picture current = pictures.get(currentPictureIdx);
					checkbox.setSelected("S".equals(current.getIsMainPic()));
					showPicture(current.getData());
				}
			}
		});
		
		mainFrame.getContentPane().add(prevPictureBtn, BorderLayout.LINE_START);
		mainFrame.getContentPane().add(panelPictures, BorderLayout.CENTER);
		mainFrame.getContentPane().add(nextPictureBtn, BorderLayout.LINE_END);
		mainFrame.getContentPane().add(panelButtons, BorderLayout.PAGE_END);
		
		mainFrame.setSize(600, 500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		if (pictures.size() > 0) {
			Picture firstPicture = pictures.get(0);
			showPicture(firstPicture.getData());
		}
	}

}
