package edu.uic.cs554.project;

/**
 * Model for an actor
 */
public class Actor {
    private final String actorName;
    private final String actorMsg;

    public Actor(String actorName, String actorMsg) {
        this.actorName = actorName;
        this.actorMsg = actorMsg;
    }

    public String getActorName() {
        return actorName;
    }

    public String getActorMsg() {
        return actorMsg;
    }

}
