package com.sulwork.backend.config;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestIdConfig {

    @EntityListeners(TestIdConfig.class)
    public static class IdFixer {

        @PrePersist
        public void fixId(Object entity) {
            try {
                var field = entity.getClass().getDeclaredField("id");
                field.setAccessible(true);
                if (field.get(entity) == null) {
                    
                }
            } catch (Exception ignored) {}
        }
    }
}
