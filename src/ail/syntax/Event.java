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

package ail.syntax;

import java.util.List;
import java.util.ArrayList;

import gov.nasa.jpf.annotation.FilterField;

/**
 * A class for AIL events.
 * 
 * @author louiseadennis
 *
 */
public class Event extends DefaultAILStructure implements Unifiable {

    /**
	 * Category for start events.
	 */
	@FilterField
	public static final byte	Estart = 10;

	/**
	 * String for fast lookup.  Not used at the moment but I expect we will
	 * want this for plan look up.
	 */
	@FilterField
	private PredicateIndicator piCache;
	
	/**
	 * Construct an event of a given category.
	 * 
	 * @param b the cateogry.
	 */
	public Event(byte b) {
		super(b);
	}
	
	/**
	 * Construct an event with a given add/delete flag, category and literal.
	 * 
	 * @param t the add/delete flag.
	 * @param b the category.
	 * @param l the literal.
	 */
	public Event(int t, byte b, Literal l) {
		super(t, b, l);
	}
	
	/**
	 * Construct an event from an add/delete flag, category and a group name.
	 * @param t
	 * @param b
	 * @param s
	 */
	public Event(int t, byte b, String s) {
		super(t, b, s);
	}
	
	/**
	 * Construct and event with a given add/delete flag and goal.
	 * 
	 * @param t the add/delete flag.
	 * @param g the goal.
	 */
	public Event(int t, Goal g) {
		super(t, g);
	}
	
	public Event(int t, byte c, Message msg) {
		super(t, c, msg);
	}
	
		
	/**
	 * Is this a start event?
	 * 
	 * @return whether this is a start event.
	 */
	public boolean isStart() {
		return (getCategory() == Estart);
	}
	
	/** return [+|-][!|?] super.getFucntorArity */
	public PredicateIndicator getPredicateIndicator() {
        if (piCache == null) {
            String s = "";
            if (isStart()) {
            	piCache = new PredicateIndicator("start", 0);
             } else {
            	if (isAddition())
            		s += "+";
            	else if (isDeletion())
            		s += "-";
            	else if (isUpdate())
            		s += "+-";
            	if (getContent() instanceof PredicateTerm) {
            		if (getContent() instanceof Goal) {
            			s += "!";
            		}
            		piCache = new PredicateIndicator(s + ((PredicateTerm) getContent()).getFunctor(), ((PredicateTerm) getContent()).getTermsSize());
            	} else if (getContent() instanceof HasTermRepresentation) {
            		Term t = ((HasTermRepresentation) getContent()).toTerm();
            		piCache = new PredicateIndicator(s + t.getFunctor(), t.getTermsSize());
            	} else {
            		piCache = new PredicateIndicator(s + "not_a_predicate", 0);
            	}
            }
        }
        return piCache;
    }
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.DefaultAILStructure#clone()
	 */
	 public Event clone() {
			if (hasContent()) {
				if (referstoGoal())  {
					return (new Event(getTrigType(), ((Goal) getContent()).clone()));
				} else if (referstoSentMessage()) {
					return (new Event(getTrigType(), getCategory(), ((Message) getContent()).clone()));
				} else {
					return (new Event(getTrigType(), getCategory(), ((Literal) getContent()).clone()));
				}
			} else {
				return (new Event(getCategory()));
			}
		}
		

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		if (isStart()) {
			s.append("start");
		} else {
			if (isAddition())
				s.append("+");
			else
				s.append("x");
			if (referstoGoal()) {
				s.append("!");
				s.append(getContent().toString());
			} else {
				s.append(getContent().toString());
			}
		}
		return s.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.DefaultAILStructure#isEvent()
	 */
	public boolean isEvent() {
		return true;
	}
		   
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.DefaultTerm#unifies(ail.syntax.Unifiable, ail.semantics.Unifier)
	 */
	public boolean unifies(Unifiable e, Unifier u) {
		Event e1 = (Event) e;
		
		if (isStart()) {
			return sameType(e1);
		} else {
			return sameType(e1) && u.unifies(getContent(), e1.getContent());
		}
 		   
	}
	   
	/**
	 * Is the event a variable - as in a reactive plan.
	 */
	public boolean isVar() {
		if (hasContent()) {
			return ((Term) getContent()).isVar();
		} else {
			return false;
		}
	}
	
	/**
	 * Equals if content is a variable.
	 */
	public boolean varequals(DefaultAILStructure s) {
		if (hasContent()) {
			if (!referstoGoal()) {
				return (((Term) getContent()).isVar() && getContent().equals(s.getContent()));
			} else {
				return (((Term) getContent()).isVar() && getContent().equals(s.getContent()));
			}
		} else {
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Unifiable#getVarNames()
	 */
	public List<String> getVarNames() {
		if (hasContent()) {
			List<String> varnames = getContent().getVarNames();
			return varnames;
		}
		return new ArrayList<String>();
	}
	
	public boolean isGround() {
		if (hasContent()) {
			return getContent().isGround();
		} 
		
		return true;
	}
	    
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Unifiable#renameVar(java.lang.String, java.lang.String)
	 */
	public void renameVar(String oldname, String newname) {
		if (hasContent()) {
			getContent().renameVar(oldname, newname);
		}
	}
	
	   public boolean match(Unifiable t1g, Unifier u) {
	    	boolean ok = false;
	    	if (t1g instanceof Event) {
	    		Event e = (Event) t1g;
	    		if (e.getCategory() == getCategory()) {
	    			if (e.hasContent()) {
	    				if (hasContent()) {
	    					ok = u.matchTerms((Term) getContent(), (Term) e.getContent());
	    				}
	    			} else if (!hasContent()) {
	    				ok = true;
	    			}
	    			
	    		}
	    	}

	    	return ok;
	    }

	   public boolean matchNG(Unifiable t1g, Unifier u) {
	    	boolean ok = false;
	    	if (t1g instanceof Event) {
	    		Event e = (Event) t1g;
	    		if (e.getCategory() == getCategory()) {
	    			if (e.hasContent()) {
	    				if (hasContent()) {
	    					ok = u.matchTermsNG((Term) getContent(), (Term) e.getContent());
	    				}
	    			} else if (!hasContent()) {
	    				ok = true;
	    			}
	    			
	    		}
	    	}

	    	return ok;
	    }


}
