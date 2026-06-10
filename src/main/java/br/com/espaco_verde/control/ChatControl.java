package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.ChatDTO;
import br.com.espaco_verde.DTO.MessageDTO;
import br.com.espaco_verde.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatControl {

    @Autowired
    private ChatService chatService;

    @GetMapping("/attendance")
    public ResponseEntity<List<ChatDTO>> listarFilaAtendimento() {
        return ResponseEntity.ok(chatService.listAttendanceQueue());
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> fetchMessageHistory(@PathVariable Long chatId){
        return ResponseEntity.ok(chatService.fetchMessageHistory(chatId));
    }

    @PostMapping("/{chatId}/send")
    public ResponseEntity<Void> sendAttendantMessage(@PathVariable Long chatId, @RequestBody String messageText){
        chatService.sendAttendantMessage(chatId, messageText);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{chatId}/close")
    public ResponseEntity<Void> closeAttendance(@PathVariable Long chatId){
        chatService.closeAttendance(chatId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{chatId}/mark-as-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long chatId) {
        chatService.markAsRead(chatId);
        return ResponseEntity.ok().build();
    }

}
