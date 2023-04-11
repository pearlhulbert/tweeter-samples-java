package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowerCountTask";
    private static final String URL_PATH = "/getfollowercount";

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        try {
            FollowerCountRequest request = new FollowerCountRequest(targetUser, authToken);
            FollowerCountResponse response = getServerFacade().getFollowerCount(request, URL_PATH);

            if (response.isSuccess()) {
                //System.out.println("GetFollowersCountTask: " + response.getCount());
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
}