package edu.byu.cs.tweeter.client.backgroundTask.handler;

import edu.byu.cs.tweeter.client.backgroundTask.observer.PageTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowingTask.
 */
public class GetFollowingHandler extends PageTaskHandler<User> {

    public GetFollowingHandler(PageTaskObserver<User> observer) {
        super(observer);
    }

}
