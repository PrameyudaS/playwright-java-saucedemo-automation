package client;

import config.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.UserRequest;

import static io.restassured.RestAssured.given;

public class UserApiClient {
  private final String token;

  public UserApiClient(String token) {
    this.token = token;
  }

  public ApiExchange listUsers(int page, int perPage) {
    String dump = "GET /users?page=" + page + "&per_page=" + perPage;
    Response response = authenticatedRequest().queryParam("page", page).queryParam("per_page", perPage)
        .when().get("/users");
    return new ApiExchange(dump, response);
  }

  public ApiExchange getUser(long id) {
    String dump = "GET /users/" + id;
    Response response = authenticatedRequest().pathParam("id", id).when().get("/users/{id}");
    return new ApiExchange(dump, response);
  }

  public ApiExchange createUser(UserRequest request) {
    return createUser(request, true);
  }

  public ApiExchange createUserWithoutAuthentication(UserRequest request) {
    return createUser(request, false);
  }

  private ApiExchange createUser(UserRequest request, boolean authenticated) {
    String dump = "POST /users\nBody: " + request + "\nAuthenticated: " + authenticated;
    RequestSpecification specification = authenticated ? authenticatedRequest() : baseRequest();
    Response response = specification.body(request).when().post("/users");
    return new ApiExchange(dump, response);
  }

  public ApiExchange updateUser(long id, UserRequest request) {
    String dump = "PUT /users/" + id + "\nBody: " + request;
    Response response = authenticatedRequest().pathParam("id", id).body(request)
        .when().put("/users/{id}");
    return new ApiExchange(dump, response);
  }

  public ApiExchange deleteUser(long id) {
    String dump = "DELETE /users/" + id;
    Response response = authenticatedRequest().pathParam("id", id)
        .when().delete("/users/{id}");
    return new ApiExchange(dump, response);
  }

  private RequestSpecification baseRequest() {
    return given().baseUri(ApiConfig.BASE_URI).accept(ContentType.JSON).contentType(ContentType.JSON);
  }

  private RequestSpecification authenticatedRequest() {
    return baseRequest().auth().oauth2(token);
  }
}
