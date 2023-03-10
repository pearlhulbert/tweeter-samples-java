package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticateTaskObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticateTaskHandler extends BackgroundTaskHandler<AuthenticateTaskObserver> {

        public AuthenticateTaskHandler(AuthenticateTaskObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccess(Message data, AuthenticateTaskObserver observer) {
                User currUser = (User) data.getData().getSerializable(AuthenticateTask.USER_KEY);
                AuthToken authToken = (AuthToken) data.getData().getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);
                Cache.getInstance().setCurrUser(currUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);
                observer.handleSuccess(currUser, authToken);
        }

}
