package com.sharashkina_kontora.travel_agency.view;

import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.TourService;
import com.sharashkina_kontora.travel_agency.service.UserService;
import com.sharashkina_kontora.travel_agency.view.components.AuthorizationComponent;
import com.sharashkina_kontora.travel_agency.view.components.MainPageComponent;
import com.sharashkina_kontora.travel_agency.view.components.RegistrationComponent;
import com.sharashkina_kontora.travel_agency.view.components.amin.AdminPage;
import com.sharashkina_kontora.travel_agency.view.components.order.EditOrderComponent;
import com.sharashkina_kontora.travel_agency.view.components.order.ShowOrderComponent;
import com.sharashkina_kontora.travel_agency.view.components.user.UserPageComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;


@Route("")
@PageTitle("Travel Agency")
@Theme(themeFolder = "my-theme")
@CssImport("./styles/styles.css")
@Slf4j
public class MainView extends VerticalLayout {
    //constants
    public static final Integer NOTIFICATION_DURATION = 3000;

    //services
    private final UserService userService;
    private final TourService tourService;

    //components
    private final RegistrationComponent registrationComponent;
    private final AuthorizationComponent authorizationComponent;
    private final MainPageComponent mainPageComponent;
    private final UserPageComponent userPageComponent;
    private final EditOrderComponent editOrderComponent;
    private final ShowOrderComponent showOrderComponent;
    private final AdminPage adminPage;
    //unauthorizedMenu
    private final MenuBar unauthorizedMenu = new MenuBar();
    private final MenuItem registration = unauthorizedMenu.addItem("Registration");
    private final MenuItem authorization = unauthorizedMenu.addItem("Authorization");
    private final MenuItem toggleButtonUnauthorizedMenu = unauthorizedMenu.addItem(VaadinIcon.MOON.create());
    //userMenu
    private final MenuBar userMenu = new MenuBar();
    private final MenuItem showOrders = userMenu.addItem("Orders");
    //private final MenuItem logUserOut = userMenu.addItem("Log out");
    private final MenuItem emailMenuButton = userMenu.addItem("");
    private final SubMenu emailMenuButtonSubMenu = emailMenuButton.getSubMenu();
    private final MenuItem toggleButtonAuthorizedMenu = userMenu.addItem(VaadinIcon.MOON.create());
    //adminMenu
    private final MenuBar adminMenu = new MenuBar();
    private final MenuItem mostPopular = adminMenu.addItem("Most Popular Tour");
    private final MenuItem lessPopular = adminMenu.addItem("Less Popular Tour");
    private final MenuItem cheapest = adminMenu.addItem("Cheapest Tour");
    private final MenuItem logAdminOut = adminMenu.addItem("Log out");
    private final MenuItem toggleButtonUserMenu = adminMenu.addItem(VaadinIcon.MOON.create());

    private final Notification tourNotification = new Notification();
    //todo add profile component
    ConfirmDialog logOutDialog = new ConfirmDialog();
    //menu
    private final Component currentComponent;
    //user
    private User user;

    public MainView(UserService userService, TourService tourService, RegistrationComponent registrationComponent, AuthorizationComponent authorizationComponent, MainPageComponent mainPageComponent, UserPageComponent userPageComponent, EditOrderComponent editOrderComponent, ShowOrderComponent showOrderComponent, AdminPage adminPage) {
        this.userService = userService;
        this.tourService = tourService;
        this.registrationComponent = registrationComponent;
        this.authorizationComponent = authorizationComponent;
        this.mainPageComponent = mainPageComponent;
        this.userPageComponent = userPageComponent;
        this.editOrderComponent = editOrderComponent;
        this.showOrderComponent = showOrderComponent;
        this.adminPage = adminPage;

        createUserMenu();
        createAdminMenu();
        configureComponents();
        configureLogOutDialog();
        initNotification();

        currentComponent = mainPageComponent.initComponent(null);

        add(createUnauthorizedMenu(), currentComponent);
    }

