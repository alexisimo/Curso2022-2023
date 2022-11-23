import Clases.Eventos;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;
import querys.SPARQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class VentanaEventos  extends JFrame {
    private String id;
    public VentanaEventos() {
    }
    public void events(String id) throws IOException, MediaWikiApiErrorException {
        setTitle("Los Bibliotecas con Eventos");
        setBounds(100,100,1300,700);
        JPanel panel=new JPanel();

        JButton buton= new JButton();
        String[] columnNames = {
                "Titulo de Evento",
                "Fecha Inicio",
                "Fecha Fin",
                "Hora Empiezo",
                "NombreInstalacion",
                "Wiki:Calle",
                "Wiki:Coordenadas"};
        SPARQL sparql= new SPARQL();
        ArrayList<Eventos> arr= sparql.queryEventos(id);
        Object[][] data = new Object[arr.size()][9];
        for (int i=0;i<arr.size();i++){
            data[i][0]=arr.get(i).getNombreEvento();
            data[i][1]=arr.get(i).getFechaInicio();
            data[i][2]=arr.get(i).getFechaFin();
            data[i][3]=arr.get(i).getHoraEmpiezo();
            data[i][4]=arr.get(i).getNombreIntalacion();
            data[i][5]=arr.get(i).getWikiStreet();
            data[i][6]=arr.get(i).getWikiCoor();
        }
        //JButton button1 = new JButton("Evento");
        JTable tabla= new JTable(data,columnNames);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JButton botonAtras=new JButton("Atras");

        botonAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Ventanas windows= null;
                try {
                    windows = new Ventanas();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (MediaWikiApiErrorException ex) {
                    throw new RuntimeException(ex);
                }
                windows.toFront();
                windows.setState(Frame.NORMAL);
                //Ventanas windows2=new Ventanas();
                //windows2.toFront();
                //windows2.bibliotecas();
                //setDefaultCloseOperation(EXIT_ON_CLOSE);
                dispose();
            }
        });
        panel.add(botonAtras);
        add(panel,BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
