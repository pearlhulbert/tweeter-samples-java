package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends AuthenticatedTask {

    private static final String LOG_TAG = "GetFollowingCountTask";
    private static final String URL_PATH = "/getfollowingcount";
    public static final String FOLLOWING_COUNT_KEY = "following-count";
    protected User targetUser;
    private int followingCount;

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
    }

    private int runCountTask() {
        try {
            FollowingCountRequest request = new FollowingCountRequest(targetUser, authToken);
            FollowingCountResponse response = getServerFacade().getFollowingCount(request, URL_PATH);

            if (response.isSuccess()) {
                System.out.println("Following:" + response.getCount());
                return response.getCount();
            }
            else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
        return 0;
    }

    @Override
    protected void processTask() {
        this.followingCount = runCountTask();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(FOLLOWING_COUNT_KEY, followingCount);
    }

}