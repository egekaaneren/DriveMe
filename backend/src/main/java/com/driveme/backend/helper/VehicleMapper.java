package com.driveme.backend.helper;

import com.driveme.backend.dto.VehicleResponse;
import com.driveme.backend.entity.Vehicle;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Vehicle entities to DTOs.
 */
@Component
public class VehicleMapper {

    public VehicleResponse toResponse(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        return VehicleResponse.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .transmissionType(vehicle.getTransmissionType())
                .seats(vehicle.getSeats())
                .status(vehicle.getStatus())
                .driverId(vehicle.getDriver() != null ? vehicle.getDriver().getId() : null)
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }
}
