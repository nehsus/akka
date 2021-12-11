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

import java.util.Objects;

/**
 * Behavior for a hash actor
 */
public class Hasher extends AbstractBehavior<Hasher.Command> {

    private static final Logger logger = LoggerFactory.getLogger(Hasher.class);

    private String message = "";
    private final String entityId;
    public Hasher(ActorContext<Command> context, String entityId) {
        super(context);
        this.entityId = entityId;
    }

    public interface Command {}

    public static class GetHash implements Command {
        private final EntityRef<ActorMain.Command> replyTo;

        public GetHash(EntityRef<ActorMain.Command> replyTo) {
            this.replyTo = replyTo;
        }
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetHash.class, this::onHash)
                .build();
    }
//
//    public static final class Hash {
//        public final String whisper;
//        public final ActorRef<Hash> nextHasher;
//
//        public Hash(String whisper, ActorRef<Hash> nextHasher) {
//            this.whisper = whisper;
//            this.nextHasher = nextHasher;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(whisper, nextHasher);
//        }
//    }

    public static Behavior<Command> create(String entityId) {
        return Behaviors.setup(context -> new Hasher(context, entityId));
    }

    private Behavior<Command> onHash(GetHash messageToReply) {
        getContext().getLog().info("Making hash ");
        //TODO: Hash messages
        message = "hashed messaged";
        messageToReply.replyTo.tell(new ActorMain.HashedMessagedReceived(this.entityId, message));
        return this;
    }
}