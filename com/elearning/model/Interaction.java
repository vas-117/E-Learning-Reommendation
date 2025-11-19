package com.elearning.model;

public class Interaction {
    private int interactionId;
    private int userId;
    private int courseId;
    private String interactionType;

    public Interaction() {}

    public Interaction(int interactionId, int userId, int courseId, String interactionType) {
        this.interactionId = interactionId;
        this.userId = userId;
        this.courseId = courseId;
        this.interactionType = interactionType;
    }

    public int getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(int interactionId) {
        this.interactionId = interactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String toString() {
        return "Interaction [interactionId=" + interactionId + ", userId=" + userId + ", courseId=" + courseId + ", interactionType=" + interactionType + "]";
    }
}