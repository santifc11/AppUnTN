package utn.TpFinal.AppUnTN.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.model.Commentary;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.CommentaryService;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @PostMapping("/document/{documentId}")
    public Commentary comment(@PathVariable Long documentId,
                              @RequestBody String content,
                              @AuthenticationPrincipal User user) {
        return commentaryService.addComment(documentId, content, user);
    }
}
