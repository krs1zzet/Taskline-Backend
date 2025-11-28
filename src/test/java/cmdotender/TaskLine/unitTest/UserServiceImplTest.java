package cmdotender.TaskLine.unitTest;

import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.mapper.UserMapper;
import cmdotender.TaskLine.features.user.repository.UserRepository;
import cmdotender.TaskLine.features.user.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private String username;
    private User savedUser;
    private UserDTO expectedDto;

    @BeforeEach
    void setUp() {
        username = "testuser";

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);

        expectedDto = new UserDTO();
        expectedDto.setId(1L);
        expectedDto.setUsername(username);
    }

    @Test
    void saveUser_shouldReturnSavedUserDTO() {
        // arrange
        when(userRepository.save(argThat(u -> username.equals(u.getUsername()))))
                .thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(expectedDto);

        // act
        UserDTO result = userService.save(username);

        // assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedDto.getId(), result.getId());
        Assertions.assertEquals(expectedDto.getUsername(), result.getUsername());

        verify(userRepository).save(argThat(u -> username.equals(u.getUsername())));
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    void findById_shouldReturnUserDTO() {
        // arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.ofNullable(savedUser));
        when(userMapper.toDTO(savedUser)).thenReturn(expectedDto);

        // act
        UserDTO result = userService.findById(userId);

        // assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedDto.getId(), result.getId());
        Assertions.assertEquals(expectedDto.getUsername(), result.getUsername());

        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(savedUser);
    }



    @Test
    void findAll_shouldReturnPagedUserDTOs() {
        // arrange
        var pageable = PageRequest.of(0, 10);
        var userPage = new PageImpl<User>(java.util.List.of(savedUser), pageable, 1);
        var expectedDtoPage = new PageImpl<UserDTO>(java.util.List.of(expectedDto), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toDTO(savedUser)).thenReturn(expectedDto);

        // act
        var resultPage = userService.findAll(pageable);

        // assert
        Assertions.assertNotNull(resultPage);
        Assertions.assertEquals(1, resultPage.getTotalElements());
        Assertions.assertEquals(expectedDto.getId(), resultPage.getContent().get(0).getId());
        Assertions.assertEquals(expectedDto.getUsername(), resultPage.getContent().get(0).getUsername());
        verify(userRepository).findAll(pageable);
        verify(userMapper).toDTO(savedUser);
    }




}
