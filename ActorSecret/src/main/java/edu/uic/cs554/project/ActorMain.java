package edu.uic.cs554.project;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import akka.cluster.sharding.typed.javadsl.EntityRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Behavior for an actor
 */
public class ActorMain extends AbstractBehavior<ActorMain.Command> {

    private static final Logger logger = LoggerFactory.getLogger(ActorMain.class);

    public interface Command {}

    public final static class StartHashing implements Command {
        private final String value;

        public StartHashing(String value) {
            this.value = value;
        }
    }

    public ActorMain(ActorContext<Command> context) {
        super(context);
        //hasher = context.spawn(Hasher.create(), "hasher");
    }

    private static EntityRef<Command> mainActor;
    private static List<EntityRef<Hasher.Command>> hashers = new ArrayList<>();

    public static Behavior<Command> create() {
        return Behaviors.setup(ActorMain::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartHashing.class, this::onStartHashing)
                .onMessage(HashedMessagedReceived.class, this::onHashMessageReceive).build();
    }

//    public static class Whisper {
//        public final String whisper;
//
//        public Whisper(String whisper) {
//            this.whisper = whisper;
//        }
//    }


    public final static class HashedMessagedReceived implements Command {
        public final String entityId;
        public final String message;

        public HashedMessagedReceived(String entityId, String message) {
            this.entityId = entityId;
            this.message = message;
        }
    }

    private Behavior<Command> onHashMessageReceive(HashedMessagedReceived messageReceived) {
        int hasherNumber = Integer.parseInt(messageReceived.entityId);

        getContext().getLog().info("Message received from : " + messageReceived.entityId + ", message : " + messageReceived.message);
        hashers.get(hasherNumber).tell(new Hasher.GetHash(mainActor));
        return this;
    }

    private Behavior<Command> onStartHashing(StartHashing command) {
        getContext().getLog().info("Started Making hash ");
        return this;
    }

//    private Behavior<Whisper> onWhisper(Whisper command) {
//
//        ActorRef<Hasher.Command> nextHasher = getContext().spawn(HasherBot.create(5), command.whisper);
//        hasher.tell(new Hasher.Command(command.whisper, nextHasher));
//
//        return this;
//    }
}