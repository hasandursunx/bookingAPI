package endpoints;

public class Routes {

    public static String base_url = "https://restful-booker.herokuapp.com";


    public static String post_url = base_url + "/booking";
    public static String get_url = base_url + "/booking/{id}";
    public static String update_url = base_url + "/booking/{id}";
    public static String delete_url = base_url + "/booking/{id}";
    public static String get_all_url = base_url + "/booking";
    public static String auth_url = base_url + "/auth";

}
