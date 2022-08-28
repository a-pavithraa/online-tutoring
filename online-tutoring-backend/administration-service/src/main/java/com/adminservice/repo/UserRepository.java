package com.adminservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Users;

public interface UserRepository extends JpaRepository<Users,Long> {

}
