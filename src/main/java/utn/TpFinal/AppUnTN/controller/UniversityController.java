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

        //Valida que el Id sea nulo antes de crear

        if (university.getId() != null) {
            return ResponseEntity.badRequest()
                    .body("Error: No puedes enviar un ID para crear una nueva universidad. El sistema lo genera automáticamente.");
        }

        //Valida que no venga vacio el nombre.

        if (university.getName() == null || university.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Error: El nombre de la universidad es obligatorio.");
        }

        University nuevaUni = universityService.guardar(university);
        return ResponseEntity.ok(nuevaUni);
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
        if (university.getId() == null || !universityService.buscarPorId(university.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("ID de universidad no válido o no existente.");
        }
        return ResponseEntity.ok(universityService.guardar(university));
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