package edu.byu.cs.tweeter.client.backgroundTask;


import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedTask extends BackgroundTask {

    private AuthToken authToken;
    public AuthenticatedTask(AuthToken authtoken, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authtoken;
    }
}
