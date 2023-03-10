package edu.byu.cs.tweeter.client.backgroundTask.handler;

import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticateTaskObserver;

public class RegisterHandler extends AuthenticateTaskHandler {

    public RegisterHandler(AuthenticateTaskObserver observer) {
        super(observer);
    }
}
