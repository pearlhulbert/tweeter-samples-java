package edu.byu.cs.tweeter.client.backgroundTask.handler;

import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;

public class LogoutHandler extends SimpleNotificationHandler {

    public LogoutHandler(SimpleNotificationObserver observer) {
        super(observer);
    }
}
