package org.karthick.dietplanner.security;

public final class SecurityConstants {
  public static final int TOKEN_EXPIRATION = 3600000;
  public static final String BEARER = "Bearer ";
  public static final String SECRET_KEY = "XmUgs0HKFfUdRW6KsyevIjmH4MMSlW8R";
  public static final String AUTHORIZATION = "Authorization";
  public static final String REGISTER_PATH = "/user/register";

  private SecurityConstants() {}
}
