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
package net.illfounded.jpulsemonitor.xml;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;

import net.illfounded.jpulsemonitor.xml.dataobject.CustomFieldDO;
import net.illfounded.jpulsemonitor.xml.dataobject.ExerciseDO;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A handler taking care of all the XML- file containing the exercise information. It loads, creates and
 * deletes nodes from the filesystem.
 */
public class XMLExerciseFileHandler extends XMLFileHandler {
    
    /**
     * Creates a new handler for the given file.
     *
     * @param xmlFile XML-file to read from.
     */
    public XMLExerciseFileHandler(File xmlFile) {
       super(xmlFile);
   }
    
    public DefaultTableModel getAllExercisesTableModel() {
        try {
            NodeList nl = _document.getElementsByTagName("exercise");
    
            int length = nl.getLength();
            HashMap custFields;
            ExerciseTableModel eTypes = new ExerciseTableModel(EXERCISE_FIELDS);
            Node n;
            NamedNodeMap nnm;
        
            for (int i =0; i < length; i++) {
                n = nl.item(i);
                nnm = n.getAttributes();
                custFields = lookupCustomValues(nnm.getNamedItem("id").getNodeValue());
                
                if (custFields.isEmpty()) {
                	eTypes.addNode(n);
                } else {
                	eTypes.addNode(n, custFields);
                }
            }
            return eTypes;
            
        } catch (Exception exe) {
            _log.log(Level.WARNING, "No exercises found for this user : " +exe.getMessage());
        }
        
        return new DefaultTableModel();
    }
    
    /**
     * Returns the values of the custom field.
     * 
     * @param exerciseId - The exercise to look for.
     * 
     * @return A map of the custom values (zero or more).
     */
    public HashMap<String, String> lookupCustomValues(String exerciseId) {
    	HashMap<String, String> map = new HashMap<String, String>();
    	try {
            NodeList nl = _document.getElementsByTagName("customfield");
            int length = nl.getLength();
            String id;

            Node nField;
            NamedNodeMap nnm;
            
            for (int i = 0; i < length; i++) {
            	nField = nl.item(i);
                nnm = nField.getAttributes();

                id = nnm.getNamedItem("id").getNodeValue();
                if (id.equals(exerciseId)) {
                	map.put(nnm.getNamedItem("name").getNodeValue(), nField.getFirstChild().getNodeValue());
                }
            }
        } catch (Exception exe) {
            _log.log(Level.WARNING, "No custom field found for this exercise : " + exe.getMessage());
        }
        return map;
    }
    
    public DefaultTableModel getAllExercisesTableModel(Date start, Date end) {
        try {
            NodeList nl = _document.getElementsByTagName("exercise");
    
            int length = nl.getLength();
            ExerciseTableModel eTypes = new ExerciseTableModel(EXERCISE_FIELDS);
            Node n, subN;
            NamedNodeMap nnm;
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateNode;
            HashMap custFields;
            
            for ( int i =0; i < length; i++ ) {
                n = nl.item( i );
                nnm = n.getAttributes();
                
                // Attemps to parse the given date and compare it to the period that should be loaded
                try {
                    subN = nnm.getNamedItem("date");
                    if (subN != null) {
                        dateNode = formatter.parse(subN.getNodeValue());
                        
                        if (dateNode.after(start) && dateNode.before(end)) {
                            // Is in period
                        	custFields = lookupCustomValues(nnm.getNamedItem("id").getNodeValue());
                        	
                        	if (custFields.isEmpty()) {
                            	eTypes.addNode(n);
                            } else {
                            	eTypes.addNode(n, custFields);
                            }
                        }
                    }
                } catch (Exception exe1) {
                    log(Level.WARNING, "Date parsing failed.", exe1);
                    // No date set... do not show the node
                }
            }
            return eTypes;
            
        } catch (Exception exe2) {
            exe2.printStackTrace();
            log(Level.WARNING, "No exercises found for this user", exe2);
        }
        
        return new DefaultTableModel();
    }
    
    /**
     * Returns all exercises having the given training and beeing in the given period.
     * 
     * @return all available exercises
     */
    public Vector<ExerciseDO> getAllExercisesVectorByTraining(String trainingId, Date start, Date end) {
        Vector<ExerciseDO> exercises = getAllExercisesVector();
        Vector<ExerciseDO> aExercises = new Vector<ExerciseDO>(exercises.size());
        Date dateExercise;
        
        // The Java5 way... forget about all the Iterator and class casting :-)
        for (ExerciseDO exercise: exercises) {
            if (exercise.getTrainingId().equals(trainingId)) {
                
                // Compare the date to the period that should be loaded
                dateExercise = exercise.getDate();
                if (dateExercise.after(start) && dateExercise.before(end)) {
                    // Is in period
                    aExercises.add(exercise);
                }
            }
        }
        
        return aExercises;
    }
    
