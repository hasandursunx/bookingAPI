package test._1_PositiveTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import endpoints.BookingEndPoints;
import io.qameta.allure.Description;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Booking;
import payload.BookingDates;

import static org.hamcrest.Matchers.notNullValue;

public class _2_CreateBooking {

    public static Integer bookingid;
    private Response response;
    public static Booking bookingPayload;
    Booking responseBooking;

    @BeforeClass
    public void setupData() throws JsonProcessingException {

        bookingPayload = new Booking();

        bookingPayload.setFirstname("Hasan");
        bookingPayload.setLastname("Dursun");
        bookingPayload.setTotalprice(0);
        bookingPayload.setDepositpaid(true);
        BookingDates bookingDates = new BookingDates("2020-01-01", "2020-01-01");
        bookingPayload.setBookingdates(bookingDates);
        bookingPayload.setAdditionalneeds("Dinner");

        response = BookingEndPoints.createBooking(bookingPayload);
        bookingid = response.jsonPath().getInt("bookingid");

        responseBooking = response.jsonPath().getObject("booking", Booking.class);

    }

    @Description("StatusCode 200 kontrolü")
    @Test(testName = "StatusCode 200 kontrolü",priority = 1)
    public void testStatusCode() {
        Assert.assertEquals(response.statusCode(), 200,"StatusCode 200");
        System.out.println(response.statusCode());

    }

    @Description("Content-Type kontrolü")
    @Test(testName = "Content-Type kontrolü",priority = 2)
    public void testContentType() {
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Content-Type kontrolü");
        System.out.println(response.contentType());
    }

    @Description("Response time kontolü")
    @Test(testName = "Response time kontolü",priority = 3)
    public void testResponseTime() {
        Assert.assertTrue(response.getTime() < 3000, "Response süresi kontrolü");
        System.out.println(response.getTime());
    }

    @Description("Response Body dolu mu kontrolü")
    @Test(testName = "Response Body dolu mu kontrolü",priority = 4)
    public void testBookingBody() {
        Assert.assertNotNull(response.body(), "Response Body dolu mu kontrolü");
        response.body().prettyPrint();
    }

    @Description("Booking Schema kontrolü")
    @Test(testName = "Booking Schema kontrolü",priority = 5)
    public void testBookingSchema() {
        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("bookingCreateJsonSchema.json"));
    }

    @Description("Firstname değer kontrolü")
    @Test(testName = "Firstname değer kontrolü",priority = 6)
    public void testFirstname() {
       Assert.assertEquals(bookingPayload.getFirstname(), responseBooking.getFirstname());
        System.out.println("Payload :"  +  bookingPayload.getFirstname() + "\nResponse :"  +  responseBooking.getFirstname());
    }

    @Description("Lastname değer kontrolü")
    @Test(testName = "Lastname değer kontrolü",priority = 7)
    public void testLastname() {
        Assert.assertEquals(bookingPayload.getLastname(), responseBooking.getLastname());
        System.out.println("Payload :"  +  bookingPayload.getLastname() + "\nResponse :"  +  responseBooking.getLastname());
    }

    @Description("Total Price değer kontrolü")
    @Test(testName = "Total Price değer kontrolü",priority = 8)
    public void testTotalPrice() {
        Assert.assertEquals(bookingPayload.getTotalprice(), responseBooking.getTotalprice());
        System.out.println("Payload :"  +  bookingPayload.getTotalprice() + "\nResponse :"  +  responseBooking.getTotalprice());
    }

    @Description("Depositpaid değer kontrolü")
    @Test(testName = "Depositpaid değer kontrolü",priority = 9)
    public void testDepositpaid() {
        Assert.assertEquals(bookingPayload.getDepositpaid(), responseBooking.getDepositpaid());
        System.out.println("Payload :"  +  bookingPayload.getDepositpaid() + "\nResponse :"  +  responseBooking.getDepositpaid());
    }
    @Description("Booking Dates - Check-in değer kontrolü")
    @Test(testName = "Booking Dates - Check-in değer kontrolü",priority = 10)
    public void testBookingDatesCheckIn() {
        Assert.assertEquals(bookingPayload.getBookingdates().getCheckin(), responseBooking.getBookingdates().getCheckin());
        System.out.println("Payload :"  +  bookingPayload.getBookingdates().getCheckin() + "\nResponse :"  +  responseBooking.getBookingdates().getCheckin());
    }

    @Description("Booking Dates - Check-out değer kontrolü")
    @Test(testName = "Booking Dates - Check-out değer kontrolü",priority = 11)
    public void testBookingDatesCheckOut() {
        Assert.assertEquals(bookingPayload.getBookingdates().getCheckout(), responseBooking.getBookingdates().getCheckout());
        System.out.println("Payload :"  +  bookingPayload.getBookingdates().getCheckout() + "\nResponse :"  +  responseBooking.getBookingdates().getCheckout());
    }
    @Description("Additional Needs değer kontrolü")
    @Test(testName = "Additional Needs değer kontrolü",priority = 12)
    public void testAdditionalNeeds() {
        Assert.assertEquals(bookingPayload.getAdditionalneeds(), responseBooking.getAdditionalneeds());
        System.out.println("Payload :"  +  bookingPayload.getAdditionalneeds() + "\nResponse :"  +  responseBooking.getAdditionalneeds());
    }








}
