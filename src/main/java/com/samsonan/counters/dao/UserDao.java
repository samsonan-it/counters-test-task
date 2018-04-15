package com.samsonan.counters.dao;

import com.samsonan.counters.domain.User;

public interface UserDao {

    User findByName(String username);

}