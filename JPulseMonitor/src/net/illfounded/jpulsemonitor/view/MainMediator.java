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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.view.action.ActionManager;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;


/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * The Mediator defines how a set of objects interact, it provides a place to localize
 * the knowledge and separate it from the interface so you don't have to go poking
 * around in the interface to make changes.
 */
public class MainMediator extends WindowAdapter {
    private Logger _log;
	private MainFrame _mainF;
    private XMLResourceBundle _bndl;
    private ActionManager _aManager;
    private boolean _admin_updated;
    private Vector<JPulsTab> _tabs;
    
    /**
     * Creates a new MainMediator
     */
    public MainMediator(JPulsemonitor monitor) {
		_log = Logger.getLogger("net.illfounded.jpulsemonitor");
		_bndl = monitor.getResourceBundle();
		_aManager = ActionManager.getInstance(monitor);
		_tabs = new Vector<JPulsTab>();
		_admin_updated = false;
	}

    public void registerExitButton(AbstractButton butExit) {
		butExit.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					endApp();
				}}
		);
	}

    public void registerNewTrainingButton(AbstractButton butAddTraining) {
        butAddTraining.setAction(_aManager.getAction(ActionManager.NEW_TRAINING));
        
        // No mnemonic on the toolbar...
        if (butAddTraining instanceof JButton) {
            butAddTraining.setMnemonic(0);
        }
    }
    
    public void registerEvalAllButton(AbstractButton butEvalAll) {
        butEvalAll.setAction(_aManager.getAction(ActionManager.EVAL_ALL));
    }
    
    public void registerAboutButton(AbstractButton butAbout) {
    	butAbout.setAction(_aManager.getAction(ActionManager.ABOUT));
    }
    
    public void registerPrefButton(AbstractButton butPref) {
    	butPref.setAction(_aManager.getAction(ActionManager.PREFERENCE));
    }
    
    public void registerExportButton(AbstractButton butExport) {
    	butExport.setAction(_aManager.getAction(ActionManager.EXPORT));
    }
    
    public void registerNewExerciseButton(AbstractButton butAddExercise) {
        butAddExercise.setAction(_aManager.getAction(ActionManager.NEW_EXERCISE));
        
        // No mnemonic on the toolbar...
        if (butAddExercise instanceof JButton) {
            butAddExercise.setMnemonic(0);
        }
    }
    
    public void registerJPulseTab(JPulsTab tab) {
        _tabs.add(tab);
    }
    
    public void registerJTabbedPane(JTabbedPane pane) {
        // Register a change listener
    	pane.addChangeListener(new ChangeListener() {
            // This method is called whenever the selected tab changes
            public void stateChanged(ChangeEvent evt) {
                if (_admin_updated) {
                    // JTabbedPane t = (JTabbedPane) evt.getSource();
                    // ((JPulsTab) t.getSelectedComponent()).refreshTab();
                    
                    // Update all and reset flag
                    for (Iterator it = _tabs.iterator(); it.hasNext();) {
                        ((JPulsTab) it.next()).refreshTab();
                    }
                    _admin_updated = false;
                }
            }
        });
    }
    
    public void registerMainFrame(MainFrame mainF) {
		_mainF = mainF;
        _mainF.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        _mainF.addWindowListener(this);
    }
    
	public void windowClosing(WindowEvent e) {
		endApp();
		// Strangely the main frame gets minimizes, workaround this problem
		_mainF.setVisible(true);
		_mainF.requestFocus();
	}
	
	/**
	 * Helpermethod to gracefully end the application.
	 */
	private void endApp() {
		int choice =
			JOptionPane.showConfirmDialog(
				_mainF,
				_bndl.getString("dialog.exit.msg"),
				_bndl.getString("dialog.exit.title"),
				JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			if (_mainF != null) {
				_mainF.removeWindowListener(this);
				_mainF.dispose();
			}
			System.exit(0);
		}
	}
	
	/**
	 * A flag which symbolizes updates in the adminstration tab. What usually means,
	 * the user lists have to be reread.
	 * 
	 * @param update - the flag to set.
	 */
	public void setAdminUpdated(boolean update) {
	    updateShowingTab();
	    _admin_updated = update;
	}
	
	/**
	 * Helpermethod to update the visible tab. E.g. after preferences
	 * have changes...
	 */
	private void updateShowingTab() {
		// Update the visble tab
		JPulsTab tab;
		for (Iterator it = _tabs.iterator(); it.hasNext();) {
			tab = (JPulsTab) it.next();
			if (tab.isShowing()) {
				tab.refreshTab();
			}
		}
	}
	
}
