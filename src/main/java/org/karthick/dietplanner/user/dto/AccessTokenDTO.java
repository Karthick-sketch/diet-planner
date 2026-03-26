package org.karthick.dietplanner.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.karthick.dietplanner.security.SecurityConstants;

@Getter
@NoArgsConstructor
public class AccessTokenDTO {

  private String accessToken;

  public AccessTokenDTO(String accessToken) {
    setAccessToken(accessToken);
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = SecurityConstants.BEARER + accessToken;
  }
}
