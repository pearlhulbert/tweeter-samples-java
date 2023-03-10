package edu.byu.cs.tweeter.client.backgroundTask.handler;

import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticateTaskObserver;

/**
 * Message handler (i.e., observer) for LoginTask
 */
public class LoginHandler extends AuthenticateTaskHandler {

    public LoginHandler(AuthenticateTaskObserver observer) {
        super(observer);
    }

}
