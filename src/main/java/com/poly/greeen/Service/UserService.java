package com.poly.greeen.Service;

import com.poly.greeen.Entity.Users;
import com.poly.greeen.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    // Lấy người dùng hiện tại
    public Optional<Users> getCurrentUser(String username) {
        return userRepository.findByEmailOrPhone(username, username);
    }

    // Tìm người dùng theo email hoặc số điện thoại
    public Optional<Users> findByEmailOrPhone(String emailOrPhone) {
        return userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone);
    }

    // Tạo mới người dùng
    public Users createUser(Users user) {
        user.setUserID(userRepository.getNextUserId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Lấy tất cả người dùng
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy người dùng theo ID
    public Optional<Users> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Cập nhật thông tin người dùng
    public Users updateUser(Integer id, Users userDetails) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setAddress(userDetails.getAddress());
        user.setCity(userDetails.getCity());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    // Cập nhật mật khẩu người dùng
    public Users updatePassword(Users user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    // Xóa người dùng theo ID
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}