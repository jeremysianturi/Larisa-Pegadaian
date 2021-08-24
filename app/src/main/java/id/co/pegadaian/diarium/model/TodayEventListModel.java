package id.co.pegadaian.diarium.model;

public class TodayEventListModel {
    String id;
    String name;
    String description;
    String event_start;
    String event_end;

    public TodayEventListModel(String id, String name, String description, String event_start, String event_end) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.event_start = event_start;
        this.event_end = event_end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent_start() {
        return event_start;
    }

    public void setEvent_start(String event_start) {
        this.event_start = event_start;
    }

    public String getEvent_end() {
        return event_end;
    }

    public void setEvent_end(String event_end) {
        this.event_end = event_end;
    }
}
