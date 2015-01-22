/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.core.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.puremvc.java.multicore.interfaces.*;
import org.puremvc.java.multicore.patterns.observer.Observer;

/**
 * A Multiton <code>IView</code> implementation.
 *
 * <P>
 * In PureMVC, the <code>View</code> class assumes these responsibilities:
 * <UL>
 * <LI>Maintain a cache of <code>IMediator</code> instances.</LI>
 * <LI>Provide methods for registering, retrieving, and removing <code>IMediators</code>.</LI>
 * <LI>Notifiying <code>IMediators</code> when they are registered or removed.</LI>
 * <LI>Managing the observer lists for each <code>INotification</code> in the application.</LI>
 * <LI>Providing a method for attaching <code>IObservers</code> to an <code>INotification</code>'s observer list.</LI>
 * <LI>Providing a method for broadcasting an <code>INotification</code>.</LI>
 * <LI>Notifying the <code>IObservers</code> of a given <code>INotification</code> when it broadcast.</LI>
 * </UL>
 *
 * @see org.puremvc.java.multicore.patterns.mediator.Mediator Mediator
 * @see org.puremvc.java.multicore.patterns.observer.Observer Observer
 * @see org.puremvc.java.multicore.patterns.observer.Notification Notification
 */
public class View implements IView {
    private final static Logger logger = Logger.getLogger(View.class.getName());
    // Mapping of Mediator names to Mediator instances
	// Mapping of Notification names to Observer lists
	private HashMap<String,List<IObserver>> observerMap;
	private HashMap<String,IMediator> mediatorMap;

	/**
	 * 	 The Multiton Key for this Core.
	 */
	protected String multitonKey;

	protected static Map<String, View> instanceMap = new HashMap<String, View>();

	/**
	 * Constructor.
	 *
	 * <P>
	 * This <code>IView</code> implementation is a Multiton,
	 * so you should not call the constructor
	 * directly, but instead call the static Multiton
	 * Factory method <code>View.getInstance( multitonKey )</code>
	 *
	 * @throws Error Error if instance for this Multiton key has already been constructed
	 *
	 */
	protected View(String key) {
		this.multitonKey = key;
		instanceMap.put(multitonKey, this);
		this.mediatorMap = new HashMap<String,IMediator>();
		this.observerMap = new HashMap<String,List<IObserver>>();
		initializeView();
	}

	/**
	 * Initialize the Singleton View instance.
	 *
	 * <P>
	 * Called automatically by the constructor, this is your opportunity to
	 * initialize the Singleton instance in your subclass without overriding
	 * the constructor.
	 * </P>
	 *
	 */
	protected void initializeView() {
	}

	/**
	 * View Singleton Factory method.
	 *
	 * @return the Singleton instance of <code>View</code>
	 */
	public synchronized static View getInstance(String key) {
		if(instanceMap.get(key) == null) {
			new View(key);
		}
		return instanceMap.get(key);
	}

	/**
	 * Notify the <code>Observers</code> for a particular
	 * <code>Notification</code>.
	 *
	 * <P>
	 * All previously attached <code>Observers</code> for this
	 * <code>Notification</code>'s list are notified and are passed a
	 * reference to the <code>Notification</code> in the order in which they
	 * were registered.
	 * </P>
	 *
	 * @param note
	 *             the <code>Notification</code> to notify
	 *             <code>Observers</code> of.
	 */
	public void notifyObservers(INotification note) {
        if (note.isLoggingEnabled()) {
            logger.fine(note.toString());
        }
		List<IObserver> observers_ref = (List<IObserver>) this.observerMap.get(note.getName());
		if (observers_ref != null) {
			// Copy observers from reference array to working array,
            // since the reference array may change during the
            //note loop
			Object[] observers = (Object[])observers_ref.toArray();

			// Notify Observers from the working array
			for (int i = 0; i < observers.length; i++) {
				IObserver observer = (IObserver)observers[i];
				observer.notifyObserver(note);
			}
		}
	}

	/**
	 * Remove the observer for a given notifyContext from an observer list for a given Notification name.
	 * <P>
	 * @param noteName which observer list to remove from
	 * @param notifyContext remove the observer with this object as its notifyContext
	 */
	public void removeObserver(String noteName, Object notifyContext) {
		// the observer list for the note under inspection
		List<IObserver> observers = observerMap.get(noteName);

		if (observers != null) {
			// find the observer for the notifyContext
			for(int i=0;i<observers.size();i++) {
				Observer observer = (Observer) observers.get(i);
				if (observer.compareNotifyContext(notifyContext) == true) {
					observers.remove(observer);
				}
			}
			// Also, when a Notification's Observer list length falls to
			// zero, delete the note key from the observer map
			if (observers.size() == 0) {
				observerMap.remove(noteName);
			}
		}
	}

