package com.blog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import com.blog.repository.CommentJpaRepository;
import com.blog.vo.Comment;

public class CommentServiceTest {

    @Mock
    CommentJpaRepository commentJpaRepository;

    @InjectMocks
    CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCommentSuccess() {
        Comment comment = new Comment(1L, "Hisyam", "Komen");
        when(commentJpaRepository.save(comment)).thenReturn(comment);
        assertTrue(commentService.saveComment(comment));
    }

    @Test
    void testSaveCommentFail() {
        Comment comment = new Comment();
        when(commentJpaRepository.save(comment)).thenReturn(null);
        assertFalse(commentService.saveComment(comment));
    }

    @Test
    void testGetCommentList() {
        when(commentJpaRepository.findAllByPostIdOrderByRegDateDesc(1L))
            .thenReturn(Arrays.asList(new Comment(), new Comment()));
        assertEquals(2, commentService.getCommentList(1L).size());
    }

    @Test
    void testSearchCommentList() {
        when(commentJpaRepository.findByPostIdAndCommentContainingOrderByRegDateDesc(1L, "Test"))
            .thenReturn(Arrays.asList(new Comment()));
        assertEquals(1, commentService.searchCommentList(1L, "Test").size());
    }

    @Test
    void testGetComment() {
        when(commentJpaRepository.findOneById(1L)).thenReturn(new Comment(1L, "Hisyam", "Komen"));
        assertNotNull(commentService.getComment(1L));
    }

    @Test
    void testDeleteCommentSuccess() {
        when(commentJpaRepository.findOneById(1L)).thenReturn(new Comment());
        assertTrue(commentService.deleteComment(1L));
        verify(commentJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCommentFail() {
        when(commentJpaRepository.findOneById(2L)).thenReturn(null);
        assertFalse(commentService.deleteComment(2L));
    }
}