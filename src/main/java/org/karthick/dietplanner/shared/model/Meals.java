package org.karthick.dietplanner.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meals {
  private List<String> breakfast;
  public List<String> lunch;
  private List<String> snack;
  private List<String> dinner;
}
