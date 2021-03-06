package com.chatalytics.compute;

import com.chatalytics.compute.storm.ChatAlyticsService;
import com.chatalytics.compute.storm.ChatAlyticsStormTopology;
import com.chatalytics.compute.util.YamlUtils;
import com.chatalytics.core.config.ChatAlyticsConfig;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Entry point for running the Storm topology.
 *
 * @author giannis
 *
 */
public class ChatAlyticsEngineMain {

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        ChatAlyticsConfig config = YamlUtils.readYamlFromResource("chatalytics.yaml",
                                                                  ChatAlyticsConfig.class);

        ChatAlyticsStormTopology chatTopology = new ChatAlyticsStormTopology(config.inputType);


        ChatAlyticsService chatalyticsService = new ChatAlyticsService(chatTopology.get(), config);
        chatalyticsService.startAsync().awaitRunning();

    }

}
