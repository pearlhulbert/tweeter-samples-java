package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.CountObserver;

public abstract class CountHandler extends BackgroundTaskHandler<CountObserver> {

    public CountHandler(CountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message data, CountObserver observer) {
        int followerCount = data.getData().getInt(GetFollowersCountTask.FOLLOWER_COUNT_KEY);
        int followingCount = data.getData().getInt(GetFollowingCountTask.FOLLOWING_COUNT_KEY);
        observer.handleSuccess(followerCount, followingCount);
    }
}
