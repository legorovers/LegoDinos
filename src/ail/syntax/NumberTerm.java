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
//
// This file is based on code from the Open Source software "Jason", copyright
// by Jomi F. Hubner and Rafael H. Bordini.  http://jason.sf.net
//----------------------------------------------------------------------------

package ail.syntax;

import ajpf.psl.MCAPLNumberTerm;

/**
 * An interface for terms that represent numbers.
 * @author lad
 *
 */
public interface NumberTerm extends Term, MCAPLNumberTerm {
	/**
	 * Return the value represented by this term.
	 */
	public double solve();
	
	/**
	 * Compare the value of this term to the value of another.
	 * @param n
	 * @return
	 */
	public int eqcompareTo(NumberTerm n);
}
