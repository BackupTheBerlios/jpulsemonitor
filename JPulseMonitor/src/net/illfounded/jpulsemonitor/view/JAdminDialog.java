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

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * JAdminDialog is the parent of all administration related input-dialogs.
 */
public abstract class JAdminDialog extends JDialog {
	protected static XMLResourceBundle _bndl;
	protected static JPulsemonitor _monitor;

	/**
	 * Creates a new JTrainingDialog.
	 */
	protected JAdminDialog(JPulsemonitor monitor) {
		super(monitor.getMainFrame(), true);
		_monitor = monitor;
		_bndl = _monitor.getResourceBundle();
		getContentPane().setLayout(new GridBagLayout());
		initGUI();
		pack();
		setResizable(false);
		
		setLocationRelativeTo(_monitor.getMainFrame());
	}

    /**
     * Initialize all the GUI suff.
     */
    protected  abstract void initGUI();
	
	/**
	 * Stores the entered data in the internal dataobject.
	 */
	protected abstract void storeData() throws ParseException;
	
    /**
     * Resets the component, means sets its fields to the initial values.
     */
    public abstract void reset();
    
	/**
	 * In Microsoft Windows environments, users can press the Escape key to close dialog windows.
	 * By default, with Java applications, this behavior is offered by neither AWT nor Swing dialogs.
	 * To present users with a more common environment, we can add this behavior.
	 */
	protected JRootPane createRootPane() {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
			    reset();
				setVisible(false);
			}
		};
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
	
	/**
	 * Helperclass to store the data and close the dialog.
	 */
	protected class OkAction implements ActionListener {
		private JAdminDialog _dialog;
		
		public OkAction(JAdminDialog dialog) {
			_dialog = dialog;
		}
		
		public void actionPerformed(ActionEvent e) {
			try {
                storeData();
            } catch (ParseException e1) {
                new JErrorDialog(_dialog, e1);
                return;
            }
			setVisible(false);
		}
	}
	
	/**
	 * Helperclass to clean up and close the dialog.
	 */
	protected class ExitAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			reset();
			setVisible(false);
		}
	}
	
}
