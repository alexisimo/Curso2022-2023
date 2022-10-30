import Clases.Bibliotecas;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;
import querys.SPARQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Ventanas extends JFrame {
    public Ventanas() throws IOException, MediaWikiApiErrorException {
        bibliotecas();
    }
    public void bibliotecas() throws IOException, MediaWikiApiErrorException {
        setTitle("Los Bibliotecas con Eventos");
        setResizable(true);
        setBounds(100,100,1300,700);
        JPanel panel=new JPanel();
        JButton buton= new JButton();
        String[] columnNames = {"Biblioteca",
                "Direccion",
                "Descripcion",
                "Horario",
                "Telefono",
                "URL",
                "ID",
                "Wikidata:Nivel de Mar"};
        SPARQL sparql= new SPARQL();
        ArrayList<Bibliotecas> arr= sparql.queryBiblioteca();
        Object[][] data = new Object[arr.size()][8];
        for (int i=0;i<arr.size();i++){
            data[i][0]=arr.get(i).getNombre();
            data[i][1]=arr.get(i).getDireccion();
            data[i][2]=arr.get(i).getDescripcion();
            data[i][3]=arr.get(i).getHorario();
            data[i][4]=arr.get(i).getTelefono();
            data[i][5]=arr.get(i).getUrl();
            data[i][6]=arr.get(i).getPk();
            data[i][7]=arr.get(i).getNivelDeMar();
        }
        //JButton button1 = new JButton("Evento");
        JTable tabla= new JTable(data,columnNames);
        add(new JScrollPane(tabla),BorderLayout.CENTER);
        JButton boton=new JButton("Eventos");
        JTextField textField = new JTextField(12);

        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sc = textField.getText();
                if(sc.isEmpty()== false) {
                    VentanaEventos windows2=new VentanaEventos();
                    windows2.toFront();
                    try {
                        windows2.events(sc);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (MediaWikiApiErrorException ex) {
                        throw new RuntimeException(ex);
                    }
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(null, "No puede estar vacío");
                }
            }
        });
        JLabel lblIntroduzcaLaRuta = new JLabel("Escriba el id de biblioteca");
        lblIntroduzcaLaRuta.setBounds(36, 24, 375, 15);
        textField.setBounds(36,94,175,19);
        panel.add(lblIntroduzcaLaRuta);
        panel.add(textField);
        panel.add(boton);
        add(panel,BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //add(laminaBoton,BorderLayout.SOUTH);

    }
    public static void main(String args[]) throws IOException, MediaWikiApiErrorException {
        Ventanas v=new Ventanas();

    }
}