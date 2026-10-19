package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {
  private final Page page;
  private final Locator username;
  private final Locator password;
  private final Locator loginButton;
  private final Locator errorMessage;

  public LoginPage(Page page) {
    this.page = page;
    username = page.getByTestId("username");
    password = page.getByTestId("password");
    loginButton = page.getByTestId("login-button");
    errorMessage = page.getByTestId("error");
  }

  public LoginPage open() {
    page.navigate("/");
    return this;
  }

  public void loginAs(String user, String pass) {
    username.fill(user);
    password.fill(pass);
    loginButton.click();
  }

  public Locator errorMessage() { return errorMessage; }
  public Locator loginButton() { return loginButton; }
}
