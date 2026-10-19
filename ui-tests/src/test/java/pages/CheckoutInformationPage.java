package pages;

import com.microsoft.playwright.Page;
import data.CheckoutData;

public class CheckoutInformationPage {
  private final Page page;

  public CheckoutInformationPage(Page page) { this.page = page; }

  public CheckoutOverviewPage continueWith(CheckoutData customer) {
    page.getByTestId("firstName").fill(customer.firstName());
    page.getByTestId("lastName").fill(customer.lastName());
    page.getByTestId("postalCode").fill(customer.postalCode());
    page.getByTestId("continue").click();
    return new CheckoutOverviewPage(page);
  }
}
