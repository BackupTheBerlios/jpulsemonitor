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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.view.action.ActionManager;
import net.illfounded.jpulsemonitor.xml.XMLAdminFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLExerciseFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;
import net.illfounded.jpulsemonitor.xml.dataobject.ExerciseDO;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;


/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A GUI component holding the training data.
 */
public class JExercisePanel extends JPulsTab {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = 5689001765003206202L;

	private XMLResourceBundle _bndl;
    private XMLExerciseFileHandler _exeF;
    private XMLAdminFileHandler _adminF;
    private ActionManager _aManager;
    private JTable _exerciseTable;
    private DateField _start;
    private DateField _end;
    private JUserChooser _userChooser;
    private ExerciseMediator _mediator;
    private JPulsemonitor _monitor;
    private JLabel _totKcal;
    private JLabel _avgHr;
    private JLabel _avgETime;
    private JLabel _avgTimeIZ;
    private String _headers[];

    /**
     * Create new JExercisePanel.
     */
    public JExercisePanel(JPulsemonitor monitor) {
        _monitor = monitor;
		_bndl = monitor.getResourceBundle();
        _exeF = monitor.getExerciseFileHandler();
        _aManager = ActionManager.getInstance(monitor);
        _adminF = monitor.getAdminFileHandler();
        _mediator = new ExerciseMediator(this);
        
        String headers[] = {_bndl.getString("table.identification"), _bndl.getString("table.date"), _bndl.getString("table.training"),
                 _bndl.getString("table.weight"), _bndl.getString("table.kcal"), _bndl.getString("table.hr"), _bndl.getString("table.timeiz"),
                 _bndl.getString("table.etime"), _bndl.getString("table.description")};
        _headers = headers;
        
        initGUI();
        
        _mediator.registerPeriodBoxes(_start, _end);
        _mediator.registerUserChooser(_userChooser);
    }

	public ExerciseDO getSelectedExercise() {
	    int rowIndex = _exerciseTable.getSelectedRow();
	    if (rowIndex < 0) {
	        return null;
	    }
	    
	    TableModel tModel = _exerciseTable.getModel();
	    // "id", "date", "training", "weight", "kcal", "average_hr", "limits", "time_in_zone", "exercise_time", "text"
	    ExerciseDO selectedE = new ExerciseDO((String)tModel.getValueAt(rowIndex, 0));
	    selectedE.setDate((String)tModel.getValueAt(rowIndex, 1));
	    selectedE.setTrainingId((String)tModel.getValueAt(rowIndex, 2));
	    selectedE.setWeight((String)tModel.getValueAt(rowIndex, 3));
	    selectedE.setKcal((String)tModel.getValueAt(rowIndex, 4));
	    selectedE.setAverageHR((String)tModel.getValueAt(rowIndex, 5));
	    selectedE.setTimeInZone((String)tModel.getValueAt(rowIndex, 6));
	    selectedE.setExerciseTime((String)tModel.getValueAt(rowIndex, 7));
	    selectedE.setText((String)tModel.getValueAt(rowIndex, 8));
	    
	    // Add custom fields
		String value;
		String name;
		
		for (int i=9; i<tModel.getColumnCount(); i++) {
			value = (String)tModel.getValueAt(rowIndex, i);
			name = _exerciseTable.getColumnName(i);
			selectedE.setCustomField(name, value);
		}
	    
	    return selectedE;
	}
    
