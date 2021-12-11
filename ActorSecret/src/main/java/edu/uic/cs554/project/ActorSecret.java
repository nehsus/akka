package edu.uic.cs554.project;

import akka.actor.AddressFromURIString;
import akka.actor.typed.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.uic.cs554.project.utils.Constant;
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
 * Main class which starts the program
 */
public class ActorSecret {

    private static final Logger logger = LoggerFactory.getLogger(ActorSecret.class);
    private static List<Actor> aList = new ArrayList<>();

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            parseYML();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config configuration = ConfigFactory.load();
        List<Integer> portsForSeedNodes = configuration
                .getStringList("akka.cluster.seed-nodes")
                .stream()
                .map(AddressFromURIString::parse)
                .map(address -> (Integer) address.port().get())
                .collect(Collectors.toList());

        List<Integer> ports = Arrays.stream(args).findFirst().map(string ->
                Collections.singletonList(Integer.parseInt(string))
        ).orElseGet(() -> {
            List<Integer> ports2 = new ArrayList<>(portsForSeedNodes);
            ports2.add(0);
            return ports2;
        });

        Config portConfig = finalPortsConfig(ports.get(0));
        if (aList.size() != 0) {
            ActorSystem.create(ActorMain.create(aList), "PasswordHashing", portConfig);
        } else {
            logger.error("No actors found..");
        }
    }

    /**
     * creates final config with ports.
     *
     * @param port
     * @return
     */
    private static Config finalPortsConfig(int port) {
        return ConfigFactory.parseMap(
                Collections.singletonMap("akka.remote.artery.canonical.port", Integer.toString(port))
        ).withFallback(ConfigFactory.load());
    }

    /**
     * parses YAML file provided as input.
     *
     * @throws IOException
     */
    private static void parseYML() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Group.class));
        try (InputStream in = ActorSecret.class.getResourceAsStream(Constant.YAML_FILE_PATH)) {
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