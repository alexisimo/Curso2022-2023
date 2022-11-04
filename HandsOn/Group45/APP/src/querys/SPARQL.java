package querys;

import Clases.Bibliotecas;
import Clases.Eventos;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

import java.util.ArrayList;

public class SPARQL {
    //static String uri = "C:\\Users\\donyi\\Documents\\GitHub\\Curso2022-2023\\HandsOn\\Group45\\rdf\\knowledge-graph.nt";
    static String uri = "C:\\Users\\donyi\\Documents\\GitHub\\Curso2022-2023\\HandsOn\\Group45\\rdf\\knowledge-graph.nt";
    public SPARQL() {

    }
    public static void print(ArrayList<QuerySolution> list){
        list.forEach((n)->System.out.println(n));
    }
    public static void printBib(ArrayList<Bibliotecas>list){
        list.forEach((n)->System.out.println(n.getPk()));
    }
    public static void printEventos(ArrayList<Eventos>list){
        list.forEach((n)->System.out.println(n.getNombreEvento()));
    }
    public static ArrayList<Bibliotecas> conv(ArrayList<QuerySolution> arr){
        ArrayList<Bibliotecas> sol= new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            String a=arr.get(i).toString();
            String[] b =a.split("\"");
            sol.add(ConvertBiblio(b));
        }
        return sol;
    }
    public static Bibliotecas ConvertBiblio(String[] s){
        Bibliotecas b=new Bibliotecas();
        for(int i=1;i<s.length;i+=2){
            switch (i){
                case 1:
                    b.setPk(s[i]);
                    break;
                case 3:
                    b.setDireccion(s[i]);
                    break;
                case 5:
                    b.setNombre(s[i]);
                    break;
                case 7:
                    b.setDescripcion(s[i]);
                    break;
                case 9:
                    b.setHorario(s[i]);
                    break;
                case 11:
                    b.setTelefono(s[i]);
                    break;
                case 13:
                    b.setUrl(s[i]);
                    break;
            }
        }
        return b;
    }
    public static ArrayList<Bibliotecas> queryBiblioteca(){
        ArrayList<QuerySolution> lista = new ArrayList<QuerySolution>();
        Query query = null;
        //FileManager.get().addLocatorClassLoader(SPARQL.class.getClassLoader());
        Model model = FileManager.get().loadModel(uri);
        String queryString1 =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "\n" +
                        "SELECT DISTINCT ?Nombre ?Descripcion ?Horario ?Localiza ?Telefono ?Url ?pk\n" +
                        "\tWHERE {\n" +
                        "  \t\t?s rdf:type <https://schema.org/Biblioteca>. \n" +
                        "  \t\t?s <https://schema.org/haspk> ?pk .\n" +
                        "\t\t?s <https://schema.org/hasdireccion> ?Localiza .\n" +
                        "\t\t?s <https://schema.org/hasnombre> ?Nombre .\n" +
                        "  \t\t?s <https://schema.org/hasdescription> ?Descripcion .\n" +
                        "  \t\t?s <https://schema.org/hashorarioBib> ?Horario .\n" +
                        "\t\t?s <https://schema.org/hastelefono> ?Telefono .\n" +
                        "  \t\t?s <https://schema.org/hasbiblioteca-url> ?Url .\n" +
                        "\t}";
        query = QueryFactory.create(queryString1);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                //System.out.println(soln);
                lista.add(soln);
            }
        } finally {
            qexec.close();
        }
        return(conv(lista));
    }
    public static ArrayList<Eventos> convEventos(ArrayList<QuerySolution> arr){
        ArrayList<Eventos> sol= new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            String a=arr.get(i).toString();
            String[] b =a.split("\"");
            sol.add(ConvertEvento(b));
        }
        return sol;
    }
    public static Eventos ConvertEvento(String[] s) {
        Eventos b = new Eventos();
        for (int i = 1; i < s.length; i += 2) {
            switch (i) {
                case 1:
                    b.setNombreEvento(s[i]);
                    break;
                case 3:
                    b.setDiasDeSemanas(s[i]);
                    break;
                case 5:
                    b.setFechaInicio(s[i]);
                    break;
                case 7:
                    b.setFechaFin(s[i]);
                    break;
                case 9:
                    b.setHoraEmpiezo(s[i]);
                    break;
                case 11:
                    b.setNombreIntalacion(s[i]);
                    break;
            }
        }
        return b;
    }
    public static ArrayList<Eventos> queryEventos(String id ){
        ArrayList<QuerySolution> lista = new ArrayList<QuerySolution>();
        Query query = null;
        //FileManager.get().addLocatorClassLoader(SPARQL.class.getClassLoader());
        Model model = FileManager.get().loadModel(uri);
        String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                        "SELECT DISTINCT ?Titulo ?Dias ?FechaInicio ?FechaFin ?Hora ?NombreInstalacion\n" +
                        "\tWHERE {\n" +
                        "  \t\t?s rdf:type <https://schema.org/Biblioteca>. \n" +
                        "  \t\t?s <https://schema.org/haspk> \""+id+"\".\n" +
                        "  \t\t?s <https://schema.org/hasEvento> ?Eventos .\n" +
                        "  \t\t?Eventos <https://schema.org/hastitulo> ?Titulo .\n" +
                        "  \t\t?Eventos <https://schema.org/hasdias> ?Dias .\n" +
                        "  \t\t?Eventos <https://schema.org/hasfecha-ini> ?FechaInicio .\n" +
                        "  \t\t?Eventos <https://schema.org/hasfecha-fin> ?FechaFin .\n" +
                        "  \t\t?Eventos <https://schema.org/hashoraEvent> ?Hora .\n" +
                        "\t\t?Eventos <https://schema.org/hasnombre-instalacion> ?NombreInstalacion .\n" +
                        "\t}";
        query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                //System.out.println(soln);
                lista.add(soln);
            }
        } finally {
            qexec.close();
        }
        return convEventos(lista);
    }
    //@SuppressWarnings("deprecation")
    public static void main (String args[]) {
        queryBiblioteca();
    }


}
