package service;

/**
 * Parsing links extracted from Dbpedia
 */
public class DbLinkParser {
    // prefix format seen in dbpedia results
    public final String prefix = "http://dbpedia.org/";
    public final String resource = "resource";
    public final String data = "data";
    public final String suffix = ".json";

    private String link;

    public DbLinkParser(String link){
        this.link = link;
    }

    /**
     * Parses json link from url extracted from dbpedia.
     *
     * Assumes format of dbpedia.org/resource/[RESOURCE]
     * @return url that will return a json format
     */
    public String parseJsonLink(){
        if(link == null){
            return null;
        }
        String dataLink = this.link.replaceFirst(resource,data);
        return dataLink + suffix;
    }
}
