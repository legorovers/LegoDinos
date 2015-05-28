// ----------------------------------------------------------------------------
// Copyright (C) 2014 Louise A. Dennis, Michael Fisher
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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import ail.semantics.AILAgent;

/**
 * A Reference to a message that may appear in a Guard.  It extends Message purely to make type matching work with the EvalutionBaseIterator.
 * I am frankly amazed this works and suspect there is an uncaught bug somewhere.
 * @author louiseadennis
 *
 */
public class GMessage implements GuardAtom<Message> {
	StringTerm sender;
	StringTerm receiver;
	StringTerm threadId;
	int performative;
	Term content;
	
	private byte category = DefaultAILStructure.AILSent;

	/**
	 * If an agent has several structures of a particular type.
	 * E.g. several belief bases, the one to be consulted for this
	 * GBelief is the one numbered DBnum.
	 * 
	 */
	private StringTerm DBnum = new StringTermImpl(AILAgent.AILdefaultBBname);
	

    /**
     * Constructor for a new message in a new thread.
     * 
     * @param ilf the illocutionary force of the message.
     * @param s the sender of the message.
     * @param r the receiver of the message.
     * @param c the content of the message.
     */
    public GMessage(byte sr, int ilf, StringTerm s, StringTerm r, Term c, StringTerm thid) {
    	sender = s;
    	receiver = r;
    	content = c;
    	performative = ilf;
       	category = sr;
       	threadId = thid;
    }
    
    /**
     * Constructor
     * @param sr
     * @param ilf
     * @param s
     * @param r
     * @param c
     */
    public GMessage(byte sr, int ilf, StringTerm s, StringTerm r, Term c) {
    	sender = s;
    	receiver = r;
    	content = c;
    	performative = ilf;
       	category = sr;
       	threadId = new StringTermImpl("thid");
    }

