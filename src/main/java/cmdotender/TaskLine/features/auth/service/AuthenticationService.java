package cmdotender.TaskLine.features.auth.service;

import cmdotender.TaskLine.features.auth.dto.AuthDTO;
import cmdotender.TaskLine.features.auth.dto.request.ChangePasswordRequest;
import cmdotender.TaskLine.features.auth.dto.request.SignInRequest;
import cmdotender.TaskLine.features.auth.dto.request.SignUpRequest;

public interface AuthenticationService {

    AuthDTO signUp(SignUpRequest request);
    AuthDTO signIn(SignInRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);

     AuthDTO loginWithOAuth(String provider, String externalId, String email, String accessToken);


}
