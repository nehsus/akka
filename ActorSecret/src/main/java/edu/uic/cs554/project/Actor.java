package edu.uic.cs554.project;

/**
 * JavaBean model for an actor
 */
public class Actor {
    private String actorName;
    private String actorMsg;

    public Actor() {}

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorMsg() {
        return actorMsg;
    }

    public void setActorMsg(String actorMsg) {
        this.actorMsg = actorMsg;
    }
}
