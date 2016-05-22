package dao;

import antics.Database;
import dto.Category;

public class CategoryDAO {
	
	public static void insertCategory(Database db, Category c) {
		db.insertCategory(c);
	}
	
	public static void getCategory(Database db, String id) {
		
	}
	
	public static void updateCategory(Database db, Category c) {
		
	}

}
