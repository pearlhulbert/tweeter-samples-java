package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SingleObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends BackgroundTaskHandler<SingleObserver<User>> {


    public GetUserHandler(SingleObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message data, SingleObserver observer) {
        User user = (User) data.getData().getSerializable(GetUserTask.USER_KEY);
        observer.handleSuccess(user);
    }
}
