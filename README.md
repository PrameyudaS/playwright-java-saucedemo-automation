# Part 1: SauceDemo UI Automation
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
# Project Structure
```text
├── pom.xml
├── README.md
└── src/
    └── test/
        └── java/
            ├── base/
            │   └── BaseTest.java
            ├── data/
            │   ├── TestUsers.java
            │   └── CheckoutData.java
            ├── pages/
            │   ├── LoginPage.java
            │   ├── InventoryPage.java
            │   ├── CartPage.java
            │   ├── CheckoutInformationPage.java
            │   ├── CheckoutOverviewPage.java
            │   └── CheckoutCompletePage.java
            └── tests/
                ├── LoginTests.java
                ├── CartTests.java
                ├── CheckoutTests.java
                └── AccountBehaviorTests.java
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


# Part 2: GoREST API Automation

## Overview

This project contains API automation tests for the GoREST User Management API.

**Technology Stack**

- Java 21
- REST Assured
- JUnit 5
- Maven

**API Under Test**

https://gorest.co.in/public/v2

The objective of this project is to validate critical user-management functionality, including CRUD operations, authentication, validation rules, and failure scenarios while maintaining a clean, reusable, and scalable test architecture.

---

# Prerequisites

- Java 17 or later (Java 21 recommended)
- Maven 3.9+
- GoREST Account
- GoREST Personal Access Token

---

# Authentication Setup

Generate a Personal Access Token from your GoREST account.

The framework supports two methods:

## Option 1: Environment Variable (Recommended)

### PowerShell

```powershell
$env:GOREST_API_TOKEN="your_token_here"
mvn clean test
```

### Command Prompt

```cmd
set GOREST_API_TOKEN=your_token_here
mvn clean test
```

---

## Option 2: Local .env File

Create a file named:

```text
.env
```

inside the project root.

Example:

```text
GOREST_API_TOKEN=your_token_here
```

The `.env` file is excluded through `.gitignore`.

The framework fails immediately if no token is provided.

---

# Exact Commands

## Run All API Tests

```powershell
mvn clean test
```

## Run Specific Test Class

```powershell
mvn "-Dtest=UserApiTests" test
```

## Compile Without Running Tests

```powershell
mvn clean test-compile
```

---

# Project Structure

```text
api-tests/
│
├── pom.xml
├── README.md
├── .env.example
│
└── src
    └── test
        ├── java
        │
        ├── assertions
        │   └── ApiAssertions.java
        │
        ├── client
        │   ├── ApiExchange.java
        │   └── UserApiClient.java
        │
        ├── config
        │   └── ApiConfig.java
        │
        ├── model
        │   ├── UserRequest.java
        │   ├── UserResponse.java
        │   └── ValidationError.java
        │
        ├── tests
        │   ├── BaseApiTest.java
        │   └── UserApiTests.java
        │
        └── util
            └── TestDataFactory.java
```

---

# Architectural Decisions

## 1. Reusable API Client Layer

### Decision

All endpoint interactions are implemented inside a dedicated service layer:

```java
UserApiClient
```

### Reason

This prevents REST Assured requests from being duplicated across test methods.

Example:

```java
users.createUser(user);
users.getUser(id);
users.updateUser(id, user);
users.deleteUser(id);
```

instead of repeatedly writing:

```java
given()
.when()
.post(...)
```

Benefits:

- Reusability
- Easier maintenance
- Cleaner tests
- Centralized endpoint management

---

## 2. POJO Request and Response Models

### Decision

Java records are used for request and response payloads.

Examples:

```java
UserRequest
UserResponse
ValidationError
```

### Reason

Avoids raw JSON string manipulation.

Benefits:

- Strong typing
- Cleaner code
- Easier serialization/deserialization
- Better IDE support

---

## 3. Shared Assertion Layer

### Decision

Common validations are centralized in:

```java
ApiAssertions
```

### Reason

Avoids repeated assertions across tests.

Examples:

```java
assertStatus(...)
assertUserMatches(...)
assertValidationError(...)
```

Benefits:

- Reduced duplication
- Consistent validations
- Easier maintenance

---

## 4. Dynamic Test Data Generation

### Decision

Unique users are created for every test execution using UUID-based values.

Example:

```text
api.test.a1b2c3d4@example.com
```

### Reason

GoREST is a shared public service.

Hardcoded email addresses cause failures when tests are re-run.

Benefits:

- Repeatable execution
- No collisions
- Safe parallel reruns in the future

---

## 5. Automatic Cleanup

### Decision

Every user created during a test is tracked and deleted in cleanup.

### Reason

The assignment explicitly states:

> Never update or delete users you did not create.

Benefits:

- Keeps the environment clean
- Supports repeatable execution
- Prevents pollution of shared test data

---

## 6. Useful Failure Diagnostics

### Decision

Assertion failures include:

- Request information
- Response status
- Response headers
- Response body

Example:

```text
--- REQUEST ---
GET /users/123

