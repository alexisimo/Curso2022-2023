package com.semanticWeb.application;

import com.semanticWeb.application.modules.Geosparql;
import com.semanticWeb.application.types.TransportFeature;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import org.apache.jena.geosparql.spatial.SpatialIndexException;

import java.util.*;

@Route("")
public class MainView extends VerticalLayout {
    private static final int DISTANCE = 1000;
    private static final String SWITCH_BIKES = "Switch to Bike Stations";
    private static final String SWITCH_EV = "Switch to Electric Vehicle Stations";
    private static final String ONT_PATH = "../../ontology/ontology_v2.ttl";
    private static final String DATA_PATH = "../../rdf/RDF-with-links-v2.ttl";

    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private String switchBtnText = SWITCH_BIKES;
    private Map<String,String> bikeStations, evStations;

    private Set<String> selectOptions;

    private Geosparql geosparql;

    private Map<String, Grid<TransportFeature>> gridMap= new HashMap<>();

    // Inicializar switchBtnText a bicis
    Button switchBtn = new Button(switchBtnText);
    Button queryBtn = new Button("Query");
    Anchor anchor = new Anchor("http://localhost:8050", "Return");


    Select<String> originSlt = new Select<>();
    Select<String> destSlt = new Select<>();


    public MainView() {
        try {
            geosparql = new Geosparql(ONT_PATH, DATA_PATH);
        } catch (SpatialIndexException e) {
            System.err.println("SpatialIndex error!");
            System.exit(1);
        }

        bikeStations = geosparql.getFeaturesFromType("bikes");
        evStations = geosparql.getFeaturesFromType("ev");

        selectOptions = bikeStations.keySet();
        originSlt.setLabel("Origin Station");
        originSlt.setItems(selectOptions);
        originSlt.setPlaceholder("Select station");

        destSlt.setLabel("Destination Station");
        destSlt.setItems(selectOptions);
        destSlt.setPlaceholder("Select station");


        switchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        switchBtn.addClickListener(click -> {
            selectOptions = (switchBtnText.equals(SWITCH_BIKES) ? evStations : bikeStations).keySet();
            originSlt.setItems(selectOptions);
            destSlt.setItems(selectOptions);
            switchBtnText = switchBtnText.equals(SWITCH_BIKES) ? SWITCH_EV : SWITCH_BIKES;
            switchBtn.setText(switchBtnText);
        });
        switchBtn.setWidth(18, Unit.EM);

        queryBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        queryBtn.addClickListener(click -> {
            // ejecuta queries
            gridMap.get(ORIGIN).setItems(getCloseFeatures(String.format("<%s>",bikeStations.get(originSlt.getValue())), DISTANCE));
            gridMap.get(DESTINATION).setItems(getCloseFeatures(String.format("<%s>",evStations.get(destSlt.getValue())), DISTANCE));

        });


        VerticalLayout vl1 = (VerticalLayout) buildVerticalLayout("Transport stations close to origin", ORIGIN);
        VerticalLayout vl2 = (VerticalLayout) buildVerticalLayout("Transport stations close to destination", DESTINATION);

        HorizontalLayout innerHl = new HorizontalLayout(vl1, vl2);
        innerHl.setFlexGrow(1, vl1);
        innerHl.setFlexGrow(1, vl2);
        innerHl.setWidthFull();

        add(getSelects());
        add(innerHl);
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

    private Component buildGrid(String id) {
        Grid<TransportFeature> grid = new Grid<>(TransportFeature.class, false);
        grid.addColumn(TransportFeature::getType).setHeader("Type");
        grid.addColumn(TransportFeature::getName).setHeader("Info");
        gridMap.put(id, grid);
        return grid;
    }

    private Component buildParagraph(String text) {
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

    private List<TransportFeature> getCloseFeatures(String node, int distance){
        List<TransportFeature> l = geosparql.getClosestFeatures(node, distance);
        l.addAll(geosparql.getClosestMetroStations(node, distance));
        return l;
    }
}
