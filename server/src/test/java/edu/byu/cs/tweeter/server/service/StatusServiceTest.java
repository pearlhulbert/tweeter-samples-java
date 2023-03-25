package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.LameFollowDAO;
import edu.byu.cs.tweeter.server.dao.LameStatusDAO;

public class StatusServiceTest {

    private StoryRequest request;
    private StoryResponse expectedResponse;
    private LameStatusDAO mockStatusDAO;
    private StatusService statusServiceSpy;

    @BeforeEach
    public void setup() {
        AuthToken authToken = new AuthToken();

        Status currStatus = new Status("post, post, post", new User("FirstName", "LastName", null), "date", null, null);

        Status resultStatus1 = new Status("post1", new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), "date", null, null);
        Status resultStatus2 = new Status("post2", new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png"), "date", null, null);
        Status resultStatus3 = new Status("post3", new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png"), "date", null, null);

        // Setup a request object to use in the tests
        request = new StoryRequest(authToken, "user", 10, currStatus);

        // Setup a mock FollowDAO that will return known responses
        //expectedResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockStatusDAO = Mockito.mock(LameStatusDAO.class);
        Mockito.when(mockStatusDAO.getStory(request)).thenReturn(expectedResponse);

        statusServiceSpy = Mockito.spy(StatusService.class);
        Mockito.when(statusServiceSpy.getStatusDAO()).thenReturn(mockStatusDAO);
    }

    /**
     * Verify that the {@link FollowService#getFollowees(FollowingRequest)}
     * method returns the same result as the {@link LameFollowDAO} class.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() {
        StoryResponse response = statusServiceSpy.getStory(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}

