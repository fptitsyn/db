package com.example.application.views.orders;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route(value = "work-orders-view")
@PageTitle("Список работ")
@Menu(order = 12, icon = LineAwesomeIconUrl.TOOLS_SOLID)

@RolesAllowed({"WORKS","GOD"})
public class WorkOrdersView   extends VerticalLayout {
    public WorkOrdersView() {
        H2 header = new H2("Не разработано...");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);
    }
}