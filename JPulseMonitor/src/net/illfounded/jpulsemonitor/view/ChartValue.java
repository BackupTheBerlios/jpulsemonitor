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

import java.util.Date;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * Represents one point in a chart (key-value pair).
 */
public class ChartValue {
    private Date _key;
    private double _value;

    public ChartValue(Date key, double value) {
        _key = key;
        _value = value;
    }
    
    /**
     * Returns the key corresponding to this ChartValue.
     * 
     * @return Returns the key, may be used as label textt.
     */
    public Date getKey() {
        return _key;
    }

    /**
     * Returns the value corresponding to this ChartValue. 
     * 
     * @return Returns the value.
     */
    public double getValue() {
        return _value;
    }

    /**
     *  Replaces the value corresponding to this entry with the specified value. 
     * 
     * @param value - The new value.
     */
    public void setValue(double value) {
        _value = value;
    }

}
