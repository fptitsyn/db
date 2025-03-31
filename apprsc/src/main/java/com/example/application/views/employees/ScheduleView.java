package com.example.application.views.employees;

import com.vaadin.flow.component.html.H2;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route(value = "schedule-view")
@PageTitle("График работы")
@Menu(order = 43, icon = LineAwesomeIconUrl.BUSINESS_TIME_SOLID)

@RolesAllowed({"HR","WORKS","GOD"})
public class ScheduleView  extends VerticalLayout {
    public ScheduleView() {
        H2 header = new H2("Не разработано...");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);
    }
}
