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
package net.illfounded.jpulsemonitor.view.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.view.JUserDialog;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;
import net.illfounded.jpulsemonitor.xml.dataobject.UserDO;


/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * An impementation of an <code>Action</code>, which offers centralized control of functionality
 * and broadcast of property changes. The <code>NewUserAction</code> handles all events
 * related to the creation of a new user entity.
 */
public class NewUserAction extends AbstractAction {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = -3003447684432230855L;

	private JPulsemonitor _monitor;
    
    /**
     * Creates a new NewUserAction.
     */
    public NewUserAction(JPulsemonitor monitor) {
        super( );
        _monitor = monitor;
        XMLResourceBundle bndl = _monitor.getResourceBundle();
        
        putValue(Action.NAME, bndl.getString("but.adduser") );
		putValue(Action.SHORT_DESCRIPTION, bndl.getString("tooltip.adduser"));
    }

    /**
     * Invoked when an action occurs.
     * 
     * @param e A semantic event which indicates that a component-defined action occured
     */
    public void actionPerformed(ActionEvent e) {
        UserDO userDO = JUserDialog.showUserDialog(_monitor);
        
        if (userDO == null) {
            return;
        }
        
        _monitor.getAdminFileHandler().createNewUser(userDO);
        _monitor.getMainFrame().getAdminPanel().refreshUser();
        
        // Cause the userlists and trainings to be reread, if become visible
        _monitor.getMainMediator().setAdminUpdated(true);
    }

}
