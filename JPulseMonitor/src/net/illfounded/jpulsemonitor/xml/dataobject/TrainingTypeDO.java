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
package net.illfounded.jpulsemonitor.xml.dataobject;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A dataobject representing a training-type, a training-type is a certain type of training focued on
 * a specific goal, like improving the stamina or the power. 
 */
public class TrainingTypeDO {
    private String _id;
    private String _priority;
    private String _text;
    private String _intensity;
    private String _time;

    /**
     * Creates a new TrainingTypeDO.
     */
    public TrainingTypeDO(String identification) {
        _id = identification;
        _text = " ";
        _intensity = " ";
        _time = " ";
        _priority = " ";
    }

    /**
     * @return Returns the Identification.
     */
    public String getIdentification() {
        return _id;
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return _text;
    }
    
    /**
     * @param text The text to set.
     */
    public void setText(String text) {
        // Perform some checks... xml does not like null string set as a child
        if (text == null) {
            _text = "";
        } else {
            _text = text.trim();
        }
    }
    
    /**
     * @return Returns the priority.
     */
    public String getPriority() {
        return _priority;
    }
    
    /**
     * @param type The priority to set.
     */
    public void setPriority(String priority) {
        _priority = priority;
    }
   
    /**
     * Returns a string representation of the dataobject.
     * 
     * @return a string representation of the object
     */
    public String toString() {
        return _text.trim();
    }
    
    /**
     * @return Returns the intensity.
     */
    public String getIntensity() {
        return _intensity;
    }
    
    /**
     * @param intensity The intensity to set.
     */
    public void setIntensity(String intensity) {
        _intensity = intensity;
    }
    
    /**
     * @return Returns the time.
     */
    public String getTime() {
        return _time;
    }
    
    /**
     * @param time The time to set.
     */
    public void setTime(String time) {
        _time = time;
    }
    
}
