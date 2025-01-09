package test._1_PositiveTest;

import endpoints.BookingEndPoints;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class _6_DeleteBooking {
    Response response;

    @BeforeClass
    public void setupData() {
        response =  BookingEndPoints.deleteBooking(_2_CreateBooking.bookingid);
        response.then().log().body();
    }
    @Description("StatusCode 201 kontrolü")
    @Test(testName = "StatusCode 201 kontrolü",priority = 1)
    public void testStatusCode() {
        Assert.assertEquals(response.statusCode(), 201,"StatusCode 201");
    }

    @Description("Content-Type kontrolü")
    @Test(testName = "Content-Type kontrolü",priority = 2)
    public void testContentType() {
        Assert.assertTrue(response.getHeader("Content-Type").contains("text/plain"), "Content-Type kontrolü");
    }

    @Description("Response time kontolü")
    @Test(testName = "Response time kontolü",priority = 3)
    public void testResponseTime() {
        Assert.assertTrue(response.getTime() < 3000, "Response süresi kontrolü");
    }

    @Description("Response Body dolu mu kontrolü")
    @Test(testName = "Response Body dolu mu kontrolü",priority = 4)
    public void testBookingBody() {
        Assert.assertNotNull(response.body(), "Response Body dolu mu kontrolü");
    }

    @Description("Response değeri Created mi kontrolü")
    @Test(testName = "Response değeri Created mi kontrolü",priority = 5)
    public void testCreatedMessage() {
        Assert.assertEquals(response.body().asString(), "Created", "Response değeri Created mi kontrolü");
    }
}
