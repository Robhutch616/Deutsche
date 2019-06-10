package inquiry.questions;

import inquiry.Answer;
import utils.FormatUtils;
import utils.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Query age of person, currently restricted to "How old is [NAME]"
 */
public class DateOfBirthQuestion extends QuestionType {

    /**
     * leading part of question
     */
    public static final String prefix = "How old is ";

    @Override
    public String extractSearchQuery(String question) {
        return question.substring(prefix.length());
    }

    /**
     * This has been extracted from the format of data returned by dbpedia
     *
     * @param question question asked by user
     * @return
     */
    @Override
    public String extractDataKey(String question) {
        return "birthdate";
    }

    @Override
    public boolean isThisQuestionType(String question) {
        return question.substring(0, prefix.length()).equals(prefix);
    }

    @Override
    public boolean parseAnswer() {
        if (answer == null || answer.getAnswer() == null) {
            return false;
        }
        String value = answer.getAnswer();
        Date birth = FormatUtils.toDate(value);
        if (birth == null) {
            Log.error("Failed to parse date");
            return false;
        }
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        calendar.setTime(birth);
        int birthYear = calendar.get(Calendar.YEAR);
        answer.setAnswer(String.valueOf(year - birthYear));
        return true;
    }
}
