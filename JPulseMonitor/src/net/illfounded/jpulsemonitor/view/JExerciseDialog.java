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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.xml.dataobject.ExerciseDO;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingDO;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * JExerciseDialog makes it easy to pop up a create/modify dialog box that prompts users
 * for information of an exercise. Almost all uses of this class are one-line calls to one of the static
 * showMultiQualiDialog methods.
 */
public class JExerciseDialog extends JAdminDialog {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = -2878495371041676351L;

	private static JExerciseDialog _dialog;
	private static ExerciseDO _exerciseDO;
	private JFormattedTextField _tFieldHeartrate;
	private JFormattedTextField _tFieldKcal;
	private JFormattedTextField _tFieldWeight;
	private JTextArea _tAreaDescription;
	private JComboBox _cBoxTraining;
	private DateField _cBoxDate;
	private JFormattedTextField _tFieldInZone;
	private JFormattedTextField _tFieldTime;
	private HashMap<String, JTextField> _custFields;
	
	/**
	 * Creates a new JExerciseDialog.
	 */
	protected JExerciseDialog(JPulsemonitor monitor) {
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
        // "Identification", "Date", "Training Id", "Weight", "Kcal", "Average HR", "Limits", "Time in zone", "Exercise time", "Description" +
        		
        _cBoxDate = CalendarFactory.createDateField();
        _cBoxTraining = new JComboBox(_monitor.getAdminFileHandler().getAllTrainingsVector());

        NumberFormat format2Decimal = DecimalFormat.getInstance();
        format2Decimal.setMaximumFractionDigits(2);
        format2Decimal.setMinimumFractionDigits(2);
        _tFieldWeight = new JFormattedTextField(format2Decimal);
        _tFieldInZone = new JFormattedTextField(format2Decimal);
        _tFieldTime = new JFormattedTextField(format2Decimal);
        _tFieldTime.setValue(new Float(60.00f));
        
        NumberFormat format0Decimal = DecimalFormat.getInstance();
        format0Decimal.setMaximumFractionDigits(0);
        format0Decimal.setMinimumFractionDigits(0);
        _tFieldKcal = new JFormattedTextField(format0Decimal);
        _tFieldHeartrate= new JFormattedTextField(format0Decimal);
        _tFieldHeartrate.setValue(new Integer(155));

        _tAreaDescription = new JTextArea(3, 20);
        _tAreaDescription.setLineWrap(true);
        _tAreaDescription.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(_tAreaDescription, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Moving the Focus with the TAB Key instead of inserting a TAB character
        _tAreaDescription.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        _tAreaDescription.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

        JLabel ld = new JLabel(_bndl.getString("label.date") +" :", JLabel.TRAILING);
        centerPanel.add(ld);
        ld.setLabelFor(_cBoxDate);
        centerPanel.add(_cBoxDate);

        JLabel ltr = new JLabel(_bndl.getString("label.training") +" :", JLabel.TRAILING);
        centerPanel.add(ltr);
        ltr.setLabelFor(_cBoxTraining);
        centerPanel.add(_cBoxTraining);

        JLabel lw = new JLabel(_bndl.getString("label.weight") +" :", JLabel.TRAILING);
        centerPanel.add(lw);
        lw.setLabelFor(_tFieldWeight);
        centerPanel.add(_tFieldWeight);
        
        JLabel lk = new JLabel(_bndl.getString("label.kcal") +" :", JLabel.TRAILING);
        centerPanel.add(lk);
        lk.setLabelFor(_tFieldKcal);
        centerPanel.add(_tFieldKcal);
        
        JLabel lh = new JLabel(_bndl.getString("label.heartrate") +" :", JLabel.TRAILING);
        centerPanel.add(lh);
        lh.setLabelFor(_tFieldHeartrate);
        centerPanel.add(_tFieldHeartrate);
        
        JLabel liz = new JLabel(_bndl.getString("label.inzone") +" :", JLabel.TRAILING);
        centerPanel.add(liz);
        liz.setLabelFor(_tFieldInZone);
        centerPanel.add(_tFieldInZone);
        
        JLabel let = new JLabel(_bndl.getString("label.exercisetime") +" :", JLabel.TRAILING);
        centerPanel.add(let);
        let.setLabelFor(_tFieldTime);
        centerPanel.add(_tFieldTime);
        
        JLabel ldesc = new JLabel(_bndl.getString("label.description") +" :", JLabel.TRAILING);
        centerPanel.add(ldesc);
        ldesc.setLabelFor(_tAreaDescription);
        centerPanel.add(scrollPane);

        // Add custom fields...
        String custFields[] = ResourceLoader.loadAdminXML().getCustomFieldTypes();
        int nbrOfCF = custFields.length;
        
        if (nbrOfCF > 0) {
        	_custFields = new HashMap<String, JTextField> ();
        
        	for (int i=0; i<nbrOfCF; i++) {
        		JLabel desc = new JLabel(custFields[i] +" :", JLabel.TRAILING);
        		centerPanel.add(desc);
        		JTextField text = new JTextField();
        		desc.setLabelFor(text);
        		centerPanel.add(text);
        	
        		_custFields.put(custFields[i], text);
        	}
        }
        
        // Lay out the panel.
        SpringUtilities.makeCompactGrid(centerPanel, 8 + nbrOfCF, 2, //rows, cols
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
	public static ExerciseDO showExerciseDialog(JPulsemonitor monitor) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JExerciseDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.training.new"));
		_dialog.reset();
		_dialog.setVisible(true);
		return _exerciseDO;
	}
	
	/**
	 * Brings up an internal dialog panel with all fields filled with
	 * the given information and the options Ok and Cancel.
	 * 
	 * @param run To CourseRun to add the exam to.
	 * @param quali To Exam that will be modified.
	 */
	public static ExerciseDO showExerciseDialog(JPulsemonitor monitor, ExerciseDO training) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JExerciseDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.training.edit"));
		_dialog.setData(training);
		_dialog.setVisible(true);
		return _exerciseDO;
	}
	
	/**
	 * Stores the entered data in the internal dataobject.
	 *
	 */
	protected void storeData() throws ParseException {
		if (_exerciseDO == null) {
			_exerciseDO = new ExerciseDO(_monitor.getExerciseFileHandler().createExerciseIdentification());
		} 
		_exerciseDO.setDate((Date)_cBoxDate.getValue());
		_exerciseDO.setText(_tAreaDescription.getText());
		_exerciseDO.setTrainingId(((TrainingDO)_cBoxTraining.getSelectedItem()).getIdentification());
		
		_tFieldHeartrate.commitEdit();
		if (_tFieldHeartrate.getValue() != null) {
		    _exerciseDO.setAverageHR(_tFieldHeartrate.getValue().toString());
		} else {
		    _exerciseDO.setAverageHR(new Integer(0));
		}
		
		try {
            _tFieldKcal.commitEdit();
            if (_tFieldKcal.getValue() != null) {
                _exerciseDO.setKcal(_tFieldKcal.getValue().toString());
            } else {
                _exerciseDO.setKcal(new Integer(0));
            }
        } catch (ParseException e) {
            // May show an info dialog
            _exerciseDO.setKcal(new Integer(0));
        }
		
		_tFieldTime.commitEdit();
		if (_tFieldTime.getValue() != null) {
		    _exerciseDO.setExerciseTime(_tFieldTime.getValue().toString());
		} else {
		    _exerciseDO.setExerciseTime(new Float(Float.NaN));
		}
		
		try {
            _tFieldInZone.commitEdit();
            if (_tFieldInZone.getValue() != null) {
                _exerciseDO.setTimeInZone(_tFieldInZone.getValue().toString());
            } else {
                _exerciseDO.setTimeInZone(new Float(Float.NaN));
            }
        } catch (ParseException e) {
            // May show an info dialog
            _exerciseDO.setTimeInZone(new Float(Float.NaN));
        }
		
		try {
		    _tFieldWeight.commitEdit();
		    if (_tFieldWeight.getValue() != null) {
		        _exerciseDO.setWeight(_tFieldWeight.getValue().toString());
		    } else {
		        _exerciseDO.setWeight(new Float(Float.NaN));
		    }
		} catch (ParseException e) {
		    // May show an info dialog
		    _exerciseDO.setWeight(new Float(Float.NaN));
		}
		
		// Fill custom fields
		Iterator it = _custFields.keySet().iterator();
		String custField;
		String value;
		JTextField text;
		while (it.hasNext()) {
			custField = (String) it.next();
			text = _custFields.get(custField);
			value = text.getText();
			_exerciseDO.setCustomField(custField, value);
		}
	}
	
	/**
	 * Set some usable data.
	 */
	private void setData(ExerciseDO exercise) {
		_exerciseDO = exercise;

		_cBoxDate.setValue(exercise.getDate());

		_tFieldWeight.setValue(exercise.getWeight());
		_tFieldKcal.setValue(exercise.getKcal());
		_tFieldHeartrate.setValue(exercise.getAverageHR());
		_tFieldInZone.setValue(exercise.getTimeInZone());
		_tFieldTime.setValue(exercise.getExerciseTime());
		_tAreaDescription.setText(exercise.getText());
		String trainingID = exercise.getTrainingId();

		// This is an edit dialog, show all available trainings...
		_cBoxTraining.removeAllItems();
		Iterator it = _monitor.getAdminFileHandler().getAllTrainingsVector()
				.iterator();
		TrainingDO training;
		String currentId;
		TrainingDO selected = null;
		while (it.hasNext()) {
			training = (TrainingDO) it.next();
			_cBoxTraining.addItem(training);

			currentId = training.getIdentification();
			if (trainingID.equals(currentId)) {
				selected = training;
			}
		}
		_cBoxTraining.setSelectedItem(selected);
		
		// Fill custom fields
		it = _custFields.keySet().iterator();
		String custField;
		String value;
		JTextField text;
		while (it.hasNext()) {
			custField = (String) it.next();
			value = exercise.getCustomField(custField);
			text = _custFields.get(custField);
			
			text.setText(value);
		}
	}

    /**
	 * Resets the component, means sets its fields to the initial values.
	 */
    public void reset() {
        _exerciseDO = null;
        _cBoxDate.setValue(new Date());
         
        _tFieldWeight.setValue(null);
        _tFieldKcal.setValue(null);
        _tFieldHeartrate.setValue(new Integer(155));
        _tFieldInZone.setValue(null);
        _tFieldTime.setValue(new Float(60.00f));
        _tAreaDescription.setText("");
        
        _cBoxTraining.removeAllItems();

        // This is a new dialog, do only show active exercises...
        Iterator it = _monitor.getAdminFileHandler().getAllTrainingsVector(true).iterator();
        while(it.hasNext()) {
            _cBoxTraining.addItem(it.next());
        }
        
		// Clear custom fields
		it = _custFields.keySet().iterator();
		String custField;
		JTextField text;
		while (it.hasNext()) {
			custField = (String) it.next();
			text = _custFields.get(custField);
			
			text.setText("");
		}
    }

    /**
     * Can be called in case a completely new initialisaiton is needed. 
     * Better think of someting else, this is not a very nice solution.
     */
    public static void destroy() {
    	if (_dialog == null) {
    		return;
    	}
    	_dialog.dispose();
    	_dialog = null;
    	
    }
    
}
