package edu.uic.cs554.project;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * Creates and tracks actors
 */
public class HasherBot {
        //extends AbstractBehavior<Hasher.Command>  {

//    private final String entityId;
//    private int hashCounter;
//
//    public HasherBot(ActorContext<Hasher.Command> context, String entityId) {
//        super(context);
//        this.entityId = entityId;
//    }
//
//    public static Behavior<Hasher.Command> create(String entityId) {
//        return Behaviors.setup(context -> new HasherBot(context, entityId));
//    }
//
//    @Override
//    public Receive<Hasher.Command> createReceive() {
//        return newReceiveBuilder().onMessage(Hasher.Command.class, this::onWhisper).build();
//    }

//    private Behavior<Hasher.Command> onWhisper(Hasher.Command message) {
//        hashCounter++;
//        getContext().getLog().info("Made hash {} ", hashCounter);
//        if (hashCounter == max) {
//            return Behaviors.stopped();
//        } else {
//            message.nextHasher.tell(new Hasher.Command(message.whisper, getContext().getSelf()));
//            return this;
//        }
//    }
}
