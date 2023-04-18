package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class PostStatusFullBackendTest {

    private MainPresenter.MainView mockView;

    private MainPresenter mainPresenterSpy;

    private LoginResponse loginResponse;

    private CountDownLatch latch;

    private ServerFacade serverFacade;


    private void resetCountDownLatch() {
        latch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        latch.await();
        resetCountDownLatch();
    }


    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        serverFacade = new ServerFacade();
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        try {
            loginResponse = serverFacade.login(loginRequest, "/login");
            Cache.getInstance().setCurrUser(loginResponse.getUser());
            Cache.getInstance().setCurrUserAuthToken(loginResponse.getAuthToken());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        resetCountDownLatch();
    }

    @Test
    public void testPostStatus() {
        String testString = "test1";

        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
//                checkParams(invocation, "test1");
//                StatusService.SimpleObserver observer = invocation.getArgument(1, StatusService.SimpleObserver.class);
//                observer.handleSuccess();
                latch.countDown();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockView).displayMessage("Status successfully posted");
        mainPresenterSpy.postStatus(testString);
        try {
            awaitCountDownLatch();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(mockView).displayMessage("Status successfully posted");
        StoryRequest storyRequest = new StoryRequest(loginResponse.getAuthToken(), loginResponse.getUser().getAlias(), 10, null);
        StoryResponse storyResponse = new StoryResponse("test");
        try {
            storyResponse = serverFacade.getStory(storyRequest, "/getstory");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        List<Status> story = storyResponse.getStory();
        Assertions.assertEquals(testString, story.get(story.size() - 1).getPost());
        Assertions.assertEquals(loginResponse.getUser().getAlias(), story.get(story.size() - 1).getUser().getAlias());
    }


}