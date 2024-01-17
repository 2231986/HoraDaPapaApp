package pt.ipleiria.estg.dei.horadapapa.models;

public class LoginInfo {
    private final String token;
    private final String userId;

    public LoginInfo(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }
}