package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PageTasks<User> {
    private static final String LOG_TAG = "GetFollowingTask";
    private static final String URL_PATH = "/getfollowees";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, messageHandler, targetUser, limit, lastFollowee);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
            String lastFolloweeAlias = lastItem == null ? null : lastItem.getAlias();

            FollowingRequest request = new FollowingRequest(authToken, targetUserAlias, limit, lastFolloweeAlias);
            FollowingResponse response = getServerFacade().getFollowees(request, URL_PATH);

            if (response.isSuccess()) {
               return new Pair<>(response.getFollowees(), response.getHasMorePages());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }
        return null;
    }


}
