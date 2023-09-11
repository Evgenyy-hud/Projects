package com.smarthomeweb.smarthome.repository;

import com.smarthomeweb.smarthome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);//поиск пользователя в базе данных по его имени


}
