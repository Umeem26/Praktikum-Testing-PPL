package com.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) untuk menerima input pembuatan Comment.
 *
 * Menggunakan POJO terpisah dari entity JPA (Comment) agar:
 * 1. Mencegah mass-assignment vulnerability (tidak expose kolom internal entity).
 * 2. Memisahkan tanggung jawab: DTO untuk input, Entity untuk persistensi.
 * 3. Menghilangkan peringatan SonarQube "Replace persistent entity with POJO/DTO".
 */
public class CommentRequest {

    @NotNull(message = "Post ID tidak boleh kosong")
    private Long postId;

    @NotBlank(message = "Nama user tidak boleh kosong")
    @Size(max = 50, message = "Nama user maksimal 50 karakter")
    private String user;

    @NotBlank(message = "Komentar tidak boleh kosong")
    private String comment;

    public CommentRequest() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
