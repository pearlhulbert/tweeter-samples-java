package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.SendMessageService;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.server.service.domain.PostStatus;

public class UpdateFeed implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        PostStatus status = new PostStatus();
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String body = msg.getBody();
            System.out.println("Body: " + body);
            status = new Gson().fromJson(body, PostStatus.class);
            System.out.println("status: " + status);
            DAOFactory daoFactory = new DynamoFactory();
            StatusService service = new StatusService(daoFactory);
            FollowService followService = new FollowService();
            DataPage<DynamoFollow> dynamoFollowers = status.getFollowers();
            System.out.println("dynamoFollowers: " + dynamoFollowers);
            System.out.println("dynamoFollowers.getValues(): " + dynamoFollowers.getValues());
            List<User> followers = followService.dataPageToFollowers(dynamoFollowers, daoFactory);
            System.out.println("followers: " + followers);
            service.updateFeed(status.getStatus(), followers);
        }
        return null;
    }
}

