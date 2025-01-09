package test._1_PositiveTest;

import endpoints.BookingEndPoints;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class _1_CreateToken {


    private Response response;

    @BeforeClass
    public void setupData() {
        response = BookingEndPoints.createToken();
    }

    @Description("Durum kodu 200 mü")
    @Test(testName = "Durum kodu 200 mü",priority = 1)
    public void testStatusCode() {
        response.then().assertThat().statusCode(200);
        System.out.println(response.statusCode());
        response.then().log().body();
    }

    @Description("Content Type kontrolü")
    @Test(testName = "Content Type kontrolü",priority = 2)
    public void testContentType() {
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Content-Type kontrolü");
        System.out.println(response.contentType());
    }

    @Description("Response süresi kontrolü")
    @Test(testName = "Response süresi kontrolü",priority = 3)
    public void testResponseTime() {
        Assert.assertTrue(response.getTime() < 3000, "Response süresi kontrolü");
        System.out.println(response.getTime());
    }

    @Description("Response Body dolu mu kontrolü")
    @Test(testName = "Response Body dolu mu kontrolü",priority = 4)
    public void testBookingBody() {
        Assert.assertNotNull(response.body(), "Response Body dolu mu kontrolü");
        response.then().log().body();
    }

    @Description("Token değeri mecvut mu kontrolü")
    @Test(testName = "Token değeri mecvut mu kontrolü",priority = 5)
    public void testResponseBody() {
        response.then().assertThat().body("token",notNullValue());;
        System.out.println(response.body().jsonPath().getString("token"));
    }

    @Description("Token 15 karakter olmalı kontrolü")
    @Test(testName = "Token 15 karakter olmalı kontrolü",priority = 6)
    public void testIsTokenString() {
        response.then().assertThat().body("token.length()", equalTo(15));;
        System.out.println(response.body().jsonPath().getString("token.length()"));
    }

}
