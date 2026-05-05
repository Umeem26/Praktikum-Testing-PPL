package com.blog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.blog.repository.CommentJpaRepository;
import com.blog.vo.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCommentsIntegration() throws Exception {
        // Skenario 1: Mengambil list komentar berdasarkan post_id
        Comment mockComment = new Comment(1L, "Hisyam", "Komentar Pertama");
        when(commentJpaRepository.findAllByPostIdOrderByRegDateDesc(1L)).thenReturn(Arrays.asList(mockComment));

        mockMvc.perform(get("/comments").param("post_id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].user").value("Hisyam"));
    }

    @Test
    public void testGetSingleCommentIntegration() throws Exception {
        // Skenario 2: Mengambil satu komentar berdasarkan id
        Comment mockComment = new Comment(1L, "Admin", "Test Komentar");
        when(commentJpaRepository.findOneById(1L)).thenReturn(mockComment);

        mockMvc.perform(get("/comment").param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.comment").value("Test Komentar"));
    }

    @Test
    public void testSaveCommentIntegration() throws Exception {
        // Skenario 3: Menyimpan komentar baru
        Comment newComment = new Comment(1L, "Hisyam", "Komentar Baru");
        when(commentJpaRepository.save(any(Comment.class))).thenReturn(newComment);

        mockMvc.perform(post("/comment")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(newComment)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value(200));
    }

    @Test
    public void testDeleteCommentIntegration() throws Exception {
        // Skenario 4: Menghapus komentar
        Comment mockComment = new Comment(1L, "Hisyam", "Hapus Komentar");
        when(commentJpaRepository.findOneById(1L)).thenReturn(mockComment);

        mockMvc.perform(delete("/comment").param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value(200));
    }

    @Test
    public void testSearchCommentsIntegration() throws Exception {
        // Skenario 5: Mencari Komentar
        Comment mockComment = new Comment(1L, "Hisyam", "Komen Ketemu");
        when(commentJpaRepository.findByPostIdAndCommentContainingOrderByRegDateDesc(1L, "Ketemu"))
            .thenReturn(Arrays.asList(mockComment));

        mockMvc.perform(get("/comments/search")
               .param("post_id", "1")
               .param("query", "Ketemu"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].comment").value("Komen Ketemu"));
    }

    @Test
    public void testSaveCommentFailIntegration() throws Exception {
        // Skenario 6: Gagal menyimpan komentar (Memancing Error 500)
        Comment newComment = new Comment(); // Data kosong
        when(commentJpaRepository.save(any(Comment.class))).thenReturn(null); // Simulasi gagal save

        mockMvc.perform(post("/comment")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(newComment)))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.result").value(500));
    }
}