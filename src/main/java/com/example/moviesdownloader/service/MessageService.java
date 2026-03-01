package com.example.moviesdownloader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final TelegramClient telegramClient;

    @Value("${telegram.bot.username}")
    private String botUsername;

    public void processUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            switch (messageText) {
                case "/start":
                    sendWelcomeMessageWithPhoto(chatId, userName);
                    break;
                case "/material":
                    sendMaterial(chatId);
                    break;
                case "/help":
                    sendHelpMessage(chatId);
                    break;
                default:
                    sendUnknownCommandMessage(chatId);
            }
        }
    }

    private void sendMaterial(long chatId) {
        try {
            ClassPathResource pdfFile =
                    new ClassPathResource("maklakov.pdf");

            SendDocument sendDocument = new SendDocument(String.valueOf(chatId),new InputFile(pdfFile.getInputStream(), "kpt-guide.pdf"));
            sendDocument.setCaption("📘 Вот материал по КПТ. Приятного изучения!");

            telegramClient.execute(sendDocument);

            log.info("✅ PDF успешно отправлен в чат {}", chatId);

        } catch (Exception e) {
            log.error("❌ Ошибка отправки PDF: {}", e.getMessage());
            sendTextMessage(chatId, "Не удалось отправить материал 😔");
        }
    }

    private void sendWelcomeMessageWithPhoto(long chatId, String userName) {
        try {
            String caption = String.format(
                    "👋 Привет, %s!\n\n" +
                            "Я Анастасия, КПТ-психолог 🌸\n\n" +
                            "Нажми /material и получи чек-лист 📘",
                    userName
            );

            ClassPathResource imgFile = new ClassPathResource("002.jpg");

            SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgFile.getInputStream(), "002.jpg"));
            sendPhoto.setCaption(caption);

            telegramClient.execute(sendPhoto); // ✅ ВАЖНО

        } catch (Exception e) {
            log.error("Ошибка отправки фото: {}", e.getMessage());
            sendTextMessage(chatId, "Фото временно недоступно");
        }
    }

    private void sendHelpMessage(long chatId) {
        sendTextMessage(chatId, "Доступные команды:\n/start\n/help");
    }

    private void sendUnknownCommandMessage(long chatId) {
        sendTextMessage(chatId, "Я не понимаю эту команду. Попробуй /help");
    }

    private void sendTextMessage(long chatId, String text) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();

            telegramClient.execute(message); // ✅ ВАЖНО

        } catch (TelegramApiException e) {
            log.error("Ошибка отправки текста: {}", e.getMessage());
        }
    }
}