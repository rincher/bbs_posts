package hdyoon.bbs.controller;

import hdyoon.bbs.dto.BbsPostDto;
import hdyoon.bbs.service.BbsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BbsController {

  private final BbsService bbsService;

  @PostMapping("/append")
  public String createPost(@RequestBody BbsPostDto postDto) {
    return bbsService.createPost(postDto);
  }

  @GetMapping("/validate")
  public String validatePassword(
    @RequestParam Long postId,
    @RequestParam String password
  ) {
    return bbsService.validatePassword(postId, password);
  }
}
