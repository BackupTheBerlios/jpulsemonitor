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
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.xml.XMLAdminFileHandler;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * JExerciseDialog makes it easy to pop up a create/modify dialog box that prompts users
 * for some basic settings.
 */
public class JPreferenceDialog extends JAdminDialog {
    // Eclipse generated serialVersionUID
	private static final long serialVersionUID = -3260884247916333210L;

	private static JPreferenceDialog _dialog;
	private JFormattedTextField _tFieldLimitMax;
	private JFormattedTextField _tFieldLimitMin;
	private JFormattedTextField _tFieldVisibleMonths;
	private JUserChooser _userChooser;
	private JCheckBox _chBoxLookfeel;
	private JCheckBox _chBoxGradient;
	private boolean _custFChanged;
	private JList _custFields;
	private DefaultListModel _model;
	private XMLAdminFileHandler _adminF;
	
	/**
	 * Creates a new JExerciseDialog.
	 */
	protected JPreferenceDialog(JPulsemonitor monitor) {
		super(monitor);
	}
	
    /**
     * Initialize all the GUI suff.
     */
    protected void initGUI() {
    	_adminF = ResourceLoader.loadAdminXML();
    	
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new SpringLayout());
        JPanel southPanel = new JPanel(new FlowLayout());
        
        centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        		
        _tFieldLimitMax = new JFormattedTextField(new Integer(ResourceLoader.getDefaultValue("limit.max")));
        _tFieldLimitMin = new JFormattedTextField(new Integer(ResourceLoader.getDefaultValue("limit.min")));
        _tFieldVisibleMonths = new JFormattedTextField(new Integer(ResourceLoader.getDefaultValue("nbr.visible.months")));
        
        _chBoxLookfeel = new JCheckBox();
        if (ResourceLoader.getSetting("app.lookfeel.on")) {
        	_chBoxLookfeel.setSelected(true);
        }

    	_chBoxGradient = new JCheckBox();
    	if (ResourceLoader.getSetting("app.gradient.on")) {
    		_chBoxGradient.setSelected(true);
    	}

    	_userChooser = new JUserChooser(_adminF.getAllUsersVector(), _bndl.getString("defaultuser.tooltip"));
    	_userChooser.setSelectedUser(_adminF.getDefaultUser());
    	
        JLabel lma = new JLabel(_bndl.getString("label.pref.limit.max") +" :", JLabel.TRAILING);
        centerPanel.add(lma);
        lma.setLabelFor(_tFieldLimitMax);
        centerPanel.add(_tFieldLimitMax);

        JLabel lmi = new JLabel(_bndl.getString("label.pref.limit.min") +" :", JLabel.TRAILING);
        centerPanel.add(lmi);
        lmi.setLabelFor(_tFieldLimitMin);
        centerPanel.add(_tFieldLimitMin);

        JLabel lmo = new JLabel(_bndl.getString("label.pref.months") +" :", JLabel.TRAILING);
        centerPanel.add(lmo);
        lmo.setLabelFor(_tFieldVisibleMonths);
        centerPanel.add(_tFieldVisibleMonths);
        
        JLabel lk = new JLabel(_bndl.getString("label.pref.Lookfeel") +" :", JLabel.TRAILING);
        centerPanel.add(lk);
        lk.setLabelFor(_chBoxLookfeel);
        centerPanel.add(_chBoxLookfeel);
        
        JLabel lh = new JLabel(_bndl.getString("label.pref.Gradient") +" :", JLabel.TRAILING);
        centerPanel.add(lh);
        lh.setLabelFor(_chBoxGradient);
        centerPanel.add(_chBoxGradient);
        
        JLabel lu = new JLabel(_bndl.getString("defaultuser") +" :", JLabel.TRAILING);
        centerPanel.add(lu);
        lu.setLabelFor(_userChooser);
        centerPanel.add(_userChooser);

        _model = new DefaultListModel();
        String[] fieldTypes = _adminF.getCustomFieldTypes();
        
        for (int i=0; i < fieldTypes.length; i++) {
        	_model.addElement(fieldTypes[i]);
        }

        _custFields = new JList(_model);
        _custFields.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        JLabel lc = new JLabel(_bndl.getString("label.pref.custom") +" :", JLabel.TRAILING);
        centerPanel.add(lc);
        lc.setLabelFor(_custFields);
        // custFields.setMinimumSize(new Dimension(50, 50));
        // custFields.setPreferredSize(new Dimension(50, 50));
        centerPanel.add(new JScrollPane(_custFields));
        
        buildPopupMenu();
        
        // Lay out the panel.
        SpringUtilities.makeCompactGrid(centerPanel, 7, 2, //rows, cols
                6, 6, //initX, initY
                6, 6); //xPad, yPad
        
        JButton ok = new JButton(_bndl.getString("btn.ok"));
		JButton canc = new JButton(_bndl.getString("btn.cancel"));
		
		ok.addActionListener(new OkAction(_dialog));
		canc.addActionListener(new ExitAction());
		 
