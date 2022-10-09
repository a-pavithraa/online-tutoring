package com.studentassessment.controller;

import com.studentassessment.entity.Assessment;
import com.studentassessment.model.CreateAssessmentRequest;
import com.studentassessment.repo.AssessmentRepo;
import com.studentassessment.service.AssessmentService;
import com.studentassessment.utils.LocalstackContainerInitializer;
import com.studentassessment.utils.MySqlDatabaseContainerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {

        LocalstackContainerInitializer.class,
        MySqlDatabaseContainerInitializer.class

}, classes = {
        LocalstackContainerInitializer.AWSConfiguration.class
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AssessmentControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AssessmentRepo assessmentRepo;

    @Autowired
    AssessmentService assessmentService;


    @BeforeEach
    void setUp() {
        assessmentRepo.deleteAllInBatch();



    }
    @Test
    void shouldInsertAssessment() throws Exception {
        CreateAssessmentRequest createAssessmentRequest = CreateAssessmentRequest.builder().assessmentDate("2022-10-09 04:35").gradeId(1L).subjectId(1L).teacherId(1L).build();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/assessment/assessment").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createAssessmentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUploadAssessment() throws Exception {
        Assessment assessment = Assessment.builder().assessmentDate(LocalDateTime.now()).gradeId(1L).subjectId(1L).build();
        CreateAssessmentRequest createAssessmentRequest = CreateAssessmentRequest.builder().assessmentDate("2022-10-09 04:35").gradeId(1L).subjectId(1L).teacherId(1L).build();
        //  assessmentRepo.persist(assessment);
        assessmentService.createAssessment(createAssessmentRequest);

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some data".getBytes());

        mockMvc.perform(multipart("/assessment/questionPaperUpload?assessmentId=1")
                        .file("file", firstFile.getBytes()))
                .andExpect(status().isOk());


    }


}
