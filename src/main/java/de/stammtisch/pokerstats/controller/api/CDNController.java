package de.stammtisch.pokerstats.controller.api;

import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/cdn")
@AllArgsConstructor
public class CDNController {
    private final UserService userService;

    @GetMapping(value = "/u/picture", produces = {"image/*"})
    public ResponseEntity<?> userProfile(@RequestParam(value = "id") long id) {
        final User user;
        try {
            user = this.userService.getUserById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final String path = "./data/user/%d/picture.%s".formatted(id, user.getProfilePictureType());
        final byte[] image;
        try {
            image = new UrlResource(Paths.get(path).toUri()).getInputStream().readAllBytes();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
