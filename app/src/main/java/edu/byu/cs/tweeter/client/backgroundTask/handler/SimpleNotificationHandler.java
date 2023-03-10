package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;
import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;

public class SimpleNotificationHandler extends BackgroundTaskHandler<SimpleNotificationObserver> {

    // used for handlers that don't pass parameters back

    public SimpleNotificationHandler(SimpleNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message data, SimpleNotificationObserver observer) {
        observer.handleSuccess();
    }
}
