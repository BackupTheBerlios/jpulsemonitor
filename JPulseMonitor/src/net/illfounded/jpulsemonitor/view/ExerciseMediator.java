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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nachocalendar.components.DateField;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * The Mediator defines how a set of objects interact, it provides a place to localize
 * the knowledge and separate it from the interface so you don't have to go poking
 * around in all the classes to make changes. This implementation takes care of the
 * <code>JExercisePanel</code> related events.
 */
public class ExerciseMediator implements ChangeListener, ItemListener {
    private Logger _log;
	private JExercisePanel _exPanel;
    private DateField _start;
    private DateField _end;
    private JUserChooser _userChooser;
    
    /**
     * Creates a new MainMediator
     */
    public ExerciseMediator(JExercisePanel exPanel) {
		_log = Logger.getLogger("net.illfounded.jpulsemonitor");
		_exPanel = exPanel;
	}

    /**
     * Register a component with the mediator.
     */
    public void registerPeriodBoxes(DateField start, DateField end) {
        _start = start;
        _end = end;
        
        _start.addChangeListener(this);
        _end.addChangeListener(this);
    }
    
    /**
     * Register a component with the mediator.
     */
    public void registerUserChooser(JUserChooser userChooser) {
        _userChooser = userChooser;
        _userChooser.addItemListener(this);
    }

    /**
     * If the user changed the period, we will be notified.
     */
    public void stateChanged(ChangeEvent e) {
        _exPanel.refreshExercise();
    }
 
    /**
     * If the user changed the current user, we will be notified.
     */
    public void itemStateChanged(ItemEvent e) {
    	if (e.getStateChange() == ItemEvent.SELECTED) {
    		_exPanel.selectedUserChanged();	
    	}
    }
    
}
