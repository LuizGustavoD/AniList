package com.anilist.backend.server.controller.profile;


import org.springframework.http.ResponseEntity;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.anilist.backend.server.DTO.profile.UserFriendshipRequestDTO;
import com.anilist.backend.server.service.profile.UserFriendshipService;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Amizade", description = "Operações de amizade entre usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile/friendship")
public class UsersFriendshipController {

    private final UserFriendshipService userFriendshipService;
    

    @Operation(summary = "Enviar solicitação de amizade", description = "Envia uma solicitação de amizade para outro usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitação enviada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "usersFriendshipLimiter")
    @PostMapping("/send/request")
    public ResponseEntity<?> sendFriendRequest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da solicitação de amizade", required = true,
                content = @Content(schema = @Schema(implementation = UserFriendshipRequestDTO.class)))
            @RequestBody UserFriendshipRequestDTO request) {
        SuccessAPIResponse<Void> response = userFriendshipService.userFriendshipRequestSend(request);
        return ResponseEntity.ok(response);
    }
    
}
