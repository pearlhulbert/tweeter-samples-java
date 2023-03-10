package edu.byu.cs.tweeter.client.backgroundTask.observer;

import android.os.Message;

import java.util.List;

public interface PageTaskObserver<T> extends ServiceObserver {
     void handleSuccess(List<T> items, boolean hasMorePages);
}
