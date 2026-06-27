//package com.timora.app.service;
//
//import com.timora.app.dto.ServiceCreateDTO;
//import com.timora.app.dto.ServiceDetailsDTO;
//import com.timora.app.dto.ServiceSummaryDTO;
//import com.timora.app.dto.ServiceUpdateDTO;
//import com.timora.app.model.enums.ServiceStatus;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ServiceService {
//
//    List<ServiceSummaryDTO> findAll();
//
//    List<ServiceSummaryDTO> getServicesBySupplier(Long supplierId);
//
//    ServiceDetailsDTO getServiceById(Long id);
//
//    ServiceDetailsDTO createService(ServiceCreateDTO dto);
//
//    ServiceDetailsDTO updateService(Long id, ServiceUpdateDTO dto);
//
//    void updateStatus(Long id, String status);
//
//    void delete(Long id);
//}