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

import java.awt.FlowLayout;
import java.awt.ItemSelectable;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.illfounded.jpulsemonitor.xml.dataobject.UserDO;

/**
 *  @author Adrian Buerki <ad@illfounded.net>
 * 
 * A visual component to display the users and select exactly one of them.
 */
public class JUserChooser extends JPanel implements ItemSelectable, Serializable {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = 5693246355244300168L;

	private JComboBox _box;
    private JLabel _lbl;
    private Logger _log;

    /**
     * Constructs a new <code>JUserChooser</code>. It takes a <code>Vector</code> of users
     * and uses it to initializes all the <code>JComboBox</code>.
     * 
     * @param users The users to initalize <code>JUserChooser</code> with.
     * @param text The text shown next to the ComboBox.
     * @param tooltip The tooltip, the text displays when the cursor lingers over the component.
     */
    public JUserChooser(Vector<UserDO> users, String text, String tooltip) {
        super();
        _log = Logger.getLogger("net.illfounded.jpulsemonitor");
        _box = new JComboBox(users);
        _lbl = new JLabel(text +" :");
        
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
        this.setToolTipText(tooltip);
        this.add(_lbl);
        this.add(_box);
    }
    
    /**
     * Constructs a new <code>JUserChooser</code>. It takes a <code>Vector</code> of users
     * and uses it to initializes all the <code>JComboBox</code>.
     * 
     * @param users The users to initalize <code>JUserChooser</code> with.
     * @param tooltip The tooltip, the text displays when the cursor lingers over the component.
     */
    public JUserChooser(Vector<UserDO> users, String tooltip) {
        super();
        _log = Logger.getLogger("net.illfounded.jpulsemonitor");
        _box = new JComboBox(users);
        
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
        this.setToolTipText(tooltip);
        this.add(_box);
    }

    /**
     * Reloads the displayed data and resets the selection. No item event is sent!
     */
    public void setUsers(Collection users) {
        _box.removeAllItems();
        Iterator it = users.iterator();
        while (it.hasNext()) {
            _box.addItem(it.next());
        }
    }
    
    /**
     * Adds a listener to the Combobox, containing the Users. The listener will be notified when the
     * choosen User is changed by the user. Item events are not sent when an item's state is set
     * programmatically. If l is null, no exception is thrown and no action is performed.
     * 
     * @param l the listener to receive events
     */
    public void addItemListener(ItemListener l) {
        _box.addItemListener(l);
    }

    /**
     * Removes an item listener. If l is null, no exception is thrown and no action is performed.
     * 
     * @return l the listener being removed
     */
    public void removeItemListener(ItemListener l) {
        _box.removeItemListener(l);
    }

    /**
     * Returns the selected items or null if no items are selected.
     * 
     * @return the selected objects
     */
    public Object[] getSelectedObjects() {
        return _box.getSelectedObjects();
    }

    /**
     * Returns the selected user or null if no items are selected.
     * 
     * @return the selected user identification.
     */
    public String getSelectedUserId() {
        if (_box.getSelectedItem() == null) {
            return null;
        }
        
        UserDO user = (UserDO)_box.getSelectedItem();
        return user.getIdentification();
    }
    
    /**
     * Returns the selected user or null if no items are selected.
     * 
     * @return the selected user.
     */
    public UserDO getSelectedUser() {
        if (_box.getSelectedItem() == null) {
            return null;
        }
        
        return (UserDO)_box.getSelectedItem();
    }
    
    /**
     * Sets the selected user. If the given <code>UserDO</code> is not in the list
     * it will be added and the selection will change to this user.
     * 
     * If this constitutes a change in the selected item, ItemListeners added to
     * the combo box will be notified with one or two ItemEvents. If there is a
     * current selected item, an ItemEvent will be fired and the state change will
     * be ItemEvent.DESELECTED. If anObject is in the list and is not currently
     * selected then an ItemEvent will be fired and the state change will be
     * ItemEvent.SELECTED.
     * 
     * ActionListeners added to the combo box will be notified with an ActionEvent
     * when this method is called.
     * 
     * @param user - The user to select.
     */
    public void setSelectedUser(UserDO user) {
        _box.setSelectedItem(user);
    }
    
}
