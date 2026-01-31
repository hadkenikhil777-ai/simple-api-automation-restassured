package pojo;

public class RefreshTokenRequest {

    private String refreshToken;
    private int expiresInMins;

    public RefreshTokenRequest(String refreshToken, int expiresInMins){
        this.refreshToken = refreshToken;
        this.expiresInMins = expiresInMins;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public int getExpiresInMins(){
        return expiresInMins;
    }
}
