package edu.byu.cs.tweeter.client.backgroundTask.observer;

import android.os.Message;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticateTaskObserver extends ServiceObserver {
    void handleSuccess(User currUser, AuthToken authToken);
    void displayMessage(String message);
    void setErrorView(Exception e);
    // void startActivity(User currUser);
}