        southPanel.add(ok);
        southPanel.add(canc);
        
        cont.add(centerPanel, BorderLayout.CENTER);
        cont.add(southPanel, BorderLayout.SOUTH);
    }
	
	/**
	 * Brings up an internal dialog panel with all fields to create
	 * a new exam and the options Ok and Cancel.
	 */
	public static void showDialog(JPulsemonitor monitor) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JPreferenceDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.pref"));
		_dialog.reset();
		_dialog.setVisible(true);
		return;
	}
	
	/**
	 * Stores the entered data in the internal dataobject.
	 */
	protected void storeData() throws ParseException {
		int min;
		int max;
		
		_tFieldLimitMax.commitEdit();
		if (_tFieldLimitMax.getValue() != null) {
			max = ((Integer)_tFieldLimitMax.getValue()).intValue();
		} else {
			max = ResourceLoader.LIMIT_MAX;
		}
		
        _tFieldLimitMin.commitEdit();
		if (_tFieldLimitMin.getValue() != null) {
			min = ((Integer)_tFieldLimitMin.getValue()).intValue();
		} else {
			min = ResourceLoader.LIMIT_MIN;
		}
		
		// Check if min limit is really smaller...
		if (min >= max) {
			throw new ParseException("Min limit must not be smaller or equal than max limits", max - min);
		}
		
		_adminF.setConfiguration("limit.max", max);
		_adminF.setConfiguration("limit.min", min);
		
		_tFieldVisibleMonths.commitEdit();
		if (_tFieldVisibleMonths.getValue() != null) {
			_adminF.setConfiguration("nbr.visible.months", ((Integer)_tFieldVisibleMonths.getValue()).intValue());
		} else {
			_adminF.setConfiguration("nbr.visible.months", ResourceLoader.SHOW_LAST_MONTHS);
		}
		
		_adminF.setConfiguration("app.lookfeel.on", _chBoxLookfeel.isSelected());
		_adminF.setConfiguration("app.gradient.on", _chBoxGradient.isSelected());
		
		_adminF.setDefaultUser(_userChooser.getSelectedUser());
		
		if (_custFChanged) {
			int length = _model.getSize();
			String custFields[] = new String[length];
			
			for (int i=0; i<length; i++) {
				custFields[i] = (String) _model.getElementAt(i);
			}
			
			_adminF.setCustomFieldTypes(custFields);
			
			/**
			 * TODO Better think of someting else, this is not very nice.
			 */
			JExerciseDialog.destroy();
		}
		
		// Update Look and Feel and all Comboboxes...
        _monitor.changeLookAndFeel();
		_monitor.getMainMediator().setAdminUpdated(true);
	}

    /**
     * Resets the component, means sets its fields to the initial values.
     */
    public void reset() {
    	_tFieldLimitMax.setValue(new Integer(ResourceLoader.getDefaultValue("limit.max")));
        _tFieldLimitMin.setValue(new Integer(ResourceLoader.getDefaultValue("limit.min")));
        _tFieldVisibleMonths.setValue(new Integer(ResourceLoader.getDefaultValue("nbr.visible.months")));
        
        if (ResourceLoader.getSetting("app.lookfeel.on")) {
        	_chBoxLookfeel.setSelected(true);
        }

        if (ResourceLoader.getSetting("app.gradient.on")) {
    		_chBoxGradient.setSelected(true);
    	}
        
        _userChooser.setUsers(_adminF.getAllUsersVector());
    	_userChooser.setSelectedUser(_adminF.getDefaultUser());
    	
    	_model.removeAllElements();
        String[] fieldTypes = _adminF.getCustomFieldTypes();
        
        for (int i=0; i < fieldTypes.length; i++) {
        	_model.addElement(fieldTypes[i]);
        }
    }
    
    private void buildPopupMenu() {
    	final JPopupMenu menu = new JPopupMenu();
    	
        // Create and add a menu item
        JMenuItem addItem = new JMenuItem(_bndl.getString("label.pref.add"));
        JMenuItem deleteItem = new JMenuItem(_bndl.getString("label.pref.delete"));
        
        addItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				 String inputValue = JOptionPane.showInputDialog(_bndl.getString("label.pref.fieldname"));
				 if (inputValue == null || inputValue.equals("")) {
				 	return;
				 }
				 _model.addElement(inputValue);
				 _custFChanged = true;
			}
        	
        });

        deleteItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int retV = JOptionPane.showConfirmDialog(_custFields, _bndl.getString("label.pref.deletewarning"), _bndl.getString("dlg.delete.title"), JOptionPane.YES_NO_OPTION);

        		if (retV == JOptionPane.YES_OPTION) {
        			_model.removeElement(_custFields.getSelectedValue());
        			_custFChanged = true;
        		}
			}
        });

        menu.add(addItem);
        menu.add(deleteItem);
        
        // Set the component to show the popup menu
        _custFields.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
    }

}
