package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import payload.Booking;

import static io.restassured.RestAssured.given;

public class BookingEndPoints {

    public static String token;


    // Token Üret
    public static Response createToken() {
        JSONObject data = new JSONObject();
        data.put("username", "admin");
        data.put("password", "password123");

        Response response =
                given().contentType(ContentType.JSON)
                        .body(data.toString())
                        .when().post(Routes.auth_url);

        token = response.jsonPath().getString("token");
        return response;
    }


    // Tüm Rezervasyonaların id'lerini getirir
    public static Response readAllBookingIds() {
        Response response = given().when().get(Routes.get_all_url);
        return response;
    }


    // Rezervasyon oluşturur.
    public static Response createBooking(Booking payload) {
        Response response =
                given().contentType(ContentType.JSON)
                        .body(payload)
                        .when().post(Routes.post_url);
        return response;
    }


    //Bir tane rezervasyonu id'sine göre  getirir.
    public static Response readBookingById(Integer id) {
        Response response = given().pathParam("id",id).when().get(Routes.get_url);
        return response;
    }


    //rezervasyonu düzenle.
    public static Response updateBooking(Integer id, Booking payload) {
        Response response =
                given()
                        .contentType("application/json")
                        .accept("application/json")
                        .cookie("token", token)
                        .pathParam("id",id)
                        .body(payload)
                        .when().put(Routes.update_url);
        return response;
    }


    //rezervasyonun bir kısmını düzenle.
    public static Response updatePatchBooking(Integer id, Booking payload) {
        Response response =
                given()
                        .contentType("application/json")
                        .accept("application/json")
                        .cookie("token", token)
                        .pathParam("id",id)
                        .body(payload)
                        .when().patch(Routes.update_url);
        return response;
    }


    // Rezervasyonu siler
    public static Response deleteBooking(Integer id) {
        Response response =
                given()
                        .contentType("application/json")
                        .accept("application/json")
                        .cookie("token", token)
                        .pathParam("id",id)
                        .when().delete(Routes.delete_url);
        return response;
    }
}
