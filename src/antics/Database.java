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
	
	private static String dbURL = "jdbc:derby:antics;create=true";
    private static String categoryTable = "category";
    private static String entityTable = "entity";
    private static String pictureTable = "picture";
    private static String documentTable = "document";
    
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
    public void insertCategory(Category c) {
        try {
            stmt = conn.createStatement();
            
            long id = c.getId();
            String name = c.getName();
            String insert = "insert into " + categoryTable + " (id, name) values (" + id + ", '" + name + "')";
            
            stmt.execute(insert);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public void updateCategory(Category c) {
        try {
            stmt = conn.createStatement();
            
            long id = c.getId();
            String name = c.getName();
            String update = "update" + categoryTable + " set name = '" + name + "' where id = '" + id + "'";
            
            stmt.execute(update);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public void deleteCategory(long id) {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + categoryTable + " where id = '" + id + "'";
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public Category getCategory(long id) {
    	Category output = new Category();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + categoryTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setName(results.getString(2));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    public ArrayList<Category> getCategories() {
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
        }
        return output;
    }
    
    // ENTITIES
    public void insertEntity(Entity e) {
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
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public void deleteEntity(long id) {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + entityTable + " where id = '" + id + "'";
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public Entity getEntity(long id) {
    	Entity output = new Entity();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + entityTable + " where id = '" + id + "'";
            
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
    
    public ArrayList<Entity> getEntities() {
    	ArrayList<Entity> output = new ArrayList<Entity>();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + entityTable;
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
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
                output.add(e);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    // PICTURES
    public void insertPicture(Picture p) {
        try {
            ps = conn.prepareStatement("Insert into " + pictureTable + " (id, entity_id, data) VALUES (?, ?, ?)");
            
            long id = p.getId();
            long entityId = p.getEntityId();
            byte[] data = p.getData();
            
            Clob clob = conn.createClob();
            OutputStream out = (OutputStream) clob.setAsciiStream(1);
            try {
	            out.write(data);
	            out.flush();
	            out.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
            
            ps.setLong(1, id);
            ps.setLong(2, entityId);
            ps.setClob(3, clob);
            
            ps.execute();
            ps.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public void deletePicture(long id) {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + pictureTable + " where id = '" + id + "'";
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public Picture getPicture(long id) {
    	Picture output = new Picture();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + pictureTable + " where id = '" + id + "'";
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setEntityId(results.getLong(2));
            	output.setData(results.getBytes(3));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
    }
    
    public ArrayList<Picture> getPicturesFromEntityId(long entityId) {
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
                output.add(p);
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
    
    // DOCUMENTS
    public void insertDocument(Document d) {
        try {
            ps = conn.prepareStatement("Insert into " + documentTable + " (id, entity_id, data) VALUES (?, ?, ?)");
            
            long id = d.getId();
            long entityId = d.getEntityId();
            byte[] data = d.getData();
            
            Clob clob = conn.createClob();
            OutputStream out = (OutputStream) clob.setAsciiStream(1);
            try {
	            out.write(data);
	            out.flush();
	            out.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
            
            ps.setLong(1, id);
            ps.setLong(2, entityId);
            ps.setClob(3, clob);
            
            ps.execute();
            ps.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public void deleteDocument(long id) {
    	try {
            stmt = conn.createStatement();
            
            String delete = "delete from " + documentTable + " where id = '" + id + "'";
            
            stmt.execute(delete);
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    public Document getDocument(long id) {
    	Document output = new Document();
        try {
            stmt = conn.createStatement();
            
            String query = "select * from " + documentTable + " where id = '" + id + "'";
            
            ResultSet results = stmt.executeQuery(query);
            while (results.next()) {
            	output.setId(results.getLong(1));
            	output.setEntityId(results.getLong(2));
            	output.setData(results.getBytes(3));
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
				} catch (IOException e) {
					e.printStackTrace();
				}
                output.add(d);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return output;
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
