package edu.uic.cs554.project;

import akka.actor.typed.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default class. Parses yml, launches actors with config
 */
public class ActorSecret {

    private static final Logger logger = LoggerFactory.getLogger(ActorSecret.class);
    List<Actor> aList = new ArrayList<>();

    public static void main(String[] args) {

        //#actor-system
        try {
            parseYML();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ActorSystem<ActorMain.Command> mainActorSystem = ActorSystem.create(
                ActorMain.create(), "PasswordHashing");

        // TODO: create multiple hashers from aList and communicate



        //actorMain.tell(new ActorMain.Whisper("test"));
        //#main-send-messages
    }

    private static void parseYML() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Group.class));
        try (InputStream in = ActorSecret.class.getResourceAsStream("/actors.yml")) {
            Group group = yaml.load(in);
            int i = 0;
            for (Actor person : group.getActors()) {
                logger.info("actor " + i + " name : " + person.getActorName());
                logger.info("actor " + i + " msg : " + person.getActorMsg());
                System.out.println("actor " + i + " name : " + person.getActorName());
                System.out.println("actor " + i + " msg : " + person.getActorMsg());
                i++;
            }
        }
    }
}