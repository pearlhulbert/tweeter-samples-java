package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask {

    protected final String alias;
    protected final String password;

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    protected User authenticatedUser;

    protected AuthToken authToken;

    protected AuthenticateTask(String alias, String password, Handler messageHandler) {
        super(messageHandler);
        this.alias = alias;
        this.password = password;
    }

    protected abstract Pair<User, AuthToken> authenticateUser();


    protected void processTask() {
        Pair<User, AuthToken> userAuthTokenPair = authenticateUser();
        authenticatedUser = userAuthTokenPair.getFirst();
        authToken = userAuthTokenPair.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

}