	/**
	 * Helpermethod to set up the GUI.
	 */
	private void initGUI() {
	    // Date selector and JControlPanel
	    JPanel northPanel = new JPanel(new BorderLayout());
	    JPanel datePanel = new JPanel(new FlowLayout());
	    JPanel southPanel = new JPanel(new FlowLayout());
	        
	    _userChooser = new JUserChooser(_adminF.getAllUsersVector(), _bndl.getString("userchooser"), _bndl.getString("userchooser.tooltip"));
	    _userChooser.setSelectedUser(_adminF.getDefaultUser());
	    
	    Calendar cal = Calendar.getInstance();
	    _end = CalendarFactory.createDateField();
	    _end.setValue(cal.getTime());
	    cal.add(Calendar.MONTH, -ResourceLoader.getDefaultValue("nbr.visible.months"));
	    _start = CalendarFactory.createDateField();
	    _start.setValue(cal.getTime());
	    
	    JPanel sessionBut = new JControlPanel(_aManager, ActionManager.NEW_EXERCISE, ActionManager.EDIT_EXERCISE, ActionManager.DELETE_EXERCISE);
	    
	    DefaultTableModel model = _exeF.getAllExercisesTableModel((Date)_start.getValue(), (Date)_end.getValue());
	    model.setColumnIdentifiers(mergeSArray(_headers,_adminF.getCustomFieldTypes()));
	    
	    _exerciseTable = new JTable(model);
	    _exerciseTable.getTableHeader().setReorderingAllowed(false);
	    _exerciseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    JScrollPane sessionScroll = new JScrollPane(_exerciseTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    
	    datePanel.add(_userChooser);
	    datePanel.add(_start);
	    datePanel.add(_end);
	    
	    northPanel.add(datePanel, BorderLayout.WEST);
	    northPanel.add(sessionBut, BorderLayout.EAST);
	    
	    _totKcal = new JLabel();
	    _avgHr = new JLabel();
	    _avgETime = new JLabel();
	    _avgTimeIZ = new JLabel();
	    
	    southPanel.add(new JLabel(_bndl.getString("status.kcal") +":"));
	    southPanel.add(_totKcal);
	    southPanel.add(new JLabel(" / " +_bndl.getString("status.hr") +":"));
	    southPanel.add(_avgHr);
	    southPanel.add(new JLabel(" / " +_bndl.getString("status.timeiz") +":"));
	    southPanel.add(_avgTimeIZ);
	    southPanel.add(new JLabel(" / " +_bndl.getString("status.etime") +":"));
	    southPanel.add(_avgETime);
	    
	    updateStausPanel();
	    
	    setLayout(new BorderLayout());
	    add(northPanel, BorderLayout.NORTH);
	    add(sessionScroll, BorderLayout.CENTER);
	    add(southPanel, BorderLayout.SOUTH);
	    
	    _exerciseTable.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2 ) {
                    Action a = _aManager.getAction(ActionManager.EDIT_EXERCISE);
                    a.actionPerformed(new ActionEvent(e.getSource(), e.getID(), null));
                }
             }
        }); 
	}

	public void refreshExercise() {
	    DefaultTableModel model = _exeF.getAllExercisesTableModel((Date)_start.getValue(), (Date)_end.getValue());

	    model.setColumnIdentifiers(mergeSArray(_headers,_adminF.getCustomFieldTypes()));
	    _exerciseTable.setModel(model);
	    updateStausPanel();
	}
	
	public void refreshUsers() {
	    _userChooser.setUsers(_adminF.getAllUsersVector());
	    _userChooser.setSelectedUser(_adminF.getDefaultUser());
	}
	
	public void selectedUserChanged() {
	    // Load the new exercise handler
	    String userId = _userChooser.getSelectedUserId();
	    if (userId == null) {
	        return;
	    }
	   _exeF = _monitor.loadExerciseFileHandler(userId);
	    refreshExercise();
	}
	
	public void refreshTab() {
	    refreshUsers();
	    selectedUserChanged();
	    Calendar cal = Calendar.getInstance();
	    _end.setValue(cal.getTime());
	    cal.add(Calendar.MONTH, -ResourceLoader.getDefaultValue("nbr.visible.months"));
	    _start.setValue(cal.getTime());
	}
	
	public void updateStausPanel() {
		int nbrOfRows = _exerciseTable.getRowCount();

		if (nbrOfRows <= 0) {
	        return;
		}

		int kcal = 0;
		int avgHr = 0;
		float avgETime = 0.0f;
	    float avgTimeIZ = 0.0f;
	    int nbrOfAvgHr = 0;
	    int nbrOfTimeIZ = 0;
	    int nbrOfETime = 0;
	    
	    TableModel tModel = _exerciseTable.getModel();		    
		for (int rowIndex = 0; rowIndex < nbrOfRows; rowIndex++) {

		    try {
		        // kcal
		        kcal += Integer.parseInt((String)tModel.getValueAt(rowIndex, 4));
		    } catch (NumberFormatException nfe) {
		        // No kcal value... ignore...
		    }

		    try {
		        // average_hr
		        avgHr += Integer.parseInt((String)tModel.getValueAt(rowIndex, 5));
		        nbrOfAvgHr ++;
		    } catch (NumberFormatException nfe) {
		        // No avgHr value... ignore...
		    }
		    
		    try {
		        // time_in_zone
		        avgTimeIZ += Float.parseFloat((String)tModel.getValueAt(rowIndex, 6));
		        nbrOfTimeIZ++;
		    } catch (NumberFormatException nfe) {
		        // No time_in_zone value... ignore...
		    }
		    
	        try {
	            // exercise_time
	            avgETime += Float.parseFloat((String)tModel.getValueAt(rowIndex, 7));
	            nbrOfETime++;
	        } catch (NumberFormatException nfe) {
		        // No exercise_time value... ignore...
		    }
		}
	    
		avgHr = avgHr/nbrOfAvgHr;
		avgETime = avgETime/nbrOfETime;
		avgTimeIZ = avgTimeIZ/nbrOfTimeIZ;
		
		NumberFormat formater = DecimalFormat.getInstance();
        formater.setMaximumFractionDigits(2);
        formater.setMinimumFractionDigits(2);

		_totKcal.setText(kcal +"");
	    _avgHr.setText(avgHr +"");
	    _avgETime.setText(formater.format(avgETime));
	    _avgTimeIZ.setText(formater.format(avgTimeIZ));
	}
	
    /**
     * Helpermethod to assemble column headers...
     */
    private String[] mergeSArray(String[]a1, String[] a2) {
    	String[] a3 = new String[a1.length + a2.length];
    	   	
    	for (int i=0; i< a1.length; i++) {
    		a3[i] = a1[i];
    	}
    	
    	for (int i=0; i< a2.length; i++) {
    		a3[a1.length + i] = a2[i];
    	}
    	
    	return a3;
    }

}
