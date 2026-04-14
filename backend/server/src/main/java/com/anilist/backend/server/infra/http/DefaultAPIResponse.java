package com.anilist.backend.server.infra.http;

import java.sql.Date;
import java.util.List;


public abstract class DefaultAPIResponse<T> {
    public boolean Success;
    public String Message;
    public T Data; 
    public List<String> Errors;
    public Date Timestamp;
}
