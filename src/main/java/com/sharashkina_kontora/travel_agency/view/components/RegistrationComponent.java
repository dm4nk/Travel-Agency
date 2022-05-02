package com.sharashkina_kontora.travel_agency.view.components;


import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
public class RegistrationComponent extends FormLayout implements KeyNotifier {
    private final UserService userService;

    private final H2 text = new H2("Registration");
    private final EmailField emailField = new EmailField("email");
    private final TextField firstName = new TextField("first name");
    private final TextField lastName = new TextField("last name");
    private final TextField phoneNumber = new TextField("number");
    private final DatePicker birthDay = new DatePicker("birthday");
    private final PasswordField passwordField1 = new PasswordField("password");
    private final PasswordField passwordField2 = new PasswordField("confirm password");
    private final Button confirm = new Button("Confirm");
    private final Button cancel = new Button("Cancel");

    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    private final Dialog dialog = new Dialog();

    @Setter
    private RegistrationEndHandler registrationEndHandler;

    private User user;

    public RegistrationComponent(UserService userService) {
        this.userService = userService;
        add(text, emailField, firstName, lastName, phoneNumber, birthDay, passwordField1, passwordField2,
                new HorizontalLayout(confirm, cancel));
        initBinder();

        confirm.getElement().getThemeList().add("primary");
        addKeyPressListener(Key.ENTER, e -> {
            try {
                confirm();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        confirm.addClickListener(e -> {
            try {
                confirm();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addClickListener(e -> dialog.close());
    }

    private void initBinder() {
        binder.forField(emailField)
                .asRequired("enter email")
                .withValidator(new EmailValidator("Not a correct email"))
                .bind(User::getEmail, User::setEmail);

        binder.forField(firstName)
                .asRequired("enter first name")
                .bind(User::getFirstName, User::setFirstName);

        binder.forField(lastName)
                .asRequired("enter last name")
                .bind(User::getLastName, User::setLastName);

        binder.forField(phoneNumber)
                .asRequired("enter phone number")
                .bind(User::getPhoneNumber, User::setPhoneNumber);

        binder.forField(birthDay)
                .bind(User::getBirthday, User::setBirthday);

        binder.forField(passwordField1)
                .asRequired("enter password")
                .withValidator(new StringLengthValidator("Password length must be > 3 and < 25", 3, 25))
                .bind(User::getPassword, User::setPassword);
    }


    public void registerUser() {
        log.debug("Begin Registration");
        user = User.builder().build();

        binder.setBean(user);

        dialog.open();
        dialog.add(this);
        emailField.focus();
    }

    private void confirm() throws Exception {
        if (passwordField1.getValue().equals(passwordField2.getValue()))
            if (binder.validate().isOk()) {
                userService.save(user);
                registrationEndHandler.onRegistrationFinished(user);
                dialog.close();
            }
    }

    public interface RegistrationEndHandler {
        void onRegistrationFinished(User user);
    }
}
