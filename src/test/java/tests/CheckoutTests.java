package tests;

import base.BaseTest;
import data.CheckoutData;
import data.TestUsers;
import org.junit.jupiter.api.*;
import pages.*;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Checkout scenarios")
class CheckoutTests extends BaseTest {
  private static final String PRODUCT = "Sauce Labs Backpack";

  @Test
  @DisplayName("TC_021 Standard user can complete checkout")
  void standardUserCanCompleteCheckout() {
    LoginPage login = new LoginPage(page).open();
    InventoryPage inventory = new InventoryPage(page);
    login.loginAs(TestUsers.STANDARD, TestUsers.PASSWORD);
    assertThat(inventory.title()).hasText("Products");

    String expectedPrice = inventory.productPrice(PRODUCT).textContent();
    inventory.addProduct(PRODUCT);
    assertThat(inventory.cartBadge()).hasText("1");

    CartPage cart = inventory.openCart();
    assertThat(cart.product(PRODUCT)).isVisible();
    assertThat(cart.productPrice(PRODUCT)).hasText(expectedPrice);

    CheckoutInformationPage information = cart.checkout();
    CheckoutOverviewPage overview = information.continueWith(CheckoutData.validCustomer());

    assertThat(page).hasURL(Pattern.compile(".*/checkout-step-two\\.html"));
    assertThat(overview.product(PRODUCT)).isVisible();
    assertThat(overview.productPrice(PRODUCT)).hasText(expectedPrice);
    assertThat(overview.itemTotal()).containsText(expectedPrice);
    assertThat(overview.total()).containsText("Total: $");

    CheckoutCompletePage complete = overview.finish();

    assertThat(page).hasURL(Pattern.compile(".*/checkout-complete\\.html"));
    assertThat(complete.completeHeader()).hasText("Thank you for your order!");
    assertThat(complete.completeText()).containsText("Your order has been dispatched");
    assertThat(complete.cartBadge()).isHidden();
  }
}
