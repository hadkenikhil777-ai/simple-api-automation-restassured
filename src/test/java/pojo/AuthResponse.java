package pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    private int id;
    private String username;
    private String email;

    private String accessToken;
    private String refreshToken;

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public String getRefreshToken(){
        return refreshToken;
    }
}
