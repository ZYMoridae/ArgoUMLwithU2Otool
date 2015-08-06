/* $Id: SaveSwingWorker.java 19879 2012-04-24 06:34:14Z mvw $
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.UIManager;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.UndoManager;
import org.uwl2owl.Converter;

/**
 * The specialized SwingWorker used for saving projects.
 * Package visibility only. This class is currently only used by
 * ProjectBrowser and any client calling should use methods there for save.
 */
class SaveSwingWorker extends SwingWorker {

    private final File file;
    private boolean result;
    private final Project project;
    private boolean exitAfterSave;
    //Joe
    private boolean owlConvert = false;
    
    /**
     * This is the only constructor for SaveSwingWorker.
     *
     * @param project   the project to save
     * @param aFile     the file that's going to be saved
     * @param exit      if true: exit ArgoUML when done
     */
    public SaveSwingWorker(
            final Project project,
            final File aFile,
            final boolean exit) {
        super("ArgoSaveProjectThread");
        file = aFile;
        this.project = project;
        exitAfterSave = exit;
    }
    
    public SaveSwingWorker(
            final Project project,
            final File aFile,
            final boolean exit,final boolean owl) {
        super("ArgoSaveProjectThread");
        file = aFile;
        this.project = project;
        exitAfterSave = exit;
        //Joe
        owlConvert = owl;
    }

    /**
     * Implements org.argouml.swingext.SwingWorker#construct(); this is
     * the main method for this SwingWorker.
     * In this case, it simply loads the project.
     *
     * @param pmw       the ProgressMonitorWindow used by ProjectBrowser
     * @return          always null
     */
    public Object construct(ProgressMonitor pmw) {
        // Save project at slightly lower priority to keep UI responsive
        Thread currentThread = Thread.currentThread();
        currentThread.setPriority(currentThread.getPriority() - 1);
        // saves the project
        result = ProjectBrowser.getInstance().trySave(file, pmw, project);
        /*
         * Start Joe 2015
         */
        if(owlConvert) {
            try {
                InputStream umlFile = new FileInputStream(new File(file.getAbsolutePath()));
                Converter conv = new Converter();
                System.out.println("<The size of the xmi file is>=>>>>>>>>>   "+umlFile.available());
                if(file!=null&&umlFile.available()>0) {
                    conv.httpConn(file);
                }
            } catch (FileNotFoundException e1) {
                // TODO: Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO: Auto-generated catch block
                e1.printStackTrace();
            }
        }
        /*
         * End
         */
        return null;
    }

    /**
     * Implements org.argouml.swingext.SwingWorker#initProgressMonitorWindow();
     * it just creates an instance of ProgressMonitor.
     *
     * @return  an instance of ProgressMonitor
     */
    public ProgressMonitor initProgressMonitorWindow() {
        Object[] msgArgs = new Object[] {file.getPath()};
        UIManager.put("ProgressMonitor.progressText", 
                Translator.localize("filechooser.save-as-project"));
        return new ProgressMonitorWindow(ArgoFrame.getFrame(),
                Translator.messageFormat("dialog.saveproject.title", msgArgs));
    }

    /**
     * Overrides the finished method of the SwingWorker class to update the GUI
     */
    public void finished() {
        super.finished();
        if (result) {
            if (exitAfterSave) {
                ProjectBrowser.getInstance().exit();
            } else {
                ProjectBrowser.getInstance().buildTitleWithCurrentProjectName();
                // TODO: Why isn't this done in save?
                UndoManager.getInstance().empty();
            }
        }
    }
}
