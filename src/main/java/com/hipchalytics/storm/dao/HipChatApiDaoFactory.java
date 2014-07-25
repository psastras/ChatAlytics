package com.hipchalytics.storm.dao;

import com.hipchalytics.config.HipChalyticsConfig;

/**
 * Factory for getting an instance of the hipchat DAO.
 *
 * @author giannis
 *
 */
public class HipChatApiDaoFactory {

    private static IHipChatApiDao hipchatDaoImpl;

    private HipChatApiDaoFactory() {
        // hide constructor
    }

    public static IHipChatApiDao getHipChatApiDao(HipChalyticsConfig hconfig) {
        if (hipchatDaoImpl == null) {
            hipchatDaoImpl = new JsonHipChatDao(hconfig);
        }
        return hipchatDaoImpl;
    }

}
