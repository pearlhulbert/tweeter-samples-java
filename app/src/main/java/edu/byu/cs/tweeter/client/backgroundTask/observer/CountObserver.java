package edu.byu.cs.tweeter.client.backgroundTask.observer;

import android.os.Message;

public interface CountObserver extends ServiceObserver {
    void handleSuccess(int count);
}
