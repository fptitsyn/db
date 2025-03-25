package com.example.application.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import jakarta.annotation.security.RolesAllowed;


@Route(value = "")
@PageTitle("О программе")
@Menu(order = 100, icon = LineAwesomeIconUrl.COLUMNS_SOLID)

@RolesAllowed({"HR","WORKS","ADMIN","USER","SALES","GOD","ANALYSTS"})

public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2("Решил(а) поработать?!!");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("==================================="));
        add(new Paragraph("Это самая крутая программа в мире"));
        add(new Paragraph("Спасибо Папе и DeepSeek 🤗"));
        add(new Paragraph("==================================="));
        add(new Paragraph("Админы - админят!"));
        add(new Paragraph("Продаваны - продают!"));
        add(new Paragraph("Работяги - работают!"));
        add(new Paragraph("Кадровики - кадрят!"));
        add(new Paragraph("Аналитики - аналичат!"));
        add(new Paragraph("==================================="));
        add(new Paragraph("И за всем ними следит GOD!"));





        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}