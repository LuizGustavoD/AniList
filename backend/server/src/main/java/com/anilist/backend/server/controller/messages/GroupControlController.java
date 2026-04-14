package com.anilist.backend.server.controller.messages;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages/group")
public class GroupControlController {
    

    @PostMapping("/create")
    public ResponseEntity<?> createGroup() {
        // Lógica para criar um grupo
        return ResponseEntity.ok("Grupo criado com sucesso");
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        // Lógica para deletar um grupo
        return ResponseEntity.ok("Grupo deletado com sucesso");
    }
    
    @GetMapping("{groupId}")
    public ResponseEntity<?> getGroupMembers(@PathVariable Long groupId) {
        // Lógica para obter os membros de um grupo
        return ResponseEntity.ok("Dados do grupo obtidos com sucesso");
    }

    @PutMapping("/{groupId}/{userId}")
    public ResponseEntity<?> updateGroupMember(@PathVariable Long groupId, @PathVariable Long userId) {
        // Lógica para atualizar um membro de um grupo
        return ResponseEntity.ok("Membro do grupo atualizado com sucesso");
    }

    
}
