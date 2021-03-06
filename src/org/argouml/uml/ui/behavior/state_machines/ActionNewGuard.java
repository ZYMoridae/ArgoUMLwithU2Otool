/* $Id: ActionNewGuard.java 19433 2011-05-15 18:49:49Z tfmorris $
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

// $header$
package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * The action to create a new Guard for a Transition. <p>
 * 
 * This action is (currently) not fit to create a guard for a transition 
 * that already has one! If this functionality is needed, then 
 * the old guard should be deleted before making a new one.
 * 
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @deprecated by Bob Tarling in 0.33.4. This is no longer used.
 * Use {@link org.argouml.ui.ActionCreateContainedModelElement} instead.
 */
@Deprecated
public class ActionNewGuard extends AbstractActionNewModelElement {

    private static ActionNewGuard singleton = new ActionNewGuard();

    /**
     * Constructor for ActionNewCallAction.
     */
    protected ActionNewGuard() {
        super();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        TargetManager.getInstance().setTarget(
                Model.getStateMachinesFactory().buildGuard(getTarget()));
    }

    /**
     * @return Returns the singleton.
     */
    public static ActionNewGuard getSingleton() {
        return singleton;
    }

    @Override
    public boolean isEnabled() {
        Object t = getTarget();
        return t != null
            && Model.getFacade().getGuard(t) == null;
    }

}