    private void configureLogOutDialog() {
        logOutDialog.setHeader("Log out");
        logOutDialog.setText("Are you sure you want to log out?");
        logOutDialog.setCancelable(true);
        logOutDialog.setConfirmText("Log out");
        logOutDialog.setConfirmButtonTheme("error primary");
        logOutDialog.addConfirmListener(event -> {
            UI.getCurrent().getSession().close();
            SecurityContextHolder.clearContext();
        });
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

    private void createAdminMenu() {
        logAdminOut.addClickListener(menuItemClickEvent -> performLogOut());
        mostPopular.addClickListener(event -> {
            StringBuilder mostPopularToursId = new StringBuilder();
            tourService
                    .findAll()
                    .stream()
                    .sorted((o1, o2) -> o2.getOrders().size() - o1.getOrders().size())
                    .limit(5)
                    .forEach(a -> mostPopularToursId.append(a.getId()).append(", "));

            openTourDialogWithText("Most Popular Tours Id: " + mostPopularToursId + "...");
        });

        lessPopular.addClickListener(event -> {
            StringBuilder mostPopularToursId = new StringBuilder();
            tourService
                    .findAll()
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getOrders().size()))
                    .limit(5)
                    .forEach(a -> mostPopularToursId.append(a.getId()).append(", "));

            openTourDialogWithText("Less Popular Tours Id: " + mostPopularToursId + "...");
        });

        cheapest.addClickListener(event -> {
            StringBuilder mostPopularToursId = new StringBuilder();
            tourService
                    .findAll()
                    .stream()
                    .sorted(Comparator.comparingDouble(o -> o.getPrice() / (double) o.getDuration()))
                    .limit(5)
                    .forEach(a -> mostPopularToursId.append(a.getId()).append(", "));

            openTourDialogWithText("Cheapest Tours Id: " + mostPopularToursId + "...");
        });

        toggleButtonUserMenu.addClickListener(menuItemClickEvent -> toggleTheme());
    }

    private MenuBar createUserMenu() {
        showOrders.addClickListener(menuItemClickEvent -> userPageComponent.initComponent(user));
        toggleButtonAuthorizedMenu.addClickListener(menuItemClickEvent -> toggleTheme());
        emailMenuButtonSubMenu.addItem("Log out", menuItemClickEvent -> logOutDialog.open());

        ConfirmDialog deleteDialog = new ConfirmDialog();
        deleteDialog.setHeader("Deleting account");
        deleteDialog.setText("All orders will be removed from your account. Are you sure?");
        deleteDialog.setCancelable(true);
        deleteDialog.setConfirmText("Delete");
        deleteDialog.setConfirmButtonTheme("error primary");
        deleteDialog.addConfirmListener(event -> {
            try {
                userService.delete(user);
                UI.getCurrent().getSession().close();
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                log.debug("Error deleting user");
                e.printStackTrace();
            }
        });

        emailMenuButtonSubMenu.addItem("Delete account", event -> deleteDialog.open());
        return userMenu;
    }

    private void performLogOut() {
        logOutDialog.open();
    }

    private void performLogIn() {
        if (user.getRole().getAuthority().equals("adm")) {
            replace(currentComponent, adminPage);
            replace(unauthorizedMenu, adminMenu);
        } else {
            replace(currentComponent, mainPageComponent.initComponent(user));
            replace(unauthorizedMenu, userMenu);
            emailMenuButton.setText(user.getFirstName() + " " + user.getLastName());
        }
    }

    private void toggleTheme() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains("dark")) {
            themeList.remove("dark");
        } else {
            themeList.add("dark");
        }
    }

    private void openTourDialogWithText(String text) {
        tourNotification.setText(text);
        tourNotification.open();
    }

    private void initNotification() {
        tourNotification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        tourNotification.setPosition(Notification.Position.BOTTOM_CENTER);
        tourNotification.setDuration(NOTIFICATION_DURATION);
    }
}
