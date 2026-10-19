package data;

public record CheckoutData(String firstName, String lastName, String postalCode) {
  public static CheckoutData validCustomer() {
    return new CheckoutData("John", "Smith", "10001");
  }
}
