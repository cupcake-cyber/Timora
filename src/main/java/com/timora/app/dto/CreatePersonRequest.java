    package com.timora.app.dto;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class CreatePersonRequest {
        private long companyId;
        private String firstName;
        private String lastName;
        private String phone;
        private String email;
        private String address;

        private UserData user;
        private CustomerData customer;
        private SupplierData supplier;

        @Getter
        @Setter
        public static class UserData {
            private String loginEmail;
            private String password;
            private String globalRole;
        }

        @Getter
        @Setter
        public static class CustomerData {
            private String notes;
        }

        @Getter
        @Setter
        public static class SupplierData {
            private String specialty;
            private String notes;
        }
    }