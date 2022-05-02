package com.sharashkina_kontora.travel_agency.view.components;

import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.UserService;
import com.sharashkina_kontora.travel_agency.view.MainView;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@SpringComponent
@UIScope
public class AuthorizationComponent extends LoginForm implements KeyNotifier {

    private final Dialog dialog = new Dialog();
    private final Notification notificationSuccessfully = new Notification();

    @Setter
    private AuthorizationEndHandler authorizationEndHandler;

    public AuthorizationComponent(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.setForgotPasswordButtonVisible(false);

        this.addLoginListener(loginEvent -> {
            UserDetails userDetails;
            try {
                userDetails = userService.loadUserByUsername(loginEvent.getUsername());
            } catch (UsernameNotFoundException e) {
                log.debug("Invalid username");
                setError(true);
                return;
            }

            if (bCryptPasswordEncoder.matches(loginEvent.getPassword(), userDetails.getPassword())) {
                log.debug("Log In Succeeded");
                setError(false);
                authorizationEndHandler.onAuthorizationFinished(
                        (User) userDetails
                );
                notificationSuccessfully.setText("Successfully logged in as " + userDetails.getUsername());
                notificationSuccessfully.open();
                dialog.close();
            } else {
                log.debug("Log In Failed : password: " + loginEvent.getPassword());
                setError(true);
            }
        });

        initNotification();
    }

    private void initNotification() {
        notificationSuccessfully.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notificationSuccessfully.setPosition(Notification.Position.BOTTOM_CENTER);
        notificationSuccessfully.setDuration(MainView.NOTIFICATION_DURATION);
    }

    public void initComponent() {
        log.debug("Begin Authorization");
        dialog.open();
        dialog.add(this);
    }

    public interface AuthorizationEndHandler {
        void onAuthorizationFinished(User user);
    }
}
