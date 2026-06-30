package com.timora.app.service;

import com.timora.app.dto.service.ServiceCreateDTO;
import com.timora.app.dto.service.ServiceDTO;
import com.timora.app.dto.service.ServicePatchDTO;

import java.util.List;

public interface ServiceService {
    ServiceDTO create(ServiceCreateDTO request);
    ServiceDTO patch(Long id, ServicePatchDTO request);
    void delete(Long id);
    List<ServiceDTO> getAll();
    ServiceDTO getById(Long id);
}