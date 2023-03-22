package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {

    private ServerFacade serverFacade;

    @BeforeEach
    public void setup() {
        serverFacade = new ServerFacade();
    }

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest("test", "test", "test", "test", "test");
        User user = new User("Allen", "Anderson", "@allen", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken token = new AuthToken();
        RegisterResponse expectedResponse = new RegisterResponse(user, token);
        RegisterResponse response = null;
        try {
            response = serverFacade.register(request, "/register");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
    }

    @Test
    public void testGetFollowers() {
        AuthToken token = new AuthToken();
        FollowerRequest request = new FollowerRequest(token, "alias", 10, "alias");
        List<User> expectedFollowers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedFollowers.add(FakeData.getInstance().getFakeUsers().get(i));
        }
        FollowerResponse expectedResponse = new FollowerResponse(expectedFollowers, true);
        FollowerResponse response = null;
        try {
            response = serverFacade.getFollowers(request, "/getfollowers");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(expectedResponse.getFollowers(), response.getFollowers());
        Assertions.assertEquals(expectedResponse.getHasMorePages(), response.getHasMorePages());
    }

    @Test
    public void testGetFollowerCount() {
        AuthToken token = new AuthToken();
        User targetUser = new User("Allen", "Anderson", "@allen", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        FollowerCountRequest request = new FollowerCountRequest(targetUser, token);
        int expectedCount = 21;
        FollowerCountResponse expectedResponse = new FollowerCountResponse(expectedCount);
        FollowerCountResponse response = null;
        try {
            response = serverFacade.getFollowerCount(request, "/getfollowercount");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(expectedResponse.getCount(), response.getCount());
    }



}
