package edu.uic.cs554.project;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Main actor to start the hashing function which takes input from different actors in form of hashed messages
 */
public class ActorMain extends AbstractBehavior<ActorMain.Command> {

    /**
     * logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ActorMain.class);

    /**
     * String for appended hash
     */
    private String finalMessage = "";

    /**
     * Command interface for starting hash implementation
     */
    public interface Command extends CborSerializable {
    }

    /**
     * Class implementing Command interface to start the hashing process.
     */
    public final static class StartHashing implements Command {
        private final String value;

        public StartHashing(String value) {
            this.value = value;
        }
    }

    /**
     * Constructor.
     *
     * @param context
     */
    public ActorMain(ActorContext<Command> context) {
        super(context);
    }

    private static EntityRef<Command> mainActor;
    private static List<EntityRef<Hasher.Command>> hashers = new ArrayList<>();

    /**
     * Function to create different actors/hashers from YAML input.
     *
     * @param aList
     * @return
     */
    public static Behavior<Command> create(List<edu.uic.cs554.project.Actor> aList) {
        return Behaviors.setup(context -> {
            ClusterSharding clusterSharding = ClusterSharding.get(context.getSystem());
            EntityTypeKey<Hasher.Command> typeKey = EntityTypeKey.create(Hasher.Command.class, "Hasher");

            clusterSharding.init(Entity.of(typeKey, ctx -> Hasher.create(ctx.getEntityId())));

            // Iterate through list of actors
            for (Actor person : aList) {
                System.out.println("Creating actor: " + person.getActorName());
                String hasherIndex = person.getActorName();
                hashers.add(clusterSharding.entityRefFor(typeKey, hasherIndex));
            }

            EntityTypeKey<ActorMain.Command> typeKeyForMainActor = EntityTypeKey.create(ActorMain.Command.class, "MainActor");
            clusterSharding.init(Entity.of(typeKeyForMainActor, ctx -> ActorMain.createActor()));

            mainActor = clusterSharding.entityRefFor(typeKeyForMainActor, "mainActor");
            System.out.println("Starting ");
            for (EntityRef<Hasher.Command> hasher : hashers) {
                hasher.tell(new Hasher.GetHash(mainActor));
            }

            return Behaviors.empty();
        });
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartHashing.class, this::onStartHashing)
                .onMessage(HashedMessagedReceived.class, this::onHashMessageReceive).build();
    }

    /**
     * TO create the main Actor.
     *
     * @return
     */
    public static Behavior<Command> createActor() {
        return Behaviors.setup(ActorMain::new);
    }

    /**
     * To implement the functionality for after message is received from hashers.
     *
     */
    public final static class HashedMessagedReceived implements Command {
        public final String entityId;
        public final String message;

        public HashedMessagedReceived(String entityId, String message) {
            this.entityId = entityId;
            this.message = message;
        }

        @Override
        public int hashCode() {
            return Objects.hash(entityId, message);
        }
    }

    private Behavior<Command> onHashMessageReceive(HashedMessagedReceived messageReceived) {
        int hasherNumber = Integer.parseInt(messageReceived.entityId);

        String oldHash = messageReceived.message;

        System.out.println("Old hash from " + hasherNumber + ", " + oldHash);
        System.out.println("New hash: " + messageReceived.hashCode());
        logger.info("Old hash from " + hasherNumber + ", " + oldHash);
        logger.info("New hash: " + messageReceived.hashCode());

        appendHash(oldHash);

        hashers.get(hasherNumber).tell(new Hasher.GetHash(mainActor));
        return this;
    }

    /**
     *
     * @param command
     * @return
     */
    private Behavior<Command> onStartHashing(StartHashing command) {
        getContext().getLog().info("Started Making hash ");
        System.out.println("Started making hash");
        return this;
    }

    private void appendHash(String hash) {
        this.finalMessage +=hash;
    }

}
