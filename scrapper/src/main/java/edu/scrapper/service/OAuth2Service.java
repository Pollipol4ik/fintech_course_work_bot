package edu.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    private final WebClient webClient;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public String refreshAccessToken(OAuth2AuthenticationToken authentication) {
        OAuth2AccessToken refreshToken = getRefreshToken(authentication);

        String response = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .bodyValue("client_id=" + clientId + "&client_secret=" + clientSecret + "&refresh_token=" + refreshToken.getTokenValue() + "&grant_type=refresh_token")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

    private OAuth2AccessToken getRefreshToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        return client.getAccessToken();
    }
}
