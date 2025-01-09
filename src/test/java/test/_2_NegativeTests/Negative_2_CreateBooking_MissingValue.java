package test._2_NegativeTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import endpoints.BookingEndPoints;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Booking;
import payload.BookingDates;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;


public class Negative_2_CreateBooking_MissingValue {
    public static Integer bookingid;
    private Response response;
    public static Booking bookingPayload;
    Booking responseBooking;

    //True değeri yanlış
    @BeforeClass
    public void setupData() throws JsonProcessingException {

        bookingPayload = new Booking();

        bookingPayload.setFirstname("Ç@t!$");

        bookingPayload.setTotalprice(0);
        bookingPayload.setDepositpaid(true);
        BookingDates bookingDates = new BookingDates("2020-01-01", "2020-01-01");
        bookingPayload.setBookingdates(bookingDates);
        bookingPayload.setAdditionalneeds("Dinner");

        response = BookingEndPoints.createBooking(bookingPayload);

        response.then().log().body();

    }

    @Description("Durum kodu 500 mü")
    @Test(testName = "Durum kodu 500 mü",priority = 1)
    public void testStatusCode() {
        response.then().assertThat().statusCode(500);
    }

    @Description("Content Type kontrolü")
    @Test(testName = "Content Type kontrolü",priority = 2)
    public void testContentType() {
        response.then().contentType(ContentType.TEXT);
    }
    @Description("Response süresi kontrolü")
    @Test(testName = "Response süresi kontrolü",priority = 3)
    public void testResponseTime() {
        response.then().time(lessThan(10000L));
    }

    @Description("Response dolu mu  Kontrolü")
    @Test(testName = "Response dolu mu  Kontrolü",priority = 4)
    public void testResponseIsNotEmpty() {
        response.then().body(not(emptyString()));
    }

    @Description("Body Internal Server Error Değeri Kontrolü")
    @Test(testName = "Body Internal Server Error Değeri Kontrolü",priority = 5)
    public void testBadCredentialsResponse() {
        response.then().body(equalTo("Internal Server Error"));
    }

}
