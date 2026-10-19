package config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ApiConfig {
  public static final String BASE_URI = "https://gorest.co.in/public/v2";
  private static final String TOKEN_KEY = "GOREST_API_TOKEN";

  private ApiConfig() {}

  public static String requireToken() {
    String token = System.getenv(TOKEN_KEY);
    if (token == null || token.isBlank()) {
      token = readTokenFromDotEnv();
    }
    if (token == null || token.isBlank()) {
      throw new IllegalStateException(
          "Missing GOREST_API_TOKEN. Set it as an environment variable or add it to api-tests/.env. " +
          "Never commit the token to Git.");
    }
    return token.trim();
  }

  private static String readTokenFromDotEnv() {
    Path path = Path.of(".env");
    if (!Files.exists(path)) return null;
    try {
      List<String> lines = Files.readAllLines(path);
      return lines.stream()
          .map(String::trim)
          .filter(line -> !line.isEmpty() && !line.startsWith("#"))
          .filter(line -> line.startsWith(TOKEN_KEY + "="))
          .map(line -> line.substring((TOKEN_KEY + "=").length()).trim())
          .map(ApiConfig::removeOptionalQuotes)
          .findFirst()
          .orElse(null);
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to read .env file", exception);
    }
  }

  private static String removeOptionalQuotes(String value) {
    if (value.length() >= 2 &&
        ((value.startsWith("\"") && value.endsWith("\"")) ||
         (value.startsWith("'") && value.endsWith("'")))) {
      return value.substring(1, value.length() - 1);
    }
    return value;
  }
}
