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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.view.action.ActionManager;
import net.illfounded.jpulsemonitor.xml.XMLAdminFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingDO;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingTypeDO;
import net.illfounded.jpulsemonitor.xml.dataobject.UserDO;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A GUI component holding the administration related tables.
 */
public class JAdminPanel extends JPulsTab {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = 145758158762790286L;

	private XMLResourceBundle _bndl;
    private XMLAdminFileHandler _adminF;
    private ActionManager _aManager;
    private JTable _trainingTTable;
    private JTable _trainingTable;
    private JTable _userTable;
    private String _headersTT[];
    private String _headersT[];
    private String _headersU[];
    private JLabel _labelBMI;
    private JLabel _labelMaxHR;


    /**
     * Create new JAdminPanel.
     */
    public JAdminPanel(JPulsemonitor monitor) {
		_bndl = monitor.getResourceBundle();
        _adminF = monitor.getAdminFileHandler();
        _aManager = ActionManager.getInstance(monitor);
        
        String headersTT[] = {_bndl.getString("table.identification"), _bndl.getString("table.priority"), _bndl.getString("table.intensity"),
	            _bndl.getString("table.time"), _bndl.getString("table.text")};
		_headersTT = headersTT;
		
	    String headersT[] = {_bndl.getString("table.identification"),_bndl.getString("table.trainingtype"), _bndl.getString("table.duration"),
	            _bndl.getString("table.distance"), _bndl.getString("table.limits"), _bndl.getString("table.active"), _bndl.getString("table.text")};
	    _headersT = headersT;
	    
	    String headersU[] = {_bndl.getString("table.identification"), _bndl.getString("table.weight"), _bndl.getString("table.size"),
	            _bndl.getString("table.yearofbirth"), _bndl.getString("table.gender"), _bndl.getString("table.name")};
	    _headersU = headersU;
	    
        initGUI();
        
        _trainingTTable.setRowSelectionInterval(0, 0);
        _trainingTable.setRowSelectionInterval(0, 0);
        _userTable.setRowSelectionInterval(0, 0);
        
        updateStausPanel();
    }

