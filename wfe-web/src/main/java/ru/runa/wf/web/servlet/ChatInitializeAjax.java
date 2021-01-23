package ru.runa.wf.web.servlet;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import ru.runa.wfe.chat.dto.ChatMessageDto;
import ru.runa.wfe.commons.web.JsonAjaxCommand;
import ru.runa.wfe.service.delegate.Delegates;
import ru.runa.wfe.user.User;

public class ChatInitializeAjax extends JsonAjaxCommand {
    @Override
    protected JSONAware execute(User user, HttpServletRequest request) throws Exception {
        Long processId = Long.parseLong(request.getParameter("processId"));
        Integer countMessages = Integer.parseInt(request.getParameter("messageCount"));
        JSONObject outputObject = new JSONObject();
        Long lastMessageId = Delegates.getChatService().getLastReadMessage(user, processId);
        List<ChatMessageDto> messages;
        JSONArray messagesArrayObject = new JSONArray();
        outputObject.put("lastMessageId", lastMessageId);
        messages = Delegates.getChatService().getNewChatMessages(user, processId);
        ChatMessageDto messageObject;
        if (messages.size() > 0) {
            // JSONObject messageObject = ChatSocket.convertMessage(messages.get(0),true);
            messageObject = messages.get(0);
            messageObject.setOld(true);
            if (messageObject.getMessage().getCreateActor().equals(user.getActor())) {
                messageObject.setCoreUserFlag(true);
                // messageObject.put("coreUser", true);
            } else {
                messageObject.setCoreUserFlag(false);
            }
            messagesArrayObject.add(messages.get(0).convert());
            for (int i = 1; i < messages.size(); i++) {
                // messageObject = ChatSocket.convertMessage(messages.get(i),false);
                messageObject = messages.get(i);
                messageObject.setOld(false);
                if (messageObject.getMessage().getCreateActor().equals(user.getActor())) {
                    messageObject.setCoreUserFlag(true);
                } else {
                    messageObject.setCoreUserFlag(false);
                }
                messagesArrayObject.add(messageObject.convert());
            }
        }
        if (messages.size() < countMessages) {// дополняем старыми
            messages = Delegates.getChatService().getChatMessages(user, processId, lastMessageId, countMessages - messages.size());
            for (int i = 0; i < messages.size(); i++) {
                // JSONObject messageObject = ChatSocket.convertMessage(messages.get(i),true);
                messageObject = messages.get(i);
                messageObject.setOld(true);
                if (messageObject.getMessage().getCreateActor().equals(user.getActor())) {
                    messageObject.setCoreUserFlag(true);
                } else {
                    messageObject.setCoreUserFlag(false);
                }
                messagesArrayObject.add(messageObject.convert());
            }
        }
        outputObject.put("messages", messagesArrayObject);
        return outputObject;
    }
}
