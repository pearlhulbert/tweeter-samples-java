package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";
    private static final String URL_PATH = "/login";

    public LoginTask(String username, String password, Handler messageHandler) {
        super(username, password, messageHandler);
    }

    @Override
    protected Pair<User, AuthToken> authenticateUser() {
        try {
            LoginRequest request = new LoginRequest(alias, password);
            LoginResponse response = getServerFacade().login(request, URL_PATH);
            System.out.println("LoginTask: " + response.getMessage());
            if (response.isSuccess()) {
                Cache.getInstance().setCurrUser(response.getUser());
                Cache.getInstance().setCurrUserAuthToken(response.getAuthToken());
                return new Pair<>(response.getUser(), response.getAuthToken());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
        return null;
    }

}
