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
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;

import net.illfounded.jpulsemonitor.xml.dataobject.TrainingDO;
import net.illfounded.jpulsemonitor.xml.dataobject.TrainingTypeDO;
import net.illfounded.jpulsemonitor.xml.dataobject.UserDO;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A handler taking care of all the XML-file containing the administrative information. It loads, creates and
 * deletes nodes from the filesystem.
 */
public class XMLAdminFileHandler extends XMLFileHandler {
    
    /**
     * Creates a new handler for the given file.
     *
     * @param xmlFile XML-file to read from.
     */
    public XMLAdminFileHandler(File xmlFile) {
       super(xmlFile);
   }
    
    public DefaultTableModel getAllTrainingTypesTableModel() {
        NodeList nl = _document.getElementsByTagName("training_type");
    
        int length = nl.getLength();
        AdminTableModel tTypes = new AdminTableModel(TRAINING_TYPE_FIELDS);
        Node n;
        
        for ( int i =0; i < length; i++ ) {
            n = nl.item( i );
            tTypes.addNode(n);
        }
   
        return tTypes;
    }
    
    public Vector<TrainingTypeDO> getAllTrainingTypesVector() {
        NodeList nl = _document.getElementsByTagName("training_type");
        int length = nl.getLength();
        Vector<TrainingTypeDO> tTypes = new Vector<TrainingTypeDO>(length);
    
        Node n;
        TrainingTypeDO tType;
        NamedNodeMap nnm;
   
        for (int i =0; i < length; i++ ) {
            n = nl.item( i );
            nnm = n.getAttributes();
            tType = new TrainingTypeDO(nnm.getNamedItem("id").getNodeValue());
            tType.setPriority(nnm.getNamedItem("priority").getNodeValue());
            tType.setText(n.getFirstChild().getNodeValue());
 
            tTypes.add(tType);
        }
        
        return tTypes;
    }

    public DefaultTableModel getAllTrainingsTableModel() {
        NodeList nl = _document.getElementsByTagName("training");
    
        int length = nl.getLength();
        AdminTableModel tTypes = new AdminTableModel(TRAINING_FIELDS);
        Node n;
        
        for (int i =0; i < length; i++) {
            n = nl.item( i );
            tTypes.addNode(n);
        }
   
        return tTypes;
    }
    
    /**
     * Returns a collection of all trainings that are active or not, depending on the parameter.
     *  
     * @return a collection of all trainings.
     */
    public Vector<TrainingDO> getAllTrainingsVector(boolean active) {
        Vector<TrainingDO> trainings = getAllTrainingsVector();
        Vector<TrainingDO> aTrainings = new Vector<TrainingDO>();
        Iterator it = trainings.iterator();
        TrainingDO training;
        
        while (it.hasNext()) {
            training = (TrainingDO) it.next();
            if (training.isActive() == active) {
                aTrainings.add(training);
            }
        }
        
        return aTrainings;
    }
    
    /**
     * Returns the value of the configuration if it is found, null otherwise.
     *  
     * @return the string loaded form the file of null.
     */
    public String getConfiguration(String tagName) {
        NodeList nl = _document.getElementsByTagName(tagName);
        int length = nl.getLength();
        if (length <=0) {
        	return null;
        } else {
        	Node n = nl.item(0);
            return n.getFirstChild().getNodeValue();
        }
    }
    
    /**
     * Stores the value of the configuration to the administrational XML-file.
     *  
     * @param tagName - the name of the tag to store the value to.
     * @param value - the value to store.
     */
    public void setConfiguration(String tagName, boolean value) {
    	if (value) {
    		setConfiguration(tagName, "yes");
    	} else {
    		setConfiguration(tagName, "no");
    	}
    }
    
    /**
     * Stores the value of the configuration to the administrational XML-file.
     *  
     * @param tagName - the name of the tag to store the value to.
     * @param value - the value to store.
     */
    public void setConfiguration(String tagName, int value) {
    	setConfiguration(tagName, value +"");
    }
    