--- RESPONSE ---
Status: 404

Body:
{
  "message": "Resource not found"
}
```

### Reason

Provides actionable troubleshooting information instead of only stack traces.

---

# Test Scenarios

## API_001 Create User Successfully

Create a new user with unique data.

Expected:

```text
201 Created
```

---

## API_002 Get User Successfully

Retrieve a user created during the test.

Expected:

```text
200 OK
```

---

## API_003 Update User Successfully

Update user details and verify persistence.

Expected:

```text
200 OK
```

---

## API_004 Delete User Successfully

Delete a created user.

Expected:

```text
204 No Content
```

---

## API_005 Verify Deleted User No Longer Exists

Request the deleted user.

Expected:

```text
404 Not Found
```

---

## API_006 List Users

Retrieve a list of users without assuming ordering.

Expected:

```text
200 OK
```

---

## API_007 Duplicate Email Validation

Attempt to create another user using an existing email address.

Expected:

```text
422 Unprocessable Entity
```

---

## API_008 Invalid Email Validation

Attempt to create a user with an invalid email format.

Expected:

```text
422 Unprocessable Entity
```

---

## API_009 Missing Required Field Validation

Attempt to create a user without a required field.

Expected:

```text
422 Unprocessable Entity
```

---

## API_010 Missing Authentication

Attempt to create a user without a Bearer token.

Expected:

```text
401 Unauthorized
```

---

# Design Considerations

Because GoREST is a shared live service:

- Unique data is generated for every run.
- Tests never use fixed user IDs.
- Tests do not assume user ordering.
- Only records created by the test are updated or deleted.
- Cleanup runs after execution.
- Authentication is externalized from source control.

---

# CI/CD Considerations

Store the API token securely as a GitHub Actions secret.

Example:

```yaml
env:
  GOREST_API_TOKEN: ${{ secrets.GOREST_API_TOKEN }}
```

Never commit:

```text
.env
```

or any token value to source control.

---


# Part 3: CI/CD Findings and Fixes

The provided `ci-broken.yml` workflow was reviewed, debugged, and extended to support both UI and API automation suites.

The repository assumes the default branch is named:

```text
main
```

The final workflow runs on:

- Pushes to `main`
- Pull Requests targeting `main`

---

## Bugs Found in the Starter Workflow

### Bug 1: Missing Repository Checkout

#### Problem

The workflow did not contain:

```yaml
actions/checkout
```

Without checking out the repository, the GitHub runner has no access to the project source code.

#### Fix

Added:

```yaml
- uses: actions/checkout@v4
```

to both jobs.

---

### Bug 2: Incorrect Java Version

#### Problem

The starter workflow used:

```yaml
java-version: '8'
```

Both the UI automation project and API automation project require Java 17+ and were implemented using Java 21.

#### Fix

Updated Java configuration to:

```yaml
java-version: '21'
```

using:

```yaml
uses: actions/setup-java@v4
```

---

### Bug 3: Maven Executed from Wrong Directory

#### Problem

The repository contains multiple Maven projects:

```text
ui-tests/
api-tests/
```

The starter workflow executed Maven commands from the repository root.

This would fail because Maven could not find the intended project.

#### Fix

Added:

```yaml
working-directory: ui-tests
```

for UI tests and:

```yaml
working-directory: api-tests
```

for API tests.

---

### Bug 4: UI Test Failures Were Hidden

#### Problem

The workflow contained:

```yaml
mvn clean test || true
```

The `|| true` statement forces a successful exit code even when tests fail.

As a result:

- Red tests produce a green pipeline.
- Failures are hidden.
- CI cannot be trusted.

#### Fix

Removed:

```yaml
|| true
```

Now the workflow fails correctly when UI tests fail.

---

### Bug 5: Playwright Browsers Were Not Installed

#### Problem

The workflow attempted to execute Playwright tests without installing browser binaries.

This causes failures on GitHub runners.

#### Fix

Added Playwright installation step:

```yaml
mvn exec:java \
-Dexec.mainClass=com.microsoft.playwright.CLI \
-Dexec.args="install --with-deps chromium"
```

This installs Chromium and required Linux dependencies before tests run.

---

### Bug 6: Missing API Authentication Secret

#### Problem

The API suite requires:

```text
GOREST_API_TOKEN
```

but the starter workflow did not provide the token to the test execution environment.

#### Fix

Added:

```yaml
env:
  GOREST_API_TOKEN: ${{ secrets.GOREST_API_TOKEN }}
