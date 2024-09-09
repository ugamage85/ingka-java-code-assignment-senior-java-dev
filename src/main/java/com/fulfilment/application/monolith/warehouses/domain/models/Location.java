package com.fulfilment.application.monolith.warehouses.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Location {
  private String identification;
  // maximum number of warehouses that can be created in this location
  private int maxNumberOfWarehouses;
  // maximum capacity of the location summing all the warehouse capacities
  private int maxCapacity;
}
