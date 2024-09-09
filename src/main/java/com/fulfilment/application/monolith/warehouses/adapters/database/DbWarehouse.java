package com.fulfilment.application.monolith.warehouses.adapters.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "warehouse")
@Cacheable
public class DbWarehouse {

  @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

  private String businessUnitCode;

  private String location;

  private Integer capacity;

  private Integer stock;

  private LocalDateTime createdAt;

  private LocalDateTime archivedAt;



  //public DbWarehouse() {}

  /*public Warehouse toWarehouse() {
    var warehouse = new Warehouse();
    warehouse.businessUnitCode = this.businessUnitCode;
    warehouse.location = this.location;
    warehouse.capacity = this.capacity;
    warehouse.stock = this.stock;
    warehouse.createdAt = this.createdAt;
    warehouse.archivedAt = this.archivedAt;
    return warehouse;
  }*/
}
