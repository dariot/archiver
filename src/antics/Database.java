package antics;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.derby.drda.NetworkServerControl;

import dto.Category;
import dto.Document;
import dto.Entity;
import dto.Picture;

public class Database {
	
	private static String dbURL = "jdbc:derby:NON_TOCCARE;create=true";
    private static String categoryTable = "category";
    private static String entityTable = "entity";
    private static String pictureTable = "picture";
    private static String pictureThumbnailsTable = "picture_thumbnails";
    private static String documentTable = "document";
    
    private static final String defaultCategoryName = "Default";
    
    private static String stringType = "STRING";
    private static String longType = "LONG";
    
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement ps = null;

    public void startServer() {
    	NetworkServerControl serverControl;
		try {
			serverControl = new NetworkServerControl();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public Connection getConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
        return conn;
    }
    
    // CATEGORIES
    public void insertCategory(Category c) throws SQLException {
        try {
            stmt = conn.createStatement();
            
            long id = c.getId();
            String name = c.getName();
            String insert = "insert into " + categoryTable + " (id, name) values (" + id + ", '" + name + "')";
            
            stmt.execute(insert);
            stmt.close();
        }  catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void updateCategory(Category c) throws SQLException{
        try {
            stmt = conn.createStatement();
            
            long id = c.getId();
            String name = c.getName();
            String update = "update " + categoryTable + " set name = '" + name + "' where id = " + id;
            
            stmt.execute(update);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void insert_update_default_category() throws SQLException {
    	Category defaultC = new Category();
    	Category c = new Category(0, defaultCategoryName);
    	defaultC = getCategoryByName(defaultCategoryName);
    	if(defaultC.getId()<0) {
    		insertCategory(c);
    	}else {
    		if(defaultC.getId()!=0) {
    			deleteCategory(defaultC.getId());
    			insertCategory(c);
    		}
    	}
    }
    
    public void deleteCategory(long id) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + categoryTable + " where id = " + id;
            
            stmt.execute(delete);
            stmt.close();
        }  catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public Category getCategory(long id) throws SQLException {
    	Category output = new Category();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + categoryTable + " where id = " + id;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setName(results.getString(2));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
        return output;
    }
    
    public Category getCategoryByName(String name) throws SQLException {
    	Category output = new Category();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + categoryTable + " where name = '" + name + "'";
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setName(results.getString(2));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
        return output;
    }
    
    public ArrayList<Category> getCategories() throws SQLException {
    	ArrayList<Category> output = new ArrayList<Category>();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + categoryTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
                Category c = new Category();
                c.setId(results.getLong(1));
                c.setName(results.getString(2));
                output.add(c);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
        return output;
    }
    
    public boolean isUsedCategory(Category c) {
    	boolean isUsedCategory = false;
    	try {
            stmt = conn.createStatement();
            
            String query = "select * from " + entityTable + " where category_id = " + c.getId();
            
            ResultSet results = stmt.executeQuery(query);
            int count = 0;
            while (results.next()) {
                count++;
            }
            results.close();
            stmt.close();
            
            if (count > 0) {
            	isUsedCategory = true;
            } else {
            	isUsedCategory = false;
            }
        }  catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return isUsedCategory;
    }
    
    public long getMaxCategoryId() {
    	long max = 0;
    	try {
            stmt = conn.createStatement();
            
            String query = "select max(id) from " + categoryTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	if (max < results.getLong(1)) {
            		max = results.getLong(1);
            	}
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return max;
    }
    
    // ENTITIES
    public void insertEntity(Entity e) throws SQLException {
        try {
            stmt = conn.createStatement();
            
            long id = e.getId();
            long categoryId = e.getCategoryId();
            String author = e.getAuthor();
            String title = e.getTitle();
            String technique = e.getTechnique();
            String measures = e.getMeasures();
            String buyYear = e.getBuyYear();
            String price = e.getPrice();
            String paymentType = e.getPaymentType();
            String originalPlace = e.getOriginalPlace();
            String actualPlace = e.getActualPlace();
            String currentValue = e.getCurrentValue();
            String currentValueDate = e.getCurrentValueDate();
            String notes = e.getNotes();
            String sold = e.getSold();
            
            String insert = "Insert into " + entityTable + " (id, category_id, author, title, technique, measures, buy_year, price, payment_type, original_place, actual_place, current_value, current_value_date, notes, sold) VALUES";
            insert += "(" + id + ", " + categoryId + ", '" + author + "', '" + title + "', '" + technique + "', '" + measures + "', '" + buyYear + "', '" + price + "', '" + paymentType + "', '" + originalPlace + "', '" + actualPlace + "', '" + currentValue + "', '" + currentValueDate + "', '" + notes + "', '" + sold + "')";
            
            stmt.execute(insert);
            stmt.close();
        }  catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void updateEntity(Entity e) throws SQLException {
        try {
            stmt = conn.createStatement();
            
            long id = e.getId();
            long categoryId = e.getCategoryId();
            String author = e.getAuthor();
            String title = e.getTitle();
            String technique = e.getTechnique();
            String measures = e.getMeasures();
            String buyYear = e.getBuyYear();
            String price = e.getPrice();
            String paymentType = e.getPaymentType();
            String originalPlace = e.getOriginalPlace();
            String actualPlace = e.getActualPlace();
            String currentValue = e.getCurrentValue();
            String currentValueDate = e.getCurrentValueDate();
            String notes = e.getNotes();
            String sold = e.getSold();
            
            String update = "Update " + entityTable + " set category_id = " + categoryId + ", ";
            update += "author = '" + author + "', title = '" + title + "', technique = '" + technique + "', ";
            update += "measures = '" + measures + "', buy_year = '" + buyYear + "', price = '" + price + "', payment_type = '" + paymentType + "', ";
            update += "original_place = '" + originalPlace + "', actual_place = '" + actualPlace + "', current_value = '" + currentValue + "', ";
            update += "current_value_date = '" + currentValueDate +"', notes = '" + notes + "', sold = '" + sold + "'";
            update += " where id = " + id;
            
            stmt.execute(update);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void deleteEntity(long id) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + entityTable + " where id = " + id;
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public Entity getEntity(long id) {
    	Entity output = new Entity();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + entityTable + " where id = " + id;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setCategoryId(results.getLong(2));
            	output.setAuthor(results.getString(3));
            	output.setTitle(results.getString(4));
            	output.setTechnique(results.getString(5));
            	output.setMeasures(results.getString(6));
            	output.setBuyYear(results.getString(7));
            	output.setPrice(results.getString(8));
            	output.setPaymentType(results.getString(9));
            	output.setOriginalPlace(results.getString(10));
            	output.setActualPlace(results.getString(11));
            	output.setCurrentValue(results.getString(12));
            	output.setCurrentValueDate(results.getString(13));
            	output.setNotes(results.getString(14));
            	output.setSold(results.getString(15));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
 
    private Entity getEntityFromResultSet(ResultSet results) throws SQLException {
    	Entity e = new Entity();
        e.setId(results.getLong(1));
        e.setCategoryId(results.getLong(2));
    	e.setAuthor(results.getString(3));
    	e.setTitle(results.getString(4));
    	e.setTechnique(results.getString(5));
    	e.setMeasures(results.getString(6));
    	e.setBuyYear(results.getString(7));
    	e.setPrice(results.getString(8));
    	e.setPaymentType(results.getString(9));
    	e.setOriginalPlace(results.getString(10));
    	e.setActualPlace(results.getString(11));
    	e.setCurrentValue(results.getString(12));
    	e.setCurrentValueDate(results.getString(13));
    	e.setNotes(results.getString(14));
    	e.setSold(results.getString(15));
    	
    	return e;
    }
    
    public ArrayList<Entity> getEntities() throws SQLException {
    	ArrayList<Entity> output = new ArrayList<Entity>();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + entityTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	Entity e = getEntityFromResultSet(results);
                output.add(e);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
        return output;
    }
    
    public long getMaxEntityId() {
    	long max = 0;
    	try {
            stmt = conn.createStatement();
            
            String query = "select max(id) from " + entityTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	if (max < results.getLong(1)) {
            		max = results.getLong(1);
            	}
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return max;
    }
    
    // PICTURES
    public void insertPicture(Picture p) throws SQLException {
        try {
            ps = conn.prepareStatement("Insert into " + pictureTable + " (id, entity_id, data, is_main_pic, name) VALUES (?, ?, ?, ?, ?)");
            
            long id = p.getId();
            long entityId = p.getEntityId();
            byte[] data = p.getData();
            String isMainPic = p.getIsMainPic().equalsIgnoreCase("")? "N":p.getIsMainPic();
            String name = p.getName();
            
            Clob clob = conn.createClob();
            OutputStream out = (OutputStream) clob.setAsciiStream(1);
            try {
	            out.write(data);
	            out.flush();
	            out.close();
            } catch (IOException ioExcept) {
            	ioExcept.printStackTrace();
            }
            
            ps.setLong(1, id);
            ps.setLong(2, entityId);
            ps.setClob(3, clob);
            ps.setString(4, isMainPic);
            ps.setString(5, name);
            
            ps.execute();
            ps.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void deletePicture(long id) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + pictureTable + " where id = " + id;
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void updatePicture(Picture p) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            long id = p.getId();
            String isMainPic = p.getIsMainPic();
            String name = p.getName();
            String update = "update " + pictureTable + " set is_main_pic = '" + isMainPic + "', name = '" + name + "' where id = " + id;
            
            stmt.execute(update);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public Picture getPicture(long id) throws SQLException {
    	Picture output = new Picture();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + pictureTable + " where id = " + id;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setEntityId(results.getLong(2));
            	output.setData(results.getBytes(3));
            	output.setIsMainPic(results.getString(4));
            	output.setName(results.getString(5));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
        return output;
    }
    
    public ArrayList<Picture> getPicturesFromEntityId(long entityId){
    	ArrayList<Picture> output = new ArrayList<Picture>();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + pictureTable + " where entity_id = " + entityId;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	Picture p = new Picture();
                p.setId(results.getLong(1));
                p.setEntityId(results.getLong(2));
                Clob clob = results.getClob(3);
                try {
					p.setData(IOUtils.toByteArray(clob.getAsciiStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
                p.setIsMainPic(results.getString(4));
                p.setName(results.getString(5));
                output.add(p);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    public Picture getMainPictureFromEntityId(long entityId) {
    	Picture output = null;
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + pictureTable + " where entity_id = " + entityId + " and is_main_pic = 'S'";
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output = new Picture();
            	output.setId(results.getLong(1));
            	output.setEntityId(results.getLong(2));
            	Clob clob = results.getClob(3);
                try {
					output.setData(IOUtils.toByteArray(clob.getAsciiStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
            	output.setIsMainPic(results.getString(4));
            	output.setName(results.getString(5));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    public long getMaxPicturesId() {
    	long max = 0;
    	try {
            stmt = conn.createStatement();
            
            String query = "select max(id) from " + pictureTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	if (max < results.getLong(1)) {
            		max = results.getLong(1);
            	}
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return max;
    }
    
    // PICTURES THUMBNAILS
    public void insertPictureThumbnail(Picture p) throws SQLException {
        try {
            ps = conn.prepareStatement("Insert into " + pictureThumbnailsTable + " (id, entity_id, data, is_main_pic) VALUES (?, ?, ?, 'N')");
            
            long id = p.getId();
            long entityId = p.getEntityId();
            byte[] data = p.getData();
            
            Clob clob = conn.createClob();
            OutputStream out = (OutputStream) clob.setAsciiStream(1);
            try {
	            out.write(data);
	            out.flush();
	            out.close();
            } catch (IOException ioExcept) {
                ioExcept.printStackTrace();
            }
            
            ps.setLong(1, id);
            ps.setLong(2, entityId);
            ps.setClob(3, clob);
            
            ps.execute();
            ps.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void deletePictureThumbnail(long id) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + pictureThumbnailsTable + " where id = " + id;
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void updatePictureThumbnail(Picture p) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            long id = p.getId();
            String isMainPic = p.getIsMainPic();
            String update = "update " + pictureThumbnailsTable + " set is_main_pic = '" + isMainPic + "' where id = " + id;
            
            stmt.execute(update);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public Picture getPictureThumbnail(long id) throws SQLException {
    	Picture output = new Picture();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + pictureThumbnailsTable + " where id = " + id;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setEntityId(results.getLong(2));
            	output.setData(results.getBytes(3));
            	output.setIsMainPic(results.getString(4));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
        return output;
    }
    
    public Picture getMainPictureThumbnailFromEntityId(long entityId) {
    	Picture output = null;
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + pictureThumbnailsTable + " where entity_id = " + entityId + " and is_main_pic = 'S'";
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output = new Picture();
            	output.setId(results.getLong(1));
            	output.setEntityId(results.getLong(2));
            	Clob clob = results.getClob(3);
                try {
					output.setData(IOUtils.toByteArray(clob.getAsciiStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
            	output.setIsMainPic(results.getString(4));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    public ArrayList<Picture> getThumbnailsFromEntityId(long entityId) {
    	ArrayList<Picture> output = new ArrayList<Picture>();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + pictureThumbnailsTable + " where entity_id = " + entityId;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	Picture p = new Picture();
                p.setId(results.getLong(1));
                p.setEntityId(results.getLong(2));
                Clob clob = results.getClob(3);
                try {
					p.setData(IOUtils.toByteArray(clob.getAsciiStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
                p.setIsMainPic(results.getString(4));
                output.add(p);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    // DOCUMENTS
    public void insertDocument(Document d) throws SQLException {
        try {
            ps = conn.prepareStatement("Insert into " + documentTable + " (id, entity_id, data, name) VALUES (?, ?, ?, ?)");
            
            long id = d.getId();
            long entityId = d.getEntityId();
            byte[] data = d.getData();
            String name = d.getName();
            
            Clob clob = conn.createClob();
            OutputStream out = (OutputStream) clob.setAsciiStream(1);
            try {
	            out.write(data);
	            out.flush();
	            out.close();
            } catch (IOException ioExcept) {
                ioExcept.printStackTrace();
            }
            
            ps.setLong(1, id);
            ps.setLong(2, entityId);
            ps.setClob(3, clob);
            ps.setString(4, name);
            
            ps.execute();
            ps.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void updateDocument (Document d) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            long id = d.getId();
            String name = d.getName();
            String update = "update " + documentTable + " set name = '" + name + "' where id = " + id;
            
            stmt.execute(update);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public void deleteDocument(long id) throws SQLException {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + documentTable + " where id = " + id;
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }
    
    public Document getDocument(long id) {
    	Document output = new Document();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + documentTable + " where id = " + id;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setEntityId(results.getLong(2));
            	Clob clob = results.getClob(3);
                try {
					output.setData(IOUtils.toByteArray(clob.getAsciiStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
                output.setName(results.getString(4));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    public ArrayList<Document> getDocumentsFromEntityId(long entityId) {
    	ArrayList<Document> output = new ArrayList<Document>();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + documentTable + " where entity_id = " + entityId;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	Document d = new Document();
                d.setId(results.getLong(1));
                d.setEntityId(results.getLong(2));
                Clob clob = results.getClob(3);
                try {
					d.setData(IOUtils.toByteArray(clob.getAsciiStream()));
				} catch (IOException ioExcept) {
		            ioExcept.printStackTrace();
		        }
                d.setName(results.getString(4));
                output.add(d);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    public long getMaxDocumentsId() {
    	long max = 0;
    	try {
            stmt = conn.createStatement();
            
            String query = "select max(id) from " + documentTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	if (max < results.getLong(1)) {
            		max = results.getLong(1);
            	}
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return max;
    }
    
    public ArrayList<Category> getListCategories() {
    	ArrayList<Category> categories = new ArrayList<Category>();
    	try {
            stmt = conn.createStatement();
            
            String query = "select * from " + categoryTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	long id = results.getLong(1);
            	String name = results.getString(2);
            	Category cat = new Category(id, name);
            	categories.add(cat);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return categories;
    }
    
    public ArrayList<String> getListAuthors() {
    	ArrayList<String> authors = new ArrayList<String>();
    	try {
            stmt = conn.createStatement();
            
            String query = "select distinct author from " + entityTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	authors.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return authors;
    }
    
    public ArrayList<String> getListLocations() {
    	ArrayList<String> locations = new ArrayList<String>();
    	try {
            stmt = conn.createStatement();
            
            String query = "select distinct actual_place from " + entityTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	locations.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return locations;
    }
    
    public ArrayList<String> getListOriginalPlaces() {
    	ArrayList<String> originalPlaces = new ArrayList<String>();
    	try {
            stmt = conn.createStatement();
            
            String query = "select distinct original_place from " + entityTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	originalPlaces.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	return originalPlaces;
    }
    
    public ArrayList<Entity> searchEntities(Category c, String author, String location, String originalPlace) {
    	ArrayList<Entity> listEntities = new ArrayList<Entity>();
    	
    	try {
            stmt = conn.createStatement();
            
            String query = "select * from " + entityTable;
            
            if ((c != null && c.getId()!= -1)|| author != null || location != null || originalPlace != null) {
            	query += " where ";
            	if (c != null && c.getId()!= -1) {
            		query = addWhereElementToQuery(query, "category_id", c.getId(), longType);
            	}
            	query = addWhereElementToQuery(query, "author", author, stringType);
            	query = addWhereElementToQuery(query, "actual_place", location, stringType);
            	query = addWhereElementToQuery(query, "original_place", originalPlace, stringType);
            	query = query.substring(0, query.length() - 4);
            }
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	Entity e = getEntityFromResultSet(results);
            	listEntities.add(e);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	
    	return listEntities;
    }
    
    public long getEntityIdFromDescription(Entity e) throws SQLException {
    	long outputId = -1;
        try {
            stmt = conn.createStatement();
            
            String query = "select id from " + entityTable + " where ";
            
            String whereQuery = "";

            whereQuery = addWhereElementToQuery(whereQuery, "category_id", e.getCategoryId(), longType);
            whereQuery = addWhereElementToQuery(whereQuery, "author", e.getAuthor(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "title", e.getTitle(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "technique", e.getTechnique(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "measures", e.getMeasures(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "buy_year", e.getBuyYear(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "price", e.getPrice(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "payment_type", e.getPaymentType(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "original_place", e.getOriginalPlace(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "actual_place", e.getActualPlace(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "current_value", e.getCurrentValue(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "current_value_date", e.getCurrentValueDate(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "notes", e.getNotes(), stringType);
            whereQuery = addWhereElementToQuery(whereQuery, "sold", e.getSold(), stringType);
            
            if(whereQuery.length()>0) {
            	whereQuery = whereQuery.substring(0, whereQuery.length() - 4);          
            	query += whereQuery;
            	ResultSet results = stmt.executeQuery(query);
            	while (results.next()) {
            		outputId = results.getLong(1);
            	}
            	results.close();
        	}
            
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
        return outputId;
    }
    
    
    public String addWhereElementToQuery (String query, String colName, Object colVal, String colType){
    	if ((colVal != null) && (colName != null && !colName.equalsIgnoreCase(""))){
    		if(colType.equalsIgnoreCase(stringType)){
    			if(!((String)colVal).equalsIgnoreCase("")) {
    				query += colName + " = '" + (String)colVal + "' and ";
    			}
    		}else if(colType.equalsIgnoreCase(longType) && (long)colVal != 0) {
    			query += colName + " = " + (long)colVal +" and ";
    		}	
    	}
    	return query;
    }
    
    public ArrayList<String> getKeywords(String column) {
    	ArrayList<String> keywords = new ArrayList<String>();
    	
    	try {
            stmt = conn.createStatement();
            
            String query = "select distinct " + column + " from " + entityTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	keywords.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    	
    	return keywords;
    }
    
    public void shutdown() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }
        } catch (SQLException sqlExcept) {}
    }

}
