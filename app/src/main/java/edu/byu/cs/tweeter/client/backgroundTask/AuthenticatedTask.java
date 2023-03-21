package edu.byu.cs.tweeter.client.backgroundTask;


import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatedTask extends BackgroundTask {

    protected AuthToken authToken;
    public AuthenticatedTask(AuthToken authtoken, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authtoken;
    }
}