	/**
	 * Helpermethod to set up the GUI.
	 */
	private void initGUI() {
	    // Training types...
	    JPanel trainingTPanel = new JPanel(new BorderLayout());
	    JPanel trainingTNorthPanel = new JPanel(new BorderLayout());
	    JLabel trainingTLabel = new JLabel(_bndl.getString("admin.type"));
	    JPanel trainingTBut = new JControlPanel(_aManager, ActionManager.NEW_TRAINING_TYPE, ActionManager.EDIT_TRAINING_TYPE, ActionManager.DELETE_TRAINING_TYPE);
	    
	    DefaultTableModel model = _adminF.getAllTrainingTypesTableModel();
	    model.setColumnIdentifiers(_headersTT);
	    
	    _trainingTTable = new JTable(model);
	    _trainingTTable.getTableHeader().setReorderingAllowed(false);
	    _trainingTTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    JScrollPane trainingTScroll = new JScrollPane(_trainingTTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    trainingTNorthPanel.add(trainingTLabel, BorderLayout.WEST);
	    trainingTNorthPanel.add(trainingTBut, BorderLayout.EAST);
	    
	    trainingTPanel.add(trainingTNorthPanel, BorderLayout.NORTH);
	    trainingTPanel.add(trainingTScroll, BorderLayout.CENTER);
		
	    // Trainings...
	    JPanel trainingPanel = new JPanel(new BorderLayout());
	    JPanel trainingNorthPanel = new JPanel(new BorderLayout());
	    JLabel trainingLabel = new JLabel(_bndl.getString("admin.training"));
	    JPanel trainingBut = new JControlPanel(_aManager, ActionManager.NEW_TRAINING, ActionManager.EDIT_TRAINING, ActionManager.DELETE_TRAINING);

	    model = _adminF.getAllTrainingsTableModel();
	    model.setColumnIdentifiers(_headersT);
	    
	    _trainingTable = new JTable(model);
		_trainingTable.getTableHeader().setReorderingAllowed(false);
		_trainingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane trainingScroll = new JScrollPane(_trainingTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    trainingNorthPanel.add(trainingLabel, BorderLayout.WEST);
	    trainingNorthPanel.add(trainingBut, BorderLayout.EAST);
	    
	    trainingPanel.add(trainingNorthPanel, BorderLayout.NORTH);
	    trainingPanel.add(trainingScroll, BorderLayout.CENTER);
	    
	    // Users...
	    JPanel userPanel = new JPanel(new BorderLayout());
	    JPanel userSouthPanel = new JPanel(new FlowLayout());
	    JPanel userNorthPanel = new JPanel(new BorderLayout());
	    JLabel userLabel = new JLabel(_bndl.getString("admin.user"));
	    JPanel userBut = new JControlPanel(_aManager, ActionManager.NEW_USER, ActionManager.EDIT_USER, ActionManager.DELETE_USER);
	    
	    model = _adminF.getAllUsersTableModel();
	    model.setColumnIdentifiers(_headersU);
	    
	    _userTable = new JTable(model);
		_userTable.getTableHeader().setReorderingAllowed(false);
		_userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane userScroll = new JScrollPane(_userTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    userNorthPanel.add(userLabel, BorderLayout.WEST);
	    userNorthPanel.add(userBut, BorderLayout.EAST);
	    
	    _labelBMI = new JLabel();
	    _labelMaxHR = new JLabel();
	    
	    userSouthPanel.add(new JLabel(_bndl.getString("status.bmi") +":"));
	    userSouthPanel.add(_labelBMI);
	    userSouthPanel.add(new JLabel(" / " +_bndl.getString("status.maxhr") +":"));
	    userSouthPanel.add(_labelMaxHR);
	    
	    userPanel.add(userNorthPanel, BorderLayout.NORTH);
	    userPanel.add(userScroll, BorderLayout.CENTER);
	    userPanel.add(userSouthPanel, BorderLayout.SOUTH);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(trainingTPanel);
		add(trainingPanel);
		add(userPanel);
		
		registerDoubleClickWithActions();
	}

	public void refreshUser() {
	    DefaultTableModel model = _adminF.getAllUsersTableModel();
	    model.setColumnIdentifiers(_headersU);
	    _userTable.setModel(model);
	    
	    _userTable.setRowSelectionInterval(0, 0);
	    updateStausPanel();
	}
	
	public void refreshTrainingType() {
	    DefaultTableModel model = _adminF.getAllTrainingTypesTableModel();
	    model.setColumnIdentifiers(_headersTT);
	    _trainingTTable.setModel(model);
	}
	
	public void refreshTraining() {
	    DefaultTableModel model = _adminF.getAllTrainingsTableModel();
	    model.setColumnIdentifiers(_headersT);
	    _trainingTable.setModel(model);
	}
	
	public void refreshTab() {
	    refreshUser();
	    refreshTraining();
	    refreshTrainingType();
	}
	
	public TrainingDO getSelectedTraining() {
	    int rowIndex = _trainingTable.getSelectedRow();
	    if (rowIndex < 0) {
	        return null;
	    }
	    
	    TableModel tModel = _trainingTable.getModel();
	    // "id", "training_type", "duration", "toughness", "text"
	    TrainingDO selectedT = new TrainingDO((String)tModel.getValueAt(rowIndex, 0));
	    selectedT.setTrainingType((String)tModel.getValueAt(rowIndex, 1));
	    selectedT.setDuration((String)tModel.getValueAt(rowIndex, 2));
	    selectedT.setDistance((String)tModel.getValueAt(rowIndex, 3));
	    selectedT.setLimits((String)tModel.getValueAt(rowIndex, 4));
	    selectedT.setActive((String)tModel.getValueAt(rowIndex, 5));
	    selectedT.setText((String)tModel.getValueAt(rowIndex, 6));
	    return selectedT;
	}
	
	/**
	 * Returns a dataobject representation of the currently selected training type.
	 * 
	 * @return the selected training type.
	 */
	public TrainingTypeDO getSelectedTrainingType() {
	    int rowIndex = _trainingTTable.getSelectedRow();
	    if (rowIndex < 0) {
	        return null;
	    }
	    
	    TableModel tModel = _trainingTTable.getModel();
	    // "id", "priority", "intensity", "time", "text"
	    TrainingTypeDO selectedT = new TrainingTypeDO((String)tModel.getValueAt(rowIndex, 0));
	    selectedT.setPriority((String)tModel.getValueAt(rowIndex, 1));
	    selectedT.setIntensity((String)tModel.getValueAt(rowIndex, 2));
	    selectedT.setTime((String)tModel.getValueAt(rowIndex, 3));
	    selectedT.setText((String)tModel.getValueAt(rowIndex, 4));
	    return selectedT;
	}
	
	/**
	 * Returns a dataobject representation of the currently selected user.
	 * 
	 * @return the selected user.
	 */
	public UserDO getSelectedUser() {
	    int rowIndex = _userTable.getSelectedRow();
	    if (rowIndex < 0) {
	        return null;
	    }
	    
	    TableModel tModel = _userTable.getModel();
	    // "id", "weight", "year_of_birth", "gender", "name"
	    UserDO selectedU = new UserDO((String)tModel.getValueAt(rowIndex, 0));
	    selectedU.setWeight((String)tModel.getValueAt(rowIndex, 1));
	    selectedU.setSize((String)tModel.getValueAt(rowIndex, 2));
	    selectedU.setYearOfBirth((String)tModel.getValueAt(rowIndex, 3));
	    selectedU.setGender((String)tModel.getValueAt(rowIndex, 4));
	    selectedU.setName((String)tModel.getValueAt(rowIndex, 5));
	    return selectedU;
	}
	
	private void registerDoubleClickWithActions() {
	    _trainingTTable.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2 ) {
                    Action a = _aManager.getAction(ActionManager.EDIT_TRAINING_TYPE);
                    a.actionPerformed(new ActionEvent(e.getSource(), e.getID(), null));
                }
             }
        }); 
	    
