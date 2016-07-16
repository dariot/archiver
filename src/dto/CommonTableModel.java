package dto;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

public class CommonTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isCellEditable(int rowIndex, int mColIndex) {
        return false;
    }
	
	@Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case 0: return ImageIcon.class;
            default: return Object.class;
        }
    }

}
