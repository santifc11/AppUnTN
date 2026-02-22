package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.model.Career;
import utn.TpFinal.AppUnTN.service.CareerService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/careers")
@PreAuthorize("hasRole('ADMIN')")
public class CareerController {

    @Autowired
    private CareerService careerService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Career career) {
        try {
            if (career.getId() != null) {
                return ResponseEntity.badRequest().body("El ID debe ser nulo para crear.");
            }
            return ResponseEntity.ok(careerService.save(career));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(careerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return careerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Career career) {
        try {
            if (career.getId() == null) {
                return ResponseEntity.badRequest().body("Se requiere ID para actualizar.");
            }
            return ResponseEntity.ok(careerService.save(career)); // Reutilizamos save
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            careerService.delete(id);
            return ResponseEntity.ok("Carrera eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String name) {
        try {
            List<Career> results = careerService.findByName(name);
            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron carreras.");
            }
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/by-university/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Career>> getByUniversity(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.getByUniversity(id));
    }
}