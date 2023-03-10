package edu.byu.cs.tweeter.client.backgroundTask.handler;

import edu.byu.cs.tweeter.client.backgroundTask.observer.CountObserver;

public class GetFollowingCountHandler extends CountHandler {

    public GetFollowingCountHandler(CountObserver observer) {
        super(observer);
    }
}
