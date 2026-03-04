package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.model.University;
import utn.TpFinal.AppUnTN.service.UniversityService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/universities")
@PreAuthorize("hasRole('ADMIN')")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody University university) {
        if (university.getId() != null) {
            return ResponseEntity.badRequest()
                    .body("Error: No puedes enviar un ID para crear una nueva universidad.");
        }

        if (university.getName() == null || university.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Error: El nombre de la universidad es obligatorio.");
        }

        try {
            University nuevaUni = universityService.guardar(university);
            return ResponseEntity.ok(nuevaUni);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado al crear la universidad.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAll")
    public ResponseEntity<List<University>> getAll() {
        return ResponseEntity.ok(universityService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<University> getById(@PathVariable Long id) {
        return universityService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody University university) {

        if (university.getId() == null) {
            return ResponseEntity.badRequest().body("Error: El ID de la universidad es necesario para actualizar.");
        }

        if (!universityService.buscarPorId(university.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: La universidad con ID " + university.getId() + " no existe.");
        }

        try {

            University actualizada = universityService.guardar(university);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado al actualizar.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!universityService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        //Al borrar una univerisdad se borran sus carreras y materias por delete on cascade.
        universityService.eliminar(id);
        return ResponseEntity.ok("Universidad eliminada correctamente.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<University>> search(@RequestParam String name) {
        return ResponseEntity.ok(universityService.buscarPorNombre(name));
    }
}