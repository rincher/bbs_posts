package hdyoon.bbs.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import hdyoon.bbs.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@DisplayName("PostRepository Test")
class PostRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private PostRepository postRepository;

  private Post testPost;

  @BeforeEach
  void setUp() {
    testPost = new Post();
    testPost.setTitle("테스트 제목");
    testPost.setContent("테스트 내용");
    testPost.setPassword("1234");
    testPost.setCreatedAt(LocalDateTime.now());
  }

  @Test
  @DisplayName("게시글 저장 및 조회")
  void saveAndFindPost() {
    Post savedPost = postRepository.save(testPost);
    assertNotNull(savedPost.getId());
    assertEquals("테스트 제목", savedPost.getTitle());
    assertEquals("테스트 테스트", savedPost.getContent());
    assertEquals("1234", savedPost.getPassword());
    assertNotNull(savedPost.getCreatedAt());
  }

  @Test
  @DisplayName("제목 검색")
  void findByTitleContaining() {
    entityManager.persistAndFlush(testPost);

    List<Post> posts = postRepository.findByTitleContaining("테스트");

    assertEquals(1, posts.size());
    assertEquals("테스트 제목", posts.get(0).getTitle());
  }

  @Test
  @DisplayName("내용 검색")
  void findByContentContaining() {
    entityManager.persistAndFlush(testPost);

    List<Post> posts = postRepository.findByContentContaining("내용");

    assertEquals(1, posts.size());
    assertEquals("테스트 내용", posts.get(0).getContent());
  }
}