	/**
	 * Register an <code>Mediator</code> instance with the <code>View</code>.
	 *
	 * <P>
	 * Registers the <code>Mediator</code> so that it can be retrieved by
	 * name, and further interrogates the <code>Mediator</code> for its
	 * <code>Notification</code> interests.
	 * </P>
	 * <P>
	 * If the <code>Mediator</code> returns any <code>Notification</code>
	 * names to be notified about, an <code>Observer</code> is created
	 * encapsulating the <code>Mediator</code> instance's
	 * <code>handleNotification</code> method and registering it as an
	 * <code>Observer</code> for all <code>Notifications</code> the
	 * <code>Mediator</code> is interested in.
	 * </p>
	 *
	 * @param mediator
	 *             the name to associate with this <code>IMediator</code>
	 *             instance
	 */
	public void registerMediator(final IMediator mediator) {
		if (!this.mediatorMap.containsKey(mediator.getMediatorName())) {
			mediator.initializeNotifier(multitonKey);

			// Register the Mediator for retrieval by name
			this.mediatorMap.put(mediator.getMediatorName(), mediator);

            // alert the mediator that it has been registered
            logger.finer("onRegister: " + mediator.getMediatorName());
            mediator.onRegister();

            for (Map.Entry<String, IFunction> entry : mediator.getObservers().entrySet()) {
                final String notificationName = entry.getKey();
                final IFunction listener = entry.getValue();
                IFunction function = new IFunction() {
                    public void onNotify(INotification note) {
                        if (note.isLoggingEnabled()) {
                            logger.finer(mediator.getClass().getSimpleName() + ": Observed " + notificationName);
                        }
                        listener.onNotify(note);
                    }
                };
                Observer observer = new Observer(function, mediator);
                registerObserver(notificationName, observer);
            }
		} else {
            logger.warning("Mediator " + mediator.getMediatorName() + " already registered");
        }
	}

	/**
	 * Register an <code>Observer</code> to be notified of
	 * <code>INotifications</code> with a given name.
	 *
	 * @param noteName
	 *             the name of the <code>Notifications</code> to notify this
	 *             <code>Observer</code> of
	 * @param observer
	 *             the <code>Observer</code> to register
	 */
	public void registerObserver(String noteName, IObserver observer) {
		if (this.observerMap.get(noteName) == null) {
			this.observerMap.put(noteName, new ArrayList<IObserver>());
		}
		List<IObserver> observers = (List<IObserver>) this.observerMap.get(noteName);
		observers.add(observer);
	}

	/**
	 * Remove an <code>Mediator</code> from the <code>View</code>.
	 *
	 * @param mediatorName
	 *             name of the <code>Mediator</code> instance to be removed.
	 */
	public IMediator removeMediator(String mediatorName) {
		// Retrieve the named mediator
		IMediator mediator = mediatorMap.get(mediatorName);

		if(mediator != null) {
            for (Map.Entry<String, IFunction> entry : mediator.getObservers().entrySet()) {
                removeObserver(entry.getKey(), mediator);
            }


			// remove the mediator from the map
			mediatorMap.remove(mediatorName);

            logger.finer("onRemove: " + mediator.getMediatorName());
			// alert the mediator that it has been removed
			mediator.onRemove();
		}
		return mediator;
	}

	/**
	 * Retrieve an <code>Mediator</code> from the <code>View</code>.
	 *
	 * @param mediatorName
	 *             the name of the <code>Mediator</code> instance to
	 *             retrieve.
	 * @return the <code>Mediator</code> instance previously registered with
	 *         the given <code>mediatorName</code>.
	 */
	public IMediator retrieveMediator(String mediatorName) {
		return (IMediator) this.mediatorMap.get(mediatorName);
	}

	/**
	 * Check if a Mediator is registered or not
	 *
	 * @param mediatorName
	 * @return whether a Mediator is registered with the given <code>mediatorName</code>.
	 */
	public boolean hasMediator(String mediatorName) {
		return mediatorMap.containsKey(mediatorName);
	}

	/**
	 * Remove an IView instance
	 *
	 * @param multitonKey of IView instance to remove
	 */
	public synchronized static void removeView(String key) {
		instanceMap.remove(key);
	}
}
