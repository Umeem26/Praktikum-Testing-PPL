package com.blog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import com.blog.repository.PostJpaRepository;
import com.blog.repository.PostRepository;
import com.blog.vo.Post;

public class PostServiceTest {

    @Mock
    PostJpaRepository jpaRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPost() {
        Post mockPost = new Post(1L, "Hisyam", "Title", "Content");
        when(jpaRepository.findOneById(1L)).thenReturn(mockPost);

        Post result = postService.getPost(1L);
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    void testGetPosts() {
        List<Post> mockList = Arrays.asList(new Post(), new Post());
        when(jpaRepository.findAllByOrderByUpdtDateDesc()).thenReturn(mockList);

        List<Post> result = postService.getPosts();
        assertEquals(2, result.size());
    }

    @Test
    void testGetPostsOrderByUpdtAsc() {
        when(postRepository.findPostOrderByUpdtDateAsc()).thenReturn(Arrays.asList(new Post()));
        assertEquals(1, postService.getPostsOrderByUpdtAsc().size());
    }

    @Test
    void testGetPostsOrderByRegDesc() {
        when(postRepository.findPostOrderByRegDateDesc()).thenReturn(Arrays.asList(new Post()));
        assertEquals(1, postService.getPostsOrderByRegDesc().size());
    }

    @Test
    void testSearchPostByTitle() {
        when(jpaRepository.findByTitleContainingOrderByUpdtDateDesc("Test")).thenReturn(Arrays.asList(new Post()));
        assertEquals(1, postService.searchPostByTitle("Test").size());
    }

    @Test
    void testSearchPostByContent() {
        when(jpaRepository.findByContentContainingOrderByUpdtDateDesc("Content")).thenReturn(Arrays.asList(new Post()));
        assertEquals(1, postService.searchPostByContent("Content").size());
    }

    @Test
    void testSavePostSuccess() {
        Post post = new Post("Hisyam", "Title", "Content");
        when(jpaRepository.save(post)).thenReturn(post);
        assertTrue(postService.savePost(post));
    }

    @Test
    void testSavePostFail() {
        Post post = new Post();
        when(jpaRepository.save(post)).thenReturn(null);
        assertFalse(postService.savePost(post));
    }

    @Test
    void testDeletePostSuccess() {
        when(jpaRepository.findOneById(1L)).thenReturn(new Post());
        assertTrue(postService.deletePost(1L));
        verify(jpaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePostFail() {
        when(jpaRepository.findOneById(2L)).thenReturn(null);
        assertFalse(postService.deletePost(2L));
    }

    @Test
    void testUpdatePostSuccess() {
        Post existingPost = new Post(1L, "Old Title", "Old Content");
        Post updateData = new Post(1L, "New Title", "New Content");
        
        when(jpaRepository.findOneById(1L)).thenReturn(existingPost);
        assertTrue(postService.updatePost(updateData));
        assertEquals("New Title", existingPost.getTitle());
    }

    @Test
    void testUpdatePostFail() {
        Post updateData = new Post(99L, "Title", "Content");
        when(jpaRepository.findOneById(99L)).thenReturn(null);
        assertFalse(postService.updatePost(updateData));
    }
}