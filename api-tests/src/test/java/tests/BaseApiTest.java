package tests;

import client.ApiExchange;
import client.UserApiClient;
import config.ApiConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseApiTest {
  protected static UserApiClient users;
  private final List<Long> createdUserIds = new ArrayList<>();

  @BeforeAll
  static void configureClient() {
    users = new UserApiClient(ApiConfig.requireToken());
  }

  protected void track(long userId) {
    createdUserIds.add(userId);
  }

  protected void untrack(long userId) {
    createdUserIds.remove(userId);
  }

  @AfterEach
  void cleanupCreatedUsers() {
    List<String> cleanupFailures = new ArrayList<>();
    for (Long id : createdUserIds) {
      try {
        ApiExchange exchange = users.deleteUser(id);
        int status = exchange.response().statusCode();
        if (status != 204 && status != 404) cleanupFailures.add(exchange.diagnosticDump());
      } catch (RuntimeException exception) {
        cleanupFailures.add("Cleanup failed for user " + id + ": " + exception.getMessage());
      }
    }
    createdUserIds.clear();
    if (!cleanupFailures.isEmpty()) {
      throw new AssertionError("Test data cleanup failed:\n" + String.join("\n", cleanupFailures));
    }
  }
}
