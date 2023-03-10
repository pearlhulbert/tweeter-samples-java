package edu.byu.cs.tweeter.client.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.User;

public interface SingleObserver<T> extends ServiceObserver{
    void handleSuccess(T param);
}
