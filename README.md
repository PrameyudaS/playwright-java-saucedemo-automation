# SauceDemo UI Automation
## Overview

This project contains UI automation tests for the SauceDemo application using:
- Java 21
- Playwright for Java (com.microsoft.playwright:playwright)
- JUnit 5
- Maven
- Page Object Model (POM)

The objective is to automate critical user journeys while maintaining a clean, scalable, and reusable test architecture.

## Technology Stack
- Java 21
- Maven
- Playwright Java
- JUnit 5


## Prerequisites

Install:
- Java 17+ (Java 21 recommended)
- Maven 3.9+
- IntelliJ IDEA (optional)

Verify installation:
```shell
java -version
mvn -version
```
## Browser Installation

Playwright downloads browser binaries separately.

Run once before executing the tests:

### PowerShell
```powershell
mvn exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install chromium"
```
### Command Prompt
```bat
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

## Exact Commands to Run the UI Suite

### Run all tests (headless)

```powershell
mvn clean test
```
### Run all tests (headed)
```powershell
mvn clean test -Dheadless=false
```
### Run Login tests only
```powershell
mvn -Dtest=LoginTests test
```
### Run Cart tests only
```powershell
mvn -Dtest=CartTests test
```
### Run Checkout tests only
```powershell
mvn -Dtest=CheckoutTests test
```
### Run Account-specific tests only
```powershell
mvn -Dtest=AccountBehaviorTests test
```

# Architectural Decisions and Reasoning

## 1. Page Object Model (POM)
### Decision

The framework uses Page Object Model.

### Reason

Separates:
- Test logic
- Page behavior
- UI locators

Without POM:
```java
page.locator("#user-name").fill("standard_user");
page.locator("#password").fill("secret_sauce");
page.locator("#login-button").click();
```
would be repeated across many tests.

With POM:
```java
loginPage.loginAs(username, password);
```
Benefits:
- Easier maintenance
- Reduced code duplication
- Better readability
- Centralized locator management
  
## 2. Shared Browser + Isolated BrowserContext
### Decision

A single browser instance is reused.

Each test creates its own:
```java
BrowserContext
Page
```
### Reason

Playwright recommends test isolation using BrowserContext.

Benefits:
- No shared cookies
- No shared session
- No shared shopping cart state
- Faster than launching a new browser per test

Example:

```java
@BeforeEach
void createContextAndPage() {
    context = browser.newContext();
    page = context.newPage();
}
```
## 3. Dedicated Test Data Layer
### Decision

Test data is stored separately.
```java
TestUsers.java
CheckoutData.java
```
###Reason

Avoid hardcoded values throughout tests.

Instead of:

```java
login("standard_user", "secret_sauce");
```
use:

```java
login(
  TestUsers.STANDARD,
  TestUsers.PASSWORD
);
```
Benefits:
- Centralized maintenance
- Easier future data-driven testing
- Improved readability
  
## 4. Web-First Assertions
### Decision

Use Playwright assertions only.

Example:

```java
assertThat(page).hasURL(...);
assertThat(locator).hasText(...);
```
### Reason

Playwright automatically:
- waits for elements
- retries assertions
- handles synchronization

No usage of:

```java
Thread.sleep()
```
Benefits:
- Faster execution
- More stable tests
- Less flaky automation
  
## 5. Resilient Locator Strategy

Priority order:
1. data-test attributes
2. Role-based locators
3. Text-based locators
4. CSS selectors as a last resort

Examples:

```java
page.getByTestId("login-button");
```
```java
page.getByRole(
  AriaRole.BUTTON,
  new Page.GetByRoleOptions()
    .setName("Checkout")
);
```
Benefits:
- More stable against UI changes
- Better alignment with Playwright best practices

# Scenarios Selected and Why

The assignment required:
- Minimum 5 scenarios
- At least 1 negative scenario
- Coverage of:
    - standard_user
    - locked_out_user
    - problem_user
    - performance_glitch_user
      
The following 6 scenarios were selected.

## 1) TC_001 Standard User Login Successfully
### Why Selected

Represents the primary login happy path.

Critical because every authenticated workflow depends on successful login.

### Coverage
- Authentication
- Navigation to Inventory page

## 2) TC_002 Locked-Out User Is Rejected
### Why Selected

Required negative login scenario.

Validates:
- Error handling
- Security restrictions
- Account-specific behavior
### Coverage
- Negative testing
- locked_out_user


## 3) TC_013 Add One Product To Cart
### Why Selected

Represents the core shopping cart workflow.

Validates:
- Add to Cart
- Cart badge update
- Cart page integrity
### Coverage
- Shopping Cart functionality
- Data consistency

## 4) TC_021 Complete Checkout Successfully
### Why Selected

Most critical business workflow.

Validates:
- Cart
- Checkout Information
- Checkout Overview
- Order Completion
### Coverage

End-to-end user journey:

```plain text
Login
→ Add Product
→ Cart
→ Checkout
→ Order Complete
```

## 5) TC_019 Verify problem_user Behavior
### Why Selected

Assignment requires account-specific testing.

The problem_user account is intended to expose abnormal UI behavior.

### Coverage
- problem_user
- Defect characterization
- UI integrity validation
### Objective

Verify the observed behavior rather than assume standard behavior.

## 6) TC_010 Verify performance_glitch_user Behavior
### Why Selected

Assignment explicitly requires handling performance_glitch_user.

### Coverage
- Delayed login behavior
- Synchronization handling
- Auto-wait strategy
### Objective

Confirm that login eventually succeeds without:

```java
Thread.sleep()
```
or arbitrary retries.

# Future Enhancements — What I Would Do Differently With More Time

Given more time, I would focus on expanding test coverage, improving automation reliability, and enhancing the overall maintainability of the framework.

### 1. Expand Functional Test Coverage
- Add negative login scenarios, including empty username, empty password, and invalid credentials.
- Extend shopping cart tests to cover removing products, adding multiple products, and verifying cart state consistency.
- Add checkout validation scenarios for missing required fields, invalid inputs, and boundary values.
- Add logout and session management tests to verify session invalidation and access restrictions after logout.

### 2. Improve Cross-Browser Coverage
- Extend automated test execution to Chromium, Firefox, and WebKit to identify browser-specific issues.
- Investigate and document any differences in application behavior across browsers.

### 3. Enhance CI/CD and Test Reporting
- Extend the GitHub Actions pipeline to support cross-browser execution.
- Improve failure diagnostics by collecting screenshots, Playwright traces, and test reports as CI artifacts.
- Explore parallel test execution to reduce overall execution time while maintaining test isolation.

### 4. Improve Framework Maintainability
- Introduce data-driven testing for scenarios with multiple input combinations.
- Refactor reusable test utilities as the test suite grows.
- Review flaky tests and improve synchronization using Playwright's built-in auto-waiting and web-first assertions without introducing unnecessary retries.

These improvements would help increase test coverage, strengthen defect detection, and make the automation framework more scalable and reliable.
