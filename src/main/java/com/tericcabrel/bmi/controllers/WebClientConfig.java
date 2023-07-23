//package com.tericcabrel.bmi.controllers;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class WebClientConfig {
//
//    @Bean
//    public WebClient webClient() {
//        return WebClient.builder()
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
//}

package com.tericcabrel.bmi.controllers;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {

        HttpClient httpClient = HttpClient.create()
                .resolver(spec -> spec.queryTimeout(Duration.ofSeconds(5)));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        final RestTemplate restTemplate = new RestTemplate();
//
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//        messageConverters.add(converter);
//        restTemplate.setMessageConverters(messageConverters);
//
//        return restTemplate;
//    }


}