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

import javax.swing.Action;

import net.illfounded.jpulsemonitor.JPulsemonitor;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * A manager that takes care of all the actions used within the application
 */
public class ActionManager {
    public static final int REFRESH = 1;
    public static final int NEW = 2;
    public static final int EDIT = 3;
    public static final int DELETE = 4;
    
    public static final int NEW_TRAINING = 5;
    public static final int EDIT_TRAINING = 6;
    public static final int DELETE_TRAINING = 7;
    
    public static final int NEW_TRAINING_TYPE = 8;
    public static final int EDIT_TRAINING_TYPE = 9;
    public static final int DELETE_TRAINING_TYPE = 10;
    
    public static final int NEW_EXERCISE = 11;
    public static final int EDIT_EXERCISE = 12;
    public static final int DELETE_EXERCISE = 13;
    
    public static final int EVAL_ALL = 14;
    public static final int ABOUT = 15;
    public static final int PREFERENCE = 16;
    public static final int EXPORT = 17;
    
    public static final int NEW_USER = 18;
    public static final int EDIT_USER = 19;
    public static final int DELETE_USER = 20;
    
    private static ActionManager _instance;
    private static JPulsemonitor _monitor;
    
    private NewTrainingAction _newTraining;
    private EditTrainingAction _editTraining;
    private DeleteTrainingAction _deleteTraining;
    
    private NewTrainingTypeAction _newTrainingType;
    private EditTrainingTypeAction _editTrainingType;
    private DeleteTrainingTypeAction _deleteTrainingType;
    
    private NewExerciseAction _newExercise;
    private EditExerciseAction _editExercise;
    private DeleteExerciseAction _deleteExercise;
    
    private EditUserAction _editUser;
    
    private EvaluateAllAction _evalAll;
    private AboutAction _about;
    private PreferenceAction _pref;
    private ExportAction _export;

    /**
     * Creates the manager
     */
    private ActionManager(JPulsemonitor monitor) {
        _monitor = monitor;
    }

    /**
     * Returns the singleton instance.
     * 
     * @return the one and only ActionManager instance
     */
    public static ActionManager getInstance(JPulsemonitor monitor) {
        if (_instance == null) {
            _instance = new ActionManager(monitor);
        }

        return _instance;
    }

    /**
     * Returns a specific action.
     * 
     * @param type the action type:
     * @return the action command
     */
    public Action getAction(int type) {
        switch (type) {
        case NEW_TRAINING:
            if (_newTraining == null) {
                _newTraining = new NewTrainingAction(_monitor);
            }
            return _newTraining;
            
         case EDIT_TRAINING:
             if (_editTraining == null) {
                 _editTraining = new EditTrainingAction(_monitor);
             }
             return _editTraining;
             
         case DELETE_TRAINING:
             if (_deleteTraining == null) {
                 _deleteTraining = new DeleteTrainingAction(_monitor);
             }
             return _deleteTraining;
             
         case NEW_TRAINING_TYPE:
             if (_newTrainingType == null) {
                 _newTrainingType = new NewTrainingTypeAction(_monitor);
             }
             return _newTrainingType;
             
         case EDIT_TRAINING_TYPE:
             if (_editTrainingType == null) {
                 _editTrainingType = new EditTrainingTypeAction(_monitor);
             }
             return _editTrainingType;
             
         case DELETE_TRAINING_TYPE:
             if (_deleteTrainingType == null) {
                 _deleteTrainingType = new DeleteTrainingTypeAction(_monitor);
             }
             return _deleteTrainingType;
             
         case NEW_EXERCISE:
             if (_newExercise == null) {
                 _newExercise = new NewExerciseAction(_monitor);
             }
             return _newExercise;
             
         case EDIT_EXERCISE:
             if (_editExercise == null) {
                 _editExercise = new EditExerciseAction(_monitor);
             }
             return _editExercise;
             
         case DELETE_EXERCISE:
             if (_deleteExercise == null) {
                 _deleteExercise = new DeleteExerciseAction(_monitor);
             }
             return _deleteExercise;
             
         case EVAL_ALL:
             if (_evalAll == null){
                 _evalAll = new EvaluateAllAction(_monitor);
             }
             return _evalAll;
             
         case NEW_USER:
             return new EmptyAction();
             
         case EDIT_USER:
             if (_editUser == null){
                 _editUser = new EditUserAction(_monitor);
             }
             return _editUser;
             
         case ABOUT:
         	if (_about == null) {
         		_about = new AboutAction(_monitor);
         	}
         	return _about;
         	
         case PREFERENCE:
         	if (_pref == null) {
         		_pref = new PreferenceAction(_monitor);
         	}
         	return _pref;
         	
         case EXPORT:
         	if (_export == null) {
         		_export = new ExportAction(_monitor);
         	}
         	return _export;
         	
         case DELETE_USER:
             return new EmptyAction();
             
        default:
            throw new IllegalArgumentException("Unknown action type: " + type);
        }
    }

}
