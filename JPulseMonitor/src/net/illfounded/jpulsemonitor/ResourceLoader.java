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
package net.illfounded.jpulsemonitor;

import java.awt.Image;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import net.illfounded.jpulsemonitor.xml.XMLAdminFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLExerciseFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;

/**
 *  @author Adrian Buerki <ad@illfounded.net>
 * 
 * This class is used to load any kind of resource form the filesytem. The problem about this is,
 * that a resource may be in a jar or some where on the local filesytem.
 */
public class ResourceLoader {
	private static XMLAdminFileHandler _adminF;
	
    // Settings do go here until they are loadable...
    public static final int SHOW_LAST_MONTHS = 6;
    public static final int LOCATION_X = 0;
    public static final int LOCATION_Y =0;
    public static final int WIDTH = 800	;
    public static final int HEIGHT = 560;
    public static final boolean MAXIMIZED = false;
    public static final boolean LANDF = true;
    public static final boolean LANDF_GRADIENT = false;
    public static final int LIMIT_MIN = 120;
    public static final int LIMIT_MAX = 170;
    
	private static Logger _log = Logger.getLogger("net.illfounded.jpulsemonitor");
	
	public static ImageIcon loadImageIcon(Class anchor, String iconName) {

	    URL imgURL = anchor.getResource(iconName);
	    
	    if (imgURL == null) {
			// Try to load it form our own resource respository
			imgURL = ResourceLoader.class.getResource(iconName);
		}
	    
		ImageIcon img = new ImageIcon(imgURL);
		return img;
	}

	public static Image loadImage(Class anchor, String imageName) {
		Image img = loadImageIcon(anchor, imageName).getImage();
		return img;
	}

	/**
	 * Loads a <code>File</code> with the given name. Working directory and
	 * user home will be used as path.
	 * 
	 * @param fileName - The name of the file to load.
	 * 
	 * @return The <code>File</code> object representing the handle for the file.
	 */
	public static File loadFile(String fileName) {
	    // Try to load from the user home
	    File file = loadFile(System.getProperty("user.home"), fileName);
	    
	    if (file.exists()) {
	        return file;
	    }
	    
	    // Try to load the file from the resource folder
        file = loadFile(System.getProperty("user.dir") +File.separator +"resources", fileName);
	    
        if (file.exists()) {
	        return file;
	    }
        
        // Try to load the file from current working directory
        file = loadFile(System.getProperty("user.dir"), fileName);
        
		return file;
	}
	
	/**
	 * Loads a <code>File</code> from the given path.
	 * 
	 * @param path - The path to search the file in.
	 * @param fileName - The name of the file to load.
	 * 
	 * @return The <code>File</code> object representing the handle for the file.
	 */
	public static File loadFile(String path, String fileName) {
		_log.log(Level.INFO, "Loading file form " +path);
		File file = new File(path, fileName);
		
		if(!file.exists()) {
			_log.log(Level.SEVERE, "File does not exist : " +fileName);
		}
		return file;
	}
	
	/**
	 * Attempts to load the <code>XMLResourceBundle</code> matching the system language.
	 * If no such file is found, the english one will be used.
	 * 
	 * @return A language dependant <code>XMLResourceBundle</code>
	 */
	public static  XMLResourceBundle loadLanguageFile() {
	    Locale loc = Locale.getDefault();
	    
	    String filename = "prop_" + loc.getLanguage() +".xml";
	    File f = loadFile(filename);
	    if (f.exists()) {
	        return new XMLResourceBundle(f);
	    }
	    _log.log(Level.INFO, "Language dependant file does not exist... loading default.");
	    return new XMLResourceBundle(loadFile("prop_en.xml"));
	}
	
	/**
	 * Loads the administration.xml file. If no file exists a new one will be created.
	 * If a resource-folder exists, the new file is created in there. If no folder is present, the xml
	 * file will be created in the user-home (omitting the doctype tag).
	 * 
	 * @return A handler to the XML file.
	 */
	public static XMLAdminFileHandler loadAdminXML() {
		if (_adminF != null) {
			return _adminF;
    	} 
		
	    File file = loadFile("administration.xml");
	    
	    if (file.exists()) {
	    	_adminF = new XMLAdminFileHandler(file);
	        return _adminF;
	    }
	    
	    try {
	        // Open the resouce folder and create a new file
	        file = new File(System.getProperty("user.dir") +File.separator +"resources", "administration.xml");
		    
	        // Create file if it does not exist
	        boolean success = file.createNewFile();
	        if (success) {
	            // Write some standart xml stuff...
	            BufferedWriter out = new BufferedWriter(new FileWriter(file));
	            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	            out.write("\n");
	            out.write("<!DOCTYPE administration SYSTEM \"administration.dtd\">");
	            out.write("\n");
	            out.write("<administration>");
	            out.write("\n");
	            out.write("</administration>");
	            out.close();
		            
	            return new XMLAdminFileHandler(file);
	        } else {
	            // Open the user home and create the new file
	            file = new File(System.getProperty("user.home"), "administration.xml");
	    
	            // Write some standart xml stuff...
	            BufferedWriter out = new BufferedWriter(new FileWriter(file));
	            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	            out.write("\n");
	            out.write("<administration>");
	            out.write("\n");
	            out.write("</administration>");
	            
	            out.close();
	            
	            return new XMLAdminFileHandler(file);
	        }
	    } catch (IOException exe) {
	        _log.log(Level.SEVERE, "File creation failed : " +exe.getMessage());
	    }
	    
        // Should never been reached...
        _log.log(Level.SEVERE, "File creation failed : " +"administration.xml");
	    
	    return null;
	}
	
