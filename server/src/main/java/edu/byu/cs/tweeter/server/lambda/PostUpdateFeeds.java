package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import javax.xml.crypto.Data;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.SendMessageService;
import edu.byu.cs.tweeter.server.service.domain.PostStatus;

public class PostUpdateFeeds implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        DAOFactory factory = new DynamoFactory();
        Status status = new Status();
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String body = msg.getBody();
            System.out.println("Body: " + body);
            status = new Gson().fromJson(body, Status.class);
            System.out.println("status: " + status);
            String alaias = status.getUser().getAlias();
            System.out.println("alaias: " + alaias);
            DataPage<DynamoFollow> followers = new DataPage<>();
            followers.setHasMorePages(true);
            DynamoFollow lastFollow = new DynamoFollow();
            while (followers.getHasMorePages()) {
                if (followers.getValues().size() == 0) {
                    followers = factory.getFollowDAO().getFollowers(status.getUser().getAlias(), 25, null);
                }
                else {
                    lastFollow = followers.getValues().get(followers.getValues().size() - 1);
                    System.out.println("lastFollow: " + lastFollow);
                    followers = factory.getFollowDAO().getFollowers(status.getUser().getAlias(), 25, lastFollow.get_follower_handle());
                }
                System.out.println("followers: " + followers);
                PostStatus postStatus = new PostStatus(status, followers);
                SendMessageService service = new SendMessageService();
                service.sendPostStatusUpdateMessage(postStatus);
            }
        }
        return null;
    }

}