	    _trainingTable.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2 ) {
                    Action a = _aManager.getAction(ActionManager.EDIT_TRAINING);
                    a.actionPerformed(new ActionEvent(e.getSource(), e.getID(), null));
                }
             }
        });
	    
	    _userTable.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2 ) {
                    Action a = _aManager.getAction(ActionManager.EDIT_USER);
                    a.actionPerformed(new ActionEvent(e.getSource(), e.getID(), null));
                } else {
                	updateStausPanel();
                }
             }
        });
	}

	public void updateStausPanel() {
	    int rowIndex = _userTable.getSelectedRow();
	    if (rowIndex < 0) {
	        return;
	    }

	    TableModel tModel = _userTable.getModel();
	    // "id", "weight", "year_of_birth", "gender", "name"
	    float weight = Float.parseFloat((String)tModel.getValueAt(rowIndex, 1));
	    float size = Float.parseFloat((String)tModel.getValueAt(rowIndex, 2));
	    int yearOfBirth = Integer.parseInt((String)tModel.getValueAt(rowIndex, 3));
	    String gender = (String)tModel.getValueAt(rowIndex, 4);
	    int year = GregorianCalendar.getInstance().get(Calendar.YEAR);
	    int age = year - yearOfBirth;

        NumberFormat formater = DecimalFormat.getInstance();
        formater.setMaximumFractionDigits(2);
        formater.setMinimumFractionDigits(2);
        
	    float bmi = calculateBMI(weight, size);
	    _labelBMI.setText(formater.format(bmi));
	    _labelBMI.setForeground(getBMIColor(bmi));

		_labelMaxHR.setText(calculateMaxHeartRate(gender, age) +"");
	}
	
    /**
     * Helpermethod to evaluate the "Body Mass Index"
     */
    private float calculateBMI(float weightKG, float sizeM) {
        float bmi = weightKG / (sizeM *sizeM);
        
        return bmi;
    }
    
    /**
     * Helpermethod to evaluate the maximal heart rate.
     */
    private float calculateMaxHeartRate(String gender, int age) {
        if (age < 40) {
            if (gender.equalsIgnoreCase("male")) {
                // Male: 220 - (your age)
                return 220 - age;
            } else {
                // Female: 226 - (your age)
                return 226 -age;
            }
        } else {
            // Male and female over 40 : 208 - (your age x 0.7)
            return 208.0f - (age * 0.7f);
        }
    }
	
    /**
     * Helpermethod to get the color to highlight the BMI value.
     */
	private Color getBMIColor(float bmi) {
	    if (bmi < 16.0f) {
	        // bmi < 16.0 : critical underweight
	        return Color.RED;
	    } else if (bmi < 18.5f) {
	        // 16 < bmi < 18.5 : underweight
	        return Color.YELLOW;
	    } else if (bmi < 25.0f) {
	        // 18.5 < bmi < 25.0 : normal weight
	        return  new Color(82, 181, 82); // a nice dark_green;
	    } else if (bmi < 30.0f) {
	        // 25.0 < bmi < 30.0 : overweight
	        return Color.YELLOW;
	    } else {
	        // bmi > 30.0 : critical overweight
	        return Color.RED;
	    }
	}
}
