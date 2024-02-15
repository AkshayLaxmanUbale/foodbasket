package com.demo.foodbasket.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.foodbasket.contracts.request.SignUpRequest;
import com.demo.foodbasket.entities.AccountStatus;
import com.demo.foodbasket.entities.Role;
import com.demo.foodbasket.entities.UserEntity;
import com.demo.foodbasket.models.AccountStatusDto;
import com.demo.foodbasket.models.RoleDto;
import com.demo.foodbasket.models.UserDto;
import com.demo.foodbasket.repositories.UserRespository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    EmailNotificationService notificationService;

    public UserDto getUserById(int id) {
        Optional<UserEntity> entity = userRespository.findById(id);
        return entity.isPresent() ? mapper.map(entity.get(), UserDto.class) : null;
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> userEntities = userRespository.findAll();

        return userEntities.stream().map(x -> mapper.map(x, UserDto.class)).toList();
    }

    public UserDto createUser(UserDto userDetails) {
        UserEntity userEntity = UserEntity.builder()
                .firstname(userDetails.getFirstname())
                .lastname(userDetails.getLastname())
                .dateOfBirth(userDetails.getDateOfBirth())
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .role(Role.SELLER)
                .status(AccountStatus.INCOMPLETE)
                .build();

        UserEntity insertedUser = userRespository.save(userEntity);

        // Send verification mail
        notificationService.sendEmailVerificationMail(insertedUser.getUsername());

        return mapper.map(insertedUser, UserDto.class);
    }

    public UserDto getUserByUsername(String username) {
        UserEntity userEntity = userRespository.findByUsername(username);
        return mapper.map(userEntity, UserDto.class);
    }

    public UserDto updateUser(int id, UserDto detailsToUpdate) {
        UserDto existingUser = getUserById(id);
        if (existingUser != null) {
            RoleDto roleToUpdate = detailsToUpdate.getRole() != null
                    && detailsToUpdate.getRole() != existingUser.getRole()
                            ? detailsToUpdate.getRole()
                            : existingUser.getRole();
            AccountStatusDto statusToUpdate = detailsToUpdate.getStatus() != null
                    && detailsToUpdate.getStatus() != existingUser.getStatus()
                            ? detailsToUpdate.getStatus()
                            : existingUser.getStatus();

            UserEntity userEntity = UserEntity.builder()
                    .id(existingUser.getId())
                    .username(existingUser.getUsername())
                    .firstname(detailsToUpdate.getFirstname() != null
                            ? detailsToUpdate.getFirstname()
                            : existingUser.getFirstname())
                    .lastname(detailsToUpdate.getLastname() != null ? detailsToUpdate.getLastname()
                            : existingUser.getLastname())
                    .dateOfBirth(detailsToUpdate.getDateOfBirth() != null
                            ? detailsToUpdate.getDateOfBirth()
                            : existingUser.getDateOfBirth())
                    .password(detailsToUpdate.getPassword() != null ? detailsToUpdate.getPassword()
                            : existingUser.getPassword())
                    .role(mapper.map(roleToUpdate, Role.class))
                    .status(mapper.map(statusToUpdate, AccountStatus.class))
                    .build();

            UserEntity updatedUser = userRespository.save(userEntity);
            return mapper.map(updatedUser, UserDto.class);
        }
        throw new EntityNotFoundException("User not found.");
    }

    public void deleteUserById(int id) {
        Optional<UserEntity> existingUser = userRespository.findById(id);
        if(existingUser!= null && existingUser.isPresent())
        {
            userRespository.deleteById(id);
        }
    }
}
