package client;

import io.restassured.response.Response;

public record ApiExchange(String requestDump, Response response) {
  public String diagnosticDump() {
    return "\n--- REQUEST ---\n" + requestDump +
        "\n--- RESPONSE ---\nStatus: " + response.statusCode() +
        "\nHeaders: " + response.headers() +
        "\nBody:\n" + response.asPrettyString();
  }
}
