# SauceDemo UI Automation

Playwright for Java, JUnit 5, Maven, Java 21, and Page Object Model.

## Covered scenarios

- TC_001: standard user login succeeds
- TC_002: locked-out user is rejected
- TC_013: add one product to cart
- TC_021: complete checkout successfully
- TC_019: problem_user displays the observed duplicate product-image defect
- TC_010: performance_glitch_user eventually reaches inventory without fixed sleeps

## Prerequisites

- Java 21, or change `maven.compiler.release` to 17 if using Java 17
- Maven 3.9+

## Install Chromium

PowerShell:

```powershell
mvn exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install chromium"
```

## Run tests

Headless:

```powershell
mvn clean test
```

Headed:

```powershell
mvn clean test -Dheadless=false
```

Run one test class:

```powershell
mvn -Dtest=LoginTests test
```

## Design notes

- Browser is shared within each test class.
- Every test gets a new BrowserContext and Page, isolating cookies, session, and cart state.
- Page Objects contain locators and user actions.
- Test classes contain scenarios and assertions.
- No `Thread.sleep()` and no broad retries are used.
- `performance_glitch_user` receives a targeted 15-second URL assertion timeout because that account intentionally responds more slowly. No arbitrary performance SLA is asserted.
- The `problem_user` test is a characterization test for the observed duplicate-image behavior. If SauceDemo fixes or changes that defect, update the assertion to match the newly observed behavior.
