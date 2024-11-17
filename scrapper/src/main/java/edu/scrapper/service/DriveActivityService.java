package edu.scrapper.service;

import edu.scrapper.dto.DriveActivityRequestDto;
import edu.scrapper.dto.DriveActivityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class DriveActivityService {

    private final WebClient webClient;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public DriveActivityResponseDto getActivity(DriveActivityRequestDto requestDto, Authentication authentication) {
        OAuth2AccessToken accessToken = getAccessToken(authentication);

        return webClient.post()
                .uri("https://driveactivity.googleapis.com/v2/activity:query")
                .header("Authorization", "Bearer " + accessToken.getTokenValue()) // Добавляем токен в заголовок запроса
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(DriveActivityResponseDto.class)
                .block();
    }

    private OAuth2AccessToken getAccessToken(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        return client.getAccessToken();
    }
}
