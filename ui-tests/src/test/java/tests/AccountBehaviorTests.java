package tests;

import base.BaseTest;
import com.microsoft.playwright.assertions.PageAssertions;
import data.TestUsers;
import org.junit.jupiter.api.*;
import pages.InventoryPage;
import pages.LoginPage;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account-specific behavior scenarios")
class AccountBehaviorTests extends BaseTest {

  @Test
  @DisplayName("TC_019 problem_user displays the observed duplicate product images")
  void problemUserDisplaysDuplicateProductImages() {
    LoginPage login = new LoginPage(page).open();
    InventoryPage inventory = new InventoryPage(page);

    login.loginAs(TestUsers.PROBLEM, TestUsers.PASSWORD);
    assertThat(inventory.title()).hasText("Products");
    assertThat(inventory.inventoryItems()).hasCount(6);

    List<String> imageSources = inventory.productImageSources();
    assertEquals(6, imageSources.size(), "Six product images should be rendered");
    assertFalse(imageSources.contains(null), "Every product image should have a src attribute");
    assertEquals(1, new HashSet<>(imageSources).size(),
        "Observed defect: problem_user shows the same image source for every product");
  }

  @Test
  @DisplayName("TC_010 performance_glitch_user eventually reaches inventory")
  void performanceGlitchUserEventuallyReachesInventory() {
    LoginPage login = new LoginPage(page).open();
    InventoryPage inventory = new InventoryPage(page);

    long startedAt = System.nanoTime();
    login.loginAs(TestUsers.PERFORMANCE_GLITCH, TestUsers.PASSWORD);

    assertThat(page).hasURL(
        Pattern.compile(".*/inventory\\.html"),
        new PageAssertions.HasURLOptions().setTimeout(15_000));
    assertThat(inventory.title()).hasText("Products");
    assertThat(inventory.inventoryItems()).hasCount(6);

    long durationMs = (System.nanoTime() - startedAt) / 1_000_000;
    System.out.printf("performance_glitch_user login duration: %d ms%n", durationMs);
  }
}
