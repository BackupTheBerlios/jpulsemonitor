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
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * XMLResourceBundle is copying the behavour of a ResourceBundle. It manages resources
 * for a locale set of static strings. This implementation, unlike suns PropertyResourceBundle
 * implementation, uses XML files. With Java 1.5 this feature will be added anyway, therefore to use
 * this class does only makes sense if you are developing with Java Version 1.4.2 or lower.
 */
public class XMLResourceBundle {
    private Document document;
    private Logger _log;

    /**
     * Creates a property resource bundle.
     *
     * @param xmlFile XML-file to read from.
     */
    public XMLResourceBundle( File xmlFile ) {
        if ( xmlFile == null ) {
            document = null;
            return;
        }

        _log = Logger.getLogger("net.illfounded.jpulsemonitor");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating( true );
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse( xmlFile );
        } catch ( Exception ex ) {
            // Error generated during setup...
            _log.severe(ex.getMessage());
            document = null;
            return;
        }
    }

    /**
     * Gets a string for the given key from this resource bundle. Calling this method is equivalent
     * to calling (String) getObject(key).
     * 
     * @param key the key for the desired string
     * @return the string for the given key
     */
    public String getString(String key) {
        Element e = document.getElementById(key);
        String str = e.getFirstChild().getNodeValue();
        return str;
    }
    
    /**
     * Returns an enumeration of the keys.
     * 
     * @return an enumeration of the keys
     */
    public Enumeration getKeys( ) {
        NodeList nl = document.getChildNodes();
        int length = nl.getLength();
        Vector<Node> keys = new Vector<Node>(length);
        
        for (int i =0; i < length; i++) {
            keys.add(nl.item(i));
        }
        
        return keys.elements();
    }

    /**
     * Gets an object for the given key from this resource bundle. Returns null if this resource
     * bundle does not contain an object for the given key.
     * 
     * @param key the key for the desired object
     * @return the object (Node) for the given key, or null
     */
    protected Object handleGetObject(String key) {
        return getString(key);
    }

}