```

using GitHub Actions repository secrets.

---

### Bug 7: No Validation That API Token Exists

#### Problem

If the secret is missing, the workflow would fail later with less helpful errors.

#### Fix

Added an explicit validation step:

```yaml
if [ -z "$GOREST_API_TOKEN" ]; then
  exit 1
fi
```

This produces a clear error message and fails early.

---

### Bug 8: UI and API Tests Shared a Single Job

#### Problem

The starter workflow executed both suites within the same job.

A failure in one stage could prevent visibility into the other suite.

#### Fix

Split the workflow into two independent jobs:

```yaml
ui-tests
api-tests
```

Benefits:

- Faster troubleshooting
- Clearer reporting
- Independent execution

---

### Bug 9: No Test Reports Were Preserved

#### Problem

The workflow did not upload any test artifacts.

Developers would need to reproduce failures locally to investigate issues.

#### Fix

Added artifact upload steps:

```yaml
actions/upload-artifact@v4
```

for:

```text
target/surefire-reports/
```

This preserves test results even when failures occur.

---

### Bug 10: No Maven Dependency Caching

#### Problem

Dependencies were downloaded during every workflow run.

This increased execution time unnecessarily.

#### Fix

Enabled Maven cache:

```yaml
cache: maven
```

inside:

```yaml
actions/setup-java
```

This significantly reduces build duration for subsequent executions.

---

### Bug 11: No Timeout Protection

#### Problem

A hanging browser session or stalled API request could keep jobs running indefinitely.

#### Fix

Added job-level timeouts:

```yaml
timeout-minutes: 30
```

for UI tests and

```yaml
timeout-minutes: 15
```

for API tests.

---

## Additional Improvements

Beyond fixing the starter workflow, the following improvements were implemented:

- Separate UI and API jobs
- Maven dependency caching
- Secure secret handling
- Playwright browser installation
- Test artifact preservation
- Explicit timeout configuration
- Independent failure reporting
- Clear diagnostics for missing secrets

---

## Result

The final CI pipeline is considered trustworthy because:

- UI failures fail the workflow.
- API failures fail the workflow.
- Missing secrets are detected immediately.
- Test reports are preserved automatically.
- Both test suites execute using the correct Java version.
- Build results accurately reflect the state of the application.


# Part 4: AI assisted workflow
AI tools were used throughout the assignment as productivity aids rather than as a source of truth. I used Microsoft Copilot/ChatGPT to help generate boilerplate code, draft test scenarios, create initial Playwright and Rest Assured project structures, explain framework setup, and review CI/CD workflow configuration.

One example where AI output required verification was the GitHub Actions configuration for publishing Allure reports to GitHub Pages. The initial suggestions were incomplete and resulted in deployment issues, including missing permissions and report publishing problems. I diagnosed the failures using GitHub Actions logs, verified the generated files in the `gh-pages` branch, and adjusted the workflow configuration until the reports were successfully deployed.

I also verified all generated test cases against the SauceDemo application and the GoRest API documentation rather than assuming the generated scenarios were correct. This included validating selectors, expected results, authentication configuration, and CI execution.

I did not delegate the final selection of test coverage to AI. Choosing which scenarios were most important for login, cart, checkout, API CRUD operations, and account-specific behaviors required judgement about risk, business value, and maintainability. AI was used to suggest ideas, but the final test scope and implementation decisions were made manually after review.


# Optional stretch goals
## Test Reporting

This project generates Allure reports for both UI and API automation suites.

### UI Report
Generated from Playwright Java test execution.

### API Report
Generated from Rest Assured test execution.

### CI Artifacts
Reports are published as GitHub Actions artifacts after every workflow run.

### GitHub Pages

UI Report:
https://prameyudas.github.io/playwright-java-saucedemo-automation/ui-report/index.html

API Report:
https://prameyudas.github.io/playwright-java-saucedemo-automation/api-report/index.html

Automated test reports are generated using Allure. Both UI (Playwright Java) and API (Rest Assured) test reports are published as GitHub Actions artifacts and can optionally be deployed to GitHub Pages for easy access and historical review.


