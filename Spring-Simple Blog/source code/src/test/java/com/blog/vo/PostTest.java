package com.blog.vo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    @Test
    void testEmptyConstructor() {
        Post post = new Post();
        assertNull(post.getId());
    }

    @Test
    void testConstructorWithUserTitleContent() {
        Post post = new Post("Hisyam", "Judul Test", "Isi Konten");
        assertEquals("Hisyam", post.getUser());
        assertEquals("Judul Test", post.getTitle());
        assertEquals("Isi Konten", post.getContent());
        assertNotNull(post.getRegDate());
        assertNotNull(post.getUpdtDate());
    }

    @Test
    void testConstructorWithIdUserTitleContent() {
        Post post = new Post(1L, "Hisyam", "Judul", "Konten");
        assertEquals(1L, post.getId());
        assertEquals("Hisyam", post.getUser());
    }

    @Test
    void testConstructorWithIdTitleContent() {
        Post post = new Post(2L, "Judul Baru", "Konten Baru");
        assertEquals(2L, post.getId());
        assertEquals("Judul Baru", post.getTitle());
    }

    @Test
    void testSettersAndGetters() {
        Post post = new Post();
        post.setId(10L);
        post.setUser("Admin");
        post.setTitle("Title");
        post.setContent("Content");

        assertEquals(10L, post.getId());
        assertEquals("Admin", post.getUser());
        assertEquals("Title", post.getTitle());
        assertEquals("Content", post.getContent());
    }
}