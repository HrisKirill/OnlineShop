package com.khrystoforov.onlineshopapp.repository;

import com.khrystoforov.onlineshopapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
