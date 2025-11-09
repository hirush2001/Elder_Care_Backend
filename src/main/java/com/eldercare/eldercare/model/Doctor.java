package com.eldercare.eldercare.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
   @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private User user;

  private String specialization;
  private String hospital;
}
