// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2015 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.uwl2owl;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.glass.ui.Window;

public class InfoDialog extends JFrame{
    
    WorkspaceDriver wd;
    JComboBox<String> relationCombo = null; 
    JComboBox<ComboItem> parentCombo = null;
    
    public InfoDialog() {
        // TODO: Auto-generated constructor stub
        init();
    }
    
    public void getList() {
        wd = new WorkspaceDriver();
        ArrayList<String> relationList = new ArrayList<String>();
        relationList = wd.getRelationlist();
        relationCombo = new JComboBox<String>();
        for (int i = 0; i < relationList.size(); i++) {
            relationCombo.addItem(relationList.get(i));
        }
//        parentCombo = wd.getParentList();
    }
    
    
    public void init() {
        
                  getList();
        
        JPanel jpanel = new JPanel();
        JButton jbutton = new JButton("OK");
        JLabel relationLabel = new JLabel("Relation:"); 
        JLabel parentLabel = new JLabel("Parent Element:"); 
        
        jpanel.setLayout(null);
        jpanel.add(relationCombo);
//        jpanel.add(parentCombo);
        jpanel.add(jbutton);
        jpanel.add(relationLabel);
        jpanel.add(parentLabel);
        
        relationLabel.setSize(100, 20);
        relationLabel.setLocation(50,100);
        relationCombo.setSize(150, 20);
        relationCombo.setLocation(170, 100);
        
//        parentLabel.setSize(100, 20);
//        parentLabel.setLocation(50,140);
//        parentCombo.setSize(150, 20);
//        parentCombo.setLocation(170, 140);
        
        jbutton.setSize(100, 20);
        jbutton.setLocation(170, 180);
        
        this.add(jpanel);
        this.setLocation(200, 200);
        this.setSize(400,400);
        this.setVisible(true);
    }
    
}
