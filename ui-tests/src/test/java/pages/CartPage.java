package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CartPage {
  private final Page page;

  public CartPage(Page page) { this.page = page; }

  public Locator title() { return page.getByTestId("title"); }
  public Locator cartItems() { return page.getByTestId("inventory-item"); }

  public Locator product(String name) {
    return cartItems().filter(new Locator.FilterOptions().setHasText(name));
  }

  public Locator productPrice(String name) {
    return product(name).getByTestId("inventory-item-price");
  }

  public Locator productQuantity(String name) {
    return product(name).getByTestId("item-quantity");
  }

  public CheckoutInformationPage checkout() {
    page.getByRole(AriaRole.BUTTON,
        new Page.GetByRoleOptions().setName("Checkout")).click();
    return new CheckoutInformationPage(page);
  }
}
