package com.driveme.backend.service;

import com.driveme.backend.common.VerificationStatus;
import com.driveme.backend.dto.CreateVehicleRequest;
import com.driveme.backend.entity.Driver;
import com.driveme.backend.entity.Vehicle;
import com.driveme.backend.repository.DriverRepository;
import com.driveme.backend.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing vehicles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Transactional
    public Vehicle createVehicle(UUID driverId, CreateVehicleRequest request) {
        log.info("Creating vehicle for driver: {}", driverId);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));

        if (vehicleRepository.findByPlateNumber(request.getPlateNumber()).isPresent()) {
            throw new IllegalStateException("Vehicle with plate already exists: " + request.getPlateNumber());
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setTransmissionType(request.getTransmissionType());
        vehicle.setSeats(request.getSeats());
        vehicle.setStatus(VerificationStatus.PENDING);
        vehicle.setDriver(driver);

        Vehicle saved = vehicleRepository.save(vehicle);
        log.info("Vehicle created with ID: {}", saved.getId());

        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Vehicle> getVehicleById(UUID vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getVehiclesOfDriver(UUID driverId) {
        return vehicleRepository.findByDriver_Id(driverId);
    }

    @Transactional
    public Vehicle approveVehicle(UUID vehicleId) {
        log.info("Approving vehicle: {}", vehicleId);

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));

        if (vehicle.getStatus() == VerificationStatus.APPROVED) {
            throw new IllegalStateException("Vehicle already APPROVED: " + vehicleId);
        }
        if (vehicle.getStatus() == VerificationStatus.REJECTED) {
            throw new IllegalStateException("Cannot approve a REJECTED vehicle: " + vehicleId);
        }

        vehicle.setStatus(VerificationStatus.APPROVED);
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle rejectVehicle(UUID vehicleId) {
        log.info("Rejecting vehicle: {}", vehicleId);

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));

        if (vehicle.getStatus() == VerificationStatus.REJECTED) {
            throw new IllegalStateException("Vehicle already REJECTED: " + vehicleId);
        }
        if (vehicle.getStatus() == VerificationStatus.APPROVED) {
            throw new IllegalStateException("Cannot reject an APPROVED vehicle: " + vehicleId);
        }

        vehicle.setStatus(VerificationStatus.REJECTED);
        return vehicleRepository.save(vehicle);
    }
}
