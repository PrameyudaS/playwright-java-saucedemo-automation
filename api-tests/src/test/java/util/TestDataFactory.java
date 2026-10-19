package util;

import model.UserRequest;
import java.util.UUID;

public final class TestDataFactory {
  private TestDataFactory() {}

  public static UserRequest uniqueActiveUser() {
    String id = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    return new UserRequest(
        "API Test User " + id,
        "api.test." + id + "@example.com",
        "male",
        "active");
  }

  public static UserRequest updatedFrom(UserRequest original) {
    return new UserRequest(
        original.name() + " Updated",
        original.email(),
        original.gender(),
        "inactive");
  }

  public static UserRequest invalidEmailUser() {
    UserRequest user = uniqueActiveUser();
    return new UserRequest(user.name(), "not-an-email", user.gender(), user.status());
  }

  public static UserRequest missingNameUser() {
    UserRequest user = uniqueActiveUser();
    return new UserRequest(null, user.email(), user.gender(), user.status());
  }
}
