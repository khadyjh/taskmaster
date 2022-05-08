package com.codeFellow.taskmaster.data;

public class Task {
    private String title;
    private String body;
    private State state;

    public Task(String title, String body, State state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", state=" + state +
                '}';
    }
}
