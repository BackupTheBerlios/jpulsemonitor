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

import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A GUI component holding two spinners to choose a value range.
 * E.g a minimal and a maximal value for a limit. Thus the minimal value
 * must be smaller than the maximal value.
 */
public class JMinMaxPanel extends JPanel {
    private JSpinner _minSp;
    private JSpinner _maxSp;
    
    /**
     * Empty default constructor, necessary to keep the class serializable viz beeing a Bean.
     * Setting minimal value to 0, maximal value to 1.
     */
    public JMinMaxPanel() {
        super();
        _minSp = new JSpinner();
        _minSp.setValue(new Integer(0));
        _maxSp = new JSpinner();
        _maxSp.setValue(new Integer(1));
        initGUI();
    }
    
    /**
     * Constructs a new JMinMaxPanel. Setting minimal- and maximal-value to the given values.
     */
    public JMinMaxPanel(int min, int max) {
        super();
        _minSp = new JSpinner();
        _minSp.setValue(new Integer(min));
        _maxSp = new JSpinner();
        _maxSp.setValue(new Integer(max));
        initGUI();
    }
    
    /**
     * Initialize all the GUI suff.
     */
    protected  void initGUI() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        add(_minSp);
        add(new JLabel(" / "));
        add(_maxSp);
    }

    /**
     * @return Returns the maximal value.
     */
    public int getMax() {
        return ((Integer)_maxSp.getValue()).intValue();
    }
    
    /**
     * @param max The maximal value to set.
     */
    public void setMax(int max) throws Exception {
        if (max < getMin()) {
            throw new Exception("Maximal value can not be smaller than minimal value");
        }
        
        _maxSp.setValue(new Integer(max));
    }
    
    /**
     * @return Returns the minimal value.
     */
    public int getMin() {
        return ((Integer)_minSp.getValue()).intValue();
    }
    
    /**
     * @param min The minimal value to set.
     */
    public void setMin(int min) throws Exception {
        if (min > getMax()) {
            throw new Exception("Minimal value can not be bigger than maximal value");
        }
        
        _minSp.setValue(new Integer(min));
    }
    
    /**
     * @param min The minimal value to set.
     * @param max The maximal value to set.
     */
    public void setValues(int min, int max) throws Exception {
        if (min > max) {
            throw new Exception("Minimal value can not be bigger than maximal value");
        }
        
        if (max < min) {
            throw new Exception("Maximal value can not be smaller than minimal value");
        }

        _minSp.setValue(new Integer(min));
        _maxSp.setValue(new Integer(max));
    }
    
    /**
     * @param values The values to set. The given <code>String</code> will be parsed, something
     * like 1-2 or 1/2 is expected.
     */
    public void setValues(String values) throws Exception {
        int min, max;
        StringTokenizer tok = new StringTokenizer(values, "/-");
        
        if (tok.countTokens() != 2) {
            throw new Exception("Two values expected!");
        }
        
        min = Integer.parseInt(tok.nextToken());
        max = Integer.parseInt(tok.nextToken());
        // setValues(min, max);
        _minSp.setValue(new Integer(min));
        _maxSp.setValue(new Integer(max));
    }
    
    /**
     * @return Returns the values wrapped in a <code>String</code>.
     */
    public String getValues() {
        return _minSp.getValue() +"/" +_maxSp.getValue();
    }
    
}
