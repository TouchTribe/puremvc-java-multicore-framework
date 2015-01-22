/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.patterns.observer;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.facade.Facade;

/**
 * A Base <code>INotifier</code> implementation.
 *
 * <P>
 * <code>MacroCommand, Command, Mediator</code> and <code>Proxy</code> all
 * have a need to send <code>Notifications</code>.
 * <P>
 * <P>
 * The <code>INotifier</code> interface provides a common method called
 * <code>sendNotification</code> that relieves implementation code of the
 * necessity to actually construct <code>Notifications</code>.
 * </P>
 *
 * <P>
 * The <code>Notifier</code> class, which all of the above mentioned classes
 * extend, provides an initialized reference to the <code>Facade</code>
 * Singleton, which is required for the convienience method for sending
 * <code>Notifications</code>, but also eases implementation as these classes
 * have frequent <code>Facade</code> interactions and usually require access
 * to the facade anyway.
 * </P>
 *
 * @see org.puremvc.java.patterns.facade.Facade Facade
 * @see org.puremvc.java.patterns.mediator.Mediator Mediator
 * @see org.puremvc.java.patterns.proxy.Proxy Proxy
 * @see org.puremvc.java.patterns.command.SimpleCommand SimpleCommand
 * @see org.puremvc.java.patterns.command.MacroCommand MacroCommand
 */
public class Notifier {
	// The Multiton Key for this app
	protected String multitonKey = null;

	protected Facade getFacade() {
		if (multitonKey == null) {
			throw new RuntimeException("Notifier not initialized");
		}
		return Facade.getInstance(multitonKey);
	}

	/**
	 * Send an <code>INotification</code>s.
	 *
	 * <P>
	 * Keeps us from having to construct new note instances in our
	 * implementation code.
	 *
	 * @param noteName
	 *            the name of the notiification to send
	 * @param body
	 *            the body of the note (optional)
	 * @param type
	 *            the type of the note (optional)
	 */
	public void sendNotification(String noteName, Object body,
			String type) {
		getFacade().sendNotification(noteName, body, type);
	}

	/**
	 * Send an <code>INotification</code>s.
	 *
	 * <P>
	 * Keeps us from having to construct new note instances in our
	 * implementation code.
	 *
	 * @param noteName
	 *            the name of the notiification to send
	 * @param body
	 *            the body of the note (optional)
	 */
	public void sendNotification(String noteName, Object body) {
		getFacade().sendNotification( noteName, body);
	}

	/**
	 * Send an <code>INotification</code>s.
	 *
	 * <P>
	 * Keeps us from having to construct new note instances in our
	 * implementation code.
	 *
	 * @param noteName
	 *            the name of the notiification to send
	 */
	public void sendNotification(String noteName) {
		getFacade().sendNotification(noteName);
	}

    public void sendNotification(INotification note)
    {
        getFacade().sendNotification(note);
    }

	/**
	 * Initialize this INotifier instance.
	 * <P>
	 * This is how a Notifier gets its multitonKey.
	 * Calls to sendNotification or to access the
	 * facade will fail until after this method
	 * has been called.</P>
	 *
	 * <P>
	 * Mediators, Commands or Proxies may override
	 * this method in order to send notes
	 * or access the Multiton Facade instance as
	 * soon as possible. They CANNOT access the facade
	 * in their constructors, since this method will not
	 * yet have been called.</P>
	 *
	 * @param key the multitonKey for this INotifier to use
	 */
	public void initializeNotifier(String key) {
		multitonKey = key;
	}
}
