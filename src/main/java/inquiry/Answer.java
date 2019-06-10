package inquiry;

import utils.Log;

public class Answer {
    // Holder for the question that led to this answer
    private String questionAsked;
    // holder for the answer
    private String answer;

    public Answer(String questionAsked, String answer) {
        this.questionAsked = questionAsked;
        this.answer = answer;
    }

    public String getQuestionAsked() {
        return questionAsked;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void print(){
        Log.l("Found answer for question '%s' : '%s'", questionAsked, answer);
    }
}
