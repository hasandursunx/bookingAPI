package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import endpoints.BookingEndPoints;
import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Booking;
import payload.BookingDates;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HappyPath {

    Booking bookingPayload;
    Integer bookingid;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public void setupData() {
        bookingPayload = new Booking();

        bookingPayload.setFirstname("Hasan");
        bookingPayload.setLastname("Dursun");
        bookingPayload.setTotalprice(0);
        bookingPayload.setDepositpaid(true);
        BookingDates bookingDates = new BookingDates("2020-01-01", "2020-01-01");
        bookingPayload.setBookingdates(bookingDates);
        bookingPayload.setAdditionalneeds("Dinner");
    }
    @Description("Token oluşturma kontrolü")
    @Test(testName = "Token oluşturuldu",priority = 1)
    public void CreateToken() {
        Response response = BookingEndPoints.createToken();
        response.then().log().body();
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .time(lessThan(10000L))
                .body("token",notNullValue());
    }

    @Description("Rezervasyon oluşturma kontrolü")
    @Test(testName = "Rezervasyon oluşturuldu",priority = 2)
    public void PostBooking() throws JsonProcessingException {
        Response response = BookingEndPoints.createBooking(bookingPayload);
        bookingid = response.jsonPath().getInt("bookingid");
        response.then().log().body();

        // Kontroller
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .time(lessThan(10000L))
                .body(notNullValue());

        //Schema kontrolü
        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("bookingCreateJsonSchema.json"));

        //Response'da id değeri varmı kontrolü
        response.then().body("bookingid",notNullValue());

        //Responsdan gelen Değerlerin kontrolü


        String bookingPayloadJSON = objectMapper.writeValueAsString(bookingPayload);
        boolean responseBookingJSON = response.body().asString().contains(bookingPayloadJSON);
        Assert.assertTrue(responseBookingJSON, "Rezervasyon bilgileri kontrolü");

    }

    @Description("Oluşturulan Rezervasyonu listeleme kontrolü")
    @Test(testName = "Oluşturulan Rezervasyon listelendi",priority = 3)
    public void GetBookingById() throws JsonProcessingException {

        Response response = BookingEndPoints.readBookingById(bookingid);

        Assert.assertEquals(response.getStatusCode(), 200, "Durum kodu 200");
        Assert.assertTrue(response.getTime() < 10000, "Response süresi kontrolü");
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Content-Type kontrolü");
        Assert.assertNotNull(response.body(), "Body değeri mevcut mu kontrolü");

        response.then().log().body();


        //Schema kontrolü
        response.then()
                .assertThat()
                .body(JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("bookingJsonSchema.json"));



        // Pojo 'dan  json'a dönüştürme
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBookingJSON = response.then().extract().body().asString();
        String bookingPayloadJSON = objectMapper.writeValueAsString(bookingPayload);

        // Responsdan gelen Değerlerin kontrolü
        Assert.assertEquals(responseBookingJSON, bookingPayloadJSON, "Response ile payload değer kontrolü");

    }

    @Description("Tüm rezervasyon id")
    @Test(testName = "Tüm Rezervasyon Id leri listeleme kontrolü",priority = 4)
    public void GetBookingIds() {
        Response response = BookingEndPoints.readAllBookingIds();
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 200, "Durum kodu 200");
        Assert.assertTrue(response.getTime() < 10000, "Response süresi kontrolü");
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Content-Type kontrolü");
        Assert.assertNotNull(response.body(), "Body değeri dolu kontrolü");

        //id'ler içerisinde oluşturulan rezervasyon id'si mevcut mu kontrolü
        List<Integer> bookingIds = response.jsonPath().getList("bookingid", Integer.class);
        boolean isBookingIdPresent = bookingIds.contains(bookingid);
        Assert.assertTrue(isBookingIdPresent, "Booking ID değeri mevcut mu kontrolü");

    }

    @Description("Rezervasyon düzenleme kontrolü")
    @Test(testName = "Rezervasyon düzenlendi",priority = 5)
    public void UpdateBooking() throws JsonProcessingException {

        // Güncellenen değerler
        bookingPayload.setFirstname("Mehmet");
        bookingPayload.setLastname("Pusmaz");
        bookingPayload.setTotalprice(1);
        bookingPayload.setDepositpaid(false);
        BookingDates bookingDates = new BookingDates("2020-01-01", "2020-01-01");
        bookingPayload.setBookingdates(bookingDates);
        bookingPayload.setAdditionalneeds("Breakfast");

        Response response = BookingEndPoints.updateBooking(bookingid, bookingPayload);
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 200, "Durum kodu 200");
        Assert.assertTrue(response.getTime() < 10000, "Response süresi kontrolü");
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Content-Type kontrolü");
        Assert.assertNotNull(response.body(), "Body değeri dolu kontrolü");

        //Schema kontrolü
        response.then()
                .assertThat()
                .body(JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("bookingJsonSchema.json"));

        // Pojo 'dan  json'a dönüştürme
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBookingJSON = response.then().extract().body().asString();
        String bookingPayloadJSON = objectMapper.writeValueAsString(bookingPayload);

        // Responsdan ve payload değerleri kontrolü
        Assert.assertEquals(responseBookingJSON, bookingPayloadJSON, "Response ile payload değer kontrolü");
    }

    @Description("Rezervasyonu patch ile düzenleme kontrolü")
    @Test(testName = "Rezervasyonun firstname ve lastname alanları düzenlendi",priority = 6)
    public void PatchBooking() throws JsonProcessingException {

        // firstname ve lastname alanları patch ile değiştirilir
        bookingPayload.setFirstname("Furkan");
        bookingPayload.setLastname("Çam");

        Response response = BookingEndPoints.updatePatchBooking(bookingid, bookingPayload);
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 200, "Durum kodu 200");
        Assert.assertTrue(response.getTime() < 10000, "Response süresi kontrolü");
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Content-Type kontrolü");
        Assert.assertNotNull(response.body(), "Body değeri dolu kontrolü");

        //Schema kontrolü
        response.then()
                .assertThat()
                .body(JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("bookingJsonSchema.json"));

        // Pojo 'dan  json'a dönüştürme
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBookingJSON = response.then().extract().body().asString();
        String bookingPayloadJSON = objectMapper.writeValueAsString(bookingPayload);

        // Responsdan ve payload değerleri kontrolü
        Assert.assertEquals(responseBookingJSON, bookingPayloadJSON, "Response ile payload değer kontrolü");
    }

    @Description("Rezervasyon silindi mi kontrolü")
    @Test(testName = "Rezervasyon silindi",priority = 7)
    public void DeleteBooking() {

        Response response = BookingEndPoints.deleteBooking(bookingid);
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 201, "Durum kodu 201 kontrolü");
        Assert.assertTrue(response.getTime() < 10000, "Response süresi kontrolü");
        Assert.assertTrue(response.getHeader("Content-Type").contains("text/plain"), "Content-Type kontrolü");
        Assert.assertNotNull(response.body(), "Body değeri dolu kontrolü");
        Assert.assertEquals(response.body().asString(), "Created", "Response değeri Created mi kontrolü");

    }
}
