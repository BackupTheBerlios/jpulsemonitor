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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import net.illfounded.jpulsemonitor.JPulsemonitor;
import net.illfounded.jpulsemonitor.ResourceLoader;
import net.illfounded.jpulsemonitor.xml.XMLResourceBundle;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * JAboutDialog show some information about the application.
 */
public class JAboutDialog extends JDialog {
	protected static XMLResourceBundle _bndl;
	protected static JPulsemonitor _monitor;
	protected JTextArea _text;
	

	/**
	 * Creates a new JTrainingDialog.
	 */
	public JAboutDialog(JPulsemonitor monitor) {
		super(monitor.getMainFrame(), true);
		_monitor = monitor;
		_bndl = _monitor.getResourceBundle();
		setTitle(_bndl.getString("app.title"));
		initGUI();
		pack();
		setResizable(false);
		
		// Centers the dialog within the screen
		setLocationRelativeTo(_monitor.getMainFrame());
	}

    /**
     * Initialize all the GUI suff.
     */
    protected  void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        
        _text = new JTextArea(_bndl.getString("about.text"));
        _text.setEditable(false);
        // text.setEnabled(false);
        _text.setBackground(this.getBackground());
       
        JButton butOk = new JButton(_bndl.getString("btn.ok"));
        butOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
			}
		});
       
        JPanel butP = new JPanel(new FlowLayout());
        butP.add(butOk);
        getContentPane().add(new JImgPanel(), BorderLayout.NORTH);
        getContentPane().add(_text, BorderLayout.CENTER);
        getContentPane().add(butP, BorderLayout.SOUTH);
    }
	
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
				setVisible(false);
			}
		};
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
	
	// inner class
	class JImgPanel extends JPanel{ 
	     private ImageIcon _img;

	     public JImgPanel() {
	         _img = ResourceLoader.loadImageIcon(JAboutDialog.class, "logo_medium.jpg");
	         
	         setPreferredSize(new Dimension(_text.getWidth(), _img.getIconHeight() + 40));
	     }

	     public void paintComponent(Graphics g) {
	         super.paintComponent(g);
	         Graphics2D g2d = (Graphics2D)g;
	         int width = (_text.getWidth() - _img.getIconWidth() ) / 2;
	         g.drawImage(_img.getImage(), width , 20, this);
	     }
	}
}
