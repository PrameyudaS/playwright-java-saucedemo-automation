# Test plan: SauceDemo Web Application

**Test Environment:** https://www.saucedemo.com/

**Test Date:** 16/10/2026

## Overview
This plan covers the user journeys in the SauceDemo web application which focusing on these three modules including Log-in module, Shopping Cart module, and Checkout flow. 

## Test Objective
To provide risk-based test coverage for the login and purchasing flow by validating authentication, shopping cart operations, and checkout functionality across all supported test accounts. 
The objective is to verify expected business behavior, identify account-specific defects, evaluate system responses to invalid and boundary inputs, and ensure a consistent and reliable end-to-end purchasing experience.

## Preconditions
- The SauceDemo application is accessible at https://www.saucedemo.com/.
- The test environment is available and functioning normally.
- The following test accounts exist and are active:
   _standard_user,
    locked_out_user,
    problem_user,
    performance_glitch_user_
- The password for all supported accounts is _secret_sauce_.
- For cart and checkout scenarios, the user must be successfully authenticated unless the scenario explicitly tests login failure behavior.


## Test Suite
### 1. Login Module
- Valid login with standard_user
- Invalid login with Locked_out_user
- Empty username validation
- Empty password validation
- Both username and password empty
- Logout and session reset

### 2. Product/Inventory relevant to cart
- Product names and prices visibility
- Add product to cart actions
- Remove product from cart actions
- Product details remain consistent between Inventory and Product Detail page

### 3. Shopping Cart Module
- Verify cart persistence
- Verify empty cart state
- Single product in cart
- Multiple products in cart
- Remove item from cart page
- Continue shopping behavior
  
### 4. Checkout flow
- Successful checkout with valid customer information
- Checkout with missing first name
- Checkout with missing last name
- Checkout with missing postal code
- Checkout with invalid or empty customer information
- Summary shows selected products and correct totals
- Continue/cancel checkout steps
- Remove items in checkout overview

### 5.Account-Specific Behavior
- Performance validation
- Checkout under degraded performance
- Problem_user checkout data integrity

## Test Cases
### TC_001: Standard user able to login succeeds
- **Module:** Login
- **Scenario:** Login with valid credentials and redirect inventory page
- **Preconditions:** User is on login page with no active session
- **Test Steps:**
  1. Open the link https://www.saucedemo.com/
  2. Enter standard_user in Username field  
  3. Enter secret_sauce in Password field
  4. Click Login button
