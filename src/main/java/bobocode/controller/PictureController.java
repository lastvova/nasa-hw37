package bobocode.controller;

import bobocode.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @GetMapping("/mars/pictures/largest")
    public ResponseEntity<Object> findPicture(@RequestParam Integer sol, @RequestParam String camera) {
        Optional<URI> uri = pictureService.findPicture(sol, camera);
        return uri.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.status(HttpStatus.FOUND).location(uri.get()).build();
    }
}
