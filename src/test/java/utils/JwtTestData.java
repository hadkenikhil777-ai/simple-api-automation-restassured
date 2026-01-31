package utils;

import pojo.LoginRequest;

import java.util.HashMap;
import java.util.Map;

public class JwtTestData {

    public static LoginRequest validLoginRequest(){
        return new LoginRequest(
                "emilys",
                "emilyspass",
                30
        );
    }

//    public static LoginRequest loginPayload() {
//    }
}