- **Test Data:** Username:standard_user / Password:secret_sauce
- **Expected Result:** Log in successfully and URL becomes [inventory.html](https://www.saucedemo.com/inventory.html) and inventory page loads
- **Test Type:** Positive
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_002: Locked out user unable to login and error message displayed
- **Module:** Login
- **Scenario:** Login with locked account
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter locked_out_user in Username field
  2. Enter secret_sauce in Password field
  3. Click Login button
- **Test Data:** Username:locked_out_user / Password:secret_sauce
- **Expected Result:** Login fail and Error message displays: Epic sadface: Sorry, this user has been locked out.
- **Test Type:** Negative
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_003 — Log in with empty username
- **Module:** Login
- **Scenario:** Log in with empty username
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Leave Username field blank
  2. Enter secret_sauce in Password field
  3. Click Login button
- **Test Data:** blank username / Password:secret_sauce
- **Expected Result:** Error message displays: Epic sadface: Username is required
- **Test Type:** Negative
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

### TC_004 — Log in with empty password
- **Module:** Login
- **Scenario:** Login with empty password
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter standard_user in Username field
  2. Leave Password field blank
  3. Click Login button
- **Test Data:** Username:standard_user / blank password
- **Expected Result:** Error message displays: Epic sadface: Password is required
- **Test Type:** Negative
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

### TC_005 — Log in with both username and password fields empty
- **Module:** Login
- **Scenario:** Login with both fields empty
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Leave Username blank
  2. Leave Password blank
  3. Click Login button
- **Test Data:** blank username / blank password
- **Expected Result:** Error message displays: Epic sadface: Username is required (Verification of the username must come first)
- **Test Type:** Negative / Edge case
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

### TC_006 — Log in with invalid username and valid password
- **Module:** Login
- **Scenario:** Log in with invalid username
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter invalid_user in Username field
  2. Enter secret_sauce in Password field
  3. Click Login button
- **Test Data:** Invalid_user: Assume as invalid_user / Password: secret_sauce
- **Expected Result:** Error message displays: Epic sadface: Username and password do not match any user in this service
- **Test Type:** Negative
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_007 — Log in with invalid password and valid username
- **Module:** Login
- **Scenario:** Log in with invalid password
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter standard_user in Username field
  2. Enter wrong_password in Password field
  3. Click Login button
- **Test Data:** Username: standard_user / Invalid password: Assume as wrong_password
- **Expected Result:** Error message displays: Epic sadface: Username and password do not match any user in this service
- **Test Type:** Negative
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_008 — Log in with Username which has leading/trailing spaces
- **Module:** Login
- **Scenario:** Username with leading/trailing spaces
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter standard_user in Username field
  2. Enter secret_sauce in Password field
  3. Click Login button
- **Test Data:** Username: " standard_user " / Password: secret_sauce
- **Expected Result:** Error message displays: Epic sadface: Username and password do not match any user in this service
- **Test Type:** Negative / Edge case
- **Priority:** Medium
- **Risk:** Low
- **Automation:** Yes
  
**Note:**
- **AMBIGUITY:** The requirements do not specify whether leading and trailing whitespace in usernames should be trimmed before authentication.
- **OBSERVED BEHAVIOR:** Logging in with " standard_user " and a valid password is rejected with the error message: "Epic sadface: Username and password do not match any user in this service."
- **ASSUMPTION:** Usernames are treated as exact-match values, and leading or trailing whitespace is not automatically trimmed during authentication. This assumption is based on the observed behavior and should be validated with additional test cases.
- **IMPACT:** Login tests will verify that usernames containing leading or trailing whitespace are rejected rather than silently normalized.

### TC_009 — Log in with long username
- **Module:** Login
- **Scenario:** Verify long username
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter Invalid Username in Username field
  2. Enter secret_sauce in Password field
  3. Click Login button
- **Test Data:** Invalid Username 50 characters / Password: secret_sauce
- **Expected Result:** Login should be rejected with an appropriate error message because the username does not match any valid account. The application should remain responsive without crashing or exposing internal errors.
- **Test Type:** Boundary
- **Priority:** Medium
- **Risk:** Low
- **Automation:** Yes

**Note:**
- **AMBIGUITY:** The maximum allowed username length is not specified in the requirements.
- **ASSUMPTION:** A 50-character username is selected as a representative long input for testing purposes. This value is not assumed to be the application's maximum allowed username length.
- **RATIONALE:** The 50-character input is significantly longer than the usernames of the four accounts in scope. It is used to explore how the application handles unusually long usernames and whether authentication fails gracefully without causing application errors.

### TC_010 — Verify login behavior and response time for performance_glitch_user
- **Module:** Login
- **Scenario:** Login with performance_glitch_user
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter performance_glitch_user in Username field
  2. Enter secret_sauce in Password field
  3. Click Login button
- **Test Data:** Username: performance_glitch_user / Password: secret_sauce
- **Expected Result:** The user should successfully log in and navigate to the inventory page, even if the login process takes longer than standard_user. No unexpected errors should occur.
- **Test Type:** Positive / Account-Specific
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

**Note:**
- **AMBIGUITY:** No performance SLA or maximum acceptable response time is specified for the login process.
- **ASSUMPTION:** The performance_glitch_user account may experience slower login performance compared to standard_user. However, the login process should eventually complete successfully without errors or timeouts under normal test conditions.
- **RATIONALE:** Since no performance threshold is defined, this test focuses on verifying successful login despite potential delays. Login duration will be measured and compared with standard_user to identify performance differences without treating an arbitrary response time as a failure.

### TC_011 — Verify inventory page integrity with problem_user
- **Module:** Login
- **Scenario:** Login with problem_user
- **Preconditions:** Login page is visible
- **Test Steps:**
  1. Enter problem_user in Username field
  2. Enter secret_sauce in Password field
  3. Click Login button
- **Test Data:** Username: problem_user / Password: secret_sauce
- **Expected Result:** Login succeeds, inventory page loads correctly, and Product names, prices, images, and buttons are displayed correctly
- **Test Type:** Positive / Account-Specific
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_012 — Verify that the authenticated user session persists after refreshing the browser page.
- **Module:** Login
- **Scenario:** Verify session persistence after successful login
- **Preconditions:** User is on the login page, no active user session exists. Browser cookies and local storage are cleared before the test.
- **Test Steps:**
  1. Enter standard_user in Username field
  2. Enter secret_sauce in Password field
  3. Click Login button
  4. Verify that login is successful and the user is redirected to the Inventory Page.
  5. Add a product "Sauce Labs Backpack" to the shopping cart.
  6. Verify that the cart badge displays 1.
  7. Refresh the browser page.
  8. Verify that the user remains authenticated and stays on the Inventory Page.
  9. Verify that the cart badge still displays 1.
  10. Navigate to the Shopping Cart Page.
  11. Verify that Sauce Labs Backpack remains in the shopping cart without requiring the user to log in again.
- **Test Data:** Username: standard_user, Password: secret_sauce, Product: Sauce Labs Backpack
- **Expected Result:** The authenticated session remains active after refreshing the browser page. The user is not redirected to the Login Page.
- **Test Type:** Positive / Functional / Session Management
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_013 — Add a product to cart and the cart badge is updated correctly.
- **Module:** Shopping Cart
- **Scenario:** Add single product to cart
- **Preconditions:** Logged in and on inventory page, empty cart
- **Test Steps:**
  1. Click Add to cart on first product
  2. Verify cart icon badge
  3. Click shopping cart
  4. Verify selected item on cart page
- **Test Data:** product name's Sauce Labs Backpack
- **Expected Result:** Cart badge count increments by 1 and selected item is present on cart page
- **Test Type:** Positive / Boundary
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_014 — Verify adding multiple different products to the cart
- **Module:** Shopping Cart
- **Scenario:** Verify adding multiple different products to the cart
- **Preconditions:** Logged in and on inventory page
- **Test Steps:**
  1. Click Add 3 different products to cart
  2. Verify cart icon badge
  3. Click shopping cart
  4. Verify selected products on cart page
- **Test Data:** 3 different products
- **Expected Result:** All selected products should be added to the cart successfully, and the cart badge should display the correct number of items.
- **Test Type:** Positive / Functional
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_015 — Remove product from cart and the cart badge is updated correctly.
- **Module:** Shopping Cart
- **Scenario:** Remove product from cart page
- **Preconditions:** Logged in, Cart contains 2 selected products
- **Test Steps:**
  1. Click Add 2 different products to cart
  2. Verify cart icon badge
  3. Click shopping cart
  4. Click Remove 1 product
  5. Verify selected item on cart page
- **Test Data:** any product
- **Expected Result:** Only 1 product left on cart page and cart badge change from 2 to 1
- **Test Type:** Positive
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_016 — Verify adding and removing the same product repeatedly.
- **Module:** Shopping Cart
- **Scenario:** Verify adding and removing the same product repeatedly
- **Preconditions:** Logged in, and on inventory page
- **Test Steps:**
  1. Click Add a product name's Sauce Labs Backpack to cart
  2. Click Remove product name's Sauce Labs Backpack from cart
  3. Click Add a product name's Sauce Labs Backpack to cart again
  4. Click shopping cart
  5. Verify selected item on cart page
- **Test Data:** product name's Sauce Labs Backpack
- **Expected Result:** Cart contains no duplicate items and the badge remains accurate
- **Test Type:** Edge Case
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes


### TC_017 — Verify that all products can be removed from the shopping cart
- **Module:** Shopping Cart
- **Scenario:** Remove all products from cart
- **Preconditions:** Logged in, Cart contains 3 different products
- **Test Steps:**
  1. Click Add 3 different products to the cart
  2. Click shopping cart
  3. Click shopping cart
  4. Click Remove for the first product
  5. Click the Remove button for the second product
  6. Click the Remove button for the last remaining product
  7. Navigate back to the Inventory Page
- **Test Data:** 3 different products
- **Expected Result:** All products should be successfully removed from the shopping cart. The cart should contain zero items, the cart badge should no longer be displayed, and all previously selected products should be available to add again from the Inventory Page.
- **Test Type:** Positive / Functional / Boundary
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes


### TC_018 — Verify Add to Cart with problem_user
- **Module:** Shopping Cart
- **Scenario:** Verify Add to Cart behavior with problem_user
- **Preconditions:** Logged in, the Inventory Page is displayed, and the cart is initially empty.
- **Test Steps:**
  1. Click the Add to Cart button for the first product
  2. Click the Add to Cart button for a different product.
  3. Click shopping cart
  4. Verify the product names, prices, and quantities
  5. Navigate back to the Inventory Page
- **Test Data:** Valid credentials (problem_user / secret_sauce), 2 different products
- **Expected Result:** All selected products should be successfully added to the shopping cart. The cart badge should reflect the correct number of items, and product information should remain consistent between the Inventory and Cart pages.
- **Test Type:** Account-Specific / Functional / Negative Behavior Exploration
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

**Note:**
- **AMBIGUITY:** The requirements do not specify which Add to Cart operations, if any, behave differently for problem_user.
- **ASSUMPTION:** The Add to Cart functionality should behave consistently across user accounts unless account-specific differences are explicitly documented.
- **Rationale:** This scenario focuses on detecting account-specific defects that may prevent products from being added correctly. It also verifies cart badge accuracy, button state changes, and product data consistency. Any unexpected behavior observed for problem_user should be recorded and compared against standard_user rather than automatically accepted as expected behavior.

### TC_019 — Verify cart navigation with performance_glitch_user
- **Module:** Shopping Cart
- **Scenario:** Verify cart navigation with performance_glitch_user
- **Preconditions:** Logged in, the Inventory Page is displayed, and the cart is initially empty.
- **Test Steps:**
  1. Click Add 2 different products to the cart
  2. Click shopping cart
  3. Verify the products displayed in the Cart Page
  4. Click Continue Shopping to return to the Inventory Page
  5. Verify the cart badge and selected product button states
  6. Navigate back to the Cart Page
- **Test Data:** Valid credentials (performance_glitch_user / secret_sauce), 2 different products
- **Expected Result:** The user should be able to navigate between the Inventory and Cart pages successfully despite potential delays. Cart contents, product information, button states, and cart badge counts should remain consistent throughout navigation.
- **Test Type:** Account-Specific / Functional / Performance Observation / Edge Case
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

**Note:**
- **AMBIGUITY:** No performance SLA or maximum acceptable page navigation time is specified for performance_glitch_user.
- **ASSUMPTION:** Navigation may be slower for performance_glitch_user compared to standard_user. However, delayed navigation should not cause incorrect cart state, lost products, duplicate products, or unexpected application errors.
- **Rationale:** This scenario verifies that delayed page navigation does not compromise shopping cart functionality or data consistency. Navigation duration is measured for observation and comparison with standard_user, rather than being evaluated against an arbitrary performance SLA.

### TC_020 — Verify that a user can successfully complete checkout with valid customer information
- **Module:** Checkout
- **Scenario:** Complete Checkout with Valid Information
- **Preconditions:** User is logged in as standard_user, and the shopping cart contains at least one product.
- **Test Steps:**
  1. Click Add one product to the shopping cart.
  2. Click shopping cart
  3. Click the Checkout button
  4. Enter valid customer information: First Name: John, Last Name: Smith, Postal Code: 10001
  5. Verify the product name, quantity, and price on the Order Overview page
  6. Verify the item subtotal and total amount
  7. Click the Finish button
  8. Verify the order confirmation message
  9. Verify the shopping cart state after checkout
  10. Click Back Home.
- **Test Data:** Account(standard_user/ secret_sauce), First Name: John, Last Name: Smith, Postal Code: 10001
- **Expected Result:** User proceeds through Checkout Information and Checkout Overview pages.
Order is completed successfully. Confirmation message is displayed and clear the shopping cart.
- **Test Type:** Positive
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

### TC_021 — Verify that checkout is prevented when the Postal Code field is left empty
- **Module:** Checkout
- **Scenario:** Verify Checkout with Empty Postal Code
- **Preconditions:** User is logged in as standard_user, and the shopping cart contains at least one product.
- **Test Steps:**
  1. Add one product to the shopping cart.
  2. Click shopping cart
  3. Click the Checkout button
  4. Enter valid customer information: First Name: John, Last Name: Smith, Leave the Postal Code field empty.
  5. Click the Continue button
  6. Verify the validation error message 
- **Test Data:** Account(standard_user/ secret_sauce), First Name: John, Last Name: Smith, Postal Code: Empty
- **Expected Result:** Validation error message is displayed Error: Postal Code is required. User cannot proceed to the next step.
- **Test Type:** Negative
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

### TC_022 — Verify checkout behavior when the shopping cart is empty
- **Module:** Checkout
- **Scenario:** Verify Checkout with Empty Cart
- **Preconditions:** The user is logged in as standard_user, and the shopping cart is empty.
- **Test Steps:**
  1. Verify that no products have been added to the cart
  2. Click shopping cart
  3. Verify the shopping cart contents
  4. Check whether the Checkout button is enabled
  5. If the Checkout button is enabled, click it
  6. Verify the application's response
- **Test Data:** Account(standard_user/ secret_sauce), Empty shopping cart (0 products)
- **Expected Result:** The shopping cart should correctly display an empty state when no products have been added.
- **Test Type:** Negative / Functional / Boundary / Edge Case
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

**Note:**
- **AMBIGUITY 1:** The requirements do not specify whether checkout should be allowed when the shopping cart is empty.
- **AMBIGUITY 2:** The requirements do not specify whether the Checkout button should be disabled, hidden, or remain enabled when the cart contains zero products.
- **AMBIGUITY 3:** No expected error message or validation behavior is defined for an empty-cart checkout attempt.
- **ASSUMPTION 1:** Checkout requires at least one product in the shopping cart.
- **ASSUMPTION 2:** The application should prevent users from completing an order when the cart is empty.
- **ASSUMPTION 3:** If an empty-cart checkout is attempted, the application should provide appropriate validation feedback or prevent the user from proceeding.
- **Rationale:** This scenario verifies checkout behavior at the lower boundary of the shopping cart item count (0 products).Allowing an empty cart to proceed through checkout may result in invalid or meaningless orders, inconsistent order data, or unnecessary processing.
The test also verifies that checkout becomes available once a valid product has been added.

### TC_023 — Verify Checkout Data Integrity for problem_user
- **Module:** Checkout
- **Scenario:** Verify Checkout Data Integrity for problem_user
- **Preconditions:** User is logged in as problem_user, and the shopping cart contains at least one successfully added product.
- **Test Steps:**
  1. Select a product and record its name and price
  2. Add the selected product to the shopping cart
  3. Navigate to the Cart Page
  4. Compare product information between the Inventory and Cart pages
  5. Click the Checkout button
  6. Enter valid customer information: First Name John, Last Name Smith, Postal Code 10001
  7. Click the Continue button
  8. Verify product names, prices, and quantities in the Checkout Overview
  9. Click the Finish button
  10. Verify the order confirmation message
  11. Verify the cart state after checkout
- **Test Data:** Account(problem_user/ secret_sauce), First Name: John, Last Name: Smith, Postal Code: 10001, one or more products
- **Expected Result:** Product name remains correct. Product price remains correct. Product image matches the selected item. Checkout summary matches cart contents.
- **Test Type:** Account-Specific / Functional 
- **Priority:** High
- **Risk:** High
- **Automation:** Yes

**Note:**
- **AMBIGUITY 1:** The requirements do not specify which checkout behaviors are expected to differ between standard_user and problem_user.
- **AMBIGUITY 2:** The expected validation rules and input restrictions for customer information fields are not fully defined.
- **ASSUMPTION 1:** Product information should remain consistent across Inventory, Cart, and Checkout Overview pages regardless of account type.
- **ASSUMPTION 2:** The problem_user account should follow the same functional checkout requirements as standard_user unless account-specific differences are explicitly documented.
- **Rationale:** This scenario focuses on identifying account-specific data integrity defects during checkout. Inconsistent product names, quantities, or prices may result in incorrect order information and inaccurate totals. These issues are particularly important because checkout is a critical business workflow. Comparing the behavior of problem_user against standard_user helps distinguish account-specific defects from general application issues.

### TC_024 — Verify Successful Checkout Under Slow Performance
- **Module:** Checkout
- **Scenario:** Verify Successful Checkout Under Slow Performance
- **Preconditions:** User is logged in as performance_glitch_user, and the cart contains 2 different products.
- **Test Steps:**
  1. Add 2 different products to the shopping cart
  2. Navigate to the Cart Page
  3. Click the Checkout button and observe the navigation duration
  4. Enter valid customer information: First Name John, Last Name Smith, Postal Code 10001
  5. Click the Continue button and observe the navigation duration
  6. Verify the selected products in the Checkout Overview
  7. Click the Finish button
  8. Verify the order confirmation message
  9. Verify the cart state after successful checkout
- **Test Data:** Account(performance_glitch_user/ secret_sauce), First Name: John, Last Name: Smith, Postal Code: 10001, 2 different products
- **Expected Result:** Checkout eventually completes successfully. No timeout occurs. No duplicated or missing products appear in the order summary.
- **Test Type:** Account-Specific / Functional / Performance Observation / Edge Case
- **Priority:** High
- **Risk:** Medium
- **Automation:** Yes

**Note:**
- **AMBIGUITY 1:** No performance SLA or maximum acceptable response time is specified for checkout operations.
- **AMBIGUITY 2:** The requirements do not specify which checkout steps, if any, are expected to experience delays for performance_glitch_user.
- **AMBIGUITY 3:** The acceptable timeout and recovery behavior for delayed checkout operations are not defined.
- **AMBIGUITY 4:** The requirements do not explicitly define whether the cart should be cleared immediately after successful checkout.
- **ASSUMPTION 1:** The performance_glitch_user account may experience slower responses than standard_user, but the checkout functionality should remain equivalent.
- **ASSUMPTION 2:** Delayed application responses should not cause product loss, duplicate items, incorrect pricing, or unexpected navigation failures.
- **ASSUMPTION 3:** A single click on Finish should be sufficient to complete checkout successfully.
- **ASSUMPTION 4:** After a successful checkout, the shopping cart should be cleared.
- **Rationale:** This scenario validates the reliability of the checkout flow under potentially slow application responses. As checkout is a critical business workflow, delays should not compromise order data accuracy or prevent successful order completion. Testing with performance_glitch_user helps identify account-specific timing issues, synchronization problems, and potential false failures caused by inappropriate automation waits. The scenario also verifies that the application can complete the purchase flow without repeated user actions, even when page transitions take longer than expected.

### TC-025 — Logout invalidates session and redirects to login
- **Module:** Log out / Session Management
- **Scenario:** User logs out successfully
- **Preconditions:** User is authenticated
- **Test Steps:**
  1. Open menu
  2. Click Logout
- **Test Data:** Username: standard_user, Password: secret_sauce
- **Expected Result:** User returns to login page and cannot access inventory without logging in again
- **Test Type:** Positive
- **Priority:** High
- **Risk:** High
- **Automation:** Yes
