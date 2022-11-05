package com.semanticWeb.application;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {
    //private Grid<String> grid = new Grid<>();
    public static final String SWITCH_BIKES = "Switch to Bike Stations";
    public static final String SWITCH_EV = "Switch to Electric Vehicle Stations";
    private String switchBtnText = SWITCH_BIKES;
    private List<String> bikeStations = new ArrayList<>(Arrays.asList("ola", "k", "ase"));
    private List<String> evStations = new ArrayList<>(Arrays.asList("Most recent first", "Rating: high to low",
            "Rating: low to high", "Price: high to low",
            "Price: low to high"));
    private List<String> selectOptions;

    // Inicializar switchBtnText a bicis
    Button switchBtn = new Button(switchBtnText);
    Button queryBtn = new Button("Query");
    Anchor anchor = new Anchor("http://localhost:8050", "Return");


    Select<String> originSlt = new Select<>();
    Select<String> destSlt = new Select<>();


    public MainView() {
//        grid.setColumns("col1", "col2", "col3");
//        add(getForm(), grid);

        selectOptions = bikeStations;
        originSlt.setLabel("Origin Station");
        originSlt.setItems(selectOptions);
        originSlt.setPlaceholder("Select station");

        destSlt.setLabel("Destination Station");
        destSlt.setItems(selectOptions);
        destSlt.setPlaceholder("Select station");
//        select.setValue("Most recent first");

        switchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        switchBtn.addClickListener(click -> {
            selectOptions = switchBtnText.equals(SWITCH_BIKES) ? evStations : bikeStations;
            originSlt.setItems(selectOptions);
            destSlt.setItems(selectOptions);
            switchBtnText = switchBtnText.equals(SWITCH_BIKES) ? SWITCH_EV : SWITCH_BIKES;
            switchBtn.setText(switchBtnText);
        });
        switchBtn.setWidth(18, Unit.EM);

        queryBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        queryBtn.addClickListener(click -> {
            // ejecuta queries
//            originSlt.getValue();
        });


        VerticalLayout vl1 = (VerticalLayout) buildVerticalLayout("Grid1", "1");
        VerticalLayout vl2 = (VerticalLayout) buildVerticalLayout("Grid2", "2");

        HorizontalLayout innerHl = new HorizontalLayout(vl1, vl2);
        innerHl.setFlexGrow(1, vl1);
        innerHl.setFlexGrow(1, vl2);
        innerHl.setWidthFull();

        add(getSelects());
        add(innerHl);

//        add(buildParagraph("Grid1"));
//        add(getForm(), buildGrid());
//        add(buildParagraph("Grid2"));
//        add(getForm(), buildGrid());
    }

    private Component getSelects() {
        HorizontalLayout hl = new HorizontalLayout(anchor, originSlt, destSlt, switchBtn, queryBtn);
        hl.setAlignItems(Alignment.END);
        hl.setPadding(true);
        hl.setJustifyContentMode(JustifyContentMode.AROUND);
        hl.setFlexGrow(1, originSlt);
        hl.setFlexGrow(1, destSlt);
        hl.setWidthFull();
        return hl;
    }

    private Component getForm() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setAlignItems(Alignment.BASELINE);

        Button searchBtn = new Button("Search");
        searchBtn.addClickListener(click -> {
            // queries
            // refrescar tabla
            //grid.setItems();
        });

        return hl;
    }

    private Component buildGrid(String id) {
        Grid<String> grid = new Grid<>(String.class, false);
        grid.setId(id);
//        grid.addColumn(Person::getFirstName).setHeader("First name");
//        grid.addColumn(Person::getLastName).setHeader("Last name");
//        grid.addColumn(Person::getEmail).setHeader("Email");
//        grid.addColumn(Person::getProfession).setHeader("Profession");
//
//        List<Person> people = DataService.getPeople();
//        grid.setItems(people);
        return grid;
    }

    private Component buildParagraph(String text) {
//        Paragraph p = new Paragraph(text);
        Label label = new Label(text);
        HorizontalLayout hl = new HorizontalLayout(label);
        hl.setSpacing(false);
        hl.setAlignItems(Alignment.END);
        hl.setJustifyContentMode(JustifyContentMode.END);
        return hl;
    }

    private Component buildVerticalLayout(String txt, String id) {
        VerticalLayout vl = new VerticalLayout(buildParagraph(txt), buildGrid(id));
        return vl;
    }
}
