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

import com.blog.repository.PostJpaRepository;
import com.blog.vo.Post;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Kita mock Repository-nya agar tidak perlu konek ke database asli saat testing
    @MockBean
    private PostJpaRepository jpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetPostsIntegration() throws Exception {
        // Skenario 1: Integrasi Controller + Service untuk mengambil semua Post
        Post mockPost = new Post(1L, "Hisyam", "Judul Integrasi", "Konten Integrasi");
        when(jpaRepository.findAllByOrderByUpdtDateDesc()).thenReturn(Arrays.asList(mockPost));

        mockMvc.perform(get("/posts"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Judul Integrasi"));
    }

    @Test
    public void testGetPostByIdIntegration() throws Exception {
        // Skenario 2: Integrasi Controller + Service untuk mengambil Post berdasarkan ID
        Post mockPost = new Post(1L, "Hisyam", "Test Get ID", "Content");
        when(jpaRepository.findOneById(1L)).thenReturn(mockPost);

        mockMvc.perform(get("/post").param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Test Get ID"));
    }

    @Test
    public void testSavePostIntegration() throws Exception {
        // Skenario 3: Integrasi Controller + Service untuk menyimpan Post baru
        Post newPost = new Post("Hisyam", "Judul Baru", "Isi Baru");
        when(jpaRepository.save(any(Post.class))).thenReturn(newPost);

        mockMvc.perform(post("/post")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(newPost)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value(200));
    }

    @Test
    public void testDeletePostIntegration() throws Exception {
        // Skenario 4: Integrasi Controller + Service untuk menghapus Post
        Post mockPost = new Post(1L, "Hisyam", "Hapus Ini", "Konten");
        when(jpaRepository.findOneById(1L)).thenReturn(mockPost);

        mockMvc.perform(delete("/post").param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value(200));
    }

    @Test
    public void testGetPostsOrderByUpdtAscIntegration() throws Exception {
        // Skenario 5: Mengambil Post urut tanggal update ASC
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(new Post())); // Mock simpel
        mockMvc.perform(get("/posts/updtdate/asc"))
               .andExpect(status().isOk());
    }

    @Test
    public void testGetPostsOrderByRegDescIntegration() throws Exception {
        // Skenario 6: Mengambil Post urut tanggal registrasi DESC
        mockMvc.perform(get("/posts/regdate/desc"))
               .andExpect(status().isOk());
    }

    @Test
    public void testSearchByTitleIntegration() throws Exception {
        // Skenario 7: Mencari Post berdasarkan Judul
        Post mockPost = new Post(1L, "Hisyam", "Cari Judul", "Konten");
        when(jpaRepository.findByTitleContainingOrderByUpdtDateDesc("Cari")).thenReturn(Arrays.asList(mockPost));

        mockMvc.perform(get("/posts/search/title").param("query", "Cari"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Cari Judul"));
    }

    @Test
    public void testSearchByContentIntegration() throws Exception {
        // Skenario 8: Mencari Post berdasarkan Konten
        mockMvc.perform(get("/posts/search/content").param("query", "Konten"))
               .andExpect(status().isOk());
    }

    @Test
    public void testModifyPostIntegration() throws Exception {
        // Skenario 9: Mengubah (Update) Post
        // Catatan: Setelah Perfective Action Fase 3, field 'user' wajib diisi
        // karena ada validasi @NotBlank. Constructor Post(id, title, content)
        // tidak menyertakan user, sehingga perlu di-set manual.
        Post updateData = new Post(1L, "Judul Baru", "Isi Baru");
        updateData.setUser("Hisyam"); // Wajib diisi agar lolos validasi @NotBlank
        when(jpaRepository.findOneById(1L)).thenReturn(updateData);
        when(jpaRepository.save(any(Post.class))).thenReturn(updateData);

        mockMvc.perform(put("/post")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(updateData)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value(200));
    }
}