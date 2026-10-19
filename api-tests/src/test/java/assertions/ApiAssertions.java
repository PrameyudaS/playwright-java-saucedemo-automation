package assertions;

import client.ApiExchange;
import model.UserRequest;
import model.UserResponse;
import model.ValidationError;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class ApiAssertions {
  private ApiAssertions() {}

  public static void assertStatus(ApiExchange exchange, int expected) {
    assertEquals(expected, exchange.response().statusCode(), exchange.diagnosticDump());
  }

  public static UserResponse assertUserMatches(ApiExchange exchange, UserRequest expected, int status) {
    assertStatus(exchange, status);
    UserResponse actual = deserializeUser(exchange);
    assertAll(exchange.diagnosticDump(),
        () -> assertNotNull(actual.id(), exchange.diagnosticDump()),
        () -> assertEquals(expected.name(), actual.name(), exchange.diagnosticDump()),
        () -> assertEquals(expected.email(), actual.email(), exchange.diagnosticDump()),
        () -> assertEquals(expected.gender(), actual.gender(), exchange.diagnosticDump()),
        () -> assertEquals(expected.status(), actual.status(), exchange.diagnosticDump()));
    return actual;
  }

  public static UserResponse deserializeUser(ApiExchange exchange) {
    try {
      return exchange.response().as(UserResponse.class);
    } catch (RuntimeException exception) {
      fail("Could not deserialize user response." + exchange.diagnosticDump(), exception);
      return null;
    }
  }

  public static void assertValidationError(ApiExchange exchange, String field, String messageFragment) {
    assertStatus(exchange, 422);
    List<ValidationError> errors = Arrays.asList(exchange.response().as(ValidationError[].class));
    boolean found = errors.stream().anyMatch(error ->
        field.equals(error.field()) && error.message() != null &&
            error.message().toLowerCase().contains(messageFragment.toLowerCase()));
    assertTrue(found,
        "Expected validation error for field '" + field + "' containing '" + messageFragment + "'." +
            exchange.diagnosticDump());
  }
}
