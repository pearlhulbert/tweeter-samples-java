package edu.byu.cs.tweeter.client.backgroundTask.handler;

import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;

public class PostStatusHandler extends SimpleNotificationHandler {

    public PostStatusHandler(SimpleNotificationObserver observer) {
        super(observer);
    }

}
