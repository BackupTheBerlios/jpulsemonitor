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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.compiere.plaf.CompiereColor;

import net.illfounded.jpulsemonitor.view.MainFrame;
import net.illfounded.jpulsemonitor.view.MainMediator;
import net.illfounded.jpulsemonitor.xml.XMLAdminFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLExerciseFileHandler;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;


/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * Main class, initializes the environment and starts up the GUI. 
 */
public class JPulsemonitor {
    private XMLResourceBundle _bndl;
    private MainMediator _mediator;
    private XMLAdminFileHandler _adminF;
    private XMLExerciseFileHandler _exeF;
    private MainFrame _mainF;

    /**
     * @throws java.awt.HeadlessException
     */
    public JPulsemonitor( ) {
        changeLookAndFeel();

        _bndl = ResourceLoader.loadLanguageFile();
        _adminF = ResourceLoader.loadAdminXML();
        
        String dUserID = _adminF.getDefaultUser().getIdentification();
        _exeF = ResourceLoader.loadExerciseXML(dUserID);
        
        _mediator = new MainMediator(this);
        
        _mainF = new MainFrame(this);

        _mainF.setLocation(ResourceLoader.getDefaultValue("app.location.x"), ResourceLoader.getDefaultValue("app.location.y"));
		_mainF.setSize(ResourceLoader.getDefaultValue("app.width"), ResourceLoader.getDefaultValue("app.height"));
		
		if (ResourceLoader.getSetting("app.maximized")) {
			// On some operating systems this will not work
			_mainF.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension dimension = toolkit.getScreenSize();
			
			if (_mainF.getWidth() < dimension.getWidth()) {
				// Thus we keep the old way
				_mainF.setState(JFrame.NORMAL);
				_mainF.setSize(dimension);
			}
		}
		
		_mainF.setVisible(true);
    }

	public XMLResourceBundle getResourceBundle() {
		return _bndl;
	}
	
	public XMLAdminFileHandler getAdminFileHandler() {
	    return _adminF;
	}
	
	public XMLExerciseFileHandler getExerciseFileHandler() {
	    return _exeF;
	}
	
	public XMLExerciseFileHandler loadExerciseFileHandler(String userId) {
	    _exeF = ResourceLoader.loadExerciseXML(userId);
	    return _exeF;
	}

	public MainFrame getMainFrame() {
	    return _mainF;
	}
	
	public MainMediator getMainMediator() {
	    return _mediator;
	}
	
	public void changeLookAndFeel() {
		// Try to set the look and feel...
        try {
            if (ResourceLoader.getSetting("app.lookfeel.on")) {
                UIManager.setLookAndFeel(new org.compiere.plaf.CompiereLookAndFeel());
                if (ResourceLoader.getSetting("app.gradient.on")) {
                    CompiereColor.setDefaultBackground (new CompiereColor(UIManager.getColor("Panel.background"), Color.white)); // Color.lightGray));
                    CompiereColor.setSetDefault (true);
                }
            }
            SwingUtilities.updateComponentTreeUI(_mainF);
        } catch (Exception e) { /* LnF failed, forget about it... */ }
	}
	
	 /**
      * Start the app!!!
      */
    public static void main(String args[]) {
        JPulsemonitor monitor = new JPulsemonitor( );
    }
    
}