    /**
     * Returns all exercises beeing in the given period.
     * 
     * @return all available exercises
     */
    public Vector<ExerciseDO> getAllExercisesVector(Date start, Date end) {
        Vector<ExerciseDO> exercises = getAllExercisesVector();
        Vector<ExerciseDO> aExercises = new Vector<ExerciseDO>(exercises.size());
        Date dateExercise;
        
        // The Java5 way... forget about all the Iterator and class casting :-)
        for (ExerciseDO exercise: exercises) {
            // Compare the date to the period that should be loaded
            dateExercise = exercise.getDate();
            if (dateExercise.after(start) && dateExercise.before(end)) {
                // Is in period
                aExercises.add(exercise);
            }
        }
        
        return aExercises;
    }

    
    /**
     * Returns all exercises beeing in the given period. Having a valid weight set.
     * 
     * @return all available exercises
     */
    public Vector<ExerciseDO> getAllExercisesVectorWeight(Date start, Date end) {
        Vector<ExerciseDO> exercises = getAllExercisesVector();
        Vector<ExerciseDO> aExercises = new Vector<ExerciseDO>(exercises.size());
        Date dateExercise;
        
        // The Java5 way... forget about all the Iterator and class casting :-)
        for (ExerciseDO exercise: exercises) {
            // Compare the date to the period that should be loaded
            dateExercise = exercise.getDate();
            if (dateExercise.after(start) && dateExercise.before(end)) {
                // Is in period
                if (exercise.getWeight() != null) {
                    aExercises.add(exercise);
                }
            }
        }
        
        return aExercises;
    }
    
    /**
     * Returns all exercises having the given training.
     * 
     * @return all available exercises
     */
    public Vector<ExerciseDO> getAllExercisesVectorByTraining(String trainingId) {
        Vector<ExerciseDO> exercises = getAllExercisesVector();
        Vector<ExerciseDO> aExercises = new Vector<ExerciseDO>(exercises.size());
        
        // The Java5 way... forget about all the Iterator and class casting :-)
        for (ExerciseDO exercise: exercises) {
            if (exercise.getTrainingId().equals(trainingId)) {
                aExercises.add(exercise);
            }
        }
        
        return aExercises;
    }
    
    /**
     * Returns all exercises.
     * 
     * @return all available exercises
     */
    public Vector<ExerciseDO> getAllExercisesVector() {
        try {
            NodeList nl = _document.getElementsByTagName("exercise");
            int length = nl.getLength();
            Vector<ExerciseDO> exercises = new Vector<ExerciseDO>(length);

            Node topN;
            Node subN;
            ExerciseDO exercise;
            NamedNodeMap nnm;

            for (int i = 0; i < length; i++) {
                topN = nl.item(i);
                nnm = topN.getAttributes();

                exercise = new ExerciseDO(nnm.getNamedItem("id").getNodeValue());

                subN = nnm.getNamedItem("date");
                if (subN != null) {
                    exercise.setDate(subN.getNodeValue());
                }

                subN = nnm.getNamedItem("training");
                if (subN != null) {
                    exercise.setTrainingId(subN.getNodeValue());
                }

                subN = nnm.getNamedItem("weight");
                if (subN != null) {
                    exercise.setWeight(subN.getNodeValue());
                }

                subN = nnm.getNamedItem("kcal");
                if (subN != null) {
                    exercise.setKcal(subN.getNodeValue());
                }

                subN = nnm.getNamedItem("average_hr");
                if (subN != null) {
                    exercise.setAverageHR(subN.getNodeValue());
                }

                subN = nnm.getNamedItem("time_in_zone");
                if (subN != null) {
                    exercise.setTimeInZone(subN.getNodeValue());
                }

                subN = nnm.getNamedItem("exercise_time");
                if (subN != null) {
                    exercise.setExerciseTime(subN.getNodeValue());
                }

                String desc = "";
                if (topN.getFirstChild() != null) {
                    desc = topN.getFirstChild().getNodeValue();
                }
                exercise.setText(desc);
                exercises.add(exercise);
            }

            return exercises;
        } catch (Exception exe) {
            _log.log(Level.WARNING, "No exercises found for this user : " + exe.getMessage());
        }
        return new Vector<ExerciseDO>();
    }
    
