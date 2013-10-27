/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.patterns.mediator;

import android.view.View;
import org.puremvc.java.multicore.interfaces.IFunction;
import org.puremvc.java.multicore.interfaces.IMediator;
import org.puremvc.java.multicore.interfaces.INotifier;
import org.puremvc.java.multicore.patterns.observer.Notifier;

import java.util.HashMap;
import java.util.Map;

/**
 * A base <code>IMediator</code> implementation.
 *
 * @see org.puremvc.java.core.view.View View
 */
public class Mediator extends Notifier implements IMediator, INotifier {

	/**
	 * The default name of the <code>Mediator</code>.
	 */
	public static final String NAME = "Mediator";

	/**
	 * The name of the <code>Mediator</code>.
	 */
	protected String mediatorName = null;

    protected HashMap<String, IFunction> observers = null;

    /**
     * Default constructor.
     *
     * JavaFX class only extends Java class with default constructor.
     *
     */
    public Mediator(String mediatorName) {
        observers = new HashMap<String, IFunction>();
        this.mediatorName = mediatorName;
    }

    public View getView()
    {
        return null;
    }

	/**
	 * Get the name of the <code>Mediator</code>.
	 *
	 * @return the name
	 */
	public final String getMediatorName() {
		return this.mediatorName;
	}

    public void registerObserver(String noteName, IFunction listener)
    {
        observers.put(noteName, listener);
    }

    public Map<String, IFunction> getObservers()
    {
        return observers;
    }

    /**
	 * Called by the View when the Mediator is registered.
	 */
	public void onRegister() {
	}

	/**
	 * Called by the View when the Mediator is removed.
	 */
	public void onRemove() {
	}
}
