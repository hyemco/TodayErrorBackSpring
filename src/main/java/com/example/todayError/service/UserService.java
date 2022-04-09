package com.example.todayError.service;

import com.example.todayError.domain.User;
import com.example.todayError.dto.UserDto.SignupRequestDto;
import com.example.todayError.exceptionHandler.CustomException;
import com.example.todayError.exceptionHandler.ErrorCode;
import com.example.todayError.repository.UserRepository;
import com.example.todayError.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void resisterUser(SignupRequestDto signupRequestDto) {

        String nickname = signupRequestDto.getNickname();

        if (UserValidator.validateUserRegister(signupRequestDto)) {

            // 중복된 닉네임일 때 예외 처리
            if ( userRepository.existsByNickname(nickname)) {
                throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
            }

            // password 암호화
            String password = passwordEncoder.encode(signupRequestDto.getPassword());
            User user = new User(nickname, password);
            userRepository.save(user);
        }
    }
    // 닉네임 중복 확인
    public boolean nicknameCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}