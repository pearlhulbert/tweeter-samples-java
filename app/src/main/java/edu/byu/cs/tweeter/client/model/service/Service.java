package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;

public abstract class Service {

    public BackgroundTaskUtils createUtils() {
        return new BackgroundTaskUtils();
    }
}
