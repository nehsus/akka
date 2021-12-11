package edu.uic.cs554.project;

import akka.actor.AddressFromURIString;
import akka.actor.typed.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default class. Parses yml, launches actors with config
 */
public class ActorSecret {

    private static final Logger logger = LoggerFactory.getLogger(ActorSecret.class);
    private static List<Actor> aList = new ArrayList<>();

    public static void main(String[] args) {

        try {
            parseYML();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config config = ConfigFactory.load();
        List<Integer> seedNodePorts = config
                .getStringList("akka.cluster.seed-nodes")
                .stream()
                .map(AddressFromURIString::parse)
                .map(addr -> (Integer) addr.port().get())
                .collect(Collectors.toList());

        List<Integer> ports = Arrays.stream(args).findFirst().map(str ->
                Collections.singletonList(Integer.parseInt(str))
        ).orElseGet(() -> {
            List<Integer> portsAndZero = new ArrayList<>(seedNodePorts);
            portsAndZero.add(0);
            return portsAndZero;
        });

        Config portConfig = configWithPort(ports.get(0));
        if (aList.size() != 0) {
            ActorSystem.create(ActorMain.create(aList), "PasswordHashing", portConfig);
        } else {
            logger.error("No actors found..");
        }
    }

    private static Config configWithPort(int port) {
        return ConfigFactory.parseMap(
                Collections.singletonMap("akka.remote.artery.canonical.port", Integer.toString(port))
        ).withFallback(ConfigFactory.load());
    }

    private static void parseYML() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Group.class));
        try (InputStream in = ActorSecret.class.getResourceAsStream("/actors.yml")) {
            Group group = yaml.load(in);
            int i = 0;
            for (Actor person : group.getActors()) {
                logger.info("actor " + i + " name : " + person.getActorName());
                logger.info("actor " + i + " msg : " + person.getActorMsg());
                aList.add(person);
                i++;
            }
        }
    }
}