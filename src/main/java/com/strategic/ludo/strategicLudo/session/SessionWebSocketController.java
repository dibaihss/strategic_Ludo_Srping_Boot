package com.strategic.ludo.strategicLudo.session;

import Entities.Session;
import Services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;


@Controller
public class SessionWebSocketController {
    @Autowired
    private SessionService sessionService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public SessionMsg sendMsg(@Payload SessionMsg sessionMsg){
        return sessionMsg;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public SessionMsg sendMsg(@Payload SessionMsg sessionMsg, SimpMessageHeaderAccessor headerAccessor){
        // add Userbane in Websoket session

        headerAccessor.getSessionAttributes().put("username", sessionMsg.getSender());

       return sessionMsg;
    }

    @MessageMapping("/chat.getCard/{cardId}")
    @SendTo("/topic/card/{cardId}")
    public String sendMsg(@DestinationVariable String cardId,@Payload String msg ,SimpMessageHeaderAccessor headerAccessor){
        // add Usernane in Websoket session
        System.out.println("Message for ID: " + cardId);
        headerAccessor.getSessionAttributes().put("username", msg);
        return msg;
    }

    @MessageMapping("/board.getPos")
    @SendTo("/topic/board")
    public String gotPos(@Payload String msg ,SimpMessageHeaderAccessor headerAccessor){
        // add Usernane in Websoket session
        
        // headerAccessor.getSessionAttributes().put("username", msg);
        System.out.println("Message for ID: " + msg);
        return msg;
    }

    @MessageMapping("/waitingRoom.gameStarted/{sessionId}")
    @SendTo("/topic/gameStarted/{sessionId}")
    public String checkGameStarted(@DestinationVariable String sessionId, @Payload String msg, SimpMessageHeaderAccessor headerAccessor) {
  
                return msg;
 
    }

    @MessageMapping("/allPlayers.sendMatchData/{sessionId}")
    @SendTo("/topic/sessionData/{sessionId}")
    public String updateSessionDatabyAllPlayers(@DestinationVariable String sessionId, @Payload String msg, SimpMessageHeaderAccessor headerAccessor) {

                return msg;
    }

    @MessageMapping("/player.getPlayer/{sessionId}")
    @SendTo("/topic/currentPlayer/{sessionId}")
    public String getPlayer(@Payload String msg ,SimpMessageHeaderAccessor headerAccessor){
        System.out.println("Message for ID: " + msg);
        return msg;
    }
    @MessageMapping("/player.Move/{sessionId}")
    @SendTo("/topic/playerMove/{sessionId}")
    public String getPlayerMove(@Payload String msg ,SimpMessageHeaderAccessor headerAccessor){
        System.out.println("Message for ID: " + msg);
        return msg;
    }


}
