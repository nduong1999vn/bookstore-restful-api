package com.duonghn.springrolejwt.service;

import com.duonghn.springrolejwt.model.Role;

public interface RoleService {
    Role findByName(String name);
}
