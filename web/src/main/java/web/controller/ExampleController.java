package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.security.service.UserService;

@RestController
@RequestMapping("/example")
@Tag(name = "Authentication")
public class ExampleController {
    private final UserService service;

    @Autowired
    public ExampleController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Available only to authorized users")
    public String example() {
        return "Hello, world!";
    }

    @GetMapping("/admin")
    @Operation(summary = "Available only to authorized users with the ADMIN role")
    @PreAuthorize("hasRole('ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }

    @GetMapping("/get-admin")
    @Operation(summary = "Get the ADMIN role (for demonstration purposes)")
    public void getAdmin() {
        service.getAdmin();
    }
}
