import org.junit.Assert;
import org.junit.Test;
import service.DbLinkParser;

/**
 * Tests for Url  link parser
 */
public class LinkerTests {

    @Test
    public void When_parsingUrls_Then_ReturnJsonLinks(){
        DbLinkParser parser = new DbLinkParser("http://dbpedia.org/resource/Category:Fast_food");
        Assert.assertEquals("http://dbpedia.org/data/Category:Fast_food.json", parser.parseJsonLink());
    }
}
