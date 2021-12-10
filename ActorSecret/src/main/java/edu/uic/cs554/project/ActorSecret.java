package edu.uic.cs554.project;

import akka.actor.typed.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ActorSystem<ActorMain.Whisper> mainActorSystem = ActorSystem.create(
                ActorMain.create(), "default");

        // TODO: create multiple hashers from aList and communicate



        //actorMain.tell(new ActorMain.Whisper("test"));
        //#main-send-messages
    }

    private static void parseYML() throws IOException {
        ArrayList<String> data;
        //InputStream inputStream = new FileInputStream(actorYAML);
        Yaml yaml = new Yaml();
        try (InputStream in = ActorSecret.class.getResourceAsStream("/actors.yml")) {
            Actors persons = yaml.loadAs(in, Actors.class);
            for (Actor person : persons.getActors()) {
                System.out.println(person);
            }
        }



//        Yaml yaml = new Yaml(new Constructor(Actor.class));
//        InputStream inputStream = ActorSecret.class
//                .getResourceAsStream("src/main/resources/actors.yml");
//
//        int count = 0;
//        for (Object object : yaml.loadAll(inputStream)) {
//            Actor actor = (Actor) object;
//            logger.info("Yaml Actor : " + count + " : name : "+ actor.getActorName());
//            logger.info("message : "+ actor.getActorMsg());
//            count++;
//        }


//        Yaml yaml = new Yaml(); //new Constructor(Actor.class)
//        data = yaml.load(inputStream);
//        logger.info("Yaml Data : " + data);
//        int i = 1;
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//
//            logger.info("Yaml Actor : " + i + " : name : "+ entry.getKey());
//            logger.info("message : "+ entry.getValue());
//            i++;
//            // TODO: Create actors list here, return
//
//        }
    }
}