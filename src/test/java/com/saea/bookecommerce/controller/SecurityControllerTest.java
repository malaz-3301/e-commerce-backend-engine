package com.saea.bookecommerce.controller;

import com.saea.bookecommerce.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityControllerTest {

    @Test
    void securityConfigEnablesMethodSecurity() {
        assertThat(SecurityConfig.class.isAnnotationPresent(EnableMethodSecurity.class)).isTrue();
    }


    @Test
    void bookControllerHasSecurityAnnotations() throws NoSuchMethodException {
        assertThat(BookController.class.getMethod("create", com.saea.bookecommerce.dto.BookRequest.class)
                .isAnnotationPresent(org.springframework.security.access.prepost.PreAuthorize.class)).isTrue();
        assertThat(BookController.class.getMethod("delete", Long.class)
                .isAnnotationPresent(org.springframework.security.access.prepost.PreAuthorize.class)).isTrue();
    }

    @Test
    void orderControllerHasSecurityAnnotations() throws NoSuchMethodException {
        assertThat(OrderController.class.getMethod("findAll")
                .isAnnotationPresent(org.springframework.security.access.prepost.PreAuthorize.class)).isTrue();
        assertThat(OrderController.class.getMethod("delete", Long.class)
                .isAnnotationPresent(org.springframework.security.access.prepost.PreAuthorize.class)).isTrue();
    }
}
