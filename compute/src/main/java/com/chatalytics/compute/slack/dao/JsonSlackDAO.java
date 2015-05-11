package com.chatalytics.compute.slack.dao;

import com.chatalytics.compute.chat.dao.AbstractJSONChatApiDAO;
import com.chatalytics.compute.chat.dao.IChatApiDAO;
import com.chatalytics.core.config.ChatAlyticsConfig;
import com.chatalytics.core.model.Message;
import com.chatalytics.core.model.Room;
import com.chatalytics.core.model.User;
import com.chatalytics.core.model.slack.json.SlackJsonModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * JSON implementation of the {@link IChatApiDAO} for Slack
 *
 * @author giannis
 *
 */
public class JsonSlackDAO extends AbstractJSONChatApiDAO {

    private static final String AUTH_TOKEN_PARAM = "token";
    private static final Logger LOG = LoggerFactory.getLogger(JsonSlackDAO.class);

    private final WebResource resource;
    private final ChatAlyticsConfig config;
    private final ObjectMapper objMapper;

    private final DateTimeZone dtz;
    private final DateTimeFormatter apiDateFormat;

    public JsonSlackDAO(ChatAlyticsConfig config) {
        super(config.slackConfig.authTokens, AUTH_TOKEN_PARAM);
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create(clientConfig);
        this.resource = client.resource(config.slackConfig.baseSlackURL);
        this.config = config;
        this.dtz = DateTimeZone.forID(config.timeZone);
        this.apiDateFormat = DateTimeFormat.forPattern(config.apiDateFormat).withZone(dtz);
        this.objMapper = new ObjectMapper();
        this.objMapper.registerModule(new SlackJsonModule());
    }

    @Override
    public Map<String, Room> getRooms() {
        WebResource roomResource = resource.path("channels.list");
        String jsonStr = getJsonResultWithRetries(roomResource, config.apiRetries);
        Collection<Room> roomCol = deserializeJsonStr(jsonStr, "channels", Room.class, objMapper);
        Map<String, Room> result = Maps.newHashMapWithExpectedSize(roomCol.size());
        for (Room room : roomCol) {
            result.put(room.getRoomId(), room);
        }
        return result;
    }

    @Override
    public Map<String, User> getUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, User> getUsersForRoom(Room room) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Message> getMessages(DateTime start, DateTime end, Room room) {
        // TODO Auto-generated method stub
        return null;
    }

    private <T> Collection<T> deserializeJsonStr(String jsonStr, String listElement,
                                                 Class<T> clazz,
                                                 ObjectMapper objMapper) {
        TypeFactory typeFactory = objMapper.getTypeFactory();
        CollectionType type = typeFactory.constructCollectionType(List.class, clazz);
        try {
            JsonNode channelsNode = objMapper.readTree(jsonStr).get(listElement);
            return objMapper.readValue(channelsNode, type);
        } catch (IOException e) {
            LOG.error("Got exception when trying to deserialize list of {}", clazz, e);
            return Lists.newArrayListWithExpectedSize(0);
        }
    }
}