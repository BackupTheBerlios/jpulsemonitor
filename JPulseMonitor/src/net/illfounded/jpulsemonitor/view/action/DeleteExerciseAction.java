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
import net.illfounded.jpulsemonitor.xml.dataobject.ExerciseDO;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * An impementation of an <code>Action</code>, which offers centralized control of functionality
 * and broadcast of property changes. The <code>DeleteExerciseAction</code> handles all events
 * related to the deletation of a exercise element in the adminstration xml-file.
 */
public class DeleteExerciseAction extends AbstractAction {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = -2818645767277518757L;

	private JPulsemonitor _monitor;
    
    /**
     * Creates a new DeleteExerciseAction
     */
    public DeleteExerciseAction(JPulsemonitor monitor) {
        super( );
        _monitor = monitor;
        XMLResourceBundle bndl = _monitor.getResourceBundle();
        
        putValue(Action.NAME, bndl.getString("but.deleteexercise") );
		putValue(Action.SHORT_DESCRIPTION, bndl.getString("tooltip.deleteexercise"));
    }

    /**
     * Invoked when an action occurs.
     * 
     * @param e A semantic event which indicates that a component-defined action occured
     */
    public void actionPerformed(ActionEvent e) {
        ExerciseDO exerciseDO = _monitor.getMainFrame().getExercisePanel().getSelectedExercise();
        
        if (exerciseDO == null) {
            return;
        }
        
        XMLResourceBundle bndl = _monitor.getResourceBundle();
            
        int retV = JOptionPane.showConfirmDialog(_monitor.getMainFrame(), bndl.getString("dlg.delete.msg"), bndl.getString("dlg.delete.title"),
        		JOptionPane.YES_NO_OPTION);

        if (retV != JOptionPane.YES_OPTION) {
            return;
        }
        
        String id = exerciseDO.getIdentification();
      
        try {
            _monitor.getExerciseFileHandler().deleteNode(id);
        } catch (Exception exe) {
            new JErrorDialog(_monitor.getMainFrame(), exe);
            return;
        }
        
        _monitor.getMainFrame().getExercisePanel().refreshExercise();
    }

}
