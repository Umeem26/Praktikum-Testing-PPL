package com.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) untuk menerima input pembuatan/update Post.
 *
 * Menggunakan POJO terpisah dari entity JPA (Post) agar:
 * 1. Mencegah mass-assignment vulnerability (tidak expose kolom internal entity).
 * 2. Memisahkan tanggung jawab: DTO untuk input, Entity untuk persistensi.
 * 3. Menghilangkan peringatan SonarQube "Replace persistent entity with POJO/DTO".
 */
public class PostRequest extends BaseUserRequest {

    private Long id;



    @NotBlank(message = "Judul post tidak boleh kosong")
    @Size(max = 200, message = "Judul maksimal 200 karakter")
    private String title;

    @NotBlank(message = "Konten post tidak boleh kosong")
    private String content;

    public PostRequest() {
        // Default constructor required by Jackson for JSON deserialization
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
