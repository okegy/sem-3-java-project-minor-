package com.cyberlearn.app.model;

import java.util.ArrayList;
import java.util.List;

public class Progress {
    private String username;
    private int quizScore;
    private int quizAttempts;
    private int encUses;
    private int scanUses;
    private int hashUses;
    private int passMgrUses;
    private List<String> completedLessons = new ArrayList<>();

    public Progress(){}

    public Progress(String username){
        this.username = username;
    }

    public String getUsername(){ return username; }
    public int getQuizScore(){ return quizScore; }
    public int getQuizAttempts(){ return quizAttempts; }
    public int getEncUses(){ return encUses; }
    public int getScanUses(){ return scanUses; }
    public int getHashUses(){ return hashUses; }
    public int getPassMgrUses(){ return passMgrUses; }
    public List<String> getCompletedLessons(){ return completedLessons == null ? new ArrayList<>() : completedLessons; }

    public void setUsername(String v){ username=v; }
    public void setQuizScore(int v){ quizScore=v; }
    public void setQuizAttempts(int v){ quizAttempts=v; }
    public void setEncUses(int v){ encUses=v; }
    public void setScanUses(int v){ scanUses=v; }
    public void setHashUses(int v){ hashUses=v; }
    public void setPassMgrUses(int v){ passMgrUses=v; }
    public void setCompletedLessons(List<String> v){ completedLessons=v; }
}
