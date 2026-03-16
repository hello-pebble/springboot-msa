package com.pebble.auth.oauth;

import com.pebble.auth.client.UserClient;
import com.pebble.auth.client.dto.FindOrCreateUserRequest;
import com.pebble.auth.client.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserClient userClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName(); // The 'sub' claim for Google

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        FindOrCreateUserRequest request = FindOrCreateUserRequest.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .displayName(name)
                .profileImageUrl(picture)
                .build();

        ResponseEntity<UserResponseDto> response = userClient.findOrCreateUser(request);
        UserResponseDto userDto = response.getBody();

        if (userDto == null) {
            throw new OAuth2AuthenticationException("Failed to find or create user.");
        }

        PrincipalDetails principalDetails = new PrincipalDetails(userDto.getId(), userDto.getUsername());
        principalDetails.setAttributes(attributes);

        return principalDetails;
    }
}
