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
import net.illfounded.jpulsemonitor.view.JExerciseDialog;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;
import net.illfounded.jpulsemonitor.xml.dataobject.ExerciseDO;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * An impementation of an <code>Action</code>, which offers centralized control of functionality
 * and broadcast of property changes. The <code>NewExerciseAction</code> handles all events
 * related to the creation of a new exercise entity in the exercise xml-file.
 */
public class NewExerciseAction extends AbstractAction {
    private JPulsemonitor _monitor;
    
    /**
     * Creates a new NewTrainingAction
     */
    public NewExerciseAction(JPulsemonitor monitor) {
        super( );
        _monitor = monitor;
        XMLResourceBundle bndl = _monitor.getResourceBundle();
        
        putValue(Action.NAME, bndl.getString("but.addexercise") );
		putValue(Action.SHORT_DESCRIPTION, bndl.getString("tooltip.addexercise"));
		
		// Resolve mnemonic from resource bundle
		char mneKey = bndl.getString("menu.edit.newexercise.mneKey").charAt(0);
		putValue(Action.MNEMONIC_KEY, new Integer(mneKey));
		// putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl " +mneKey));
    }

    /**
     * Invoked when an action occurs.
     * 
     * @param e A semantic event which indicates that a component-defined action occured
     */
    public void actionPerformed(ActionEvent e) {
        ExerciseDO exerciseDO = JExerciseDialog.showExerciseDialog(_monitor);
        
        if (exerciseDO == null) {
            return;
        }
        
        _monitor.getExerciseFileHandler().createNewExercise(exerciseDO);
        
        _monitor.getMainFrame().getExercisePanel().refreshExercise();
    }

}
