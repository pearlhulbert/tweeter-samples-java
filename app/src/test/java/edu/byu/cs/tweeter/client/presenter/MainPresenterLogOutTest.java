package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class MainPresenterLogOutTest {

    private MainPresenter.MainView mockView;
    private UserService mockUserService;
    private Cache mockCache;

    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockUserService = Mockito.mock(UserService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        // will always work, other won't mock void methods
        // Mockito.doReturn(mockUserService).when(mainPresenterSpy).getUserService();
        // safer, type checking
        Mockito.when(mainPresenterSpy.getUserService()).thenReturn(mockUserService);

        Cache.setInstance(mockCache);
    }

    // make functions to get rid of duplicate code

    @Test
    public void testLogout_successful() {

        // abstract inner class for answer, replace observer call
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                UserService.LogOutObserver observer = invocation.getArgument(0);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).startLogout(Mockito.any());
        mainPresenterSpy.startLogout();
        Mockito.verify(mockView).setLogoutToast();
        Mockito.verify(mockCache).clearCache();
        // Mockito.verify(mockView).clearInfoMessage();
        // Mockito.verify(mockView).logout();
    }

    @Test
    public void testLogout_unsuccessful() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                UserService.LogOutObserver observer = invocation.getArgument(0);
                observer.displayMessage("error");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).startLogout(Mockito.any());
        mainPresenterSpy.startLogout();

        // Mockito.verify(mockView).setLogoutToast();
        // Mockito.verify(mockView).displayMessage("Logging out...");
        Mockito.verify(mockView).setLogoutToast();
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        // Mockito.verify(mockView).clearInfoMessage();
        // Mockito.verify(mockView).logout();
    }

    @Test
    public void testLogout_withException() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                UserService.LogOutObserver observer = invocation.getArgument(0);
                // handleException
                observer.displayMessage("exception");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).startLogout(Mockito.any());
        mainPresenterSpy.startLogout();

        // Mockito.verify(mockView).setLogoutToast();
        // Mockito.verify(mockView).displayMessage("Logging out...");
        Mockito.verify(mockView).setLogoutToast();
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        // Mockito.verify(mockView).clearInfoMessage();
        // Mockito.verify(mockView).logout();
        // check exception message

    }
}