    /**
     * Constructor for a new message in an existing thread.
     * 
     * @param ilf the illocutionary force of the message.
     * @param s the sender of the message.
     * @param r the receiver of the message.
     * @param c the content of the message.
     * @param thid the thread of the message.
     */
    public GMessage(byte sr, int ilf, String s, String r, Term c, String thid) {
    	this(sr, ilf, new StringTermImpl(s), new StringTermImpl(r), c, new StringTermImpl(thid));
      }
     
    
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GLogicalFormula#logicalConsequence(ail.semantics.AILAgent, ail.syntax.Unifier, java.util.List)
	 */
	public Iterator<Unifier> logicalConsequence(AILAgent ag, Unifier un, List<String> varnames) {
		List<Message> ul = new ArrayList<Message>();
		if (category == DefaultAILStructure.AILSent) {
			ul.addAll(ag.getOutbox());
		} else {
			ul.addAll(ag.getInbox());
		}
		
		EvaluationBase<Message> leb = new ListEvaluationBase<Message>(ul);
		return new EvaluationBaseIterator<Message>(leb, un, this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public GMessage clone() {
		return(new GMessage(category, performative, (StringTerm) sender.clone(), (StringTerm) receiver.clone(), (Term) content.clone(), (StringTerm) threadId.clone()));
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#isTrivial()
	 */
	public boolean isTrivial() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#getEB()
	 */
	public StringTerm getEB() {
		return DBnum;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#getEBType()
	 */
	public byte getEBType() {
		return category;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#hasLogicalContent()
	 */
	public boolean hasLogicalContent() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#getLogicalContent()
	 */
	public Predicate getLogicalContent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#isVar()
	 */
	public boolean isVar() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#getPredicateIndicator()
	 */
	public PredicateIndicator getPredicateIndicator() {
		return null;
	}
	
	/**
	 * Return the sender as a term.
	 * @return
	 */
	public StringTerm getSenderTerm() {
		return sender;
	}
	
	/**
	 * Return the reciever as a term.
	 * @return
	 */
	public StringTerm getReceiverTerm() {
		return receiver;
	}
	
	/***
	 * Return the performative.
	 * @return
	 */
	public int getIlf() {
		return performative;
	}
	
	/**
	 * Getter for the thread ID.
	 * @return
	 */
	public StringTerm getThdID() {
		return threadId;
	}
	
	/**
	 * Getter for logical content.
	 * @return
	 */
	public Term getContent() {
		return content;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Unifiable#unifies(ail.syntax.Unifiable, ail.syntax.Unifier)
	 */
	public boolean unifies(Unifiable t, Unifier u) {
		if (t instanceof GMessage) {
			GMessage tm = (GMessage) t;
			if (sender.unifies(tm.getSenderTerm(), u)) {
				if (receiver.unifies(tm.getReceiverTerm(), u)) {
					if (performative == tm.getIlf()) {
						if (content.unifies(tm.getContent(), u)) {
							return threadId.unifies(tm.getThdID(), u);
						}
					}
				}
			}
		}
		
		if (t instanceof Message) {
			return unifieswith((Message) t, u, "");
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.GuardAtom#unifieswith(ail.syntax.Unifiable, ail.syntax.Unifier, java.lang.String)
	 */
	public boolean unifieswith(Message m, Unifier u, String s) {
		if (sender.unifies(new StringTermImpl(m.getSender()), u) || sender.unifies(new Predicate(m.getSender()), u)) {
			if (receiver.unifies(new StringTermImpl(m.getReceiver()), u) || receiver.unifies(new Predicate(m.getReceiver()), u)) {
				if (performative == m.getIlForce()) {
					if (content.unifies(m.getPropCont(), u)) {
						return threadId.unifies(m.getThreadId(), u);
					}
				}
			}
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#standardise_apart(ail.syntax.Unifiable, ail.syntax.Unifier, java.util.List)
	 */
	public void standardise_apart(Unifiable t, Unifier u, List<String> varnames) {
	   	List<String> tvarnames = t.getVarNames();
    	List<String> myvarnames = getVarNames();
    	tvarnames.addAll(varnames);
    	ArrayList<String> replacednames = new ArrayList<String>();
    	ArrayList<String> newnames = new ArrayList<String>();
    	for (String s:myvarnames) {
    		if (tvarnames.contains(s)) {
    			if (!replacednames.contains(s)) {
    				String s1 = DefaultAILStructure.generate_fresh(s, tvarnames, myvarnames, newnames, u);
    				renameVar(s, s1);
    				u.renameVar(s, s1);
    			}
    		}
    	}
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#getVarNames()
	 */
	public List<String> getVarNames() {
    	ArrayList<String> varnames = new ArrayList<String>();
    	varnames.addAll(sender.getVarNames());
    	varnames.addAll(receiver.getVarNames());
    	varnames.addAll(content.getVarNames());
    	varnames.addAll(threadId.getVarNames());
		return varnames;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#renameVar(java.lang.String, java.lang.String)
	 */
	public void renameVar(String oldname, String newname) {
		sender.renameVar(oldname, newname);
		receiver.renameVar(oldname, newname);
		content.renameVar(oldname, newname);
		threadId.renameVar(oldname, newname);

		
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#match(ail.syntax.Unifiable, ail.syntax.Unifier)
	 */
	public boolean match(Unifiable t, Unifier u) {
		if (t instanceof GMessage) {
			GMessage tm = (GMessage) t;
			if (sender.match(tm.getSenderTerm(), u)) {
				if (receiver.match(tm.getReceiverTerm(), u)) {
					if (performative == tm.getIlf()) {
						if (content.match(tm.getContent(), u)) {
							return threadId.match(tm.getThdID(), u);
						}
					}
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#matchNG(ail.syntax.Unifiable, ail.syntax.Unifier)
	 */
	public boolean matchNG(Unifiable t, Unifier u) {
		if (t instanceof GMessage) {
			GMessage tm = (GMessage) t;
			if (sender.matchNG(tm.getSenderTerm(), u)) {
				if (receiver.matchNG(tm.getReceiverTerm(), u)) {
					if (performative == tm.getIlf()) {
						if (content.matchNG(tm.getContent(), u)) {
							return threadId.matchNG(tm.getThdID(), u);
						}
					}
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#isGround()
	 */
	public boolean isGround() {
		return (sender.isGround() & receiver.isGround() & content.isGround() & threadId.isGround());
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#apply(ail.syntax.Unifier)
	 */
	public boolean apply(Unifier theta) {
		return (sender.apply(theta) && receiver.apply(theta) && content.apply(theta) && threadId.apply(theta));
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#makeVarsAnnon()
	 */
	public void makeVarsAnnon() {
		sender.makeVarsAnnon();
		receiver.makeVarsAnnon();
		content.makeVarsAnnon();
		threadId.makeVarsAnnon();
	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Message#strip_varterm()
	 */
	public Unifiable strip_varterm() {
		return new GMessage(category, performative, (StringTerm) sender.strip_varterm(), (StringTerm) receiver.strip_varterm(), (Term) content.strip_varterm(), (StringTerm) threadId.strip_varterm());
	}
	
	public Unifiable resolveVarsClusters() {
		return new GMessage(category, performative, (StringTerm) sender.resolveVarsClusters(), (StringTerm) receiver.resolveVarsClusters(), (Term) content.resolveVarsClusters(), (StringTerm) threadId.resolveVarsClusters());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
        s.append("<").append(threadId).append(",").append(sender).append(",").append(performative);
        s.append(",").append(receiver).append(",").append(content).append(">");
        String s1 = s.toString();
        return s1;
    }


}
