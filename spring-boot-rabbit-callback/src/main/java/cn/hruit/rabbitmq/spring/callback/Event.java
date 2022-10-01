package cn.hruit.rabbitmq.spring.callback;

import java.util.Date;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 21:07
 **/

public class Event {
    private String id;
    private Date date;
    private String content;

    public Event(String id, Date date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
