package com.mzoubi.smartcity.modules.alert.controller;

import com.mzoubi.smartcity.common.dto.ApiResponse;
import com.mzoubi.smartcity.common.dto.PagedResponse;
import com.mzoubi.smartcity.common.utils.PaginationUtils;
import com.mzoubi.smartcity.modules.alert.dto.AlertDto;
import com.mzoubi.smartcity.modules.alert.messages.AlertMessages;
import com.mzoubi.smartcity.modules.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private static final Set<String> ALLOWED_FIELDS = Set.of("id");

    private final AlertService alertService;

    @GetMapping()
    public ResponseEntity<ApiResponse<PagedResponse<AlertDto>>> getAllAlerts(
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<AlertDto> alerts = PagedResponse.from(alertService.getAllAlerts(pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        AlertMessages.GET_ALL_ALERTS_MESSAGE, alerts, HttpStatus.OK));
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<ApiResponse<PagedResponse<AlertDto>>> getAlertsByCity(
            @PathVariable Long cityId,
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<AlertDto> alerts = PagedResponse.from(alertService.getAlertsByCity(cityId, pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        AlertMessages.GET_ALERTS_BY_CITY_MESSAGE + cityId, alerts, HttpStatus.OK));
    }

    @GetMapping("/resolved")
    public ResponseEntity<ApiResponse<PagedResponse<AlertDto>>> getResolvedAlerts(
            @PathVariable Long cityId,
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<AlertDto> alerts = PagedResponse.from(alertService.getResolvedAlerts(pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        AlertMessages.GET_RESOLVED_ALERTS_MESSAGE, alerts, HttpStatus.OK));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<PagedResponse<AlertDto>>> getActiveAlerts(
            @PathVariable Long cityId,
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<AlertDto> alerts = PagedResponse.from(alertService.getActiveAlerts(pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        AlertMessages.GET_ACTIVE_ALERTS_MESSAGE, alerts, HttpStatus.OK));
    }

    @PutMapping("/{alertId}/resolve")
    public ResponseEntity<ApiResponse<Void>> resolveAlert(
            @PathVariable Long alertId) {
        alertService.resolveAlert(alertId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(AlertMessages.RESOLVE_ALERT_MESSAGE, null, HttpStatus.OK));
    }


}
