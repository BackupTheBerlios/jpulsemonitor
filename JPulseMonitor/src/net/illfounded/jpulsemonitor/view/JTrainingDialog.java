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
import java.awt.KeyboardFocusManager;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingDO;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingTypeDO;



/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * JTrainingDialog makes it easy to pop up a create/modify dialog box that prompts users
 * for information of a training. Almost all uses of this class are one-line calls to one of the static
 * showMultiQualiDialog methods.
 */
public class JTrainingDialog extends JAdminDialog {
    private static JTrainingDialog _dialog;
	private static TrainingDO _trainingDO;
	private JFormattedTextField _tFieldDuration;
	private JFormattedTextField _tFieldDistance;
	private JComboBox _cBoxTType;
	private JTextArea _TAreaText;
	private JMinMaxPanel _tFieldLimits;
	private JCheckBox _jCactive;

	/**
	 * Creates a new JTrainingDialog.
	 */
	protected JTrainingDialog(JPulsemonitor monitor) {
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
        
        _cBoxTType = new JComboBox(_monitor.getAdminFileHandler().getAllTrainingTypesVector());
        _tFieldDuration = new JFormattedTextField(new Float(0));
        
        NumberFormat formatDuration = DecimalFormat.getInstance();
        formatDuration.setMaximumFractionDigits(2);
        formatDuration.setMinimumFractionDigits(2);
        _tFieldDuration = new JFormattedTextField(formatDuration);

        NumberFormat formatDistance = DecimalFormat.getInstance();
        formatDistance.setMaximumFractionDigits(4);
        formatDistance.setMinimumFractionDigits(2);
        _tFieldDistance = new JFormattedTextField(formatDistance);
        
        _tFieldLimits = new JMinMaxPanel(ResourceLoader.getDefaultValue("limit.min"), ResourceLoader.getDefaultValue("limit.max"));
        
        _jCactive = new JCheckBox();
        _jCactive.setSelected(true);

        _TAreaText = new JTextArea(3, 20);
        _TAreaText.setLineWrap(true);
        _TAreaText.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(_TAreaText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Moving the Focus with the TAB Key instead of inserting a TAB character
        _TAreaText.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        _TAreaText.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

        JLabel ltp = new JLabel(_bndl.getString("label.trainingType") +" :", JLabel.TRAILING);
        centerPanel.add(ltp);
        ltp.setLabelFor(_cBoxTType);
        centerPanel.add(_cBoxTType);

        JLabel ld = new JLabel(_bndl.getString("label.duration") +" :", JLabel.TRAILING);
        centerPanel.add(ld);
        ld.setLabelFor(_tFieldDuration);
        centerPanel.add(_tFieldDuration);

        JLabel ldist = new JLabel(_bndl.getString("label.distance") +" :", JLabel.TRAILING);
        centerPanel.add(ldist);
        ldist.setLabelFor(_tFieldDistance);
        centerPanel.add(_tFieldDistance);
        
        JLabel ll = new JLabel(_bndl.getString("label.limits") +" :", JLabel.TRAILING);
        centerPanel.add(ll);
        ll.setLabelFor(_tFieldLimits);
        centerPanel.add(_tFieldLimits);
        
        JLabel la = new JLabel(_bndl.getString("label.active") +" :", JLabel.TRAILING);
        centerPanel.add(la);
        la.setLabelFor(_jCactive);
        centerPanel.add(_jCactive);
        
        JLabel ltx = new JLabel(_bndl.getString("label.text") +" :", JLabel.TRAILING);
        centerPanel.add(ltx);
        ltx.setLabelFor(_TAreaText);
        centerPanel.add(scrollPane);

        // Lay out the panel.
        SpringUtilities.makeCompactGrid(centerPanel, 6, 2, //rows, cols
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
	public static TrainingDO showTrainingDialog(JPulsemonitor monitor) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JTrainingDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.training.new"));
		_dialog.reset();
		_dialog.setVisible(true);
		return _trainingDO;
	}
	
	/**
	 * Brings up an internal dialog panel with all fields filled with
	 * the given information and the options Ok and Cancel.
	 * 
	 * @param run To CourseRun to add the exam to.
	 * @param quali To Exam that will be modified.
	 */
	public static TrainingDO showTrainingDialog(JPulsemonitor monitor, TrainingDO training) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JTrainingDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.training.edit"));
		_dialog.setData(training);
		_dialog.setVisible(true);
		return _trainingDO;
	}
	
	/**
	 * Stores the entered data in the internal dataobject.
	 *
	 */
	protected void storeData() throws ParseException {
		if (_trainingDO == null) {
			_trainingDO = new TrainingDO(_monitor.getAdminFileHandler().createTrainingIdentification());
		}
		
		_tFieldDuration.commitEdit();
		if (_tFieldDuration.getValue() != null) {
		    _trainingDO.setDuration(_tFieldDuration.getValue().toString());
		} else {
		    _trainingDO.setDuration(new Float(Float.NaN));
		}
		
	    _trainingDO.setText(_TAreaText.getText());
	    
	    _tFieldDistance.commitEdit();
	    if (_tFieldDistance.getValue() != null) {
	        _trainingDO.setDistance(_tFieldDistance.getValue().toString());
	    } else {
	        _trainingDO.setDistance(new Float(Float.NaN));
	    }
	    
	    _trainingDO.setLimits(_tFieldLimits.getValues());
	    _trainingDO.setActive(_jCactive.isSelected());
	    _trainingDO.setTrainingType(((TrainingTypeDO)_cBoxTType.getSelectedItem()).getIdentification());
	}
	
	/**
	 * Set some usable data.
	 */ 
    private void setData(TrainingDO training) {
        _trainingDO = training;
        _tFieldDuration.setValue(_trainingDO.getDuration());
        _TAreaText.setText(_trainingDO.getText());
        _tFieldDistance.setValue(_trainingDO.getDistance());
        _jCactive.setSelected(training.isActive());
        
        try {
            _tFieldLimits.setValues(training.getLimits());
        } catch (Exception e) {
            try {
             _tFieldLimits.setValues(100, 200);
            } catch (Exception e1) {
                // Should not happen... do nothing...
            }
        }
        
        String typeId = _trainingDO.getTrainingType();
        for(int i=0; i < _cBoxTType.getItemCount(); i++) {
           String currentId = ((TrainingTypeDO)_cBoxTType.getItemAt(i)).getIdentification();
           if (typeId.equals(currentId)) {
               _cBoxTType.setSelectedIndex(i);
               break;
           }
        }
    }

    /**
     * Resets the component, means sets its fields to the initial values.
     */
    public void reset() {
        _trainingDO = null;
        _tFieldDuration.setValue(null);
        _TAreaText.setText("");
        _tFieldDistance.setValue(null);
        _jCactive.setSelected(true);
        
        try {
            _tFieldLimits.setValues(ResourceLoader.getDefaultValue("limit.min"), ResourceLoader.getDefaultValue("limit.max"));
        } catch (Exception e) {
            // Should not happen... do nothing...
        }
        
        _cBoxTType.removeAllItems();
        Iterator it = _monitor.getAdminFileHandler().getAllTrainingTypesVector().iterator();
        while(it.hasNext()) {
           _cBoxTType.addItem(it.next());
        }
    }

}
