package com.driveme.backend.dto;

import com.driveme.backend.common.TransmissionType;
import com.driveme.backend.common.VerificationStatus;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class VehicleResponse {

    private UUID id;
    private String plateNumber;
    private TransmissionType transmissionType;
    private int seats;
    private VerificationStatus status;
    private UUID driverId;
    private Instant createdAt;
    private Instant updatedAt;
}
