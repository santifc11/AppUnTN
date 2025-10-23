package utn.TpFinal.AppUnTN.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.TpFinal.AppUnTN.model.Subject;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @GetMapping("/all")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(Arrays.asList(Subject.values()));
    }
}
