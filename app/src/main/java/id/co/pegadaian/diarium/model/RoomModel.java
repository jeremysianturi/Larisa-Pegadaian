package id.co.pegadaian.diarium.model;

public class RoomModel {
    String room_id;
    String room_name;
    String floor;
    String building;

    public RoomModel(String room_id, String room_name, String floor, String building) {
        this.room_id = room_id;
        this.room_name = room_name;
        this.floor = floor;
        this.building = building;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
