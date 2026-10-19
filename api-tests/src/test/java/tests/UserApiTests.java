package tests;

import client.ApiExchange;
import model.UserRequest;
import model.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestDataFactory;

import static assertions.ApiAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GoREST user API")
class UserApiTests extends BaseApiTest {
  private static final long NON_EXISTENT_ID = 999_999_999_999L;

  @Test
  @DisplayName("API_001 creates a user with unique data")
  void createsUser() {
    UserRequest request = TestDataFactory.uniqueActiveUser();
    UserResponse created = assertUserMatches(users.createUser(request), request, 201);
    track(created.id());
  }

  @Test
  @DisplayName("API_002 retrieves a user created by this test")
  void retrievesCreatedUser() {
    UserRequest request = TestDataFactory.uniqueActiveUser();
    UserResponse created = assertUserMatches(users.createUser(request), request, 201);
    track(created.id());

    assertUserMatches(users.getUser(created.id()), request, 200);
  }

  @Test
  @DisplayName("API_003 updates a user created by this test")
  void updatesCreatedUser() {
    UserRequest original = TestDataFactory.uniqueActiveUser();
    UserResponse created = assertUserMatches(users.createUser(original), original, 201);
    track(created.id());

    UserRequest updated = TestDataFactory.updatedFrom(original);
    assertUserMatches(users.updateUser(created.id(), updated), updated, 200);
    assertUserMatches(users.getUser(created.id()), updated, 200);
  }

  @Test
  @DisplayName("API_004 deletes a user and verifies it no longer exists")
  void deletesCreatedUser() {
    UserRequest request = TestDataFactory.uniqueActiveUser();
    UserResponse created = assertUserMatches(users.createUser(request), request, 201);
    track(created.id());

    assertStatus(users.deleteUser(created.id()), 204);
    untrack(created.id());
    assertStatus(users.getUser(created.id()), 404);
  }

  @Test
  @DisplayName("API_005 lists users without assuming ordering")
  void listsUsersWithoutOrderingAssumption() {
    ApiExchange exchange = users.listUsers(1, 10);
    assertStatus(exchange, 200);
    UserResponse[] results = exchange.response().as(UserResponse[].class);
    assertNotNull(results, exchange.diagnosticDump());
    assertTrue(results.length <= 10, exchange.diagnosticDump());
  }

  @Test
  @DisplayName("API_006 rejects a duplicate email")
  void rejectsDuplicateEmail() {
    UserRequest request = TestDataFactory.uniqueActiveUser();
    UserResponse created = assertUserMatches(users.createUser(request), request, 201);
    track(created.id());

    UserRequest duplicate = new UserRequest("Another Name", request.email(), "female", "active");
    assertValidationError(users.createUser(duplicate), "email", "taken");
  }

  @Test
  @DisplayName("API_007 rejects an invalid email")
  void rejectsInvalidEmail() {
    assertValidationError(users.createUser(TestDataFactory.invalidEmailUser()), "email", "invalid");
  }

  @Test
  @DisplayName("API_008 rejects a missing required name")
  void rejectsMissingName() {
    assertValidationError(users.createUser(TestDataFactory.missingNameUser()), "name", "blank");
  }

  @Test
  @DisplayName("API_009 returns not found for an unknown user")
  void returnsNotFoundForUnknownUser() {
    assertStatus(users.getUser(NON_EXISTENT_ID), 404);
  }

  @Test
  @DisplayName("API_010 rejects an unauthenticated create request")
  void rejectsUnauthenticatedCreate() {
    assertStatus(users.createUserWithoutAuthentication(TestDataFactory.uniqueActiveUser()), 401);
  }
}
