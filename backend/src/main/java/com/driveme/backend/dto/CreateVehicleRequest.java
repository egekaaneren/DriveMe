package com.driveme.backend.dto;

import com.driveme.backend.common.TransmissionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a vehicle for a driver.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    private String plateNumber;
    private TransmissionType transmissionType;
    private int seats;
}
