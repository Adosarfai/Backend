package com.adosar.backend.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class FeedController {

	@MessageMapping("/ping")
	@SendTo("/feed")
	public String ping(Message message) {
		return "Pong " + message.getPayload();
	}

}
