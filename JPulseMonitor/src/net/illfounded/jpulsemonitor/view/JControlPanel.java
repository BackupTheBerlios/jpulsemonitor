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
package net.illfounded.jpulsemonitor.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.view.action.ActionManager;


public class JControlPanel extends JPanel implements Serializable {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = 8496893509374733951L;

	private JButton _btnNew;
    private JButton _btnEdit;
    private JButton _btnDelete;
    
    /**
     * Empty default constructor, necessary to keep the class serializable viz beeing a Bean.
     */
    public JControlPanel() {
        super();
        _btnNew = new JButton();
        _btnEdit = new JButton();
        _btnDelete = new JButton();

        _btnNew.setIcon(ResourceLoader.loadImageIcon(JControlPanel.class, "New16.gif"));
        _btnNew.setPreferredSize(new Dimension(24, 24));
        _btnNew.setMinimumSize(new Dimension(24, 24));
        _btnNew.setMaximumSize(new Dimension(24, 24));
        
        _btnEdit.setIcon(ResourceLoader.loadImageIcon(JControlPanel.class, "Edit16.gif"));
        _btnEdit.setPreferredSize(new Dimension(24, 24));
        _btnEdit.setMinimumSize(new Dimension(24, 24));
        _btnEdit.setMaximumSize(new Dimension(24, 24));
        
        _btnDelete.setIcon(ResourceLoader.loadImageIcon(JControlPanel.class, "Delete16.gif"));
        _btnDelete.setPreferredSize(new Dimension(24, 24));
        _btnDelete.setMinimumSize(new Dimension(24, 24));
        _btnDelete.setMaximumSize(new Dimension(24, 24));
        
        
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
        this.add(_btnNew);
        this.add(_btnEdit);
        this.add(_btnDelete);
    }
    
    /**
     * Constructs a new <code>JControlPanle</code> using the given identifications to get the actions and
     * configure the button.
     */
    public JControlPanel(ActionManager aManager, int newActionID, int editActionID, int deleteActionID) {
        this();
        
        setNewAction(aManager.getAction(newActionID));
        setEditAction(aManager.getAction(editActionID));
        setDeleteAction(aManager.getAction(deleteActionID));
    }
    
    /**
     * Set the Action used for the new button. If no ImageIcon is set, the default one will be used.
     * 
     * @param action The Action to use
     */
    public void setNewAction(Action action) {
        // Check if icon is definded, otherwise add one
        if (action.getValue(Action.SMALL_ICON) == null) {
            action.putValue(Action.SMALL_ICON, ResourceLoader.loadImageIcon(JControlPanel.class, "New16.gif"));
        }
        _btnNew.setAction(action);
        
        // No mnemonic on the control component...
        _btnNew.setMnemonic(0);
        
        // an icon-only button
        _btnNew.setText(""); 
    }
    
    /**
     * Set the Action used for the edit button. If no ImageIcon is set, the default one will be used.
     * 
     * @param action The Action to use
     */
    public void setEditAction(Action action) {
        // Check if icon is definded, otherwise add one
        if (action.getValue(Action.SMALL_ICON) == null) {
            action.putValue(Action.SMALL_ICON, ResourceLoader.loadImageIcon(JControlPanel.class, "Edit16.gif"));
        }
        _btnEdit.setAction(action);
        
        // No mnemonic on the control component...
        _btnEdit.setMnemonic(0);
        
        // an icon-only button
        _btnEdit.setText(""); 
    }
    
    /**
     * Set the Action used for the delete button. If no ImageIcon is set, the default one will be used.
     * 
     * @param action The Action to use
     */
    public void setDeleteAction(Action action) {
        // Check if icon is definded, otherwise add one
        if (action.getValue(Action.SMALL_ICON) == null) {
            action.putValue(Action.SMALL_ICON, ResourceLoader.loadImageIcon(JControlPanel.class, "Delete16.gif"));
        }
        _btnDelete.setAction(action);
        
        // No mnemonic on the control component...
        _btnDelete.setMnemonic(0);
        
        // an icon-only button
        _btnDelete.setText(""); 
    }
    
    /**
     * Returns the Action used for the new button.
     * 
     * @return the current used <code>Action</code>
     */
    public Action getNewAction() {
        return _btnNew.getAction();
    }
    
    /**
     * Returns the Action used for the edit button.
     * 
     * @return the current used <code>Action</code>
     */
    public Action getEditAction() {
        return _btnEdit.getAction();
    }
    
    /**
     * Returns the Action used for the delete button.
     * 
     * @return the current used <code>Action</code>
     */
    public Action getDeleteAction() {
        return _btnDelete.getAction();
    }
    
}