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
package net.illfounded.jpulsemonitor.view;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;


/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MainFrame extends JFrame{
    // Eclipse generated serialVersionUID
	private static final long serialVersionUID = -7583434062535302432L;

	private XMLResourceBundle _bndl;
    private MainMediator _mediator;
    private JPulsemonitor _monitor;
    private JAdminPanel _adminPanel;
    private JExercisePanel _exercisePanel;
    private JEvaluatePanel _evalPanel;
    private JTabbedPane _tabbedPane;

    /**
     * @throws java.awt.HeadlessException
     */
    public MainFrame(JPulsemonitor monitor) {
        _monitor = monitor;
		_bndl = monitor.getResourceBundle();
        _mediator = monitor.getMainMediator();
        
        setTitle(_bndl.getString("app.title"));
		setIconImage(ResourceLoader.loadImage(MainFrame.class, "logo_small.gif"));
		
        initGUI();
    }

	/**
	 * Helpermethod to set up the GUI.
	 */
	private void initGUI() {
		// Set up menubar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// Set up a menu, file
		JMenu menuF = new JMenu(_bndl.getString("menu.file"));
		// Resolve mnemonic from resource bundle
		char mneKey = _bndl.getString("menu.file.mneKey").charAt(0);
		menuF.setMnemonic((int)mneKey);
		menuBar.add(menuF);
		
		// Export menuitem
		JMenuItem menuItemExport = new JMenuItem();
		menuF.add(menuItemExport);
		menuF.add(new JSeparator());
		
		// Exit menuitem
		JMenuItem menuItemClose = new JMenuItem(_bndl.getString("menu.file.exit"));
		// Resolve mnemonic from resource bundle
		mneKey = _bndl.getString("menu.file.exit.mneKey").charAt(0);
		menuItemClose.setMnemonic((int)mneKey);
		menuF.add(menuItemClose);
		
		// Set up a menu, edit
		JMenu menuE = new JMenu(_bndl.getString("menu.edit"));
		// Resolve mnemonic from resource bundle
		mneKey = _bndl.getString("menu.edit.mneKey").charAt(0);
		menuE.setMnemonic((int)mneKey);
		menuBar.add(menuE);
		
		// Set up a menu, help
		JMenu menuH = new JMenu(_bndl.getString("menu.help"));
		// Resolve mnemonic from resource bundle
		mneKey = _bndl.getString("menu.help.mneKey").charAt(0);
		menuH.setMnemonic((int)mneKey);
		menuBar.add(menuH);
		
		// Add about menuitem
		JMenuItem menuItemAbout = new JMenuItem();
		menuH.add(menuItemAbout);
		
		// Add exercise menuitem
		JMenuItem menuItemAddExercise = new JMenuItem();
		menuE.add(menuItemAddExercise);
		
		// Add training menuitem
		JMenuItem menuItemAddTraining = new JMenuItem();
		menuE.add(menuItemAddTraining);
		
		// Add evaluate menuitem
		JMenuItem evalAll = new JMenuItem();
		menuE.add(evalAll);
		
	    // Add separator
		menuE.add(new JSeparator());
		
		// Add preference menuitem
		JMenuItem pref = new JMenuItem();
		menuE.add(pref);
		
		// Set up Toolbar
		Container cPane = getContentPane();
		cPane.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
				
		JButton butAddExercise = new JButton();
		JButton butAddTraining = new JButton();
		JButton butEvalAll = new JButton();
		JButton butAbout = new JButton();
		JButton butPref = new JButton();
		toolBar.add(butAddExercise);
		toolBar.add(butAddTraining);
		toolBar.add(butEvalAll);
		toolBar.add(butPref);
		toolBar.add(butAbout);
		
		// Set up main window
		_tabbedPane = new JTabbedPane();
		
		_adminPanel = new JAdminPanel(_monitor);
		_exercisePanel = new JExercisePanel(_monitor);
		_evalPanel = new JEvaluatePanel(_monitor);
		
		_tabbedPane.addTab(_bndl.getString("tab.session.title"), _exercisePanel);
		_tabbedPane.addTab(_bndl.getString("tab.admin.title"), _adminPanel);
		_tabbedPane.addTab(_bndl.getString("tab.evaluation.title"), _evalPanel);
		_tabbedPane.setSelectedIndex(0);
		
		cPane.add(toolBar, BorderLayout.NORTH);
		cPane.add(_tabbedPane, BorderLayout.CENTER);
		
		// Register stuff with mediator
		_mediator.registerMainFrame(this);
		_mediator.registerExitButton(menuItemClose);
		_mediator.registerNewExerciseButton(butAddExercise);
		_mediator.registerNewExerciseButton(menuItemAddExercise);
		_mediator.registerNewTrainingButton(butAddTraining);
		_mediator.registerNewTrainingButton(menuItemAddTraining);
		_mediator.registerEvalAllButton(evalAll);
		_mediator.registerEvalAllButton(butEvalAll);
		_mediator.registerPrefButton(pref);
		_mediator.registerPrefButton(butPref);
		_mediator.registerAboutButton(menuItemAbout);
		_mediator.registerAboutButton(butAbout);
		_mediator.registerExportButton(menuItemExport);
		_mediator.registerJTabbedPane(_tabbedPane);
		_mediator.registerJPulseTab(_exercisePanel);
		_mediator.registerJPulseTab(_evalPanel);
	}
	
	public JAdminPanel getAdminPanel() {
	    return _adminPanel;
	}
	
	public JExercisePanel getExercisePanel() {
	    return _exercisePanel;
	}
	
	public JEvaluatePanel getEvaluatePanel() {
	    return _evalPanel;
	}
	
	public void setSelectedTab(int index) {
	    _tabbedPane.setSelectedIndex(index);
	}

}
