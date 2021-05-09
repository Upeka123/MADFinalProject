package com.example.expensetrackerforuniversitystudent;

public class Answer {
    private String idNo;
    private String questionID;
    private String timestamp;
    private String answer;

    public Answer() {
    }

    public Answer(String idNo, String questionID, String timestamp, String answer) {
        this.idNo = idNo;
        this.questionID = questionID;
        this.timestamp = timestamp;
        this.answer = answer;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
