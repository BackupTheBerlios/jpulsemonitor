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
 * A dataobject representing a training, a training is a type of workout that can take place.
 * A performed training (containg a date) is called a session. 
 */
public class TrainingDO {
    private String _id;
    private Float _duration;
    private Float _distance;
    private String _trainingType;
    private boolean _active;
    private String _limits;
    private String _text;

    /**
     * Creates a new TrainingDO.
     */
    public TrainingDO(String identification) {
        _id = identification;
        _text = " ";
        _limits = " ";
        _duration = new Float(Float.NaN);
        _distance = new Float(Float.NaN);
    }

    /**
     * @return Returns the duration.
     */
    public Float getDuration() {
        if (_duration.isNaN()) {
            return null;
        }
        return _duration;
    }
    
    /**
     * @param duration The duration to set.
     */
    public void setDuration(Float duration) {
        _duration = duration;
    }
    
    /**
     * Settermethod for duration. The method attemps to parse the given String.
     * If parsing fails the value is set to NaN.
     * 
     * @param duration The duration to set.
     */
    public void setDuration(String duration) {
        try {
            _duration = new Float(duration);
        } catch (Exception e) {
            _duration = new Float(Float.NaN);
        }
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
     * @return Returns whether the training is active or not.
     */
    public boolean isActive() {
        return _active;
    }
    
    /**
     * @return Returns whether the training is active or not.
     * The return value is a String representation; 'yes' or 'no'.
     */
    public String getActive() {
        if (_active) {
            return "yes";
        }
        return "no";
    }
    
    /**
     * @param toughness Sets whether the training is active or not.
     */
    public void setActive(boolean active) {
        _active = active;
    }
    
    /**
     * Sets whether the training is active or not. The method attemps to parse the given String.
     * If parsing fails the value is set to false.
     * 
     * @param distance The distance to set.
     */
    public void setActive(String active) {
        if (active.equalsIgnoreCase("yes") || active.equalsIgnoreCase("true")) {
            _active = true;
        } else {
            _active = false;
        }
    }
    
    /**
     * Settermethod for distance. The method attemps to parse the given String.
     * If parsing fails the value is set to NaN.
     * 
     * @param distance The distance to set.
     */
    public void setDistance(String distance) {
        try {
            _distance = new Float(distance);
        } catch (Exception e) {
            _distance = new Float(Float.NaN);
        }
    }
   
    /**
     * @param distance The distance to set.
     */
    public void setDistance(Float distance) {
        _distance = distance;
    }
    
    /**
     * @return Returns the distance.
     */
    public Float getDistance() {
        if (_distance.isNaN()) {
            return null;
        }
        return _distance;
    }
    
    /**
     * @return Returns the training-type.
     */
    public String getTrainingType() {
        return _trainingType;
    }
    
    /**
     * @param type The training-type to set.
     */
    public void setTrainingType(String trainingType) {
        _trainingType = trainingType;
    }
  
    /**
     * Returns a string representation of the dataobject.
     * 
     * @return a string representation of the object
     */
    public String toString() {
        return getText().trim();
    }

    /**
     * @return Returns the limits.
     */
    public String getLimits() {
        return _limits;
    }
    
    /**
     * @param limits The limits to set.
     */
    public void setLimits(String limits) {
        _limits = limits;
    }
    
}
