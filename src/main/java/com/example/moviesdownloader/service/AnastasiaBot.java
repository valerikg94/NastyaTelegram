package com.example.moviesdownloader.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class AnastasiaBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    
    private final MessageService messageService;
    private final String botToken;
    
    @Autowired
    public AnastasiaBot(@Value("${telegram.bot.token}") String botToken,
                        MessageService messageService) {
        this.botToken = botToken;
        this.messageService = messageService;
        // ✅ Вот здесь создаем TelegramClient!
    }
    
    @Override
    public String getBotToken() {
        return botToken;
    }
    
    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
    
    @Override
    public void consume(Update update) {
        messageService.processUpdate(update);
    }
}