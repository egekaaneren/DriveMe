package com.driveme.backend.repository;

import com.driveme.backend.common.VerificationStatus;
import com.driveme.backend.entity.Vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Vehicle entity operations.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    List<Vehicle> findByDriver_Id(UUID driverId);

    Optional<Vehicle> findByPlateNumber(String plateNumber);

    List<Vehicle> findByStatus(VerificationStatus status);

    boolean existsByDriver_IdAndPlateNumber(UUID driverId, String plateNumber);
}
