import javax.swing.*;
import java.awt.*;

public class Main {
    JMenu menu, submenu;
    JMenuItem i1, i2, i3, i4, i5;
    public Main(){
        JFrame frame = new JFrame("Bibliotecas");
        // Setting the width and height of frame
        frame.setSize(950, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);

        String[] columnNames = {"Biblioteca",
                "Localidad",
                "Descripcion",
                "Horario",
                "URL"};
        Object[][] data = {
                {"Bibliometro", "Madrid",
                        "El Bibliometro es un servicio de extensi?n bibliotecaria destinado al fomento de la lectura mediante el pr?stamo gratuito de libros en el Metro de Madrid. Promovido conjuntamente por el Ayuntamiento de Madrid y la Comunidad de Madrid en colaboraci?n con Metro, el Bibliometro es el primer servicio com?n a los dos sistemas de Bibliotecas P?blicas del Ayuntamiento de Madrid y la Comunidad de Madrid. El servicio gestionado por el Ayuntamiento de Madrid se centraliza en el Departamento de Bibliotecas P?blicas, localizado en la calle Conde Duque 11. El servicio de Bibliored es similar, pero en superficie.", 24, Boolean.FALSE},
                {"Bibliored Centro Cultural Galileo", "Madrid",
                        "El Bibliored es un servicio de extensi?n bibliotecaria destinado al fomento de la lectura mediante el pr?stamo gratuito de libros, un proyecto similar al de Bibliometro pero en la superficie. Existen dos m?dulos: el de el Centro Cultural Galileo y la Sala Le?n Felipe. El servicio, gestionado por el Ayuntamiento de Madrid, se coordina desde el Departamento de Bibliotecas P?blicas, localizado en la calle Conde Duque, 9-11.", 24, Boolean.TRUE},
                {"Biblioteca P?blica Antonio Mingote (Latina)", "Madrid",
                        "El Bibliored es un servicio de extensi?n bibliotecaria destinado al fomento de la lectura mediante el pr?stamo gratuito de libros, un proyecto similar al de Bibliometro pero en la superficie. Existen dos m?dulos: el de el Centro Cultural Galileo y la Sala Le?n Felipe. El servicio, gestionado por el Ayuntamiento de Madrid, se coordina desde el Departamento de Bibliotecas P?blicas, localizado en la calle Conde Duque, 9-11.", 24, Boolean.FALSE},
                {"Biblioteca Publica Jos? Acu?a (Moncloa - Aravaca)", "Madrid",
                        "La Biblioteca P?blica de Aluche se inaugur? en junio de 1992 con la asistencia del entonces alcalde Don Jos? Mar?a Alvarez del Manzano. Se sit?a en la primera planta del Centro Cultural Fernando de los R?os, dise?ado por el arquitecto Jos? Mar?a Guijarro. Desde 1986 y hasta la fecha de su inauguraci?n oficial, se encontraba situada en este mismo centro cultural y planta pero en el ala derecha del edificio y con la mitad de superficie. La ubicaci?n de la biblioteca en el centro cultural en la d?cada de los 80  se decidi? porque ?ste era considerado un elemento aglutinador de la vida asociativa y cultural de los distritos.", 24, Boolean.TRUE},
                {"Biblioteca Publica Jos? Hierro (Usera)", "Madrid",
                        "La biblioteca se abri? al p?blico en mayo de 1999 denomin?ndose Biblioteca Mateo Inurria, por estar situada en dicha calle. En 2008, por acuerdo plenario, pas? a llamarse Biblioteca D?maso Alonso. Y es que este poeta vivi? en el n?mero 25 de la calle Alberto Alcocer, en una casita con huerta y jard?n situada en su momento a las afueras de Madrid, en Chamart?n de la Rosa. En cuanto a los or?genes de la biblioteca, en 1997 la sede del diario Ya se convierte en sede del Barclays Bank. A cambio, la entidad cede una parcela al Ayuntamiento para la construcci?n de una biblioteca, que entreg? ya equipada. En 2008 se ampli? el vest?bulo, se instal? un ascensor y se cerr? una de las dos escaleras. En su lugar se coloc? un lucernario para iluminar la sala de lectura.", 24, Boolean.FALSE}
        };

        JTable table = new JTable(data, columnNames);
        table.setAutoscrolls(true);
        table.setRowHeight(50);
        table.setFillsViewportHeight(true);
        panel.setLayout(new BorderLayout());
        panel.add(table.getTableHeader(),BorderLayout.PAGE_START);

        panel.add(table,BorderLayout.CENTER);


        /*
         * 调用用户定义的方法并添加组件到面板
         */
        // 设置界面可见
        frame.setVisible(true);
    }
    public static void main(String args[])
    {
        new Ventanas();
    }

}