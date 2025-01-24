package com.example.mimir.repositories;

import com.example.mimir.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// JPARepository its the final interface that implements CrudRepository and ListingPagingSortingRespository
// https://pt.stackoverflow.com/questions/4088/utilizando-o-reposit%C3%B3rio-do-jpa
public interface UserRepository extends JpaRepository<User, UUID> {
}
