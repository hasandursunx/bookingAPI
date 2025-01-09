package test._1_PositiveTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import endpoints.BookingEndPoints;
import io.qameta.allure.Description;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.BookingDates;

import static test._1_PositiveTest._2_CreateBooking.bookingPayload;

public class _4_UpdateBooking {

    Response response;

    @BeforeClass
    public void setupData() throws JsonProcessingException {

        bookingPayload.setFirstname("Mehmet");
        bookingPayload.setLastname("Pusmaz");
        bookingPayload.setTotalprice(1);
        bookingPayload.setDepositpaid(false);
        BookingDates bookingDates = new BookingDates("2020-01-01", "2020-01-01");
        bookingPayload.setBookingdates(bookingDates);
        bookingPayload.setAdditionalneeds("Breakfast");

        response = BookingEndPoints.updateBooking(_2_CreateBooking.bookingid, bookingPayload);

        response.then().log().body();
    }

    @Description("StatusCode 200 kontrolü")
    @Test(testName = "StatusCode 200 kontrolü",priority = 1)
    public void testStatusCode() {
        Assert.assertEquals(response.statusCode(), 200,"StatusCode 200");
    }

    @Description("Content-Type kontrolü")
    @Test(testName = "Content-Type kontrolü",priority = 2)
    public void testContentType() {
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Content-Type kontrolü");
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

    @Description("Booking Schema kontrolü")
    @Test(testName = "Booking Schema kontrolü",priority = 5)
    public void testBookingSchema() {
        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("bookingJsonSchema.json"));
    }
    @Description("Firstname değer kontrolü")
    @Test(testName = "Firstname değer kontrolü",priority = 6)
    public void testFirstname() {
        Assert.assertEquals(_2_CreateBooking.bookingPayload.getFirstname(), response.jsonPath().get("firstname"));
    }

    @Description("Lastname değer kontrolü")
    @Test(testName = "Lastname değer kontrolü",priority = 7)
    public void testLastname() {
        Assert.assertEquals(_2_CreateBooking.bookingPayload.getLastname(), response.jsonPath().get("lastname"));
    }

    @Description("Total Price değer kontrolü")
    @Test(testName = "Total Price değer kontrolü",priority = 8)
    public void testTotalPrice() {
        Assert.assertEquals(_2_CreateBooking.bookingPayload.getTotalprice(), response.jsonPath().get("totalprice"));
    }

    @Description("Depositpaid değer kontrolü")
    @Test(testName = "Depositpaid değer kontrolü",priority = 9)
    public void testDepositpaid() {
        Assert.assertEquals(_2_CreateBooking.bookingPayload.getDepositpaid(), response.jsonPath().get("depositpaid"));
    }
    @Description("Booking Dates - Check-in değer kontrolü")
    @Test(testName = "Booking Dates - Check-in değer kontrolü",priority = 10)
    public void testBookingDatesCheckIn() {
        Assert.assertEquals(_2_CreateBooking.bookingPayload.getBookingdates().getCheckin(), response.jsonPath().get("bookingdates.checkin"));
    }

    @Description("Booking Dates - Check-out değer kontrolü")
    @Test(testName = "Booking Dates - Check-out değer kontrolü",priority = 11)
    public void testBookingDatesCheckOut() {
        Assert.assertEquals(_2_CreateBooking.bookingPayload.getBookingdates().getCheckout(), response.jsonPath().get("bookingdates.checkout"));
    }
    @Description("Additional Needs değer kontrolü")
    @Test(testName = "Additional Needs değer kontrolü",priority = 12)
    public void testAdditionalNeeds() {
        Assert.assertEquals(_2_CreateBooking.bookingPayload.getAdditionalneeds(), response.jsonPath().get("additionalneeds"));
    }
}
