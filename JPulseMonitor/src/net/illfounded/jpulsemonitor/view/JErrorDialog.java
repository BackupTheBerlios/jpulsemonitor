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
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 *
 * A component to show any kind of message to the user.
 * Like exceptions or validation errors...
 */
public class JErrorDialog extends JDialog {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = -2244557222171743162L;

	private JTextArea _tAreaDescription;
    private JButton _butOK;
    private String _title;
    private String _message;
    
    public JErrorDialog(Frame owner, Exception exe) {
        super(owner, true);
        _title = exe.getMessage();
        _message = parseStack(exe);
        initGUI();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public JErrorDialog(Dialog owner, Exception exe) {
        super(owner, true);
        _title = exe.getMessage();
        _message = parseStack(exe);
        initGUI();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /**
     * Initialize all the GUI suff.
     */
    protected void initGUI() {
        Container cPane = getContentPane();
        cPane.setLayout(new BorderLayout());
        
        _butOK = new JButton("Ok");
        _butOK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }});
        
        _tAreaDescription = new JTextArea(6, 35);
        _tAreaDescription.setLineWrap(true);
        _tAreaDescription.setEditable(false);
        _tAreaDescription.setWrapStyleWord(true);
        _tAreaDescription.setText(_message);
        
        JScrollPane scrollPane = new JScrollPane(_tAreaDescription, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        cPane.add(scrollPane, BorderLayout.CENTER);
        cPane.add(_butOK, BorderLayout.SOUTH);
        
        setTitle(_title);
        pack();
        setResizable(false);
    }
    
    private String parseStack(Exception exe) {
        StackTraceElement[] stack = exe.getStackTrace();
        String msg = "";
        for (int i=0; i<stack.length; i++) {
            msg += stack[i].toString() + "\n";
        }
        return msg;
    }
    
}
