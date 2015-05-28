// ----------------------------------------------------------------------------
// Copyright (C) 2008-2012 Louise A. Dennis, Berndt Farwer, Michael Fisher and 
// Rafael H. Bordini.
// 
// This file is part of the Agent Infrastructure Layer (AIL)
//
// The AIL is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
// 
// The AIL is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
// To contact the authors:
// http://www.csc.liv.ac.uk/~lad
//----------------------------------------------------------------------------

package ail.semantics.operationalrules;

import java.util.Iterator;

import ail.semantics.AILAgent;
import ail.syntax.Unifier;
import ail.syntax.Literal;
import ail.syntax.PredicateTerm;

import ajpf.util.AJPFLogger;

/**
 * Rule to drop a belief from the Belief Base.
 * 
 * @author lad
 *
 */
public class HandleDropBelief extends HandleBelief {
	private static final String name = "Handle Drop Belief";
	private static final String logname = "ail.semantics.operationalrules";
	
	/*
	 * (non-Javadoc)
	 * @see ail.semantics.operationalrules.OSRule#getName()
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.semantics.operationalrules.HandleBelief#checkPreconditions(ail.semantics.AILAgent)
	 */
	public boolean checkPreconditions(AILAgent a) {
		return(super.checkPreconditions(a) && topdeed.isDeletion());
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.semantics.operationalrules.HandleTopDeed#apply(ail.semantics.AILAgent)
	 */
	public void apply(AILAgent a) {	
		Iterator<PredicateTerm> bl = a.getBB(topdeed.getDBnum()).getRelevant(b);
				
		while (bl.hasNext()) {
			Literal bp = (Literal) bl.next();
			Unifier un = new Unifier();
						
			if (a.relevant(bp, b)) {
				if (un.sunifies(b, bp)) {
					a.delBel(topdeed.getDBnum(), bp);
					if (AJPFLogger.ltFine(logname)) {
						AJPFLogger.fine(logname, a.getAgName() + " dropped " + bp);
					}
								
					thetahd.compose(thetab);
					thetahd.compose(un);
				}
			}
		}
		
		i.tlI(a);
		i.compose(thetahd);
	}
}