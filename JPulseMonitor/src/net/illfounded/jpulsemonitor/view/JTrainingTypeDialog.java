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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingTypeDO;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * JTrainingTypeDialog makes it easy to pop up a create/modify dialog box that prompts users
 * for information of a training-type. Almost all uses of this class are one-line calls to one of the static
 * showMultiQualiDialog methods.
 */
public class JTrainingTypeDialog extends JAdminDialog {
	private static JTrainingTypeDialog _dialog;
	private static TrainingTypeDO _trainingTypeDO;
	private JTextField _tFieldIntensity;
	private JTextField _tFieldPriority;
	private JTextField _tFieldTime;
	private JTextArea _tAreaText;
	private static JPulsemonitor _monitor;

	/**
	 * Creates a new JTrainingDialog.
	 */
	protected JTrainingTypeDialog(JPulsemonitor monitor) {
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
        
        _tFieldIntensity = new JTextField();
        _tFieldTime = new JTextField();
        _tFieldPriority = new JTextField();

        _tAreaText = new JTextArea(3, 20);
        _tAreaText.setLineWrap(true);
        _tAreaText.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(_tAreaText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Moving the Focus with the TAB Key instead of inserting a TAB character
        _tAreaText.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        _tAreaText.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

        JLabel lp = new JLabel(_bndl.getString("label.priority") +" :", JLabel.TRAILING);
        centerPanel.add(lp);
        lp.setLabelFor(_tFieldPriority);
        centerPanel.add(_tFieldPriority);

        JLabel li = new JLabel(_bndl.getString("label.intensity") +" :", JLabel.TRAILING);
        centerPanel.add(li);
        li.setLabelFor(_tFieldIntensity);
        centerPanel.add(_tFieldIntensity);

        JLabel lt = new JLabel(_bndl.getString("label.time") +" :", JLabel.TRAILING);
        centerPanel.add(lt);
        lt.setLabelFor(_tFieldTime);
        centerPanel.add(_tFieldTime);
        
        JLabel ltx = new JLabel(_bndl.getString("label.text") +" :", JLabel.TRAILING);
        centerPanel.add(ltx);
        ltx.setLabelFor(_tAreaText);
        centerPanel.add(scrollPane);

        // Lay out the panel.
        SpringUtilities.makeCompactGrid(centerPanel, 4, 2, //rows, cols
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
	 * a new training-type and the options Ok and Cancel.
	 * 
	 * @param run To CourseRun to add the exam to.
	 */
	public static TrainingTypeDO showTrainingTypeDialog(JPulsemonitor monitor) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JTrainingTypeDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.trainingtype.new"));
		_dialog.reset();
		_dialog.setVisible(true);
		return _trainingTypeDO;
	}
	
	/**
	 * Brings up an internal dialog panel with all fields filled with
	 * the given information and the options Ok and Cancel.
	 * 
	 * @param run To CourseRun to add the exam to.
	 * @param quali To Exam that will be modified.
	 */
	public static TrainingTypeDO showTrainingTypeDialog(JPulsemonitor monitor, TrainingTypeDO training) {
		if(_dialog == null) {
			_monitor = monitor;
			_dialog = new JTrainingTypeDialog(monitor);
		}
		_dialog.setTitle(_bndl.getString("dialog.trainingtype.edit"));
		_dialog.setData(training);
		_dialog.setVisible(true);
		return _trainingTypeDO;
	}
	
	/**
	 * Stores the entered data in the internal dataobject.
	 */
	protected void storeData() {
		if (_trainingTypeDO == null) {
		    _trainingTypeDO = new TrainingTypeDO(_monitor.getAdminFileHandler().createTrainingTypeIdentification());
		} 
		_trainingTypeDO.setIntensity(_tFieldIntensity.getText());
		_trainingTypeDO.setText(_tAreaText.getText());
		_trainingTypeDO.setPriority(_tFieldPriority.getText());
	    _trainingTypeDO.setTime(_tFieldTime.getText());
	}
	
	/**
	 * Set some usable data.
	 */ 
    private void setData(TrainingTypeDO training) {
        _trainingTypeDO = training;
        _tFieldIntensity.setText(_trainingTypeDO.getIntensity());
        _tAreaText.setText(_trainingTypeDO.getText());
        _tFieldPriority.setText(_trainingTypeDO.getPriority());
        _tFieldTime.setText(_trainingTypeDO.getTime());
    }

    /**
     * Resets the component, means sets its fields to the initial values.
     */
    public void reset() {
        _trainingTypeDO = null;
        _tFieldIntensity.setText("");
        _tAreaText.setText("");
        _tFieldPriority.setText("");
        _tFieldTime.setText("");
    }

}
