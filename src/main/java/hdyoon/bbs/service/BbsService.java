package hdyoon.bbs.service;

import hdyoon.bbs.dto.BbsPostDto;
import hdyoon.bbs.model.Post;
import hdyoon.bbs.repository.PostRepository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BbsService {

    private final PostRepository repository;

    // 쓰기 작업 - writer 데이터소스 사용
    @Transactional
    public String createPost(BbsPostDto postDto) {
        // 제목, 내용, 암호 검증
        if (postDto.getTitle() == null || postDto.getTitle().trim().isEmpty()) {
            return "제목을 입력해주세요.";
        }

        if (postDto.getContent() == null || postDto.getContent().trim().isEmpty()) {
            return "내용을 입력해주세요.";
        }

        if (postDto.getPassword() == null || postDto.getPassword().trim().isEmpty()) {
            return "암호를 입력해주세요.";
        }

        // Post 엔티티 생성 및 저장
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setPassword(postDto.getPassword());

        repository.save(post);

        return "게시글이 성공적으로 등록되었습니다.";
    }

    // 읽기 작업 - reader 데이터소스 사용
    @Transactional(readOnly = true)
    public String validatePassword(Long postId, String password) {
        Optional<Post> entity = repository.findById(postId);

        if (entity.isPresent() && entity.get().getPassword().equals(password)) {
            return "인증 성공";
        }
        return "암호가 일치하지 않습니다.";
    }

    // 추가 읽기 메서드들 예시
    @Transactional(readOnly = true)
    public Optional<Post> findById(Long postId) {
        return repository.findById(postId);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllPosts() {
        return repository.findAll();
    }

    // 수정 작업 - writer 데이터소스 사용
    @Transactional
    public String updatePost(Long postId, BbsPostDto postDto) {
        Optional<Post> entity = repository.findById(postId);

        if (entity.isEmpty()) {
            return "게시글을 찾을 수 없습니다.";
        }

        Post post = entity.get();

        if (!post.getPassword().equals(postDto.getPassword())) {
            return "암호가 일치하지 않습니다.";
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        repository.save(post);

        return "게시글이 성공적으로 수정되었습니다.";
    }

    // 삭제 작업 - writer 데이터소스 사용
    @Transactional
    public String deletePost(Long postId, String password) {
        Optional<Post> entity = repository.findById(postId);

        if (entity.isEmpty()) {
            return "게시글을 찾을 수 없습니다.";
        }

        if (!entity.get().getPassword().equals(password)) {
            return "암호가 일치하지 않습니다.";
        }

        repository.deleteById(postId);
        return "게시글이 성공적으로 삭제되었습니다.";
    }
}