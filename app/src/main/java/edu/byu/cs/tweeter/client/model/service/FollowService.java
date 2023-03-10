package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.CountObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.PageTaskObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SingleObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service {

    private BackgroundTaskUtils utils = createUtils();

    public interface SimpleObserver extends SimpleNotificationObserver {
    }

    public interface PageObserver extends PageTaskObserver<User> {
    }

    public interface CountingObserver extends CountObserver {
        void updateFollowersCount(int count);
        void updateFolloweeCount(int count);
    }

    public interface RelObserver extends SingleObserver<Boolean> {
        void updateSelectedUserFollowingAndFollowers();
        void updateFollowRelationship(boolean isFollower);
    }

    public void loadMoreFollowingItems(User user, int pageSize, User lastFollowee, PageObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        utils.runTask(getFollowingTask);
    }

    public void loadMoreFollowersItems(User user, int pageSize, User lastFollower, PageObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        utils.runTask(getFollowersTask);
    }

    public void unfollowUser(User selectedUser, RelObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new IsFollowerHandler(observer));
        utils.runTask(unfollowTask);
    }

    public void followUser(User selectedUser, RelObserver observer) {
        BackgroundTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new IsFollowerHandler(observer));
       utils.runTask(followTask);
    }

    public void getCounts(User selectedUser, CountObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);

    }

    public void isFollower(User selectedUser, RelObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        utils.runTask(isFollowerTask);
    }


}

