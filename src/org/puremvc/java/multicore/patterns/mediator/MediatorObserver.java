package org.puremvc.java.multicore.patterns.mediator;

import android.util.Log;
import org.puremvc.java.multicore.interfaces.IFunction;
import org.puremvc.java.multicore.interfaces.IMediator;
import org.puremvc.java.multicore.interfaces.INotification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: guido
 * Date: 8/30/13
 * Time: 2:26 PM
 */
public class MediatorObserver
{
    private Object target;
    private Method method;
    private String notificationName;

    public MediatorObserver(Object target, Method method, String notificationName)
    {
        this.target = target;
        this.notificationName = notificationName;
        this.method = method;
        method.setAccessible(true);
    }

    public void handleNotification(INotification note)
    {
        try {
            method.invoke(target, note);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Could invoke method during handleNotification", e);
        }
    }

    public String getNotificationName()
    {
        return notificationName;
    }
}
