package edu.byu.cs.tweeter.client.backgroundTask.handler;

import edu.byu.cs.tweeter.client.backgroundTask.observer.CountObserver;

public class GetFollowersCountHandler extends CountHandler {

    public GetFollowersCountHandler(CountObserver observer) {
        super(observer);
    }
}
