package com.example.expensetrackerforuniversitystudent;


import java.io.Serializable;

public class Question implements Serializable {
    private String studentID;
    private String question;
    private String department;
    private String timestamp;

    public Question() {

    }//empty constructor for firebase

    public Question(String studentID, String question, String department, String timestamp) {
        this.studentID = studentID;
        this.question = question;
        this.department = department;
        this.timestamp = timestamp;
    }

    //getters and setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
