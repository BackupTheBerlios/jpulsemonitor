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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.xml.XMLExerciseFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;
import net.illfounded.jpulsemonitor.xml.dataobject.ExerciseDO;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingDO;
import net.illfounded.jpulsemonitor.xml.dataobject.UserDO;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * A JPanel showing all active trainings in a list of JCheckBoxes.
 */
public class JTrainingChooser extends JPanel implements ItemListener {
    // Eclipse generated serialVersionUID
	private static final long serialVersionUID = -1164153318958918525L;

	private XMLResourceBundle _bndl;
    private Vector<JCheckBox> _checkBoxes;
    private JEvaluatePanel _diagram;
    private Logger _log;
    private JPanel _chkbPanel;
    private JPulsemonitor _monitor;
    private HashMap<String, TrainingDO> _trainings;
    private JButton _butRefresh;
    private JMinMaxPanel _panelMinMax;
    private JCheckBox _chkbMinMax;
    private JCheckBox _chkbWeight;
    private XMLExerciseFileHandler _exeF;

    /**
     * Creates a new treepanel object.
     */
    public JTrainingChooser(JPulsemonitor monitor, JEvaluatePanel diagram) {
        super(new BorderLayout());
        _monitor = monitor;
        _bndl = _monitor.getResourceBundle();
        _checkBoxes = new Vector<JCheckBox>();
        _diagram = diagram;
        _log = Logger.getLogger("net.illfounded.jpulsemonitor");
        _exeF =  _monitor.getExerciseFileHandler();

        Border border = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        _diagram.setBorder(border);

        JComponent south = buildSelector();
        JComponent center = buildCheckBoxPanel();
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    /**
     * Helpermethod to set up the button panel.
     */
    private JComponent buildSelector() {
        JPanel pnl = new JPanel(new FlowLayout());
        _butRefresh = new JButton(_bndl.getString("btn.refresh"));

        _butRefresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JAreaChartPanel chart = new JAreaChartPanel(_bndl.getString("chart.title"),
                        _bndl.getString("chart.date"), _bndl.getString("chart.value"));
                HashMap selectedTrainings = getSelectedTrainings();
                Iterator it = selectedTrainings.keySet().iterator();
                String key;
                
                while (it.hasNext()) {
                    key = (String ) it.next();
                    chart.addTimeSeries(key, (ChartValue[])selectedTrainings.get(key));
                }

                if (_chkbMinMax.isSelected()) {
                    chart.addMarker(_bndl.getString("limit.max"), _panelMinMax.getMax());
                    chart.addMarker(_bndl.getString("limit.min"), _panelMinMax.getMin());
                }
                
                if (_chkbWeight.isSelected()) {
                    chart.addTimeSeries(_bndl.getString("label.weight"), getWeightSeries());
                }
                _diagram.setChart(chart);
            }
        });

        pnl.add(_butRefresh);
        return pnl;
    }

    /**
     * Helpermethod to set up the checkboxes.
     */
    public void refreshCheckBoxes() {
        Collection trainings = _monitor.getAdminFileHandler().getAllTrainingsVector(true);

        _trainings.clear();

        _checkBoxes.removeAllElements();
        _chkbPanel.removeAll();
        
        Iterator it = trainings.iterator();
        JCheckBox chkb;
        TrainingDO training;

        while (it.hasNext()) {
            training = (TrainingDO) it.next();
            String name = training.getText();
            _trainings.put(name, training);

            chkb = new JCheckBox(name);
            _checkBoxes.add(chkb);
            _chkbPanel.add(chkb);
        }
        
        try {
        	ResourceLoader res = new ResourceLoader();
			_panelMinMax.setMax(ResourceLoader.getDefaultValue("limit.max"));
			_panelMinMax.setMin(ResourceLoader.getDefaultValue("limit.min"));
        } catch (Exception e) { /* Should not happen */ }
    }
    
    public void evalAll() {
        Iterator it = _checkBoxes.iterator();
        JCheckBox chkb;
        TrainingDO training;

        while (it.hasNext()) {
            chkb = (JCheckBox) it.next();
            chkb.setSelected(true);
        }

        _butRefresh.doClick();
    }
    
    /**
     * Helpermethod to set up the checkboxes.
     */
    private JComponent buildCheckBoxPanel() {
        JPanel main = new JPanel(new BorderLayout());
        _chkbPanel = new JPanel();
        JPanel panelLimit = new JPanel(new BorderLayout());
        ResourceLoader res = new ResourceLoader();
        _panelMinMax = new JMinMaxPanel(ResourceLoader.getDefaultValue("limit.min"), ResourceLoader.getDefaultValue("limit.max"));
        _chkbMinMax = new JCheckBox(_bndl.getString("label.showlimits"), false);
        _chkbWeight = new JCheckBox(_bndl.getString("label.showweight"));
                
        main.setBackground(Color.WHITE);

        Collection trainings = _monitor.getAdminFileHandler().getAllTrainingsVector(true);

        _trainings = new HashMap<String, TrainingDO>();

        _chkbPanel.setLayout(new GridLayout(_trainings.size(), 1, 5, 5));
        _chkbPanel.setBackground(Color.WHITE);

        Iterator it = trainings.iterator();
        JCheckBox chkb;
        TrainingDO training;

        while (it.hasNext()) {
            training = (TrainingDO) it.next();
            String name = training.getText();
            _trainings.put(name, training);

            chkb = new JCheckBox(name);
            _checkBoxes.add(chkb);
            _chkbPanel.add(chkb);
        }

        JScrollPane sPane = new JScrollPane(main);
        Border border = BorderFactory.createLoweredBevelBorder();
        sPane.setBorder(border);
        
        panelLimit.add(_chkbMinMax, BorderLayout.NORTH);
        panelLimit.add(_panelMinMax, BorderLayout.SOUTH);
        main.add(_chkbPanel, BorderLayout.NORTH);
        main.add(_chkbWeight, BorderLayout.CENTER);
        main.add(panelLimit, BorderLayout.SOUTH);
        return sPane;
    }

    private HashMap getSelectedTrainings() {
        HashMap<String, ChartValue[]> selectedTrainings = new HashMap<String, ChartValue[]>();
        Iterator it = _checkBoxes.iterator();
        JCheckBox chkb;
        TrainingDO training;
        
        while (it.hasNext()) {
            chkb = (JCheckBox) it.next();
            if (chkb.isSelected()) {
                training = (TrainingDO)_trainings.get(chkb.getText());
                selectedTrainings.put(chkb.getText(), getTrainingSeries(training.getIdentification()));
            }
        }
        return selectedTrainings;
    }
    
    /**
     * Helpermethod to load all the exercises having a weight and construct an arry of chart values. 
     */
    private ChartValue[] getWeightSeries() {
        Collection exercises = _monitor.getExerciseFileHandler().getAllExercisesVectorWeight(_diagram.getStart(), _diagram.getEnd());
        ExerciseDO exercise;
        ChartValue[] weights = new ChartValue[exercises.size()];
        Iterator it = exercises.iterator();
        int i = 0;
        
        while (it.hasNext()) {
            exercise = (ExerciseDO) it.next();
            if (exercise.getWeight() != null ) {
                weights[i] = new ChartValue(exercise.getDate(), exercise.getWeight().doubleValue());
                i++;
            }
        }
       
        return weights;
    }
    
    /**
     * Helpermethod to load all the exercises having a weight and construct an arry of chart values. 
     */
    private ChartValue[] getTrainingSeries(String trainingId) {
        Collection exercises = _exeF.getAllExercisesVectorByTraining(trainingId, _diagram.getStart(), _diagram.getEnd());
        ExerciseDO exercise;
        ChartValue[] trainings = new ChartValue[exercises.size()];
        Iterator it = exercises.iterator();
        int i = 0;
        
        while (it.hasNext()) {
            exercise = (ExerciseDO) it.next();
            if (exercise.getAverageHR() != null ) {
                trainings[i] = new ChartValue(exercise.getDate(), exercise.getAverageHR().doubleValue());
                i++;
            }
        }
       
        return trainings;
    }
    
    /**
     * If the user changed the current user, we will be notified.
     */
    public void itemStateChanged(ItemEvent e) {
    	if (e.getStateChange() == ItemEvent.SELECTED) {
    		String userId = ((UserDO)e.getItem()).getIdentification();
    		_exeF = _monitor.loadExerciseFileHandler(userId);
    	}
	}
    
}

