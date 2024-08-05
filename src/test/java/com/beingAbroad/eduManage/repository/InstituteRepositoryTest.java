package com.beingAbroad.eduManage.repository;

import com.beingAbroad.eduManage.entity.Institute;
import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class InstituteRepositoryTest {

    @Autowired
    InstituteRepository instituteRepository;
    Institute testInstitute;
    @BeforeEach
    void setUp() {
        testInstitute = new Institute();

        testInstitute.setId(UUID.randomUUID());
        testInstitute.setName("Test Institute");
        testInstitute.setLocation("Test Location");
        testInstitute.setEmail("test@example.com");
        testInstitute.setPhoneNumber("1234567890");
        testInstitute.setInstituteCode("TEST123");
       instituteRepository.save(testInstitute);
    }

    @AfterEach
    void tearDown() {
        testInstitute = null;
       instituteRepository.deleteAll();
    }

    @Test
    void testExistsByInstituteCode_ShouldFindExistingCode() {
        boolean exists = instituteRepository.existsByInstituteCode(testInstitute.getInstituteCode());
        assertThat(exists).isTrue();
    }


    @Test
    void testExistsByInstituteCode_ShouldNotFoundNonexistentCode() {
        boolean exists = instituteRepository.existsByInstituteCode("NON_EXISTENT_CODE");
        assertThat(exists).isFalse();
    }

    @Test
    void testExistsByEmail_ShouldFindExistingEmail() {
        boolean exists = instituteRepository.existsByEmail(testInstitute.getEmail());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail_ShouldNotFoundNonexistentEmail() {
        boolean exists = instituteRepository.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }
}