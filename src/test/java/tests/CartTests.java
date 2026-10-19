package tests;

import base.BaseTest;
import data.TestUsers;
import org.junit.jupiter.api.*;
import pages.*;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Shopping cart scenarios")
class CartTests extends BaseTest {
  private static final String PRODUCT = "Sauce Labs Backpack";

  @Test
  @DisplayName("TC_013 Standard user can add one product to cart")
  void standardUserCanAddOneProductToCart() {
    LoginPage login = new LoginPage(page).open();
    InventoryPage inventory = new InventoryPage(page);
    login.loginAs(TestUsers.STANDARD, TestUsers.PASSWORD);
    assertThat(inventory.title()).hasText("Products");

    String inventoryPrice = inventory.productPrice(PRODUCT).textContent();
    inventory.addProduct(PRODUCT);

    assertThat(inventory.cartBadge()).hasText("1");
    CartPage cart = inventory.openCart();

    assertThat(page).hasURL(Pattern.compile(".*/cart\\.html"));
    assertThat(cart.title()).hasText("Your Cart");
    assertThat(cart.cartItems()).hasCount(1);
    assertThat(cart.product(PRODUCT)).isVisible();
    assertThat(cart.productQuantity(PRODUCT)).hasText("1");
    assertThat(cart.productPrice(PRODUCT)).hasText(inventoryPrice);
  }
}