    /**
     * Returns all customfields.
     * 
     * @return all available customfields
     */
    public HashMap<String, Vector<CustomFieldDO>> getAllCustomFields() {
        try {
            NodeList nl = _document.getElementsByTagName("customfield");
            int length = nl.getLength();
            HashMap<String, Vector<CustomFieldDO>> hFields = new HashMap<String, Vector<CustomFieldDO>>(length);

            Node topN;
            String id;
            String name;
            String value;
            Vector<CustomFieldDO> vFields;
            NamedNodeMap nnm;

            for (int i = 0; i < length; i++) {
                topN = nl.item(i);
                nnm = topN.getAttributes();
                id = nnm.getNamedItem("id").getNodeValue();
                name = nnm.getNamedItem("name").getNodeValue();
                value = "";
                
                if (topN.getFirstChild() != null) {
                    value = topN.getFirstChild().getNodeValue();
                }
                
                vFields = hFields.get(id);
                if (vFields == null) {
                	vFields = new Vector<CustomFieldDO>();
                }
                vFields.add(new CustomFieldDO(name, value));
                
                hFields.put(id, vFields);
            }

            return hFields;
        } catch (Exception exe) {
            _log.log(Level.WARNING, "No custom fields found for this user : " + exe.getMessage());
        }
        return new HashMap<String, Vector<CustomFieldDO>>(0);
    }
    
    /**
     * Returns all customfields.
     * 
     * @return all available customfields
     */
    public Vector<CustomFieldDO> getCustomField(String exerciseId) {
    	HashMap<String, Vector<CustomFieldDO>> fields = getAllCustomFields();

    	return fields.get(exerciseId);
    }
  
    /**
     * Deletes the node with the given id.
     * 
     * @param identification the id of the node to delete
     * 
     * @return true if a node was found and removed, false otherwise
     */
    public boolean deleteNode(String identification) throws Exception {
        try {
            Element elem = _document.getElementById(identification);
            
            Node parent = elem.getParentNode();
            parent.removeChild(elem);
            
            storeXML();
        } catch (Exception exe) {
            log(Level.WARNING, "get elementById( .. ) returned null", exe);
            // getElementById(identification) is a DOM Leve 3 method and may not have worked, because
            // some other methods ( DOM Level 1) are namespace ignorant. Mixing both sets of methods
            // can lead to unpredictable results. We try to look for our element to delete by ourself...
            try {
                Node n = findNodeById("exercise", identification);
                if (n != null) {
                    // Got that sucker...
                    n.getParentNode().removeChild(n);
                    storeXML();
                    return true;
                }
            } catch (Exception exe2) {
                // If that failes, there is nothing we can do :-(
                log(Level.SEVERE, "Delete node failed", exe2);
                throw exe2;
            }
        }
        return true;
    }
    
    /**
     * Creates and stores a new exercise element. The given dataobject is used to take the values from.
     * There is no validation within this method, be sure the data is consistent.
     * 
     * @param trainingDO the data used to build the node
     * @return the newly created node
     */
    public Node createNewExercise(ExerciseDO exerciseDO) {
        NodeList nl = _document.getElementsByTagName("exercises");
        Node parent = nl.item(0);
        
        Element newE = _document.createElementNS(null, "exercise");
        
        newE.setAttribute("id", exerciseDO.getIdentification());
        // Format the date first 
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	    String date = formatter.format(exerciseDO.getDate());

	    checkAndInsertAttribut(newE, "date", date);
	    checkAndInsertAttribut(newE, "training", exerciseDO.getTrainingId());
	    checkAndInsertAttribut(newE, "weight", exerciseDO.getWeight());
	    checkAndInsertAttribut(newE, "kcal", exerciseDO.getKcal());
	    checkAndInsertAttribut(newE, "average_hr", exerciseDO.getAverageHR());
	    checkAndInsertAttribut(newE, "time_in_zone", exerciseDO.getTimeInZone());
	    checkAndInsertAttribut(newE, "exercise_time", exerciseDO.getExerciseTime());
	    
        if (!exerciseDO.getText().equals("")) {
            newE.appendChild(_document.createTextNode(exerciseDO.getText()));
        }

        parent.appendChild(newE);
        
        // Deal with the custom fields...
        HashMap<String, String> custFields = exerciseDO.getCustomFields();
		Iterator it = custFields.keySet().iterator();
		String custField;
		String value;
		
		while (it.hasNext()) {
			custField = (String) it.next();
			value = custFields.get(custField);
			createCustomField(exerciseDO.getIdentification(), custField, value);
		}

        storeXML();
        
        return newE;
    }
    
