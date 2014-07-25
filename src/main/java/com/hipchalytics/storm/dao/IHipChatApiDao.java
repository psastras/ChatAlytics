package com.hipchalytics.storm.dao;

import com.hipchalytics.model.Message;
import com.hipchalytics.model.Room;
import com.hipchalytics.model.User;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * Interface for interacting with the HipChatAPI.
 *
 * @author giannis
 *
 */
public interface IHipChatApiDao {

    /**
     * Gets all public and private rooms.
     *
     * @return A map of room IDs to {@link Room}s.
     */
    public Map<Integer, Room> getRooms();

    /**
     * Gets all the users.
     *
     * @return A map of user IDs to {@link User}s
     */
    public Map<Integer, User> getUsers();

    /**
     * Gets the list of participating users in a room.
     *
     * @param room
     *            The room for which to get the users.
     * @return A map of user IDs to {@link User}s that are participating in the <code>room</code>
     */
    public Map<Integer, User> getUsersForRoom(Room room);

    /**
     * Gets the list of messages in a room for a particular date range
     *
     * @param start
     *            The start date inclusive
     * @param end
     *            The end date inclusive
     * @param room
     *            The room or which messages will be fetched from
     * @return A list of messages containing the room ID
     */
    public List<Message> getMessages(DateTime start, DateTime end, Room room);

}
