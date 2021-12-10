package edu.uic.cs554.project;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Objects;

/**
 * Behavior for a hash actor
 */
public class Hasher extends AbstractBehavior<Hasher.Hash> {

    public Hasher(ActorContext<Hash> context) {
        super(context);
    }

    @Override
    public Receive<Hash> createReceive() {
        return newReceiveBuilder().onMessage(Hash.class, this::onHash).build();
    }

    public static final class Hash {
        public final String whisper;
        public final ActorRef<Hash> nextHasher;

        public Hash(String whisper, ActorRef<Hash> nextHasher) {
            this.whisper = whisper;
            this.nextHasher = nextHasher;
        }

        @Override
        public int hashCode() {
            return Objects.hash(whisper, nextHasher);
        }
    }

    public static Behavior<Hash> create() {
        return Behaviors.setup(Hasher::new);
    }

    private Behavior<Hash> onHash(Hash command) {
        getContext().getLog().info("Making hash: {} ", command.whisper);
        int hash = hashCode();

        command.nextHasher.tell(new Hash(command.whisper, getContext().getSelf()));

        return this;
    }

}
