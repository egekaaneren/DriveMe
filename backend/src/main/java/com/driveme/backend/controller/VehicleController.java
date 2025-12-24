package com.driveme.backend.controller;

import com.driveme.backend.dto.CreateVehicleRequest;
import com.driveme.backend.dto.VehicleResponse;
import com.driveme.backend.entity.Vehicle;
import com.driveme.backend.helper.VehicleMapper;
import com.driveme.backend.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for vehicle operations.
 */
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle", description = "Vehicle management APIs")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    @PostMapping("/driver/{driverId}")
    @Operation(summary = "Create a vehicle", description = "Creates a new vehicle for a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<VehicleResponse> createVehicle(
            @Parameter(description = "Driver ID") @PathVariable UUID driverId,
            @Valid @RequestBody CreateVehicleRequest request
    ) {
        log.info("Creating vehicle for driver: {}", driverId);
        Vehicle vehicle = vehicleService.createVehicle(driverId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleMapper.toResponse(vehicle));
    }

    @GetMapping("/{vehicleId}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieves a vehicle by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<VehicleResponse> getVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable UUID vehicleId
    ) {
        return vehicleService.getVehicleById(vehicleId)
                .map(vehicle -> ResponseEntity.ok(vehicleMapper.toResponse(vehicle)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get vehicles of a driver", description = "Retrieves all vehicles belonging to a driver")
    public ResponseEntity<List<VehicleResponse>> getVehiclesByDriver(
            @Parameter(description = "Driver ID") @PathVariable UUID driverId
    ) {
        List<VehicleResponse> vehicles = vehicleService.getVehiclesOfDriver(driverId)
                .stream()
                .map(vehicleMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vehicles);
    }

    @PostMapping("/{vehicleId}/approve")
    @Operation(summary = "Approve a vehicle", description = "Marks a pending vehicle as APPROVED")
    public ResponseEntity<?> approveVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable UUID vehicleId
    ) {
        try {
            Vehicle vehicle = vehicleService.approveVehicle(vehicleId);
            return ResponseEntity.ok(vehicleMapper.toResponse(vehicle));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{vehicleId}/reject")
    @Operation(summary = "Reject a vehicle", description = "Marks a pending vehicle as REJECTED")
    public ResponseEntity<?> rejectVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable UUID vehicleId
    ) {
        try {
            Vehicle vehicle = vehicleService.rejectVehicle(vehicleId);
            return ResponseEntity.ok(vehicleMapper.toResponse(vehicle));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
