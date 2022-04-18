package com.sharashkina_kontora.travel_agency.view;

import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.UserService;
import com.sharashkina_kontora.travel_agency.view.components.AuthorizationComponent;
import com.sharashkina_kontora.travel_agency.view.components.MainPageComponent;
import com.sharashkina_kontora.travel_agency.view.components.RegistrationComponent;
import com.sharashkina_kontora.travel_agency.view.components.UserPageComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import org.springframework.security.core.context.SecurityContextHolder;


@Route("")
@PageTitle("Travel Agency")
@Theme(themeFolder = "my-theme")
public class MainView extends VerticalLayout {
    //services
    private final UserService userService;

    //components
    private final RegistrationComponent registrationComponent;
    private final AuthorizationComponent authorizationComponent;
    private final MainPageComponent mainPageComponent;
    private final UserPageComponent userPageComponent;
    //unauthorizedMenu
    private final MenuBar unauthorizedMenu = new MenuBar();
    private final MenuItem registration = unauthorizedMenu.addItem("Registration");
    private final MenuItem authorization = unauthorizedMenu.addItem("Authorization");
    //authorizedMenu
    private final MenuBar authorizedMenu = new MenuBar();
    private final MenuItem addOrder = authorizedMenu.addItem("Add Order");
    private final MenuItem logOut = authorizedMenu.addItem("Log out");
    //todo add profile component
    private final MenuItem emailMenuButton = authorizedMenu.addItem("");
    private final Label sure = new Label("Do you want to log out?");
    private final Button acceptLogOut = new Button("Accept");
    private final Button cancelLogOut = new Button("Cancel");
    private final Dialog logOutDialog = new Dialog(new VerticalLayout(sure, new HorizontalLayout(acceptLogOut, cancelLogOut)));
    //menu
    private final Component currentComponent;
    //user
    private User user;

    public MainView(UserService userService, RegistrationComponent registrationComponent, AuthorizationComponent authorizationComponent, MainPageComponent mainPageComponent, UserPageComponent userPageComponent) {
        this.userService = userService;
        this.registrationComponent = registrationComponent;
        this.authorizationComponent = authorizationComponent;
        this.mainPageComponent = mainPageComponent;
        this.userPageComponent = userPageComponent;

        createAuthorizedMenu();
        configureComponents();
        configureLogOutDialog();

        currentComponent = mainPageComponent;

        add(createUnauthorizedMenu(), currentComponent);
    }

    private void configureLogOutDialog() {
        acceptLogOut.getElement().getThemeList().add("error");

        acceptLogOut.addClickListener(event -> {
            replace(authorizedMenu, unauthorizedMenu);
            user = User.builder().build();
            mainPageComponent.changeTextToMainPage();

            //should wipe out stored data
            UI.getCurrent().getSession().close();
            SecurityContextHolder.clearContext();
        });
        cancelLogOut.addClickListener(event -> logOutDialog.close());
    }

    private void configureComponents() {
        registrationComponent.setRegistrationEndHandler(user1 -> {
            this.user = user1;
            performLogIn();
        });

        authorizationComponent.setAuthorizationEndHandler(user1 -> {
            this.user = user1;
            performLogIn();
        });
    }

    private MenuBar createUnauthorizedMenu() {
        registration.addClickListener(menuItemClickEvent -> registrationComponent.registerUser());
        authorization.addClickListener(menuItemClickEvent -> authorizationComponent.initComponent());

        return unauthorizedMenu;
    }

    private MenuBar createAuthorizedMenu() {
        //addNote.addClickListener(menuItemClickEvent -> editNoteComponent.editNote(Note.builder().user(user).build()));

        logOut.addClickListener(menuItemClickEvent -> performLogOut());
        return authorizedMenu;
    }

    private void performLogOut() {
        logOutDialog.open();
    }

    private void performLogIn() {
        replace(unauthorizedMenu, authorizedMenu);
        userPageComponent.initComponent(user);
        replace(mainPageComponent, userPageComponent);
        emailMenuButton.setText(user.getFirstName() + " " + user.getLastName());
    }
}
