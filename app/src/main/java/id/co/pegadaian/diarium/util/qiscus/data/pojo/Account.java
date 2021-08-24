package id.co.pegadaian.diarium.util.qiscus.data.pojo;

/**
 * Created by adicatur on 12/24/16.
 */

public class Account {

    private String identityToken;
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public String getIdentityToken() {
        return identityToken;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setIdentityToken(String identityToken) {
        this.identityToken = identityToken;
    }
}
