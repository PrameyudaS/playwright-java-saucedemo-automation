package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutCompletePage {
  private final Page page;

  public CheckoutCompletePage(Page page) { this.page = page; }

  public Locator completeHeader() { return page.getByTestId("complete-header"); }
  public Locator completeText() { return page.getByTestId("complete-text"); }
  public Locator cartBadge() { return page.getByTestId("shopping-cart-badge"); }
}
