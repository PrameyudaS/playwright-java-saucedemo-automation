package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutOverviewPage {
  private final Page page;

  public CheckoutOverviewPage(Page page) { this.page = page; }

  public Locator product(String name) {
    return page.getByTestId("inventory-item")
        .filter(new Locator.FilterOptions().setHasText(name));
  }

  public Locator productPrice(String name) {
    return product(name).getByTestId("inventory-item-price");
  }

  public Locator itemTotal() { return page.getByTestId("subtotal-label"); }
  public Locator total() { return page.getByTestId("total-label"); }

  public CheckoutCompletePage finish() {
    page.getByTestId("finish").click();
    return new CheckoutCompletePage(page);
  }
}
