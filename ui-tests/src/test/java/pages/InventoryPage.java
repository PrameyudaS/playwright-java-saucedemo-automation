package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage {
  private final Page page;

  public InventoryPage(Page page) { this.page = page; }

  public Locator title() { return page.getByText( "Products", new Page.GetByTextOptions().setExact(true) ); }
  public Locator inventoryItems() { return page.getByTestId("inventory-item"); }
  public Locator cartBadge() { return page.getByTestId("shopping-cart-badge"); }
  public Locator cartLink() { return page.getByTestId("shopping-cart-link"); }

  public Locator product(String name) {
    return inventoryItems().filter(new Locator.FilterOptions().setHasText(name));
  }

  public Locator productPrice(String name) {
    return product(name).getByTestId("inventory-item-price");
  }

  public Locator productImage(String name) {
    return product(name).locator("img");
  }

  public void addProduct(String name) {
    product(name).getByRole(
        AriaRole.BUTTON,
        new Locator.GetByRoleOptions().setName("Add to cart")
    ).click();
  }

  public CartPage openCart() {
    cartLink().click();
    return new CartPage(page);
  }

  public List<String> productImageSources() {
    List<String> imageSources = new ArrayList<>();
    Locator images = inventoryItems().locator("img");
    int count = images.count();
    for (int i = 0; i < count; i++) {

      imageSources.add(
              images.nth(i)
                      .getAttribute("src")
      );
    }
    return imageSources;
  }
}
