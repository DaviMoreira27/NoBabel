package com.example.mimir.dto;

/*
  In Java, a `record` is a special type of class introduced in Java 14 (as a preview feature) and became stable in Java 16.
  It is a compact syntax to define immutable data classes that automatically generate common methods like:
  - `equals()`
  - `hashCode()`
  - `toString()`
  - Getters for the fields
  - A constructor for all fields

  A record automatically provides:
  - An immutable state, which means once created, the fields of a record cannot be modified.
  - A canonical constructor (a constructor that matches the parameters of the record).

  The `SessionData` in your example would be defined as:
*/

public record SessionData(
        String email,
        long expirationTime,
        long sessionStartTime,
        long sessionMaxIdleTime,
        boolean isExpired,
        String userAgent,
        String ipAddress,
        long lastRequestTime
) {
    /*
      The record `SessionData` is a class that can hold data. However, in this example, no fields are declared inside the parentheses.
      This means the `SessionData` record doesn't contain any fields or data.

      If you want to define fields, you would specify them inside the parentheses, like so:
      public record SessionData(String userId, String sessionToken) { }

      This will automatically generate:
      - A constructor that takes `userId` and `sessionToken`.
      - Getter methods for the fields: `userId()` and `sessionToken()`.
      - A `toString()` method to output a human-readable representation of the record.
      - An `equals()` and `hashCode()` method for equality comparison.

      Example of a filled record:
      public record SessionData(String userId, String sessionToken) { }
      SessionData sessionData = new SessionData("user1", "token123");
      System.out.println(sessionData.userId()); // "user1"
      System.out.println(sessionData.sessionToken()); // "token123"
    */
}
