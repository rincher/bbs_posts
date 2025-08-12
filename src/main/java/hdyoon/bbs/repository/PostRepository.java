package hdyoon.bbs.repository;

import hdyoon.bbs.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  // 제목으로 검색
  List<Post> findByTitleContaining(String title);

  // 내용으로 검색
  List<Post> findByContentContaining(String content);

  // 제목 또는 내용으로 검색
  List<Post> findByTitleContainingOrContentContaining(
    String title,
    String content
  );

  // 생성일 기준 정렬 (최신순)
  List<Post> findAllByOrderByCreatedAtDesc();

  // 특정 기간 내 게시글 조회
  List<Post> findByCreatedAtBetween(
    LocalDateTime startDate,
    LocalDateTime endDate
  );

  // 암호 검증용 (ID와 암호로 조회)
  @Query("SELECT p FROM Post p WHERE p.id = :id AND p.password = :password")
  Optional<Post> findByIdAndPassword(
    @Param("id") Long id,
    @Param("password") String password
  );

  // 페이징 처리용 - 최신순으로 특정 개수만 조회
  @Query(
    "SELECT p FROM Post p ORDER BY p.createdAt DESC LIMIT :limit OFFSET :offset"
  )
  List<Post> findPostsWithPaging(
    @Param("limit") int limit,
    @Param("offset") int offset
  );
}
