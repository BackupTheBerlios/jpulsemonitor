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
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.view.JAboutDialog;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * An impementation of an <code>Action</code>, which offers centralized control of functionality
 * and broadcast of property changes. The <code>AboutAction</code> opens up the about dialog.
 */
public class AboutAction extends AbstractAction {
    private JPulsemonitor _monitor;
    private JAboutDialog _dlg;
    
    /**
     * Creates a new AboutAction
     */
    public AboutAction(JPulsemonitor monitor) {
        super( );
        _monitor = monitor;
        XMLResourceBundle bndl = _monitor.getResourceBundle();
        
        putValue(Action.NAME, bndl.getString("menu.help.about"));
        char mnem = bndl.getString("menu.help.about.mneKey").charAt(0);
        putValue(Action.MNEMONIC_KEY, new Integer(mnem));
		putValue(Action.SHORT_DESCRIPTION, bndl.getString("tooltip.about"));
		putValue(Action.SMALL_ICON, ResourceLoader.loadImageIcon(AboutAction.class, "About16.gif"));
    }

    /**
     * Invoked when an action occurs.
     * 
     * @param e A semantic event which indicates that a component-defined action occured
     */
    public void actionPerformed(ActionEvent e) {
    	if (_dlg == null) {
    		_dlg = new JAboutDialog(_monitor);
    	}
    	_dlg.setVisible(true);
    }

}
