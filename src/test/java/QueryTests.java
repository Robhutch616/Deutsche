import inquiry.Answer;
import inquiry.Question;
import inquiry.questions.DataQuestion;
import inquiry.questions.DateOfBirthQuestion;
import org.junit.Assert;
import org.junit.Test;
import service.DbpediaClient;

public class QueryTests {

    public final String AGE_QUESTION = "How old is Tony Blair";
    public final String DATA_QUESTION = "What is the birth place of David Cameron";

    @Test
    public void testDataForDataQuestion(){
        DataQuestion question = new DataQuestion();
        Assert.assertTrue(question.isThisQuestionType(DATA_QUESTION));
        Assert.assertFalse(question.isThisQuestionType(AGE_QUESTION));
        Assert.assertEquals(question.extractSearchQuery(DATA_QUESTION), "David Cameron");
        Assert.assertEquals(question.extractDataKey(DATA_QUESTION), "birthplace");
    }

    @Test
    public void testDataForAgeQuestion(){
        DateOfBirthQuestion question = new DateOfBirthQuestion();
        Assert.assertTrue(question.isThisQuestionType(AGE_QUESTION));
        Assert.assertFalse(question.isThisQuestionType(DATA_QUESTION));
        Assert.assertEquals(question.extractSearchQuery(AGE_QUESTION), "Tony Blair");
        Assert.assertEquals(question.extractDataKey(AGE_QUESTION), "birthdate");
    }

    @Test
    public void testAgeQuestion(){
        Answer answer = Question.ask(AGE_QUESTION);
        Assert.assertEquals("66", answer.getAnswer());
    }

    @Test
    public void testDataQuestion(){
        Answer answer = Question.ask(DATA_QUESTION);
        Assert.assertEquals("London, England, UK", answer.getAnswer());
    }
}
