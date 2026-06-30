//package com.timora.app.service;
//
//import com.timora.app.dto.BookingCreateDTO;
//import com.timora.app.dto.BookingDetailsDTO;
//import com.timora.app.dto.BookingSummaryDTO;
//
//import java.util.List;
//
//
//
//public interface BookingService {
//
//    // CREATE
//    BookingDetailsDTO create(BookingCreateDTO dto);
//
//    // READ
//    BookingDetailsDTO getById(Long id);
//
//    List<BookingSummaryDTO> getMyBookings();
//
//    List<BookingSummaryDTO> getByCompany(Long companyId);
//
//    List<BookingSummaryDTO> getByCustomer(Long customerId);
//
//    // STATE
//    BookingDetailsDTO confirm(Long id);
//
//    BookingDetailsDTO cancel(Long id);
//
//    BookingDetailsDTO complete(Long id);
//
//    // DELETE
//    void delete(Long id);
//}