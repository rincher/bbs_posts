package hdyoon.bbs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.

@Suite
@SelectClasses(
  { BbsServiceTest.class, PostRepositoryTest.class, BbsControllerTest.class }
)
@DisplayName("BBS 애플리케이션 전체 테스트 스위트")
class BbsApplicationTestSuite {}
