import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TBot extends TelegramLongPollingBot {
    private long chat_id;

    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId();

        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        chat_id = update.getMessage().getChatId();
        sendMessage.setText(input(update.getMessage().getText()));

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "@Jovajbot";
    }

    @Override
    public String getBotToken() {
        return "843494970:AAGmf3kRRpEGnyOkyils3f9xbsmBmlK7eEw";
    }

    public String input(String msg){
        if(msg.contains("Hi") || msg.contains("Hello") || msg.contains("Привет")){
            return "Привет дружище!";
        }
        if(msg.contains("Информация о книге")){
            return getInfoBook();
        }
        if(msg.contains("/person")) {
            msg = msg.replace("/person ", "");
            return getInfoPerson(msg);
        }

        return "Не понял!";
    }

    public String getInfoBook(){
        SendPhoto sendPhotoRequest = new SendPhoto();

        try(InputStream in = new URL("vk.com").openStream()){
            Files.copy(in, Paths.get("D:\\srgBook"));  // Выгружаем изображение с интернета
            sendPhotoRequest.setChatId(chat_id);
            sendPhotoRequest.setPhoto(new File("D:\\srgBook"));
            execute(sendPhotoRequest); // отправка картинки
            Files.delete(Paths.get("D:\\srgBook")); // удаление картинки
        }
        catch (IOException ex){
            System.out.println("File not found");
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }

//        String info = book.getTitle()
//                + "\nАвтор " + book.getAutorName()
//                + "\nЖанр " + book.getGeners()
//                + "\n\nОписание\n" + book.getDescription()
//                + "\n\nКоличество лайков " + book.getLikes()
//                + "\n\nПоследние коментарии\n" + book.getCommentList();
        return "info";
    }

    public String getInfoPerson(String msg){
//        Autor aut = new Autor(msg);

        SendPhoto sendPhotoRequest = new SendPhoto();
        try(InputStream in = new URL("vk.com").openStream()){
            Files.copy(in, Paths.get("D:\\srgBook"));  // Выгружаем изображение с интернета
            sendPhotoRequest.setChatId(chat_id);
            sendPhotoRequest.setPhoto(new File("D:\\srgBook"));
            execute(sendPhotoRequest); // отправка картинки
            Files.delete(Paths.get("D:\\srgBook")); // удаление картинки
        }
        catch (IOException ex){
            System.out.println("File not found");
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
//        aut.getImg();

        return "aut.getInfoPerson()";
    }
}