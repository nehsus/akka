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
        //final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "helloakka");
        //#actor-system

        //#main-send-messages
        //greeterMain.tell(new GreeterMain.SayHello("Charles"));
        //#main-send-messages
        try {
            parseYML();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void parseYML() throws FileNotFoundException {
        Map<String, String> data;
        InputStream inp = new FileInputStream(actorYAML);
        Yaml yaml = new Yaml(); //new Constructor(Actor.class)
        data = yaml.load(inp);
        System.out.println(data);

        for (Map.Entry<String, String> set : data.entrySet()) {
            System.out.println(set);
            // Create actors here


        }
    }

}