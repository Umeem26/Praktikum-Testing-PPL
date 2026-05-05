package com.blog.vo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    @Test
    void testConstructorsAndGettersSetters() {
        Comment comment = new Comment(1L, "Hisyam", "Ini komentar");
        comment.setId(100L);
        
        assertEquals(100L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("Hisyam", comment.getUser());
        assertEquals("Ini komentar", comment.getComment());
        assertNotNull(comment.getRegDate());
        
        Comment emptyComment = new Comment();
        emptyComment.setPostId(2L);
        emptyComment.setUser("Test");
        emptyComment.setComment("Isi");
        assertEquals(2L, emptyComment.getPostId());
    }
}