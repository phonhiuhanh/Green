package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmailOrPhone(String email, String phone);

    @Query("SELECT COALESCE(MAX(u.userID), 0) + 1 FROM Users u")
    int getNextUserId();
}
