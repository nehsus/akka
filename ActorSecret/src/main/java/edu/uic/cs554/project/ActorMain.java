package edu.uic.cs554.project;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Behavior for an actor
 */
public class ActorMain extends AbstractBehavior<ActorMain.Whisper> {

    private static final Logger logger = LoggerFactory.getLogger(ActorMain.class);
    private final ActorRef<Hasher.Hash> hasher;

    public ActorMain(ActorContext<Whisper> context) {
        super(context);
        hasher = context.spawn(Hasher.create(), "hasher");
    }

    @Override
    public Receive<Whisper> createReceive() {
        return newReceiveBuilder().onMessage(Whisper.class, this::onWhisper).build();
    }

    public static class Whisper {
        public final String whisper;

        public Whisper(String whisper) {
            this.whisper = whisper;
        }
    }

    public static Behavior<Whisper> create() {
        return Behaviors.setup(ActorMain::new);
    }

    private Behavior<Whisper> onWhisper(Whisper command) {

        ActorRef<Hasher.Hash> nextHasher = getContext().spawn(HasherBot.create(5), command.whisper);
        hasher.tell(new Hasher.Hash(command.whisper, nextHasher));

        return this;
    }
}