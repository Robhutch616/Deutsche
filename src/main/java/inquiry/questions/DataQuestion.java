package inquiry.questions;

import inquiry.Answer;
import inquiry.Question;
import utils.FormatUtils;

/**
 * Data type question allows asking the system any combination of "What is the [DATA_KEY] of [THING]
 */
public class DataQuestion extends QuestionType {

    /**
     * leading part of question
     */
    public static final String prefix = "What is the ";
    public static final String intermediate = "of";

    @Override

    public String extractSearchQuery(String question) {
        int pos = question.indexOf(intermediate);
        if (pos == -1) {
            return null;
        }
        return question.substring(pos + intermediate.length()).trim();
    }

    @Override
    public String extractDataKey(String question) {
        int pos = question.indexOf(intermediate);
        if (pos == -1) {
            return null;
        }
        return FormatUtils.removeWhitespace(question.substring(prefix.length(), pos)).toLowerCase();
    }

    @Override
    public boolean isThisQuestionType(String question) {
        return question.substring(0, prefix.length()).equals(prefix);
    }

    @Override
    public boolean parseAnswer() {
        return answer != null && answer.getAnswer() != null;
    }
}
