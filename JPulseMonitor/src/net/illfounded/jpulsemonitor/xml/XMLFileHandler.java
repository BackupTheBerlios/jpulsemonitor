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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * A handler taking care of all the XML- files. This abstract class has some
 * commen methods and constants defined.
 */
public abstract class XMLFileHandler {
	private File _xmlFile;
	private Transformer _transformer;
	private DOMSource _source;
	private StreamResult _result;
	protected Document _document;
	protected Logger _log;
	public static final String EXERCISE_FIELDS[] = { "id", "date", "training",
			"weight", "kcal", "average_hr", "time_in_zone", "exercise_time" };
	public static final String TRAINING_FIELDS[] = { "id", "training_type",
			"duration", "distance", "limits", "active" };
	public static final String TRAINING_TYPE_FIELDS[] = { "id", "priority",
			"intensity", "time" };
	public static final String USER_FIELDS[] = { "id", "weight", "size",
			"year_of_birth", "gender" };

	/**
	 * Creates a new handler for the given file.
	 * 
	 * @param xmlFile
	 *            XML-file to read from.
	 */
	public XMLFileHandler(File xmlFile) {
		if (xmlFile == null) {
			_document = null;
			return;
		}
		_xmlFile = xmlFile;
		_log = Logger.getLogger("net.illfounded.jpulsemonitor");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);

		// Use a Transformer for the file output
		TransformerFactory tFactory = TransformerFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			_document = builder.parse(xmlFile);

			// Init the transformer
			_transformer = tFactory.newTransformer();

			// By default, the DOCTYPE is not written when using a transformer
			// to dump a DOM document to an XML file
			// Set the system id manually...
			_transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, _document
					.getDoctype().getSystemId());

			_source = new DOMSource(_document);

			// Simply write the file to system out for now...
			_result = new StreamResult(_xmlFile);

		} catch (Exception ex) {
			// Error generated during setup...
			_log.log(Level.SEVERE, ex.getMessage());
			_document = null;
			return;
		}
	}

	/**
	 * Deletes the node with the given id.
	 * 
	 * @param identification
	 *            the id of the node to delete
	 * 
	 * @return true if a node was found and removed, false otherwise
	 */
	public abstract boolean deleteNode(String identification) throws Exception;

	/**
	 * Helpermetoh to look up a <code>Node</code> by id doing the search the
	 * hard way.
	 */
	protected Node findNodeById(String tagName, String nodeId) {
		NodeList nl = _document.getElementsByTagName(tagName);
		int length = nl.getLength();
		Node n;
		NamedNodeMap nnm;
		String currentId;

		for (int i = 0; i < length; i++) {
			n = nl.item(i);
			nnm = n.getAttributes();
			currentId = nnm.getNamedItem("id").getNodeValue();
			if (currentId.equals(nodeId)) {
				// Got that sucker...
				return n;
			}
		}
		return null;
	}

	/**
	 * Helpermethod to store the xml back to the filesystem.
	 */
	protected void storeXML() {
		try {
			_transformer.transform(_source, _result);
		} catch (Exception ex) {
			_log.severe(ex.getMessage());
		}
	}

	/**
	 * Helpermethod deal with attributes of nodes.
	 */
	protected void updateOrRemoveAttribut(Node parent, NamedNodeMap nnm,
			String attrName, Object value) {
		Node attr;
		// Attribut does not exist yet, insert...
		attr = nnm.getNamedItem(attrName);

		if (attr == null && value != null) {
			attr = _document.createAttribute(attrName);
			attr.setNodeValue(value.toString());

			parent.appendChild(attr);
			return;
		}

		// Attribut does exist, but needs to be removed...
		if (attr != null && value == null) {
			parent.removeChild(attr);
			return;
		}

		// Attribut does exist and needs to be updated...
		if (attr != null && value != null) {
			attr.setNodeValue(value.toString());
		}
	}

	/**
	 * Helpermethod deal with attributes of nodes.
	 */
	protected void checkAndInsertAttribut(Element element, String attrName,
			Object value) {
		if (value != null) {
			element.setAttribute(attrName, value.toString());
		}
	}

	/**
	 * Dump a log record to the logger
	 */
	protected void log(Level level, String message, Exception e) {
		if (e instanceof SAXParseException) {
			SAXParseException se = (SAXParseException) e;
			// Get details
			int line = se.getLineNumber();
			int col = se.getColumnNumber();
			String publicId = se.getPublicId();
			String systemId = se.getSystemId();

			// Append details to message
			message += ": " + e.getMessage() + ": line=" + line + ", col="
					+ col + ", PUBLIC=" + publicId + ", SYSTEM=" + systemId;

		} else {
			message += ": " + e.getMessage();
		}

		// Log the message
		_log.log(level, message);
	}

	/**
	 * Checks if the given node has still children, used to check the integrity
	 * before a delete operation is executed.
	 * 
	 * @return true if there is still a child.
	 */
	public boolean checkHasRefId(String parentId, String refIdName,
			String childTag) {
		NodeList nl = _document.getElementsByTagName(childTag);

		int length = nl.getLength();
		Node n;
		NamedNodeMap nnm;
		String refId;
		for (int i = 0; i < length; i++) {
			n = nl.item(i);
			nnm = n.getAttributes();
			refId = nnm.getNamedItem(refIdName).getNodeValue();

			if (refId.equals(parentId)) {
				return true;
			}
		}
		return false;
	}

}