package test._2_NegativeTests;

import endpoints.Routes;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Negative_1_CreateToken_InvalidUsernamePassword {
    Response response;
    @BeforeClass
    public void setupData() {
        JSONObject data = new JSONObject();
        data.put("username","1");
        data.put("password","1");


        response = given().contentType(ContentType.JSON)
                        .body(data.toString())
                        .when().post(Routes.auth_url);

        response.then().log().body();

    }
    @Description("Durum kodu 200 mü")
    @Test(testName = "Durum kodu 200 mü",priority = 1)
    public void testStatusCode() {
        response.then().assertThat().statusCode(200);
    }

    @Description("Content Type kontrolü")
    @Test(testName = "Content Type kontrolü",priority = 2)
    public void testContentType() {
        response.then().contentType(ContentType.JSON);
    }
    @Description("Response süresi kontrolü")
    @Test(testName = "Response süresi kontrolü",priority = 3)
    public void testResponseTime() {
        response.then().time(lessThan(10000L));
    }

    @Description("Response dolu mu  Kontrolü")
    @Test(testName = "Response dolu mu  Kontrolü",priority = 5)
    public void testResponseIsNotEmpty() {
        response.then().body(not(emptyString()));
    }

    @Description("Bad Credientials Değeri Kontrolü")
    @Test(testName = "Bad Credientials Değeri Kontrolü",priority = 5)
    public void testBadCredentialsResponse() {
        response.then().body("reason", equalTo("Bad credentials"));
    }




}
