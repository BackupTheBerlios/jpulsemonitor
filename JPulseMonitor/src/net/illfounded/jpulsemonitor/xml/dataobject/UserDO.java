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
 * A dataobject representing a user, a user is a real person who wants to enter his of her training-data. 
 */
public class UserDO {
    private String _id;
    private Float _weight;
    private Float _size;
    private Integer _yearOfBirth;
    private String _gender;
    private String _name;
    
    /**
     * Creates a new UserDO.
     */
    public UserDO(String identification) {
        _id = identification;
        _name = " ";
        _gender = "male";
    }
    
    /**
     * @return Returns the gender.
     */
    public String getGender() {
        return _gender;
    }
    
    /**
     * @param gender The gender to set.
     */
    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("female")) {
            _gender = "female";
        } else if (gender.equalsIgnoreCase("f")) {
            _gender = "female";
        } else {
            _gender = "male";
        }
    }
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        // Perform some checks... xml does not like null string set as a child
        if (name == null) {
            _name = "";
        } else {
            _name = name.trim();
        }
    }
    
    /**
     * @return Returns the weight.
     */
    public Float getWeight() {
        if (_weight.isNaN()) {
            return null;
        }
        
        return _weight;
    }
    
    /**
     * @return Returns the size.
     */
    public Float getSize() {
        if (_size.isNaN()) {
            return null;
        }
        
        return _size;
    }
    
    /**
     * @param weight The weight to set.
     */
    public void setWeight(Float weight) {
        _weight = weight;
    }
    
    /**
     * @param size The size to set.
     */
    public void setSize(Float size) {
        _size = size;
    }
    
    /**
     * Settermethod for weight. The method attemps to parse the given String.
     * If parsing fails the value is set to NaN.
     * 
     * @param type The weight to set.
     */
    public void setWeight(String weight) {
        try {
             setWeight(new Float(weight));
        } catch (Exception e) {
            _weight = new Float(Float.NaN);
        }
    }
    
    /**
     * Settermethod for size. The method attemps to parse the given String.
     * If parsing fails the value is set to NaN.
     * 
     * @param type The size to set.
     */
    public void setSize(String size) {
        try {
             setSize(new Float(size));
        } catch (Exception e) {
            _size = new Float(Float.NaN);
        }
    }
    
    /**
     * @return Returns the _yearOfBirth.
     */
    public Integer getYearOfBirth() {
        return _yearOfBirth;
    }
    
    /**
     * @param yearOfBirth The yearOfBirth to set.
     */
    public void setYearOfBirth(Integer yearOfBirth) {
        _yearOfBirth = yearOfBirth;
    }

    /**
     * Settermethod for yearOfBirth. The method attemps to parse the given String.
     * If parsing fails the value is set to 0;
     * 
     * @param type The yearOfBirth to set.
     */
    public void setYearOfBirth(String yearOfBirth) {
        try {
             _yearOfBirth = new Integer(yearOfBirth);
        } catch (Exception e) {
            _yearOfBirth = new Integer(0);
        }
    }

    /**
     * @return Returns the identification.
     */
    public String getIdentification() {
        return _id;
    }
    
    /**
     * Returns a string representation of the dataobject.
     * 
     * @return a string representation of the object
     */
    public String toString() {
        return _name.trim();
    }
    
}
