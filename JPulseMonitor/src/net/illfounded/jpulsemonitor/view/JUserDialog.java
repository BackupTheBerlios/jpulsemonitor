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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.xml.dataobject.UserDO;



/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * JTrainingDialog makes it easy to pop up a create/modify dialog box that prompts users
 * for information of a user. Almost all uses of this class are one-line calls to one of the static
 * showMultiQualiDialog methods.
 */
public class JUserDialog extends JAdminDialog {
    // Eclipse generated serialVersionUID
	private static final long serialVersionUID = -1926860478305829569L;

	private static JUserDialog _dialog;
	private static UserDO _userDO;
	private JFormattedTextField _tFieldWeight;
	private JFormattedTextField _tFieldSize;
	private JFormattedTextField _tFieldYearOfBirth;
	private JTextField _tFieldName;
	private JComboBox _cbGender;

	/**
	 * Creates a new JTrainingDialog.
	 */
	protected JUserDialog(JPulsemonitor monitor) {
		super(monitor);
	}
	

    /**
     * Initialize all the GUI suff.
     */
    protected void initGUI() {
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new SpringLayout());
        JPanel southPanel = new JPanel(new FlowLayout());
        
        centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        NumberFormat formatWeight = DecimalFormat.getInstance();
        formatWeight.setMaximumFractionDigits(2);
        formatWeight.setMinimumFractionDigits(2);
        _tFieldWeight = new JFormattedTextField(formatWeight);
        _tFieldWeight.setValue(new Float(80.00f));

        _tFieldSize = new JFormattedTextField(formatWeight);
        _tFieldSize.setValue(new Float(180.00f));
        
		_tFieldYearOfBirth = new JFormattedTextField(new Integer(1980));
		
        _tFieldName = new JTextField(20);
        
        String items[] = {_bndl.getString("label.gender.male"), _bndl.getString("label.gender.female") };
        _cbGender = new JComboBox(items);

        JLabel lw = new JLabel(_bndl.getString("label.weight") +" :", JLabel.TRAILING);
        centerPanel.add(lw);
        lw.setLabelFor(_tFieldWeight);
        centerPanel.add(_tFieldWeight);
        
        JLabel ls = new JLabel(_bndl.getString("label.size") +" :", JLabel.TRAILING);
        centerPanel.add(ls);
        ls.setLabelFor(_tFieldSize);
        centerPanel.add(_tFieldSize);

        JLabel lb = new JLabel(_bndl.getString("label.yearofbirth") +" :", JLabel.TRAILING);
        centerPanel.add(lb);
        lb.setLabelFor(_tFieldYearOfBirth);
        centerPanel.add(_tFieldYearOfBirth);
        
        JLabel ln = new JLabel(_bndl.getString("label.name") +" :", JLabel.TRAILING);
        centerPanel.add(ln);
        ln.setLabelFor(_tFieldName);
        centerPanel.add(_tFieldName);

        JLabel lg = new JLabel(_bndl.getString("label.gender") +" :", JLabel.TRAILING);
        centerPanel.add(lg);
        lg.setLabelFor(_cbGender);
        centerPanel.add(_cbGender);
 
        // Lay out the panel.
        SpringUtilities.makeCompactGrid(centerPanel, 5, 2, //rows, cols
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
	 * 
	 * @param run To CourseRun to add the exam to.
	 */
	public static UserDO showUserDialog(JPulsemonitor monitor) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JUserDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.user.new"));
		_dialog.reset();
		_dialog.setVisible(true);
		return _userDO;
	}
	
	/**
	 * Brings up an internal dialog panel with all fields filled with
	 * the given information and the options Ok and Cancel.
	 * 
	 * @param run To CourseRun to add the exam to.
	 * @param quali To Exam that will be modified.
	 */
	public static UserDO showUserDialog(JPulsemonitor monitor, UserDO user) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JUserDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.user.edit"));
		_dialog.setData(user);
		_dialog.setVisible(true);
		return _userDO;
	}
	
	/**
	 * Stores the entered data in the internal dataobject.
	 *
	 */
	protected void storeData() throws ParseException {
		if (_userDO == null) {
			_userDO = new UserDO(_monitor.getAdminFileHandler().createUserIdentification());
		}
		
		_tFieldWeight.commitEdit();
		if (_tFieldWeight.getValue() != null) {
		    _userDO.setWeight(_tFieldWeight.getValue().toString());
		} else {
		    _userDO.setWeight(new Float(Float.NaN));
		}
		
		_tFieldSize.commitEdit();
		if (_tFieldSize.getValue() != null) {
		    _userDO.setSize(_tFieldSize.getValue().toString());
		} else {
		    _userDO.setSize(new Float(Float.NaN));
		}
		
		_userDO.setName(_tFieldName.getText());
	    
	    _tFieldYearOfBirth.commitEdit();
	    if (_tFieldYearOfBirth.getValue() != null) {
	        _userDO.setYearOfBirth(_tFieldYearOfBirth.getValue().toString());
	    } else {
	        _userDO.setYearOfBirth(new Integer(0));
	    }
	    
        if (_cbGender.getSelectedIndex() == 0) {
            _userDO.setGender("male");
        } else {
            _userDO.setGender("female");
        }
	}
	
	/**
	 * Set some usable data.
	 */ 
    private void setData(UserDO user) {
        _userDO = user;
        _tFieldWeight.setValue(_userDO.getWeight());
        _tFieldSize.setValue(_userDO.getSize());
        _tFieldYearOfBirth.setValue(_userDO.getYearOfBirth());
        _tFieldName.setText(_userDO.getName());
        
        if (_userDO.getGender().equalsIgnoreCase("male")) {
            _cbGender.setSelectedIndex(0);
        } else {
            _cbGender.setSelectedIndex(1);
        }
    }

    /**
     * Resets the component, means sets its fields to the initial values.
     */
    public void reset() {
        _userDO = null;
        _tFieldWeight.setValue(new Float(80.00f));
        _tFieldSize.setValue(new Float(180.00f));
        _tFieldYearOfBirth.setValue(new Integer(1980));
        _cbGender.setSelectedIndex(0);
    }

}
