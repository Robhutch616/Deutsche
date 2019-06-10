package inquiry;

import inquiry.questions.DataQuestion;
import inquiry.questions.DateOfBirthQuestion;
import inquiry.questions.QuestionType;
import utils.Log;

import java.util.Vector;

/**
 * Holder class for questions to ask the system.
 * <p>
 * The idea
 */
public class Question {

    public static QuestionType[] types = new QuestionType[]{
            new DataQuestion(),
            new DateOfBirthQuestion()
    };

    /**
     * Searches for answer the a given question is the questions follows rules given by one of the defined question Types
     * @param question
     * @return
     */
    public static Answer ask(String question) {
        Log.l("Asking question : ",question);
        for(QuestionType type : types){
            if(type.isThisQuestionType(question)){
                // NOTE: This is single threaded due to time  - can be made multithreaded though
                type.searchForAnswer(question);
                if(type.parseAnswer()) {
                    type.printAnswer();
                    return type.getAnswer();
                }
                Log.error("Failed to parse answer");
            }
        }
        Log.error("Question could not be answered");
        return null;
    }


}
