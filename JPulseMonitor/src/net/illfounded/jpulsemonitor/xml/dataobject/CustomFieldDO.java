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
 * A dataobject representing an custom_field of exersice. A custom_field can
 * only hold a String. 
 */
public class CustomFieldDO {
	private String _name;
	private String _value;

	/**
	 * Creates a new CustomFieldDO.
	 */
	public CustomFieldDO(String name, String value) {
		_name = name;
		_value = value;
	}
	
    /**
     * @return Returns the name.
     */
    public String getName() {
        if (_name == null) {
            return "";
        }
        return _name;
    }
    
    /**
     * @param String The name to set.
     */
    public void setName(String name) {
        _name = name;
    }
    
    /**
     * @return Returns the value.
     */
    public String getValue() {
        if (_value == null) {
            return "";
        }
        return _value;
    }
    
    /**
     * @param String The value to set.
     */
    public void setValue(String value) {
        _value = value;
    }
    
    /**
     * Returns a string representation of the dataobject.
     * 
     * @return a string representation of the object
     */
    public String toString() {
        return getValue().trim();
    }

}
