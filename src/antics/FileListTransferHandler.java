package antics;


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import org.apache.commons.lang3.ArrayUtils;

import dto.Category;
import dto.Document;
import dto.Entity;
import dto.Picture;


public class FileListTransferHandler extends TransferHandler  {

	private static final long serialVersionUID = 1L;
	private static Database db;
	private static final String defaultCategoryName = "Default";
	
	
	public FileListTransferHandler(Database db) {
		this.db = db;
	}
	
	public int getSourceActions(JComponent c) {
		  return COPY_OR_MOVE;
	}
	
	public boolean canImport(TransferSupport ts) {
		  return ts.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
	}
	
	public int getFileType(File f){
		String[] splittedName = f.getPath().split("\\.");
		String ext = splittedName[1].toLowerCase();
		if(PictureFactory.isPictureFile(ext)){
			if(PictureFactory.isPictureFileSupported(ext)){
				return 0;
			}else {
				JOptionPane.showMessageDialog(null, "Il formato dell'immagine non e' supportato", "Formato non corretto", JOptionPane.INFORMATION_MESSAGE);
				return -1;
			}
		}else if(DocumentFactory.isDocumentFile(ext)) {
			if(DocumentFactory.isDocumentFileSupported(ext)) {
				return 1;
			}else {
				JOptionPane.showMessageDialog(null, "Il formato del documento non e' supportato", "Formato non corretto", JOptionPane.INFORMATION_MESSAGE);
				return -1;
			}
		}else {
			JOptionPane.showMessageDialog(null, "Il formato del file non e' supportato", "Formato non corretto", JOptionPane.INFORMATION_MESSAGE);
			return -1;
		}
	}

	public boolean importData(TransferSupport ts) {
		   try {

		     List data = (List) ts.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
		     if (data.size() < 1) {
		        return false;
		     }

		     ArrayList<File> listModel = new ArrayList<File>();
		     for (Object item : data) {
		        File file = (File) item;
		        listModel.add(file);
		     }
		     
		     
		     
		     for (File file : listModel) {
		    	 final long newEntityId = db.getMaxEntityId() + 1;
		    	 int fileType = getFileType(file);
		    	 if(fileType>=0){
			    	 if(fileType==0){
			    	 
				    	 byte[] bytes = getBytesFromFile(file);
						 long PicId = db.getMaxPicturesId() + 1;
						 String name = Antics.getFileNameFromPath(file.getAbsolutePath());
		
						 Picture p = new Picture();
							p.setId(PicId);
							p.setEntityId(newEntityId);
							p.setData(bytes);
							p.setName(name);
		
							db.insertPicture(p);
		
							ImageIcon icon = new ImageIcon(bytes);
							icon = PictureFactory.resizeIcon(icon, 80, 80);
							Picture thumbnail = new Picture();
							thumbnail.setId(PicId);
							thumbnail.setEntityId(newEntityId);
							
							//PictureFactory.showPicture(icon.getImage().getSrc());
							
							ByteArrayOutputStream os = new ByteArrayOutputStream();
							String extension = Antics.getFileExtension(name);
							try {
								ImageIO.write(PictureFactory.getRenderedImage(icon.getImage()), extension, os);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							thumbnail.setData(os.toByteArray());
							db.insertPictureThumbnail(thumbnail);
			     		}else if(fileType==1){
			     			byte[] bytes = getBytesFromFile(file);
							
							long DocId = db.getMaxDocumentsId() + 1;
							
							String name = Antics.getFileNameFromPath(file.getPath());
							
							Document d = new Document();
							d.setId(DocId);
							d.setEntityId(newEntityId);
							d.setName(name);
							d.setData(bytes);
	
							db.insertDocument(d);
			     		}
						
						Entity newEntity = new Entity();
					    Category c = db.getCategoryByName(defaultCategoryName);
					    newEntity.setId(newEntityId);
						newEntity.setCategoryId(c.getId());
						newEntity.setTitle("Titolo provvisorio" + Long.toString(newEntityId));
						newEntity.setAuthor("Autore provvisorio" + Long.toString(newEntityId));
						Entity entityPresente = db.getEntity(newEntityId);
						if(entityPresente.getId()>0) {
							Antics.updateEntity(newEntityId, newEntity);;
						}else{
							Antics.addEntity(newEntity);
						}
		    	 }
		     }
		     
			Antics.loadEntities();
			
			Antics.filterTableEntities(null,"","","");
			
		    return true;
		  } catch (UnsupportedFlavorException e) {
			 e.printStackTrace();
			 return false;
		  } catch (IOException e) {
			 e.printStackTrace();
			 return false;
		  } catch (SQLException e) {
			 e.printStackTrace();
			 return false;
		  } catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}
	}

	
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
}
