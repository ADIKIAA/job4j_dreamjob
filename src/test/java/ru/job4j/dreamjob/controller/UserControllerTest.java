package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;

    private UserService userService;

    @BeforeEach
    public void initService() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetRegisterPageThenGerRegisterPage() {
        var view = userController.getRegisterPage();

        assertThat(view).isEqualTo("/users/register");
    }

    @Test
    public void whenPostRegisterNewUserThenGetVacanciesListPage() {
        var userOptional = Optional.of(
                new User("123@ya.r", "name", "password"));
        when(userService.save(any())).thenReturn(userOptional);

        var model = new ConcurrentModel();
        var view = userController.register(userOptional.get(), model);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenPostRegisterUserWithExistEmailThenGetErrorWithMessage() {
        when(userService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(new User(), model);
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(message).isEqualTo("Пользователь с такой почтой уже существует");
    }

    @Test
    public void whenLoginExistUserThenGetVacanciesListPage() {
        var user = new User("123@ya.r", "name", "password");
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));

        HttpServletRequest request = new MockHttpServletRequest();
        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);
        var session = request.getSession();
        var actualUser = session.getAttribute("user");

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenLoginNotExistUserThenGetLoginPageWithMessage() {
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        HttpServletRequest request = new MockHttpServletRequest();
        var model = new ConcurrentModel();
        var view = userController.loginUser(new User(), model, request);
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("users/login");
        assertThat(message).isEqualTo("Почта или пароль введены неверно");
    }

}