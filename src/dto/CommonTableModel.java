package dto;

import javax.swing.table.DefaultTableModel;

public class CommonTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isCellEditable(int rowIndex, int mColIndex) {
        return false;
    }

}
