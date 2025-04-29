package com.strategic.ludo.strategicLudo.session;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionMsg {

    private String content;
    private String sender;
    private MessageType type;
}
