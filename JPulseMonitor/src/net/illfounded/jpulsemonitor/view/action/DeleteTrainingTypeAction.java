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
import javax.swing.JOptionPane;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.view.JErrorDialog;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingTypeDO;


/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * An impementation of an <code>Action</code>, which offers centralized control of functionality
 * and broadcast of property changes. The <code>DeleteTrainingTypeAction</code> handles all events
 * related to the deletation of a training-type entity in the adminstration xml-file.
 */
public class DeleteTrainingTypeAction extends AbstractAction {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = -463253038087208761L;

	private JPulsemonitor _monitor;
    
    /**
     * Creates a new DeleteTrainingTypeAction
     */
    public DeleteTrainingTypeAction(JPulsemonitor monitor) {
        super( );
        _monitor = monitor;
        XMLResourceBundle bndl = _monitor.getResourceBundle();
        
        putValue(Action.NAME, bndl.getString("but.deletetrainingtype") );
		putValue(Action.SHORT_DESCRIPTION, bndl.getString("tooltip.deletetrainingtype"));
    }

    /**
     * Invoked when an action occurs.
     * 
     * @param e A semantic event which indicates that a component-defined action occured
     */
    public void actionPerformed(ActionEvent e) {
        TrainingTypeDO trainingTDO = _monitor.getMainFrame().getAdminPanel().getSelectedTrainingType();
        
        if (trainingTDO == null) {
            return;
        }
        
        XMLResourceBundle bndl = _monitor.getResourceBundle();
            
        int retV = JOptionPane.showConfirmDialog(_monitor.getMainFrame(), bndl.getString("dlg.delete.msg"), bndl.getString("dlg.delete.title"),
                    JOptionPane.YES_NO_OPTION);

        if (retV == JOptionPane.NO_OPTION) {
            return;
        }
        
        String id = trainingTDO.getIdentification();
        
        try {
            if (_monitor.getAdminFileHandler().checkHasRefId(id, "training_type", "training")) {
                JOptionPane.showMessageDialog(_monitor.getMainFrame(), bndl.getString("dlg.nodelete.msg"));
                return;
            }
            _monitor.getAdminFileHandler().deleteNode(id);
        } catch (Exception exe) {
            new JErrorDialog(_monitor.getMainFrame(), exe);
            return;
        }
        
        _monitor.getMainFrame().getAdminPanel().refreshTrainingType();
    }

}