    /**
     * Helpermethod to stores a configuration to the administrational XML-file.
     *  
     * @param tagName - the name of the tag to store the value to.
     * @param value - the value to store.
     */
    private void setConfiguration(String tagName, String value) {
    	NodeList nl = _document.getElementsByTagName(tagName);
        int length = nl.getLength();
        Node node;
        if (length <=0) {
        	// Does not exist yet, create a new tag
        	nl = _document.getElementsByTagName("configuration");
            Node parent = nl.item(0);
            
            Element newT = _document.createElement(tagName);
            parent.appendChild(newT);
        	node = newT;
        } else {
        	node = nl.item(0);
        }

        // If no child exists, create new one...
        if (node.getFirstChild() == null) {
        	node.appendChild(_document.createTextNode(value));
        } else {
        	node.replaceChild(_document.createTextNode(value), node.getFirstChild());
        }
        storeXML();
    }

    /**
     * Returns a collection of all trainings.
     *  
     * @return a collection of all trainings.
     */
    public Vector<TrainingDO> getAllTrainingsVector() {
        NodeList nl = _document.getElementsByTagName("training");
        int length = nl.getLength();
        Vector<TrainingDO> tTrainings = new Vector<TrainingDO>(length);
    
        Node n, subN;
        TrainingDO tType;
        NamedNodeMap nnm;
        
        for ( int i =0; i < length; i++ ) {
            n = nl.item(i);
            nnm = n.getAttributes();
            tType = new TrainingDO(nnm.getNamedItem("id").getNodeValue());
            tType.setTrainingType(nnm.getNamedItem("training_type").getNodeValue());
            
            subN = nnm.getNamedItem("duration");
            if (subN != null) {
                tType.setDuration(subN.getNodeValue());
            }
            
            subN = nnm.getNamedItem("distance");
            if (subN != null) {
                tType.setDistance(subN.getNodeValue());
            }
            
            subN = nnm.getNamedItem("limits");
            if (subN != null) {
                tType.setLimits(subN.getNodeValue());
            }
            
            subN = nnm.getNamedItem("active");
            if (subN != null) {
                tType.setActive(subN.getNodeValue());
            }
            
            tType.setText(n.getFirstChild().getNodeValue());
 
            tTrainings.add(tType);
        }
   
        return tTrainings;
    }
    
    /**
     * Returns the default user of the system. Null if non is set...
     * 
     * @return the default user
     */
    public UserDO getDefaultUser() {
        try {
            NodeList nl = _document.getElementsByTagName("users");
            String userID = nl.item(0).getAttributes().getNamedItem("default").getNodeValue();
        
            Node dUser = _document.getElementById(userID);
            NamedNodeMap nnm = dUser.getAttributes();
            UserDO user = new UserDO(nnm.getNamedItem("id").getNodeValue());
            user.setGender(nnm.getNamedItem("gender").getNodeValue());
            user.setWeight(nnm.getNamedItem("weight").getNodeValue());
            user.setSize(nnm.getNamedItem("size").getNodeValue());
            user.setYearOfBirth(nnm.getNamedItem("year_of_birth").getNodeValue());
            user.setName(dUser.getFirstChild().getNodeValue());
        
            return user;
        } catch (Exception exe) {
            log(Level.WARNING, "No default user found", exe);
        }
        return null;
    }
    
    public DefaultTableModel getAllUsersTableModel() {
        NodeList nl = _document.getElementsByTagName("user");

        int length = nl.getLength();
        AdminTableModel tTypes = new AdminTableModel(USER_FIELDS);
        Node n;
        
        for ( int i =0; i < length; i++ ) {
            n = nl.item( i );
            tTypes.addNode(n);
        }
   
        return tTypes;
    }
    
    /**
     * Returns a collection of all users.
     *  
     * @return a collection of all users.
     */
    public Vector<UserDO> getAllUsersVector() {
        NodeList nl = _document.getElementsByTagName("user");
        int length = nl.getLength();
        Vector<UserDO> users = new Vector<UserDO>(length);
    
        NamedNodeMap nnm;
        UserDO user;
        Node n;
        
        for (int i =0; i < length; i++ ) {
            n = nl.item(i);
            nnm = n.getAttributes();
            user = new UserDO(nnm.getNamedItem("id").getNodeValue());
            user.setGender(nnm.getNamedItem("gender").getNodeValue());
            user.setWeight(nnm.getNamedItem("weight").getNodeValue());
            user.setSize(nnm.getNamedItem("size").getNodeValue());
            user.setYearOfBirth(nnm.getNamedItem("year_of_birth").getNodeValue());
            user.setName(n.getFirstChild().getNodeValue());
            users.add(user);
        }
   
        return users;
    }
  
