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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A dataobject representing an exersice, an exercise is one workout. Having a date and
 * all the important data like puls, weight and so on... 
 */
public class ExerciseDO {
    private String _id;
    private Float _weight;
    private Float _timeInZone;
    private Float _exerciseTime;
    private Integer _averageHR;
    private String _training_id;
    private Date _date;
    private String _text;
    private Integer _kcal;
    private HashMap<String, String> _custFields;

    /**
     * Creates a new ExerciseDO.
     */
    public ExerciseDO(String identification) {
        _id = identification;
        _text = " ";
        _training_id = " ";
        _timeInZone = new Float(Float.NaN);
        _exerciseTime = new Float(Float.NaN);
        _weight = new Float(Float.NaN);
    }

    /**
     * @return Returns the time in zone.
     */
    public Float getTimeInZone() {
        if (_timeInZone.isNaN()) {
            return null;
        }
        
        return _timeInZone;
    }
    
    /**
     * @param timeInZone The time in zone to set.
     */
    public void setTimeInZone(Float timeInZone) {
        if (timeInZone.floatValue() <= 0.0f){
            _timeInZone = new Float(Float.NaN);
        }
        _timeInZone = timeInZone;
    }
    
    /**
     * Settermethod for timeInZone. The method attemps to parse the given String.
     * If parsing fails the value is set to NaN.
     * 
     * @param type The priority to set.
     */
    public void setTimeInZone(String timeInZone) {
        try {
             setTimeInZone(new Float(timeInZone));
        } catch (Exception e) {
            _timeInZone = new Float(Float.NaN);
        }
    }
    
    /**
     * @return Returns the exercise time.
     */
    public Float getExerciseTime() {
        if (_exerciseTime.isNaN()) {
            return null;
        }
        
        return _exerciseTime;
    }
    
    /**
     * @param exerciseTime The exercise time to set.
     */
    public void setExerciseTime(Float exerciseTime) {
        _exerciseTime = exerciseTime;
    }
    
    /**
     * Settermethod for timeInZone. The method attemps to parse the given String.
     * If parsing fails the value is set to NaN.
     * 
     * @param type The priority to set.
     */
    public void setExerciseTime(String exerciseTime) {
        try {
             _exerciseTime = new Float(exerciseTime);
        } catch (Exception e) {
            _exerciseTime = new Float(Float.NaN);
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
     * @return Returns the weight.
     */
    public Float getWeight() {
        if (_weight.isNaN() || _weight.floatValue() <= 0.0) {
            return null;
        }

        return _weight;
    }
    
    /**
     * @param weight The weight to set.
     */
    public void setWeight(Float weight) {
        if (weight.floatValue() <= 0.0f) {
            _weight = new Float(Float.NaN);
        }
        _weight = weight;
    }
    
    /**
     * Settermethod for weight. The method attemps to parse the given String.
     * If parsing fails the value is set to NaN.
     * 
     * @param type The priority to set.
     */
    public void setWeight(String weight) {
        try {
             setWeight(new Float(weight));
        } catch (Exception e) {
            _weight = new Float(Float.NaN);
        }
    }
    
    /**
     * @return Returns the training.
     */
    public String getTrainingId() {
        return _training_id;
    }
    
    /**
     * @param type The training to set.
     */
    public void setTrainingId(String training_id) {
        _training_id = training_id;
    }
    
    /**
     * @return Returns the date.
     */
    public Date getDate() {
        return _date;
    }
    
	/**
	 * @return Returns a nicely formated the date.
	 */
	public String getFormatedDate() {
	    DateFormat formatter = new SimpleDateFormat("dd-MM-yy");
	    return formatter.format(_date);
	}
    
    /**
     * @param date The Date to set.
     */
    public void setDate(Date date) {
        _date = date;
    }
    
    /**
     * Settermethod for date. The method attemps to parse the given String.
     * If parsing fails the value is set to the actual date.
     * 
     * @param type The priority to set.
     */
    public void setDate(String date) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        
        try {
             _date = formatter.parse(date);
        } catch (Exception e) {
            _date = new Date();
        }
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
     * @return Returns the average heart rate.
     */
    public Integer getAverageHR() {
        return _averageHR;
    }
    
    /**
     * @param averageHR The average heart rate to set.
     */
    public void setAverageHR(Integer averageHR) {
        _averageHR = averageHR;
    }
    
    /**
     * Settermethod for averageHR. The method attemps to parse the given String.
     * If parsing fails the value is set to 0.
     * 
     * @param type The priority to set.
     */
    public void setAverageHR(String averageHR) {
        try {
             _averageHR = new Integer(averageHR);
        } catch (Exception e) {
            _averageHR = new Integer(0);
        }
    }
    
    /**
     * @return Returns the kcal.
     */
    public Integer getKcal() {
        if (_kcal.intValue() <= 0) {
            return null;
        }
        return _kcal;
    }
    
    /**
     * @param kcal The kcal to set.
     */
    public void setKcal(Integer kcal) {
        if (kcal.intValue() <= 0) {
            kcal = new Integer(0);
        }
        _kcal = kcal;
    }
    
    /**
     * Settermethod for kcal. The method attemps to parse the given String.
     * If parsing fails the value is set to 0.
     * 
     * @param type The priority to set.
     */
    public void setKcal(String kcal) {
        try {
             setKcal(new Integer(kcal));
        } catch (Exception e) {
            _kcal = new Integer(0);
        }
    }
    
    /**
     * Stores the data of a custom field.
     * 
     * @param fieldName - The name of the field.
     * @param value - The value to be stored.
     */
    public void addCustomField(String fieldName, String value) {
    	if (_custFields == null) {
    		_custFields = new HashMap<String, String>();
    	}
    	_custFields.put(fieldName, value);
    }
    
    /**
     * Returns the data stored in a custom field.
     * 
     * @param fieldName - The name of the field.
     */
    public String getCustomField(String fieldName) {  		
    	if (_custFields == null) {
    		return "";
    	}
    	String retV = _custFields.get(fieldName);
    	return retV == null ? "" : retV;
    }
    
}
