package utn.TpFinal.AppUnTN.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.model.Punctuation;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.PunctuationService;

@RestController
@RequestMapping("/api/punctuation")
@RequiredArgsConstructor
public class PunctuationController {

    private final PunctuationService punctuationService;

    @PostMapping("/document/{documentId}")
    public Punctuation rate(@PathVariable Long documentId,
                            @RequestParam int value,
                            @AuthenticationPrincipal User user) {
        return punctuationService.rate(documentId, value, user);
    }
}