	/**
	 * Loads the exercise.xml file for the given user. If no file exists a new one will be created.
	 * If a resource-folder exists, the new file is created in there. If no folder is present, the xml
	 * file will be created in the user-home (omitting the doctype tag).
	 * 
	 * @param userID - A unique id, assigned to a user.
	 * 
	 * @return A handler to the XML file.
	 */
	public static XMLExerciseFileHandler loadExerciseXML(String userID) {
	    File file = loadFile("exercises_" +userID +".xml");
	    
	    if (file.exists()) {
	        return new XMLExerciseFileHandler(file);
	    }
	    
	    try {
	        // Open the resouce folder and create a new file
	        file = new File(System.getProperty("user.dir") +File.separator +"resources", "exercises_" +userID +".xml");
		    
	        // Create file if it does not exist
	        boolean success = file.createNewFile();
	        if (success) {
	            // Write some standart xml stuff...
	            BufferedWriter out = new BufferedWriter(new FileWriter(file));
	            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	            out.write("\n");
	            out.write("<!DOCTYPE data SYSTEM \"data.dtd\">");
	            out.write("\n");
	            out.write("<data>");
	            out.write("\n");
	            out.write("<exercises id=" +userID +"\">");
	            out.write("\n");
	            out.write("</exercises>");
	            out.write("\n");
	            out.write("</data>");
	            out.close();
		            
	            return new XMLExerciseFileHandler(file);
	        } else {
	            // Open the user home and create the new file
	            file = new File(System.getProperty("user.home"), "exercises_" +userID +".xml");
	    
	            // Write some standart xml stuff...
	            BufferedWriter out = new BufferedWriter(new FileWriter(file));
	            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	            out.write("\n");
	            out.write("<data>");
	            out.write("\n");
	            out.write("<exercises id=" +userID +"\">");
	            out.write("\n");
	            out.write("</exercises>");
	            out.write("\n");
	            out.write("</data>");
	            out.close();
	            
	            return new XMLExerciseFileHandler(file);
	        }
	    } catch (IOException exe) {
	        _log.log(Level.SEVERE, "File creation failed : " +exe.getMessage());
	    }
	    
        // Should never been reached...
        _log.log(Level.SEVERE, "File creation failed : " +"exercises_" +userID +".xml");
        return null;
	}

	/**
	 * Returns some common settings, are loaded form the administration xml file.
	 * 
	 * @param identifier - The key used to look up the setting.
	 */
	public static int getDefaultValue(String identifier) {
		// Don't forget to check whether the file handler is loaded...
		if (_adminF == null) {
			_adminF = loadAdminXML();
    	}
		String value;
	    if (identifier.equalsIgnoreCase("app.width")) {
	    	return parseAndReturn("app.width", WIDTH);
	    } else if (identifier.equalsIgnoreCase("app.height")) {
	    	return parseAndReturn("app.height", HEIGHT);
	    } else if (identifier.equalsIgnoreCase("app.location.x")) {
	    	return parseAndReturn("app.location.x", LOCATION_X);
	    } else if (identifier.equalsIgnoreCase("app.location.y")) {
	    	return parseAndReturn("app.location.y", LOCATION_Y);
	    } else if (identifier.equalsIgnoreCase("limit.max")) {
	    	return parseAndReturn("limit.max", LIMIT_MAX);
	    } else if (identifier.equalsIgnoreCase("limit.min")) {
	    	return parseAndReturn("limit.min", LIMIT_MIN);
	    } else if (identifier.equals("nbr.visible.months")) {
	    	return parseAndReturn("nbr.visible.months", SHOW_LAST_MONTHS);
	    }
	    return -1;
	}

	/**
	 * Helpermethod to load a default value.
	 */
	private static int parseAndReturn(String tagName, int defaultValue) {
		String value = _adminF.getConfiguration(tagName);
		if (value != null) {
			return Integer.parseInt(value);
		}
		return defaultValue;
	}
	
	/**
	 * Helpermethod to load a setting.
	 */
	private static boolean parseAndReturn(String tagName, boolean defaultValue) {
		String value = _adminF.getConfiguration(tagName);
		if (value != null) {
			if (value.equalsIgnoreCase("yes")) {
    			return true;
    		}
    		return false;
		}
		return defaultValue;
	}
	
	/**
	 * Returns some common settings, are loaded form the administration xml file.
	 * 
	 * @param identifier - The key used to look up the setting.
	 */
	public static boolean getSetting(String identifier) {
		// Don't forget to check whether the file handler is loaded...
		if (_adminF == null) {
			_adminF = loadAdminXML();
    	} 
		String value;

	    if (identifier.equalsIgnoreCase("app.maximized")) {
	    	return parseAndReturn("app.maximized", MAXIMIZED);
	    } else if (identifier.equalsIgnoreCase("app.lookfeel.on")) {
	    	return parseAndReturn("app.lookfeel.on", LANDF);
	    } else if (identifier.equalsIgnoreCase("app.gradient.on")) {
	    	return parseAndReturn("app.gradient.on", LANDF_GRADIENT);
	    }
	    return false;
	}

}
