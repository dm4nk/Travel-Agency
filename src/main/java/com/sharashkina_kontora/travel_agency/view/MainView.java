package com.sharashkina_kontora.travel_agency.view;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Status;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.UserService;
import com.sharashkina_kontora.travel_agency.view.components.AuthorizationComponent;
import com.sharashkina_kontora.travel_agency.view.components.MainPageComponent;
import com.sharashkina_kontora.travel_agency.view.components.RegistrationComponent;
import com.sharashkina_kontora.travel_agency.view.components.UserPageComponent;
import com.sharashkina_kontora.travel_agency.view.components.order.EditOrderComponent;
import com.sharashkina_kontora.travel_agency.view.components.order.ShowOrderComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import org.springframework.security.core.context.SecurityContextHolder;


@Route("")
@PageTitle("Travel Agency")
@Theme(themeFolder = "my-theme")
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {
    //constants
    public static final Integer NOTIFICATION_DURATION = 3000;

    //services
    private final UserService userService;

    //components
    private final RegistrationComponent registrationComponent;
    private final AuthorizationComponent authorizationComponent;
    private final MainPageComponent mainPageComponent;
    private final UserPageComponent userPageComponent;
    private final EditOrderComponent editOrderComponent;
    private final ShowOrderComponent showOrderComponent;
    //unauthorizedMenu
    private final MenuBar unauthorizedMenu = new MenuBar();
    private final MenuItem registration = unauthorizedMenu.addItem("Registration");
    private final MenuItem authorization = unauthorizedMenu.addItem("Authorization");
    private final MenuItem toggleButtonUnauthorizedMenu = unauthorizedMenu.addItem(VaadinIcon.MOON.create());
    //authorizedMenu
    private final MenuBar authorizedMenu = new MenuBar();
    private final MenuItem showOrders = authorizedMenu.addItem("Orders");
    private final MenuItem logOut = authorizedMenu.addItem("Log out");
    private final MenuItem emailMenuButton = authorizedMenu.addItem("");
    private final MenuItem toggleButtonAuthorizedMenu = authorizedMenu.addItem(VaadinIcon.MOON.create());
    //todo add profile component
    private final Label sure = new Label("Do you want to log out?");
    private final Button acceptLogOut = new Button("Accept");
    private final Button cancelLogOut = new Button("Cancel");
    private final Dialog logOutDialog = new Dialog(new VerticalLayout(sure, new HorizontalLayout(acceptLogOut, cancelLogOut)));
    //menu
    private final Component currentComponent;
    //user
    private User user;

    public MainView(UserService userService, RegistrationComponent registrationComponent, AuthorizationComponent authorizationComponent, MainPageComponent mainPageComponent, UserPageComponent userPageComponent, EditOrderComponent editOrderComponent, ShowOrderComponent showOrderComponent) {
        this.userService = userService;
        this.registrationComponent = registrationComponent;
        this.authorizationComponent = authorizationComponent;
        this.mainPageComponent = mainPageComponent;
        this.userPageComponent = userPageComponent;
        this.editOrderComponent = editOrderComponent;
        this.showOrderComponent = showOrderComponent;

        createAuthorizedMenu();
        configureComponents();
        configureLogOutDialog();

        authorizedMenu.getElement().getClassList().add("top-menu");
        unauthorizedMenu.getElement().getClassList().add("top-menu");

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
        //todo configure services so that they could return valid objects, so that 'this.user = user1;' could work
        registrationComponent.setRegistrationEndHandler(user1 -> {
            //this.user = user1;
            this.user = userService.findById(user1.getId()).orElse(null);
            performLogIn();
        });

        authorizationComponent.setAuthorizationEndHandler(user1 -> {
            //this.user = user1;
            this.user = userService.findById(user1.getId()).orElse(null);
            performLogIn();
        });

        editOrderComponent.setChangeHandler(user1 -> {
            //this.user = user1;
            this.user = userService.findById(user1.getId()).orElse(null);
            userPageComponent.initComponent(user);
        });

        showOrderComponent.setChangeHandler(user1 -> {
            this.user = userService.findById(user1.getId()).orElse(null);
            userPageComponent.initComponent(user);
        });
    }

    private MenuBar createUnauthorizedMenu() {
        registration.addClickListener(menuItemClickEvent -> registrationComponent.registerUser());
        authorization.addClickListener(menuItemClickEvent -> authorizationComponent.initComponent());
        toggleButtonUnauthorizedMenu.addClickListener(menuItemClickEvent -> toggleTheme());
        return unauthorizedMenu;
    }

    private MenuBar createAuthorizedMenu() {
        //showOrders.addClickListener(menuItemClickEvent -> editOrderComponent.editOrder(Order.builder().user(user).status(Status.PLANNED).build()));
        showOrders.addClickListener(menuItemClickEvent -> userPageComponent.initComponent(user));
        toggleButtonAuthorizedMenu.addClickListener(menuItemClickEvent -> toggleTheme());
        logOut.addClickListener(menuItemClickEvent -> performLogOut());
        return authorizedMenu;
    }

    private void performLogOut() {
        logOutDialog.open();
    }

    private void performLogIn() {
        replace(unauthorizedMenu, authorizedMenu);
        //userPageComponent.initComponent(user);
        //replace(mainPageComponent, userPageComponent);
        emailMenuButton.setText(user.getFirstName() + " " + user.getLastName());
    }

    private void toggleTheme(){
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains("dark")) {
            themeList.remove("dark");
        } else {
            themeList.add("dark");
        }
    }
}