    /**
     * Creates and stores a new training-type entity. The given dataobject is used to take the values from.
     * There is no validation within this method, be sure the data is consistent.
     * 
     * @param trainingTDO the data used to build the node
     * @return the newly created node
     */
    public Node createNewTrainingType(TrainingTypeDO trainingTDO) {
        NodeList nl = _document.getElementsByTagName("training_types");
        Node parent = nl.item(0);

        Element newT = _document.createElement("training_type");
        
        checkAndInsertAttribut(newT, "id", trainingTDO.getIdentification());
        checkAndInsertAttribut(newT, "priority", trainingTDO.getPriority());
        checkAndInsertAttribut(newT, "intensity", trainingTDO.getIntensity());
        checkAndInsertAttribut(newT, "time", trainingTDO.getTime());
        
        if (!trainingTDO.getText().equals("")) {
            newT.appendChild(_document.createTextNode(trainingTDO.getText()));
        }
        
        parent.appendChild(newT);
        
        storeXML();
        
        return newT;
    }
    
    /**
     * Modifies an exixting training-type entity. The given dataobject is used to take the values from.
     * There is no validation within this method, be sure the data is consistent.
     * 
     * @param trainingTDO the data used to modify the node
     * @return the modified node
     */
    public Node modifyTrainingType(TrainingTypeDO trainingTDO) {
        Node node = findNodeById("training_type", trainingTDO.getIdentification());
        NamedNodeMap nnm = node.getAttributes();
        Node subN;

        updateOrRemoveAttribut(node, nnm, "priority", trainingTDO.getPriority());
        updateOrRemoveAttribut(node, nnm, "intensity", trainingTDO.getIntensity());
        updateOrRemoveAttribut(node, nnm, "time", trainingTDO.getTime());
        
        if (!trainingTDO.getText().equals("")) {
            // If no child exists, create new one...
            if (node.getFirstChild() == null) {
                node.appendChild(_document.createTextNode(trainingTDO.getText()));
            } else {
                node.replaceChild(_document.createTextNode(trainingTDO.getText()), node.getFirstChild());
            }
        } else {
            // If child exists, delete it...
            if (node.getFirstChild() != null) {
                node.removeChild(node.getFirstChild());
            }
        }
        
        storeXML();
        
        return node;
    }
    
    /**
     * Creates and stores a new training entity. The given dataobject is used to take the values from.
     * There is no validation within this method, be sure the data is consistent.
     * 
     * @param trainingDO the data used to build the node
     * @return the newly created node
     */
    public Node createNewTraining(TrainingDO trainingDO) {
        NodeList nl = _document.getElementsByTagName("trainings");
        Node parent = nl.item(0);
        
        Element newT = _document.createElement("training");
        
        checkAndInsertAttribut(newT, "id", trainingDO.getIdentification());
        checkAndInsertAttribut(newT, "training_type", trainingDO.getTrainingType());
        checkAndInsertAttribut(newT, "duration", trainingDO.getDuration());
        checkAndInsertAttribut(newT, "distance", trainingDO.getDistance());
        checkAndInsertAttribut(newT, "limits", trainingDO.getLimits());
        checkAndInsertAttribut(newT, "active", trainingDO.getActive());
        
        if (!trainingDO.getText().equals("")) {
            newT.appendChild(_document.createTextNode(trainingDO.getText()));
        }
        
        parent.appendChild(newT);
        
        storeXML();
        
        return newT;
    }
    
