package edu.uic.cs554.project;

import akka.actor.typed.ActorSystem;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static edu.uic.cs554.project.utils.Constant.actorYAML;

/**
 * Default class. Parses yml, launches actors with config
 */
public class ActorSecret {

    List<Actor> aList = new ArrayList<>();

    public static void main(String[] args) {

        //#actor-system

        try {
            parseYML();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final ActorSystem<ActorMain.Whisper> actorMain = ActorSystem.create(
                ActorMain.create(), "default");

        // TODO: create multiple hashers from aList and communicate



        //actorMain.tell(new ActorMain.Whisper("test"));
        //#main-send-messages


    }

    private static void parseYML() throws FileNotFoundException {
        Map<String, String> data;
        InputStream inp = new FileInputStream(actorYAML);
        Yaml yaml = new Yaml(); //new Constructor(Actor.class)
        data = yaml.load(inp);
        System.out.println(data);

        for (Map.Entry<String, String> set : data.entrySet()) {
            System.out.println(set);
            // TODO: Create actors list here, return


        }
    }

}