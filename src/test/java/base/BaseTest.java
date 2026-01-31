package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import java.util.ResourceBundle;

public class BaseTest {

    @BeforeClass
    public void setup(){
        ResourceBundle  config = ResourceBundle.getBundle("config");
        RestAssured.baseURI =config.getString("base.url");
        RestAssured.baseURI =config.getString("base.url");
    }
}
