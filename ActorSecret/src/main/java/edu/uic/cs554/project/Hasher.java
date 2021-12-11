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

    private final String entityId;
    private String message = "";

    public Hasher(ActorContext<Command> context, String entityId) {
        super(context);
        this.entityId = entityId;
    }

    public interface Command extends CborSerializable {}

    public static class GetHash implements Command {
        private final EntityRef<ActorMain.Command> replyTo;

        public GetHash(EntityRef<ActorMain.Command> replyTo) {
            this.replyTo = replyTo;
        }
    }

    public static Behavior<Command> create(String entityId) {
        return Behaviors.setup(context -> new Hasher(context, entityId));
    }

    private Behavior<Command> onHash(GetHash messageToReply) {
        logger.info("Generating hash for {}", messageToReply);
        System.out.println("msg: " + messageToReply + " " + hashCode());
        message = ""+hashCode();
        messageToReply.replyTo.tell(new ActorMain.HashedMessagedReceived(this.entityId, message));
        return this;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetHash.class, this::onHash)
                .build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, message);
    }
}