package com.climbme.app.training;
import com.climbme.app.auth.UserAccount;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/training") public class TrainingController { private final TrainingService service; public TrainingController(TrainingService service){this.service=service;} @GetMapping public List<TrainingService.View> list(@AuthenticationPrincipal UserAccount user){return service.list(user);}@PostMapping public ResponseEntity<TrainingService.View> create(@AuthenticationPrincipal UserAccount user,@Valid @RequestBody TrainingSession.Request request){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user,request));}@PutMapping("/{id}") public TrainingService.View update(@AuthenticationPrincipal UserAccount user,@PathVariable Long id,@Valid @RequestBody TrainingSession.Request request){return service.update(user,id,request);}@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal UserAccount user,@PathVariable Long id){service.delete(user,id);return ResponseEntity.noContent().build();} }
