package com.example.application.views;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route(value = "To-Do-View")
@PageTitle("Нужно сделать")
@Menu(order = 102, icon = LineAwesomeIconUrl.EXCLAMATION_TRIANGLE_SOLID)

@RolesAllowed({"HR", "WORKS", "ADMIN", "USER", "SALES", "GOD", "ANALYSTS"})
public class ToDoView extends VerticalLayout {
    public ToDoView() {
        setSpacing(false);
        add(new Span("Сгорание бонусов???"));

        setSizeFull();
    }
}