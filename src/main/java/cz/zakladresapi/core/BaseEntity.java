package cz.zakladresapi.core;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

/**
 * Předek entit
 */
@MappedSuperclass // nejedná se o entitu
@Data
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "zmenacas", insertable = false, updatable = false)
  private LocalDateTime zmenaCas;

  @Column(name = "zmenauzivatel", insertable = false, updatable = false)
  private String zmenaUzivatel;

  @Column(name = "platnost", insertable = false, updatable = true)
  private boolean platnost;
}