    /**
     * Modifies an exixting training element. The given dataobject is used to take the values from.
     * There is no validation within this method, be sure the data is consistent.
     * 
     * @param trainingDO the data used to modify the node
     * @return the modified node
     */
    public Node modifyTraining(TrainingDO trainingDO) {
        Node node = findNodeById("training", trainingDO.getIdentification());
        NamedNodeMap nnm = node.getAttributes();
        Node subN;

        updateOrRemoveAttribut(node, nnm, "training_type", trainingDO.getTrainingType());
        updateOrRemoveAttribut(node, nnm, "duration", trainingDO.getDuration());
        updateOrRemoveAttribut(node, nnm, "distance", trainingDO.getDistance());
        updateOrRemoveAttribut(node, nnm, "limits", trainingDO.getLimits());
        updateOrRemoveAttribut(node, nnm, "active", trainingDO.getActive());
        
        if (!trainingDO.getText().equals("")) {
            // If no child exists, create new one...
            if (node.getFirstChild() == null) {
                node.appendChild(_document.createTextNode(trainingDO.getText()));
            } else {
                node.replaceChild(_document.createTextNode(trainingDO.getText()), node.getFirstChild());
            }
        } else {
            // If child exists, delete it...
            if (node.getFirstChild() != null) {
                node.removeChild(node.getFirstChild());
            }
        }
        
        storeXML();
        
        return node;
    }
    
    /**
     * Modifies an exixting user element. The given dataobject is used to take the values from.
     * There is no validation within this method, be sure the data is consistent.
     * 
     * @param userDO the data used to modify the node
     * @return the modified node
     */
    public Node modifyUser(UserDO userDO) {
        Node node = findNodeById("user", userDO.getIdentification());
        NamedNodeMap nnm = node.getAttributes();
        Node subN;

        updateOrRemoveAttribut(node, nnm, "gender", userDO.getGender());
        updateOrRemoveAttribut(node, nnm, "weight", userDO.getWeight());
        updateOrRemoveAttribut(node, nnm, "size", userDO.getSize());
        updateOrRemoveAttribut(node, nnm, "year_of_birth", userDO.getYearOfBirth());
        
        if (!userDO.getName().equals("")) {
            // If no child exists, create new one...
            if (node.getFirstChild() == null) {
                node.appendChild(_document.createTextNode(userDO.getName()));
            } else {
                node.replaceChild(_document.createTextNode(userDO.getName()), node.getFirstChild());
            }
        } else {
            // If child exists, delete it...
            if (node.getFirstChild() != null) {
                node.removeChild(node.getFirstChild());
            }
        }
        
        storeXML();
        
        return node;
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
                Node n = findNodeById("training_type", identification);
                if (n != null) {
                    // Got that sucker...
                    n.getParentNode().removeChild(n);
                    storeXML();
                    return true;
                }
                
                n = findNodeById("training", identification);
                if (n != null) {
                    // Got that sucker...
                    n.getParentNode().removeChild(n);
                    storeXML();
                    return true;
                }
                
                n = findNodeById("user", identification);
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
     * Looks up the next free, thus unique, identification for an exercise
     * element.
     */
    public String createTrainingTypeIdentification() {
        try {
            int nextId = getNextIdentification("training_type");
            return "y-" + nextId;
        } catch (Exception exe) {
            // Should actually never happen, but we dont want to crash if it does.
            // Do it the old way...
    	    Date now = new Date();
    	    return "y-" + now.getTime();
        }
    }
    
	/**
     * Looks up the next free, thus unique, identification for an exercise
     * element.
     */
    public String createUserIdentification() {
        try {
            int nextId = getNextIdentification("user");
            return "u-" + nextId;
        } catch (Exception exe) {
            // Should actually never happen, but we dont want to crash if it does.
            // Do it the old way...
    	    Date now = new Date();
    	    return "u-" + now.getTime();
        }
    }
    
	/**
     * Looks up the next free, thus unique, identification for an exercise
     * element.
     */
    public String createTrainingIdentification() {
        try {
            int nextId = getNextIdentification("training");
            return "t-" + nextId;
        } catch (Exception exe) {
            // Should actually never happen, but we dont want to crash if it does.
            // Do it the old way...
    	    Date now = new Date();
    	    return "t-" + now.getTime();
        }
    }
    
	/**
	 * Helpermethod to create a new unique identification.
	 */
    private int getNextIdentification(String elementName) {
       NodeList nl = _document.getElementsByTagName(elementName);
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
       return ++highestVal;
    }
        
}
