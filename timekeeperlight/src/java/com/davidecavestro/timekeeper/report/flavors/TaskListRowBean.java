/*
 * CumulateLocalProgressesRowBean.java
 *
 * Created on January 20, 2007, 7:44 PM
 */

package com.davidecavestro.timekeeper.report.flavors;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Dati di riga per il report avanzamenti giornalieri con dettaglio.
 *
 * @author Davide Cavestro
 */
public class TaskListRowBean extends Object implements Serializable {
	
	private PropertyChangeSupport propertySupport;
	
	public TaskListRowBean () {
		propertySupport = new PropertyChangeSupport (this);
	}
	
	public void addPropertyChangeListener (PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener (listener);
	}
	
	public void removePropertyChangeListener (PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener (listener);
	}

	/**
	 * Holds value of property taskName.
	 */
	private String taskName;

	/**
	 * Getter for property taskName.
	 * @return Value of property taskName.
	 */
	public String getTaskName () {
		return this.taskName;
	}

	/**
	 * Setter for property taskName.
	 * @param taskName New value of property taskName.
	 */
	public void setTaskName (String taskName) {
		this.taskName = taskName;
	}

	/**
	 * Holds value of property duration.
	 */
	private Long duration;

	/**
	 * Getter for property duration.
	 * @return Value of property duration.
	 */
	public Long getDuration () {
		return this.duration;
	}

	/**
	 * Setter for property duration.
	 * @param duration New value of property duration.
	 */
	public void setDuration (Long duration) {
		this.duration = duration;
	}

	/**
	 * Holds value of property taskDescription.
	 */
	private String taskDescription;

	/**
	 * Getter for property taskDescription.
	 * @return Value of property taskDescription.
	 */
	public String getTaskDescription () {
		return this.taskDescription;
	}

	/**
	 * Setter for property taskDescription.
	 * @param taskDescription New value of property taskDescription.
	 */
	public void setTaskDescription (String taskDescription) {
		this.taskDescription = taskDescription;
	}

	/**
	 * Holds value of property actionsNotes.
	 */
	private String actionsNotes;

	/**
	 * Getter for property actionsNotes.
	 * @return Value of property actionsNotes.
	 */
	public String getActionsNotes () {
		return this.actionsNotes;
	}

	/**
	 * Setter for property actionsNotes.
	 * @param actionsNotes New value of property actionsNotes.
	 */
	public void setActionsNotes (String actionsNotes) {
		this.actionsNotes = actionsNotes;
	}

	/**
	 * Holds value of property taskHierarchy.
	 */
	private String taskHierarchy;

	/**
	 * Getter for property taskHierarchy.
	 * @return Value of property taskHierarchy.
	 */
	public String getTaskHierarchy () {
		return this.taskHierarchy;
	}

	/**
	 * Setter for property taskHierarchy.
	 * @param taskHierarchy New value of property taskHierarchy.
	 */
	public void setTaskHierarchy (String taskHierarchy) {
		this.taskHierarchy = taskHierarchy;
	}

	
	private java.sql.Timestamp startDate;
	
	public java.sql.Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(java.sql.Timestamp startDate) {
		this.startDate = startDate;
	}

	private java.sql.Timestamp finishDate;
	
	public java.sql.Timestamp getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(java.sql.Timestamp finishDate) {
		this.finishDate = finishDate;
	}

	
	/**
	 * Crea una collezione di bean a scopo di test.
	 */
	public static Collection<TaskListRowBean> createBeanCollection () {
		final Collection<TaskListRowBean> list = new ArrayList<TaskListRowBean> ();

		{
			final TaskListRowBean r = new TaskListRowBean ();
			r.setActionsNotes ("test 1\ntest 2\ntest 3");
			r.setDuration (3l);
			r.setTaskDescription ("task di test");
			r.setTaskHierarchy ("root/task 1");
			r.setTaskName ("task 1.1");
			list.add (r);
		}
		
		{
			final TaskListRowBean r = new TaskListRowBean ();
			r.setActionsNotes ("test a");
			r.setDuration (4l);
			r.setTaskDescription ("task di test 2");
			r.setTaskHierarchy ("root/task 1");
			r.setTaskName ("task 1.2");
			list.add (r);
		}
		
		
		return list;
	}
}