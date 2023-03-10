package edu.byu.cs.tweeter.client.backgroundTask.observer;

public interface SimpleNotificationObserver extends ServiceObserver {
    void handleSuccess();
    void displayMessage(String message);

}
