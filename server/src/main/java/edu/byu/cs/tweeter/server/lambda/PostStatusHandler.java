package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse>
{
    public static void main(String[] args) {
        LoginHandler loginHandler = new LoginHandler();
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        LoginResponse loginResponse = loginHandler.handleRequest(loginRequest, null);
        AuthToken authToken = loginResponse.getAuthToken();
        DAOFactory daoFactory = new DynamoFactory();
        DynamoUser dUser1 = daoFactory.getUserDAO().getUser("@user1");
        User user1 = daoFactory.getUserDAO().dynamoUserToUser(dUser1);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        String dateStr = String.valueOf(date.getTime());
        List<String> test = new ArrayList<>();
        Status status = new Status("Hello world", user1, dateStr, test, test);
        PostStatusRequest request = new PostStatusRequest(status, authToken);
        PostStatusHandler handler = new PostStatusHandler();
        PostStatusResponse response = handler.handleRequest(request, null);
        System.out.println(response.isSuccess());
    }

    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {
        StatusService service = new StatusService(new DynamoFactory());
        return service.postStatus(request);
    }
}
