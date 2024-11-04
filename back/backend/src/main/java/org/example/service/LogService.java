package org.example.service;

import org.example.model.entity.Log;

import java.util.Set;

public interface LogService {

    /**
     * This method will be used to create LIKES or VIEWS
     * */
    public Log createLog(Log log);

    public Set<Log> getLogsByUserName(String userName, Log.Type type);

    public Set<Log> getAllLogsByUserName(String userName);

    public Float getUserFameRate(String userName);

}
