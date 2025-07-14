package com.bankcards.service;


import com.bankcards.dto.AuthRequest;
import com.bankcards.dto.UserDto;
import com.bankcards.entity.Role;
import com.bankcards.entity.User;
import com.bankcards.exception.RoleNotFoundException;
import com.bankcards.exception.UserIdNotFoundException;
import com.bankcards.repository.RoleRepository;
import com.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtos = userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    public ResponseEntity<UserDto> getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException(id));
        return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> update(long id, UserDto userDto) {
        userRepository.findById(id).orElseThrow(() -> new UserIdNotFoundException(id));
        userRepository.save(modelMapper.map(userDto, User.class));
        return new ResponseEntity<>("User updated!",HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> delete(long id) {
        userRepository.findById(id).orElseThrow(() -> new UserIdNotFoundException(id));
        userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> create(AuthRequest authRequest) {
        User user = modelMapper.map(authRequest, User.class);

        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Default role USER not found"));
        user.setRoles(Set.of(defaultRole));

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        return new ResponseEntity<>("User has been created fine!",HttpStatus.CREATED);
    }
}
