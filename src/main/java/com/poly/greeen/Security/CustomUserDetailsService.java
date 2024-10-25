package com.poly.greeen.Security;

import com.poly.greeen.Repository.CustomerRepository;
import com.poly.greeen.Repository.UsersRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    public static final Map<String, String> ROLES = Map.of(
            "ROLE_MANAGER", "Quản lý",
            "ROLE_CUSTOMER", "Khách hàng",
            "ROLE_STAFF", "Nhân viên"
    );
    private final CustomerRepository customerRepository;
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var customer = customerRepository.findByEmailOrPhone(username, username).orElse(null);
        if (customer != null) {
            var authUser = AuthUser.builder()
                    .uniqueId(username)
                    .role(ROLES.get("ROLE_CUSTOMER"))
                    .name(customer.getUsername())
                    .password(customer.getPassword())
                    .build();
            return CustomUserDetails.builder()
                    .authUser(authUser)
                    .build();
        }
        var user = usersRepository.findByEmailOrPhone(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
        if (user != null) {
            var authUser = AuthUser.builder()
                    .uniqueId(username)
                    .role(user.getRole().getRoleName())
                    .name(user.getUsername())
                    .password(user.getPassword())
                    .build();
            return CustomUserDetails.builder()
                    .authUser(authUser)
                    .build();
        }
        return null;
    }
}
