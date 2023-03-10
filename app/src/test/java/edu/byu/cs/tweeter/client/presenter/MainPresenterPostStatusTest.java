package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class MainPresenterPostStatusTest {

    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;

    private MainPresenter mainPresenterSpy;

    private void callPostStatus(Answer answer, String testString) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus(testString);
    }

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        // will always work, other won't mock void methods
        // Mockito.doReturn(mockUserService).when(mainPresenterSpy).getUserService();
        // safer, type checking
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

    }

    @Test
    public void testPostStatus_successful() {

        String testString = "test1";

        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                checkParams(invocation, "test1");
                StatusService.SimpleObserver observer = invocation.getArgument(1, StatusService.SimpleObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        callPostStatus(answer, testString);
        Mockito.verify(mockView).postToast();
    }

    @Test
    public void testPostStatus_unsuccessful() {
        String testString = "test2";
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                checkParams(invocation, "test2");
                StatusService.SimpleObserver observer = invocation.getArgument(1, StatusService.SimpleObserver.class);
                observer.displayMessage("error");
                return null;
            }
        };

        callPostStatus(answer, testString);
        Mockito.verify(mockView).displayMessage("error");
    }

    @Test
    public void testLogout_withException() {
        String testString = "test3";
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                checkParams(invocation, "test3");
                StatusService.SimpleObserver observer = invocation.getArgument(1, StatusService.SimpleObserver.class);
                Exception exception = new Exception("exception");
                observer.displayMessage("exception: " + exception.getMessage());
                return null;
            }
        };

        callPostStatus(answer, testString);
        Mockito.verify(mockView).displayMessage("exception: exception");
    }

    private void checkParams(InvocationOnMock invocation, String testString) {
        String post = invocation.getArgument(0);
        Assertions.assertEquals(testString, post);
    }


}
