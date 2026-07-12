package com.timora.app.controller;

import com.timora.app.dto.booking.BookingCreateDTO;
import com.timora.app.dto.booking.BookingDTO;
import com.timora.app.dto.booking.BookingPatchDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Crear una nueva reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping
    public ResponseEntity<BookingDTO> create(@Valid @RequestBody BookingCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.create(dto));
    }

    @Operation(summary = "Actualizar parcialmente una reserva")
    @PatchMapping("/{id}")
    public ResponseEntity<BookingDTO> patch(
            @Parameter(description = "ID de la reserva") @PathVariable Long id,
            @Valid @RequestBody BookingPatchDTO dto) {
        return ResponseEntity.ok(bookingService.patch(id, dto));
    }

    @Operation(summary = "Eliminar (soft delete) una reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todas las reservas de la compañía")
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllByCompany() {
        return ResponseEntity.ok(bookingService.getAllByCompany());
    }

    @Operation(summary = "Obtener una reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getById(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @Operation(summary = "Obtener todas las reservas de un cliente")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getAllByCustomer(
            @Parameter(description = "ID del cliente") @PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getAllByCustomer(customerId));
    }

    @Operation(summary = "Obtener todas las reservas de un servicio")
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<BookingDTO>> getAllByService(
            @Parameter(description = "ID del servicio") @PathVariable Long serviceId) {
        return ResponseEntity.ok(bookingService.getAllByService(serviceId));
    }

    @Operation(summary = "Obtener todas las reservas de un proveedor")
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<BookingDTO>> getAllBySupplier(
            @Parameter(description = "ID del proveedor") @PathVariable Long supplierId) {
        return ResponseEntity.ok(bookingService.getAllBySupplier(supplierId));
    }

    @Operation(summary = "Obtener reservas de un proveedor en un rango de fechas")
    @GetMapping("/supplier/{supplierId}/range")
    public ResponseEntity<List<BookingDTO>> getBySupplierAndDateRange(
            @Parameter(description = "ID del proveedor") @PathVariable Long supplierId,
            @Parameter(description = "Fecha inicio (ISO: 2026-01-01T00:00:00)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha fin (ISO: 2026-12-31T23:59:59)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(
                bookingService.getBySupplierAndDateRange(supplierId, startDate, endDate)
        );
    }

    @Operation(summary = "Validar solapamiento de reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "No hay solapamiento"),
            @ApiResponse(responseCode = "400", description = "Hay solapamiento o datos inválidos")
    })
    @GetMapping("/validate-overlap")
    public ResponseEntity<Void> validateOverlap(
            @Parameter(description = "ID del servicio") @RequestParam Long serviceId,
            @Parameter(description = "Inicio del rango (ISO: 2026-07-15T10:00:00)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "Fin del rango (ISO: 2026-07-15T10:30:00)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "ID de reserva a excluir (para updates)")
            @RequestParam(required = false) Long excludeId) {
        bookingService.validateOverlap(serviceId, startTime, endTime, excludeId);
        return ResponseEntity.ok().build();
    }
}