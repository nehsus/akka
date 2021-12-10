package edu.uic.cs554.project;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * Creates and tracks actors
 */
public class HasherBot extends AbstractBehavior<Hasher.Hash>  {

    private final int max;
    private int hashCounter;

    public HasherBot(ActorContext<Hasher.Hash> context, int max) {
        super(context);
        this.max = max;
    }

    public static Behavior<Hasher.Hash> create(int max) {
        return Behaviors.setup(context -> new HasherBot(context, max));
    }

    @Override
    public Receive<Hasher.Hash> createReceive() {
        return newReceiveBuilder().onMessage(Hasher.Hash.class, this::onWhisper).build();
    }

    private Behavior<Hasher.Hash> onWhisper(Hasher.Hash message) {
        hashCounter++;
        getContext().getLog().info("Made hash {} for {}", hashCounter, message.nextHasher);
        if (hashCounter == max) {
            return Behaviors.stopped();
        } else {
            message.nextHasher.tell(new Hasher.Hash(message.whisper, getContext().getSelf()));
            return this;
        }
    }
}
