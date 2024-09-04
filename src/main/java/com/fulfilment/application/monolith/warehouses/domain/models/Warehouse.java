package com.fulfilment.application.monolith.warehouses.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Warehouse {

  // unique identifier
  private String businessUnitCode;

  private String location;

  private Integer capacity;

  private Integer stock;

  private LocalDateTime createdAt;

  private LocalDateTime archivedAt;
}
