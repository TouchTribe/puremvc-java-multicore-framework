/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.patterns.observer;

import org.puremvc.java.multicore.interfaces.IFunction;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.interfaces.IObserver;

/**
 * A base <code>IObserver</code> implementation.
 *
 * <P>
 * An <code>Observer</code> is an object that encapsulates information about
 * an interested object with a method that should be called when a particular
 * <code>INotification</code> is broadcast.
 * </P>
 *
 * <P>
 * In PureMVC, the <code>Observer</code> class assumes these responsibilities:
 * <UL>
 * <LI>Encapsulate the note (callback) method of the interested object.</LI>
 * <LI>Encapsulate the note context (this) of the interested object.</LI>
 * <LI>Provide methods for setting the note method and context.</LI>
 * <LI>Provide a method for notifying the interested object.</LI>
 * </UL>
 *
 * @see org.puremvc.java.core.view.View View
 * @see org.puremvc.java.patterns.observer.Notification Notification
 */
public class Observer implements IObserver {

	private Object context;

	private IFunction notify;

	/**
	 * Constructor.
	 *
	 * <P>
	 * The note method on the interested object should take one
	 * parameter of type <code>INotification</code>
	 * </P>
	 *
	 * @param notify
	 *            the note method of the interested object
	 * @param context
	 *            the note context of the interested object
	 */
	public Observer(IFunction notify, Object context) {
		this.setNotifyContext(context);
		this.setNotifyMethod(notify);
	}

	/**
	 * Compare an object to the note context.
	 *
	 * @param object
	 *            the object to compare
	 * @return boolean indicating if the object and the note context are
	 *         the same
	 */
	public boolean compareNotifyContext(Object object) {
		return this.context == object;
	}

	/**
	 * Notify the interested object.
	 *
	 * @param note
	 *            the <code>INotification</code> to pass to the interested
	 *            object's note method.
	 */
	public void notifyObserver(INotification note) {
		this.getNotifyMethod().onNotify( note );
	}

	/**
	 * Set the note context.
	 *
	 * @param notifyContext
	 *            the note context (this) of the interested object.
	 */
	public void setNotifyContext(Object notifyContext) {
		this.context = notifyContext;
	}

	/**
	 * Set the note method.
	 *
	 * <P>
	 * The note method should take one parameter of type
	 * <code>INotification</code>.
	 * </P>
	 *
	 * @param notifyMethod
	 *            the note (callback) method of the interested object.
	 */
	public void setNotifyMethod(IFunction notifyMethod) {
		this.notify = notifyMethod;
	}

	/**
	 * Get the note method.
	 *
	 * @return the note (callback) method of the interested object.
	 */
	public IFunction getNotifyMethod() {
		return this.notify;
	}

	/**
	 * Get the note context.
	 *
	 * @return the note context (<code>this</code>) of the
	 *         interested object.
	 */
	public Object getNotifyContext() {
		return this.context;
	}

}
