package com.PartWork.NTCITP.controller;

import com.PartWork.NTCITP.model.*;
import com.PartWork.NTCITP.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PartworkController {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;


    @PostMapping("/users/register")
    public ResponseEntity<AppUser> register(@Valid @RequestBody AppUser user) {
        return ResponseEntity.ok(userRepository.save(user));
    }


    @PostMapping("/jobs")
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) {
        return ResponseEntity.ok(jobRepository.save(job));
    }


    @GetMapping("/jobs")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }


    @PutMapping("/jobs/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        return jobRepository.findById(id).map(job -> {
            job.setTitle(updatedJob.getTitle());
            job.setSalary(updatedJob.getSalary());
            job.setAddress(updatedJob.getAddress());
            return ResponseEntity.ok(jobRepository.save(job));
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/applications")
    public ResponseEntity<JobApplication> applyToJob(@RequestBody JobApplication app) {
        return ResponseEntity.ok(applicationRepository.save(app));
    }


    @PutMapping("/applications/{id}/status")
    public ResponseEntity<JobApplication> updateAppStatus(@PathVariable Long id, @RequestParam String status) {
        return applicationRepository.findById(id).map(app -> {
            app.setStatus(status);
            return ResponseEntity.ok(applicationRepository.save(app));
        }).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/jobs/{id}/complete")
    public ResponseEntity<Job> completeJob(@PathVariable Long id) {
        return jobRepository.findById(id).map(job -> {
            job.setStatus("ЗАВЕРШЕНЕ");
            job.setPaymentStatus("ОПЛАЧЕНО");
            return ResponseEntity.ok(jobRepository.save(job));
        }).orElse(ResponseEntity.notFound().build());
    }
}
