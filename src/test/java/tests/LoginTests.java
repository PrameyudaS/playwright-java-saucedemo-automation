package tests;

import base.BaseTest;
import com.microsoft.playwright.assertions.PageAssertions;
import data.TestUsers;
import org.junit.jupiter.api.*;
import pages.InventoryPage;
import pages.LoginPage;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Login scenarios")
class LoginTests extends BaseTest {

  @Test
  @DisplayName("TC_001 Standard user can log in successfully")
  void standardUserCanLoginSuccessfully() {
    LoginPage login = new LoginPage(page).open();
    InventoryPage inventory = new InventoryPage(page);

    login.loginAs(TestUsers.STANDARD, TestUsers.PASSWORD);

    assertThat(page).hasURL(Pattern.compile(".*/inventory\\.html"));
    assertThat(inventory.title()).hasText("Products");
    assertThat(inventory.inventoryItems()).hasCount(6);
  }

  @Test
  @DisplayName("TC_002 Locked-out user is rejected")
  void lockedOutUserIsRejected() {
    LoginPage login = new LoginPage(page).open();

    login.loginAs(TestUsers.LOCKED_OUT, TestUsers.PASSWORD);

    assertThat(login.errorMessage())
        .hasText("Epic sadface: Sorry, this user has been locked out.");
    assertThat(page).hasURL(Pattern.compile("https://www\\.saucedemo\\.com/?"));
    assertThat(login.loginButton()).isVisible();
  }
}
