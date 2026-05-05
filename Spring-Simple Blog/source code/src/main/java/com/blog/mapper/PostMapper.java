package com.blog.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blog.vo.Post;

public class PostMapper implements RowMapper<Post> {

	@Override
	public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
		Post post = new Post();
		
		post.setId(rs.getLong("id"));
		post.setUser(rs.getString("user"));
		post.setTitle(rs.getString("title"));
		post.setContent(rs.getString("content"));
		if(rs.getTimestamp("reg_date") != null) {
			post.setRegDate(rs.getTimestamp("reg_date").toLocalDateTime());
		}
		if(rs.getTimestamp("updt_date") != null) {
			post.setUpdtDate(rs.getTimestamp("updt_date").toLocalDateTime());
		}
		
		return post;
	}

}
