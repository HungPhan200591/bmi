package com.tericcabrel.bmi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HUNGPC1
 */

@RestController
public class WebClientController {

    @Autowired
    private WebClient webClient;

//    @Autowired
//    private RestTemplate restTemplate;

    @GetMapping ("/webflux/sample-api-get")
    public ResponseEntity<WebClientTestGetDTO> getSampleObject() {
        WebClientTestGetDTO webClientTestGetDTO = new WebClientTestGetDTO("1", "2", "3", "4", "5", "6", "7");
        return ResponseEntity.ok(webClientTestGetDTO);
    }

    @GetMapping ("/webflux/sample-api-get-list")
    public ResponseEntity<List<WebClientTestGetDTO>> getSampleListObject() {
        return ResponseEntity.ok(Arrays.asList(
                new WebClientTestGetDTO("user_sumasapo_no", "スマサポNO", "VARCHAR", "", "", "", ""),
                new WebClientTestGetDTO("agenda_solution_nickname", "ニックネーム", "VARCHAR", "", "", "", ""),
                new WebClientTestGetDTO("agenda_solution_company", "所属(事業者・団体等）", "VARCHAR", "", "", "", ""),
                new WebClientTestGetDTO("agenda_solution_activity_area", "希望活動地域", "VARCHAR", "", "", "", "")
                ));
    }

    @GetMapping ("/webflux/get")
    public ResponseEntity<List<WebClientTestGetDTO>> getMethodTest() {

        String uri = "http://localhost:8080/webflux/sample-api-get-list";

        Mono<List<WebClientTestGetDTO>> response = webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});

        List<WebClientTestGetDTO> list = response.block();

        return ResponseEntity.ok(list);
    }


    @GetMapping ("/webflux/get-mono")
    public Mono<List<WebClientTestGetDTO>> getMethodTestMono() {

        String uri = "http://localhost:8080/webflux/sample-api-get-list";
//        String uri = "https://n1xozv1mki.execute-api.ap-northeast-1.amazonaws.com/test/headerlist?TemplateID=3002";

        Mono<List<WebClientTestGetDTO>> listMono =  webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});

        System.out.println(listMono);

        return listMono;
    }

    @GetMapping ("/rest-temp/get")
    public String getMethodTestRestTemplate() {

//        String uri = "https://n1xozv1mki.execute-api.ap-northeast-1.amazonaws.com/test/headerlist?TemplateID=3002";
        String uri = "https://5rradn1k5i.execute-api.ap-northeast-1.amazonaws.com/test/userauthinfos/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImZ1SktmYXU2OF8wR3dTSjB3Z1hiMCJ9.eyJodHRwczovL2VtYWlsIjoiZnB0ZGVtby50ZXN0LjAyQGdtYWlsLmNvbSIsImh0dHBzOi8vVVNFUl9UTVBfSUQiOjQzODgwNiwiaXNzIjoiaHR0cHM6Ly90cG4taHNqLWRldi5hdS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NDY5IiwiYXVkIjpbImh0dHBzOi8vYXdzLWp3dC1hdXRob3JpemVyIiwiaHR0cHM6Ly90cG4taHNqLWRldi5hdS5hdXRoMC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNjkwMDMwMDU2LCJleHAiOjE2OTAxMTY0NTYsImF6cCI6InB5bGIzMVQ2b21pMU5iSmxrcHVvMlpJSGRSaFFmMWM0Iiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCJ9.WUyq58VIb3a9lCHC4SEy7q3QFZz0lbtPrguepAEHseQn_hPSaAASs1gGw3cwXvYpQhoQ0d3wvs6KzjtQfVuPHnCTQwT9V5qMmLCbswzDYpAs_1HOPGeedMCPZowJmToZQ7ErYp7ei2rM_18wYiIvZ7c8ANlmoJnXoxI79Nwrr82ZHrklUTbwq-rNHsYVgD1mQ8G5j67YfJwohWWbwH02kjpuj-xEEvrWSC8DF4qnmo4LC7YJg052BsWxS7tqGllNTo1IYNQCYKWTuxyxLVB-puqaNTgh14uzsu_CWw5jc4VWHVeKoBu2k65SBp0QsWdAsu2p-oo3rokiKfVxtui6lw");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return new RestTemplate()
                .exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<String>() {})
                .getBody();
    }
}
