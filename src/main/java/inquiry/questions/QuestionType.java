package inquiry.questions;


import inquiry.Answer;
import service.DbpediaClient;
import service.JsonCallback;
import utils.Log;

public abstract class QuestionType {

    protected Answer answer = null;

    /**
     * Extracts the search query terms from the question
     *
     * @param question - question asked by user
     * @return Search query
     */
    abstract public String extractSearchQuery(String question);

    /**
     * Extracts the data key to be used in search of the answer
     *
     * @param question question asked by user
     * @return
     */
    abstract public String extractDataKey(String question);

    /**
     * @param question
     * @return true if the question fits this objects type, false otherwise
     */
    abstract public boolean isThisQuestionType(String question);

    /**
     * initiate search for answer
     *
     * @param question
     * @return
     */
    public void searchForAnswer(String question) {
        DbpediaClient.Instance().searchQuery(extractSearchQuery(question), extractDataKey(question), new JsonCallback() {
            @Override
            public void onFoundValue(String value) {
                Log.l("Found answer value : %s", value);
                answer = new Answer(question, value);
            }

            @Override
            public void onFailure(String reason) {
                Log.error("Failed to extact answer for question %s : %s", question, reason);
            }
        });
    }

    abstract public boolean parseAnswer();

    public Answer getAnswer() {
        return answer;
    }

    public void printAnswer(){
        answer.print();
    }
}
