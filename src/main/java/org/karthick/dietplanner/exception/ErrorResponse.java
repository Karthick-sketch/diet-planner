package org.karthick.dietplanner.exception;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private List<String> error;

  public ErrorResponse(String errorMessage) {
    this.error = List.of(errorMessage);
  }
}