    /**
     * Modifies an existing exercise element. The given dataobject is used to take the values from.
     * There is no validation within this method, be sure the data is consistent.
     * 
     * @param trainingDO the data used to modify the node
     * @return the modified node
     */
    public Node modifyExercise(ExerciseDO exerciseDO) {
        // Element elem = _document.getElementById(exerciseDO.getIdentification());
        Node node = findNodeById("exercise", exerciseDO.getIdentification());
        NamedNodeMap nnm = node.getAttributes();
        
        Node subN;

        // Format the date first 
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	    String date = formatter.format(exerciseDO.getDate());

        updateOrRemoveAttribut(nnm, "date", date);
        updateOrRemoveAttribut(nnm, "training", exerciseDO.getTrainingId());
        updateOrRemoveAttribut(nnm, "weight", exerciseDO.getWeight());
        updateOrRemoveAttribut(nnm, "kcal", exerciseDO.getKcal());
        updateOrRemoveAttribut(nnm, "average_hr",exerciseDO.getAverageHR());
        updateOrRemoveAttribut(nnm, "time_in_zone", exerciseDO.getTimeInZone());
        updateOrRemoveAttribut(nnm, "exercise_time", exerciseDO.getExerciseTime());

        if (!exerciseDO.getText().equals("")) {
            // If no child exists, create new one...
            if (node.getFirstChild() == null) {
                node.appendChild(_document.createTextNode(exerciseDO.getText()));
            } else {
                node.replaceChild(_document.createTextNode(exerciseDO.getText()), node.getFirstChild());
            }
        } else {
            // If child exists, delete it...
            if (node.getFirstChild() != null) {
                node.removeChild(node.getFirstChild());
            }
        }

        // Deal with the custom fields...
        HashMap<String, String> custFields = exerciseDO.getCustomFields();
		Iterator it = custFields.keySet().iterator();
		String custField;
		String value;
		
		while (it.hasNext()) {
			custField = (String) it.next();
			value = custFields.get(custField);
			updateCustomField(exerciseDO.getIdentification(), custField, value);
		}
        
        storeXML();
        
        return node;
    }
    
	/**
     * Looks up the next free, thus unique, identification for an exercise
     * element.
     */
    public String createExerciseIdentification() {
        try {
            NodeList nl = _document.getElementsByTagName("exercise");
            String sCurrentId;
            int iCurrentId;
            int highestVal = 0;
            NamedNodeMap nnm;
            int length = nl.getLength();
            Node n;

            for (int i = 0; i < length; i++) {
                n = nl.item(i);
                nnm = n.getAttributes();
                sCurrentId = nnm.getNamedItem("id").getNodeValue();
                iCurrentId = Integer.parseInt(sCurrentId.substring(2));

                if (iCurrentId > highestVal) {
                    highestVal = iCurrentId;
                }
            }
            return "e-" + ++highestVal;
        } catch (Exception exe) {
            // Should actually never happen, but we dont want to crash if it does.
            // Do it the old way...
            Date now = new Date();
            return "e-" + now.getTime();
        }
    }
    
    /**
     * Helpermethod to insert a new custom field.
     */
    private void createCustomField(String exerciseId, String custName, String value) {
    	NodeList nl = _document.getElementsByTagName("customfields");
    	Node parent = nl.item(0);
		
    	Element newE = _document.createElementNS(null, "customfield");
    	newE.setAttribute("id", exerciseId);
    	newE.setAttribute("name", custName);
    	newE.appendChild(_document.createTextNode(value));
	        	
    	parent.appendChild(newE);
    }
    
    /**
     * Helpermethod to update a custom field.
     */
    private void updateCustomField(String exerciseId, String custName, String value) {
    	Node n = lookupCustomValue(exerciseId, custName);
		
    	if (n == null) {
    		createCustomField(exerciseId, custName, value);
    	} else {
    		NamedNodeMap nnm = n.getAttributes();
    		updateOrRemoveAttribut(nnm, "id", exerciseId);
        	updateOrRemoveAttribut(nnm, "name", custName);
        	n.replaceChild(_document.createTextNode(value), n.getFirstChild());
    	}
    }
    
    /**
     * Helpermethod to get a certain custom field.
     */
    public Node lookupCustomValue(String exerciseId, String custName) {
    	try {
            NodeList nl = _document.getElementsByTagName("customfield");
            int length = nl.getLength();
            String id;
            String name;
            Node nField;
            NamedNodeMap nnm;
            
            for (int i = 0; i < length; i++) {
            	nField = nl.item(i);
                nnm = nField.getAttributes();
                id = nnm.getNamedItem("id").getNodeValue();
                if (id.equals(exerciseId)) {
                	name = nnm.getNamedItem("name").getNodeValue();
                	if (name.equals(custName)) {
                		return nField;
                	}
                }
            }
        } catch (Exception exe) {
            _log.log(Level.WARNING, "No custom field found for this exercise : " + exe.getMessage());
        }
        return null;
    }
    
}
