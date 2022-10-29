import Clases.Bibliotecas;
import querys.SPARQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Ventanas extends JFrame {
    public Ventanas(){
        bibliotecas();
    }
    public void bibliotecas() {
        setTitle("Los Bibliotecas con Eventos");
        setResizable(true);
        setBounds(300,300,1300,700);
        JPanel panel=new JPanel();
        JButton buton= new JButton();
        String[] columnNames = {"Biblioteca",
                "Direccion",
                "Descripcion",
                "Horario",
                "Telefono",
                "URL",
                "ID"};
        SPARQL sparql= new SPARQL();
        ArrayList<Bibliotecas> arr= sparql.queryBiblioteca();
        Object[][] data = new Object[arr.size()][7];
        for (int i=0;i<arr.size();i++){
            data[i][0]=arr.get(i).getNombre();
            data[i][1]=arr.get(i).getDireccion();
            data[i][2]=arr.get(i).getDescripcion();
            data[i][3]=arr.get(i).getHorario();
            data[i][4]=arr.get(i).getTelefono();
            data[i][5]=arr.get(i).getUrl();
            data[i][6]=arr.get(i).getPk();
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
                    windows2.events(sc);
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
    public static void main(String args[])
    {
        Ventanas v=new Ventanas();

    }
}