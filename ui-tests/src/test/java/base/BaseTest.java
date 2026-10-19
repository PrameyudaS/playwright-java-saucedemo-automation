package base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public abstract class BaseTest {
  protected static final String BASE_URL = "https://www.saucedemo.com/";

  protected static Playwright playwright;
  protected static Browser browser;
  protected BrowserContext context;
  protected Page page;

  @BeforeAll
  static void launchBrowser() {
    playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute("data-test");
    boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
    browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions().setHeadless(headless));
  }

  @BeforeEach
  void createIsolatedContext() {
    context = browser.newContext(
        new Browser.NewContextOptions().setBaseURL(BASE_URL));
    page = context.newPage();
  }

  @AfterEach
  void closeContext() {
    if (context != null) context.close();
  }

  @AfterAll
  static void closePlaywright() {
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
  }
}
