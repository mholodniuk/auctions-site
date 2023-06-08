package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.benchmark.TrackExecutionTime;
import com.pwr.auctionsite.data.dao.UserDAO;
import com.pwr.auctionsite.data.dao.UserRepository;
import com.pwr.auctionsite.data.dto.NewAddressDTO;
import com.pwr.auctionsite.data.dto.NewUserDTO;
import com.pwr.auctionsite.data.dto.views.UserInfoDTO;
import com.pwr.auctionsite.data.entity.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final UserDAO userDAO;

    public List<UserInfoDTO> findUsersPaged(String username, int offset, int limit) {
        return userDAO.findAllPaged(username, offset, limit);
    }

    @Transactional
    @TrackExecutionTime
    public void store(NewUserDTO userDTO, NewAddressDTO addressDTO) {
        var address = addressService.store(addressDTO);
        var user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setBlocked(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        user.setRole(User.Role.USER);
        user.setAddress(address);

        userRepository.save(user);
    }

    public void deleteById(Long usedId) {
        userRepository.deleteById(usedId);
    }

    @Transactional
    public void setUserBlocked(Long userId, boolean block) {
        userRepository.setBlockedUser(userId, block);
    }
}
