package org.programmers.kdtboard.service;

import static java.text.MessageFormat.*;

import org.programmers.kdtboard.controller.response.ErrorCodeMessage;
import org.programmers.kdtboard.converter.Converter;
import org.programmers.kdtboard.domain.post.Post;
import org.programmers.kdtboard.domain.post.PostRepository;
import org.programmers.kdtboard.dto.PostDto.PostCreateRequest;
import org.programmers.kdtboard.dto.PostDto.PostResponse;
import org.programmers.kdtboard.dto.PostDto.PostUpdateRequest;
import org.programmers.kdtboard.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserService userService;
	private final Converter converter;

	public PostService(PostRepository postRepository,
		UserService userService, Converter converter) {
		this.postRepository = postRepository;
		this.userService = userService;
		this.converter = converter;
	}

	public PostResponse create(PostCreateRequest postCreateRequest) {
		var post = Post.create(postCreateRequest.title(), postCreateRequest.content());
		var userResponseDto = this.userService.findById(postCreateRequest.userId());
		post.setUser(this.converter.userDtoConverter(userResponseDto));
		this.postRepository.save(post);

		return this.converter.postConverter(post);
	}

	public PostResponse findById(Long id) {
		var post = this.postRepository.findById(id)
			.orElseThrow(
				() -> new EntityNotFoundException(format("post id : {0}, 없는 id 입니다.", id),
					ErrorCodeMessage.POST_ID_NOT_FOUND));

		return this.converter.postConverter(post);
	}

	public Page<PostResponse> findAll(Pageable pageable) {
		return this.postRepository.findAll(pageable).map(this.converter::postConverter);
	}

	public PostResponse update(Long id, PostUpdateRequest postUpdateRequest) {
		var post = this.postRepository.findById(id)
			.orElseThrow(() ->
				new EntityNotFoundException(format("post id : {0}, 없는 id 입니다.", id),
					ErrorCodeMessage.POST_ID_NOT_FOUND));
		post.update(postUpdateRequest.title(), postUpdateRequest.content());

		return this.converter.postConverter(post);
	}
}
