package ureca.ureca_miniproject2.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.util.exception.custom.UserNotFoundException;
import ureca.ureca_miniproject2.util.response.FailureMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResponse createPost(PostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .likeCnt(0)
                .viewCnt(0)
                .reportCnt(0)
                .createdAt(LocalDateTime.now())
                .state(PostState.SALE)
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .toList();
    }

    @Override
    public PostResponse getPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        return PostResponse.from(post);
    }

    @Override
    public PostResponse updatePost(Integer postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        post.update(request.title(),
                request.content(),
                request.price(),
                request.imageUrl(),
                request.state());

        return PostResponse.from(post);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        postRepository.delete(post);
    }
}
