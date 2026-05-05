package com.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Base class untuk DTO yang memiliki field user.
 * Dibuat untuk menghindari code duplication (DRY principle) yang terdeteksi oleh SonarQube.
 */
public abstract class BaseUserRequest {

    @NotBlank(message = "Nama user tidak boleh kosong")
    @Size(max = 50, message = "Nama user maksimal 50 karakter")
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
