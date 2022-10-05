package bobocode.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final String BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=DEMO_KEY&camera=NAVCAM";
    private final RestTemplate restTemplate;

    public Optional<URI> findPicture(Integer sol, String camera) {
        URI uri = buildUri(sol, camera);
        return Optional.ofNullable(restTemplate.getForObject(uri, JsonNode.class)).stream()
                .flatMap(response -> response.findValues("img_src")
                        .stream()
                        .map(node -> URI.create(node.asText())))
                .parallel()
                .map(url -> new AbstractMap.SimpleEntry<>(url, getLength(url)))
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

    }

    private Long getLength(URI url) {
        URI location = restTemplate.headForHeaders(url)
                .getLocation();
        return restTemplate.headForHeaders(location)
                .getContentLength();
    }

    private URI buildUri(Integer sol, String camera) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("api_key", "DEMO")
                .queryParamIfPresent("sol", Optional.ofNullable(sol))
                .queryParamIfPresent("camera", Optional.ofNullable(camera))
                .build()
                .toUri();
    }
}
