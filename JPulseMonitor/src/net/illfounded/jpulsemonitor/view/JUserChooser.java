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
    private JComboBox _box;
    private JLabel _lbl;
    private Logger _log;

    /**
     * Constructs a new <code>JUserChooser</code>. It takes a <code>Vector</code> of users
     * and uses it to initializes all the <code>JComboBox</code>.
     * 
     * @param factory an xp6-factory to get the ResourceBundle and the DBAccess
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
     * @return the selected user identification
     */
    public String getSelectedUserId() {
        if (_box.getSelectedItem() == null) {
            return null;
        }
        
        UserDO user = (UserDO)_box.getSelectedItem();
        return user.getIdentification();
    }
    
}
