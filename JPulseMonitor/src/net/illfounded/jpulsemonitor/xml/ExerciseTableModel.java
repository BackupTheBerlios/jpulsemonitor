/*
 * This file is part of the JPulsemonitor.
 *
 * JPulsemonitor is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.illfounded.jpulsemonitor.xml;

import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import net.illfounded.jpulsemonitor.ResourceLoader;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * This is an implementation of <code>TableModel</code> that can manage
 * <code>Node</code>s as elements. Most of the behavior is very standard,
 * not editable and so on...
 */
public class ExerciseTableModel extends DefaultTableModel {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = 7527377212790723129L;

	private String[] _fields;
	private String[] _customFields;
    private Vector<Node> _nodeVector;
    
    /**
     * Constructs a ExerciseTableModel and initializes the table by passing the columnNames. The
     * fieldsNames are used to look up data in a XML-<code>Node</code>.
     * 
     * @param columnNames - the names of the columns
     * @param fieldsNames - The element names that will occure in the XML-<code>Node</code>
     */
    public ExerciseTableModel(String[] columnNames, String[] fieldsNames) {
        super(columnNames, 0);
        _fields = fieldsNames;
        _customFields = ResourceLoader.loadAdminXML().getCustomFieldTypes();
        _nodeVector = new Vector<Node>();
    }

    /**
     * Constructs a ExerciseTableModel. The fieldsNames are used to look up data in a
     * XML-<code>Node</code>.
     * 
     * @param fieldsNames - The element names that will occure in the XML-<code>Node</code>
     */
    public ExerciseTableModel(String[] fieldsNames) {
        super(0, 0);
        _fields = fieldsNames;
        _customFields = ResourceLoader.loadAdminXML().getCustomFieldTypes();
        _nodeVector = new Vector<Node>();
    }
    
    /**
     * Returns true if the cell at rowIndex and columnIndex is editable. Otherwise, setValueAt
     * on the cell will not change the value of that cell.
     * 
     * @param rowIndex - the row whose value to be queried
     * @param columnIndex - the column whose value to be queried
     * 
     * @return true if the cell is editable
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Nothing is editable
        return false;
    }

    /**
     * Adds a new row to the <code>TableModel</code> the fields given during construcion
     * will be used to parse the <code>Node</code>.
     * 
     * @param n - The <code>Node</code> to add
     */
    public void addNode(Node n) {
        Vector<String> newRow = new Vector<String>(columnIdentifiers.size());
        NamedNodeMap nnm = n.getAttributes();
        
        for (int i=0; i<_fields.length; i++) {
            if (nnm.getNamedItem(_fields[i]) == null) {
                newRow.add("");
            } else {
                newRow.add(nnm.getNamedItem(_fields[i]).getNodeValue());
            }
        }

        String desc = "";
        if (n.getFirstChild() != null) {
            desc = n.getFirstChild().getNodeValue();
        }
        
        newRow.add(desc);
        
        dataVector.add(newRow);
        _nodeVector.add(n);
    }
    
    /**
     * Adds a new row to the <code>TableModel</code> the fields given during construcion
     * will be used to parse the <code>Node</code>.
     * 
     * @param n - The <code>Node</code> to add.
     * @param custFieldValues - The custom values.
     */
    public void addNode(Node n, HashMap custFieldValues) {
        Vector<String> newRow = new Vector<String>(columnIdentifiers.size());
        NamedNodeMap nnm = n.getAttributes();
        
        for (int i=0; i<_fields.length; i++) {
            if (nnm.getNamedItem(_fields[i]) == null) {
                newRow.add("");
            } else {
                newRow.add(nnm.getNamedItem(_fields[i]).getNodeValue());
            }
        }

        String desc = "";
        if (n.getFirstChild() != null) {
            desc = n.getFirstChild().getNodeValue();
        }
        
        newRow.add(desc);
        
        // Add custom fields...
        for (int i=0; i<_customFields.length; i++) {
        	String value = (String) custFieldValues.get(_customFields[i]);
        	if (value == null) {
        		newRow.add("");
        	} else {
        		newRow.add(value);
        	}
        }        
        
        dataVector.add(newRow);
        _nodeVector.add(n);
    }

    /**
     * Sets the object value for the cell at <code>column</code> and <code>row</code>.
     * <code>aValue</code> is the new value. This method will generate a
     * <code>tableChanged</code> notification.
     *
     * @param aValue - the new value; this can be null
     * @param row - the row whose value is to be changed
     * @param column - the column whose value is to be changed
     */
    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);
        
       // Update the xml
       Node n = (Node)_nodeVector.get(row);
       
       NamedNodeMap nnm = n.getAttributes();
       
       // nnm.setNamedItem();
    }
    
}
