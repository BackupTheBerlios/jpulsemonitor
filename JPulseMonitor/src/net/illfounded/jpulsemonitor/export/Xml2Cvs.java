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
package net.illfounded.jpulsemonitor.export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.illfounded.jpulsemonitor.xml.dataobject.ExerciseDO;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * Util class handels export of exercises to CSV file. 
 */
public class Xml2Cvs {
	public static String SEPERATOR = ",";
	private FileOutputStream _fos = null;
	private static Logger _log = Logger.getLogger("net.illfounded.jpulsemonitor");
	private Vector _useTitles;
	
	public Xml2Cvs(Vector<ExerciseDO> exercises, File outFile) {
		init(outFile);
		parse(exercises);
		try {
			_fos.flush();
			_fos.close();
		} catch (Exception exe) {
			_log.log(Level.SEVERE, "Error during export : " +exe.getMessage());
		}
	}
	
	private void parse(Vector<ExerciseDO> exercises) {
		// Print titles...
		println("ID, DATE, TRAINING, WEIGHT, KCAL, AVERAGE_HR, TIME_IN_ZONE, EXERCISE_TIME");
		
		// The Java5 way... forget about all the Iterator and class casting :-)
        for (ExerciseDO exercise: exercises) {
           
        	print(exercise.getIdentification());
            print(exercise.getDate().toString());
            print(exercise.getTrainingId());
            print(exercise.getWeight());
            print(exercise.getKcal());
            print(exercise.getAverageHR());
            print(exercise.getTimeInZone());
            println(exercise.getExerciseTime());
        }
	}
	
	private void init(File fileOut) {
		try {
			_fos = new FileOutputStream(fileOut);
		} catch (Exception exe) {
			_log.log(Level.SEVERE, "Unable to create output file : " +exe.getMessage());
			try {
				_fos.close();
			} catch (Exception e) { /* Do nothing */ }
		}
	}

	/**
	 * Helpermethod to write an <code>Object</code> to the output-file. Adds a seperator
	 * and takes care of the logging.
	 * 
	 * @param txt The line to be written.
	 */
	private void print(Object obj) {
		String tmp = "";
		if (obj != null) {
			tmp = obj.toString();
		}
		
		tmp += SEPERATOR;
		try {
			_fos.write(tmp.getBytes());
		} catch (Exception exe) {
			_log.log(Level.SEVERE, "Unable to write : " +exe.getMessage());
		}
	}
	
	/**
	 * Helpermethod to write an <code>Object</code> to the output-file. Adds a new line
	 * once the <code>Object</code> has been written and takes care of logging.
	 * 
	 * @param obj The object to be written.
	 */
	private void println(Object obj) {
		String tmp = "";
		if (obj != null) {
			tmp = obj.toString();
		}
		
		try {
			_fos.write(tmp.getBytes());
			_fos.write("\n".getBytes());
		} catch  (Exception exe) {
			_log.log(Level.SEVERE, "Unable to write : " +exe.getMessage());
		}
	}

}
