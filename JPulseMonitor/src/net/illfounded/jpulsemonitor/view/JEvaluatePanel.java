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
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.view.action.ActionManager;
import net.illfounded.jpulsemonitor.xml.XMLAdminFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLExerciseFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A GUI component holding the training data.
 */
public class JEvaluatePanel extends JPulsTab {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = -3871197906798678282L;

	private XMLResourceBundle _bndl;
    private XMLExerciseFileHandler _exeF;
    private XMLAdminFileHandler _adminF;
    private ActionManager _aManager;
    private JTable _exerciseTable;
    private DateField _start;
    private DateField _end;
    private JUserChooser _userChooser;
    private JPulsemonitor _monitor;
    private JPanel _chartScroll;
    private JTrainingChooser _tChooser;

    /**
     * Create new JExercisePanel.
     */
    public JEvaluatePanel(JPulsemonitor monitor) {
        _monitor = monitor;
		_bndl = monitor.getResourceBundle();
        _exeF = monitor.getExerciseFileHandler();
        _aManager = ActionManager.getInstance(monitor);
        _adminF = monitor.getAdminFileHandler();
		
        initGUI();
        
    }
    
	/**
	 * Helpermethod to set up the GUI.
	 */
	private void initGUI() {
	    // Date selector and JControlPanel
	    JPanel northPanel = new JPanel(new BorderLayout());
	    JPanel datePanel = new JPanel(new FlowLayout());
	    
	    _userChooser = new JUserChooser(_adminF.getAllUsersVector(), _bndl.getString("userchooser"), _bndl.getString("userchooser.tooltip"));
	    _userChooser.setSelectedUser(_adminF.getDefaultUser());

	    Calendar cal = Calendar.getInstance();
	    _end = CalendarFactory.createDateField();
	    _end.setValue(cal.getTime());
	    cal.add(Calendar.MONTH, - ResourceLoader.getDefaultValue("nbr.visible.months"));
	    _start = CalendarFactory.createDateField();
	    _start.setValue(cal.getTime());
	    
	    JSplitPane centerComp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    
	    _chartScroll = new JPanel(new BorderLayout());
	    _tChooser = new JTrainingChooser(_monitor, this);
	    centerComp.add(_tChooser);
	    centerComp.add(_chartScroll);
	    
	    _userChooser.addItemListener(_tChooser);
	    
	    datePanel.add(_userChooser);
	    datePanel.add(_start);
	    datePanel.add(_end);
	    
	    northPanel.add(datePanel, BorderLayout.WEST);
	    	    
	    setLayout(new BorderLayout());
	    add(northPanel, BorderLayout.NORTH);
	    add(centerComp, BorderLayout.CENTER);
	}

	public void setChart(JAreaChartPanel chart) {
	    _chartScroll.removeAll();
	    JScrollPane chartScroll = new JScrollPane(chart, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	    // BorderLayout is important, otherwise resizing and scroll do not work correct
	    _chartScroll.add(chartScroll, BorderLayout.CENTER);
	    
	    _chartScroll.updateUI();
	}

    /* (non-Javadoc)
     * @see net.illfounded.jpulsemonitor.view.JPulsTab#refreshTab()
     */
    public void refreshTab() {
        _tChooser.refreshCheckBoxes();
        _userChooser.setUsers(_adminF.getAllUsersVector());
        _userChooser.setSelectedUser(_adminF.getDefaultUser());
        
	    Calendar cal = Calendar.getInstance();
	    _end.setValue(cal.getTime());
	    cal.add(Calendar.MONTH, -ResourceLoader.getDefaultValue("nbr.visible.months"));
	    _start.setValue(cal.getTime());
    }
	
    public Date getStart() {
        return (Date)_start.getValue();
    }
    
    public Date getEnd() {
        return (Date)_end.getValue();
    }
  
    public void evalAll() {
        _tChooser.evalAll();
    }
}
