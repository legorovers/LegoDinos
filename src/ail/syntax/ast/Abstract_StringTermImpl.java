// ----------------------------------------------------------------------------
// Copyright (C) 2012 Louise A. Dennis, and  Michael Fisher 
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
// License along with the AIL; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
// To contact the authors:
// http://www.csc.liv.ac.uk/~lad
//
//----------------------------------------------------------------------------

package ail.syntax.ast;

import ail.syntax.StringTermImpl;

import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.Verify;

/**
 * Generic Description of Abstract Classes in AIL and AJPF
 * -------------------------------------------------------
 * 
 * We use "Abstract" versions of syntax items for all bits of state that we sometimes wish to store in the native
 * java VM as well in the JavaPathfinder VM.  In particular files are parsed into the native VM and then the relevant
 * initial state of the multi-agent system is reconstructed in the model-checking VM.  This is done to improve
 * efficiency of parsing (the native VM is faster).  We also represent properties for model checking in the native VM 
 * and, indeed the property automata is stored only in the native VM.  We used Abstract classes partly because less
 * computational content is needed for these objects in the native VM and so a smaller representation can be used
 * but also because specific support is needed for transferring information between the two virtual machines both
 * in terms of methods and in terms of the data types chosen for the various fields.  It was felt preferable to 
 * separate these things out from the classes used for the objects that determine the run time behaviour of a MAS.
 * 
 * Abstract classes all have a method (toMCAPL) for creating a class for the equivalent concrete object used
 * when executing the MAS.  They also have a method (newJPFObject) that will create an equivalent object in the 
 * model-checking virtual machine from one that is held in the native VM.  At the start of execution the agent
 * program is parsed into abstract classes in the native VM.  An equivalent structure is then created in the JVM
 * using calls to newJPFObject and this structure is then converted into the structures used for executing the MAS
 * by calls to toMCAPL.
 * 
 */

/** 
 * Immutable class for string terms.
 * 
 * @author jomi
 */
public final class Abstract_StringTermImpl implements Abstract_StringTerm {
	/**
	 * The String.
	 */
	private final String fValue;

	/**
	 * Constructor.
	 */
	public Abstract_StringTermImpl() {
		super();
		fValue = null;
	}
	
	/**
	 * Constructor.
	 * @param fs
	 */
	public Abstract_StringTermImpl(String fs) {
		fValue = fs;
	}
	
	/**
	 * Getter for the string.
	 */
	public String getString() {
		return fValue;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ajpf.psl.ast.Abstract_MCAPLTerm#toMCAPL()
	 */
	public StringTermImpl toMCAPL() {
		return new StringTermImpl(getString());
	}
	
	/*
	 * (non-Javadoc)
	 * @see ajpf.psl.ast.Abstract_MCAPLTerm#newJPFObject(gov.nasa.jpf.jvm.MJIEnv)
	 */
	public int newJPFObject(MJIEnv env) {
		int objref = env.newObject("ail.syntax.ast.Abstract_StringTermImpl");
		if (Abstract_Predicate.strings.containsKey(getString())) {
			env.setReferenceField(objref, "fValue", Abstract_Predicate.strings.get(getString()));
		} else {
			int stringref = env.newString(getString());
			Abstract_Predicate.strings.put(getString(), stringref);
			env.setReferenceField(objref, "fValue", stringref);
		}
		return objref;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Abstract_StringTermImpl clone() {
		return new Abstract_StringTermImpl(fValue);
	}
	
	/**
	 * There is no subterm, only a string.
	 */
	public Abstract_Term getTerm(int i) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ajpf.psl.ast.Abstract_MCAPLTerm#createInJPF(gov.nasa.jpf.jvm.JVM)
	 */
	public int createInJPF(VM vm) {
		Verify.log("ail.syntax.ast", Verify.WARNING, "Abstract_StringTermImpl should not be being created from Listener");
		return 0;
	}

}
