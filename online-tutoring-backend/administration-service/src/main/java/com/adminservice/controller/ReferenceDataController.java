package com.adminservice.controller;

import com.adminservice.model.DropdownRecord;
import com.adminservice.service.CourseRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/mdm/referenceData")
public class ReferenceDataController {

    private final CourseRegistrationService courseRegistrationService;

    @GetMapping("/subjects")
    public List<DropdownRecord> getSubjects(){
        return courseRegistrationService.getAllSubjects();
    }

    @GetMapping("/grades")
    public List<DropdownRecord> getGrades(){
        return courseRegistrationService.getAllGrades();
    }

}
