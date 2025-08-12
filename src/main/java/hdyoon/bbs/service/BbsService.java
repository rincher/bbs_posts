package hdyoon.bbs.service;

import hdyoon.bbs.dto.BbsPostDto;
import hdyoon.bbs.model.Post;
import hdyoon.bbs.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BbsService {

  private final PostRepository repository;

  public String createPost(BbsPostDto postDto) {
    // 제목, 내용, 암호 검증
    if (postDto.getTitle() == null || postDto.getTitle().trim().isEmpty()) {
      return "제목을 입력해주세요.";
    }

    if (postDto.getContent() == null || postDto.getContent().trim().isEmpty()) {
      return "내용을 입력해주세요.";
    }

    if (
      postDto.getPassword() == null || postDto.getPassword().trim().isEmpty()
    ) {
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

  public String validatePassword(Long postId, String password) {
    Optional<Post> entity = repository.findById(postId);

    if (entity.isPresent() && entity.get().getPassword().equals(password)) {
      return "인증 성공";
    }
    return "암호가 일치하지 않습니다.";
  }
}
