package com.qatar.bank.dto;

import java.util.List;

import com.qatar.bank.constants.Constants;
import com.qatar.bank.entity.Post;

public class PostDTO {
	
	private List<Post> posts;
	private long total;
	private long page;
	private boolean isFirst;
	private boolean isLast;

	public List<Post> getPosts() {
		return posts;
	}

	public long getTotal() {
		return total;
	}
	public long getPage() {
		return page;
	}
	public boolean isFirst() {
		return isFirst;
	}
	public boolean isLast() {
		return isLast;
	}
	public PostDTO(List<Post> posts, long total, long page) {
		super();
		this.posts = posts;
		this.total = total;
		this.page = page;
		if(total <= Constants.LIMIT * page){
			this.isLast = true;
		}
		if(page == 1){
			this.isFirst = true;
		}
	}
	
	
}
