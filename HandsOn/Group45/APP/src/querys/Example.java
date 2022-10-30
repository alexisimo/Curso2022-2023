package querys;

import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.examples.ExampleHelpers;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;
import java.util.Collections;

import static org.wikidata.wdtk.examples.FetchOnlineDataExample.printDocumentation;

public class Example {
    public static void main(String[] args) throws IOException, MediaWikiApiErrorException {
        ExampleHelpers.configureLogging();
        printDocumentation();

        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(
                ApiConnection.getWikidataApiConnection(),
                Datamodel.SITE_WIKIDATA);
        wbdf.getFilter().setLanguageFilter(Collections.singleton("es"));
        //---------------
        EntityDocument madrid = wbdf.getEntityDocument("Q2807");
        Statement stat=  ((ItemDocument) madrid).findStatement("P2044");
        System.out.println(stat.getClaim().getValue());


        String retr= stat.getValue().toString();
        String[] arr1=retr.split("/");
        String Code=arr1[4].split(" ")[0];
        System.out.println(Code);
        EntityDocument res = wbdf.getEntityDocument(Code);
        if (res instanceof ItemDocument) {
            System.out.println("The English name for entity sol is "
                    + ((ItemDocument) res).getLabels().get("es").toString());
        }
        //----------
        /*
        System.out.println("*** Fetching data for one entity:");
        EntityDocument q42 = wbdf.getEntityDocument("Q42");
        System.out.println(q42);

        EntityDocument q85 = wbdf.getEntityDocument("Q84");

        System.out.println(q85.toString());
        if (q85 instanceof ItemDocument) {
            System.out.println("The English name for entity Q85 is "
                    + ((ItemDocument) q85).getLabels().get("fr").toString());
        }

        if (q42 instanceof ItemDocument) {
            System.out.println("The English name for entity Q42 is "
                    + ((ItemDocument) q42).getLabels().get("en").getText());
        }
        Statement statement=  ((ItemDocument) q42).findStatement("P31");
        String web= statement.getValue().toString();
        String[] arr=web.split("/");
        String numb=arr[4].split(" ")[0];
        System.out.println(numb);
        EntityDocument sol = wbdf.getEntityDocument(numb);
        if (sol instanceof ItemDocument) {
            System.out.println("The English name for entity sol is "
                    + ((ItemDocument) sol).getLabels().toString());
        }
        System.out.println("Esto P31 "+ statement.getValue());
        /*
        //statement.getClaim().toString();
        System.out.println("Esto P31 "+ statement.getClaim().getSubject());
        System.out.println("*** Fetching data for several entities:");
        Map<String, EntityDocument> results = wbdf.getEntityDocuments("Q80",
                "P31");
        // Keys of this map are Qids, but we only use the values here:
        for (EntityDocument ed : results.values()) {
            System.out.println("Successfully retrieved data for "
                    + ed.getEntityId().getId());
        }

        System.out
                .println("*** Fetching data using filters to reduce data volume:");
        // Only site links from English Wikipedia:
        wbdf.getFilter().setSiteLinkFilter(Collections.singleton("enwiki"));
        // Only labels in French:
        wbdf.getFilter().setLanguageFilter(Collections.singleton("fr"));
        // No statements at all:
        wbdf.getFilter().setPropertyFilter(Collections.emptySet());
        EntityDocument q8 = wbdf.getEntityDocument("Q8");
        if (q8 instanceof ItemDocument) {
            System.out.println("The French label for entity Q8 is "
                    + ((ItemDocument) q8).getLabels().get("fr").getText()
                    + "\nand its English Wikipedia page has the title "
                    + ((ItemDocument) q8).getSiteLinks().get("enwiki")
                    .getPageTitle() + ".");
        }

        System.out.println("*** Fetching data based on page title:");
        EntityDocument edPratchett = wbdf.getEntityDocumentByTitle("enwiki",
                "Terry Pratchett");
        System.out.println("The Qid of Terry Pratchett is "
                + edPratchett.getEntityId().getId());

        System.out.println("*** Fetching data based on several page titles:");
        results = wbdf.getEntityDocumentsByTitle("enwiki", "Wikidata",
                "Wikipedia");
        // In this case, keys are titles rather than Qids
        for (Map.Entry<String, EntityDocument> entry : results.entrySet()) {
            System.out
                    .println("Successfully retrieved data for page entitled \""
                            + entry.getKey() + "\": "
                            + entry.getValue().getEntityId().getId());
        }

        System.out.println("** Doing search on Wikidata:");
        for(WbSearchEntitiesResult result : wbdf.searchEntities("Douglas Adams", "fr")) {
            System.out.println("Found " + result.getEntityId() + " with label " + result.getLabel());
        }

        System.out.println("*** Done.");


        EntityDocument q84 = wbdf.getEntityDocument("Q42");

        System.out.println(q84.toString());
        if (q84 instanceof ItemDocument) {
            System.out.println("The English name for entity Q84 is "
                    + ((ItemDocument) q84).getLabels().get("fr").toString());
        }*/
    }
}