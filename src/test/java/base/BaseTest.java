package base;

import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

import java.util.ResourceBundle;

public class BaseTest {

    protected static ResourceBundle config;

    @BeforeClass
    public void setup(){
        config = ResourceBundle.getBundle("config");
        RestAssured.baseURI =config.getString("base.url");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


//    @AfterSuite
//    public void tearDown() {
//        AuthContext.clear();
//    }

}
