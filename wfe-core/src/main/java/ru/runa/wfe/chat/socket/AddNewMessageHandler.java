package ru.runa.wfe.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.runa.wfe.chat.ChatMessage;
import ru.runa.wfe.chat.ChatMessageFile;
import ru.runa.wfe.chat.dto.ChatMessageDto;
import ru.runa.wfe.chat.dto.ChatNewMessageDto;
import ru.runa.wfe.chat.logic.ChatLogic;
import ru.runa.wfe.execution.logic.ExecutionLogic;
import ru.runa.wfe.user.Actor;
import ru.runa.wfe.user.Executor;
import ru.runa.wfe.user.User;
import ru.runa.wfe.user.logic.ExecutorLogic;

//@ApplicationScoped // ненужен?
//@Interceptors({ SpringBeanAutowiringInterceptor.class })
@Component
public class AddNewMessageHandler implements ChatSocketMessageHandler {

    @Autowired
    private ChatSessionHandler sessionHandler;
    @Autowired
    private ChatLogic chatLogic;
    @Autowired
    private ExecutionLogic executionLogic;
    @Autowired
    private ExecutorLogic executorLogic;

    @Transactional
    @Override
    public void handleMessage(Session session, String objectMessage, User user) throws IOException {
        if (executionLogic.getProcess(user, (Long) session.getUserProperties().get("processId")).isEnded()) {
            return;
        }
        ChatNewMessageDto message = (ChatNewMessageDto) ChatNewMessageDto.load(objectMessage, ChatNewMessageDto.class);
        ChatMessage newMessage = new ChatMessage();
        newMessage.setCreateActor(user.getActor());
        newMessage.setText(message.getMessage());// message
        newMessage.setQuotedMessageIds(message.getIdHierarchyMessage());// idHierarchyMessage
        boolean haveFiles = message.getIsHaveFile();// haveFile
        Boolean isPrivate = message.getIsPrivate();// isPrivate
        String privateNames = message.getPrivateNames();// privateNames
        String[] loginsPrivateTable = privateNames != null ? privateNames.split(";") : new String[0];
        long processId = Long.parseLong(message.getProcessId());// processId
        newMessage.setCreateDate(new Date(Calendar.getInstance().getTime().getTime()));
        Set<Executor> mentionedExecutors = new HashSet<Executor>();
        searchMentionedExecutor(mentionedExecutors, newMessage, loginsPrivateTable, user, session);
        if (haveFiles) {
            // waiting for upload
            session.getUserProperties().put("activeProcessId", processId);
            session.getUserProperties().put("activeMessage", newMessage);
            session.getUserProperties().put("activeIsPrivate", isPrivate);
            session.getUserProperties().put("activeMentionedExecutors", mentionedExecutors);
            session.getUserProperties().put("activeFileNames", message.getFileNames());// fileNames
            session.getUserProperties().put("activeFileSizes", message.getFileSizes());// fileSizes
            session.getUserProperties().put("activeFilePosition", 0);
            Integer fileNumber = 0;
            session.getUserProperties().put("activeFileNumber", fileNumber);
            session.getUserProperties().put("errorFlag", false);
            session.getUserProperties().put("activeFiles", new ArrayList<ChatMessageFile>());
            session.getUserProperties().put("activeLoadFile", new byte[(message.getFileSizes()).get(0).intValue()]);
            JSONObject sendObject = new JSONObject();
            sendObject.put("messType", "stepLoadFile");
            sessionHandler.sendToSession(session, sendObject.toString());
        } else {
            Collection<Actor> mentionedActors = new HashSet<Actor>();
            for (Executor mentionedExecutor : mentionedExecutors) {
                if (mentionedExecutor instanceof Actor) {
                    mentionedActors.add((Actor) mentionedExecutor);
                }
            }
            Long newMessId = chatLogic.saveMessage(user.getActor(), processId, newMessage, mentionedExecutors, isPrivate);
            newMessage.setId(newMessId);
            ChatMessageDto chatMessageDto = new ChatMessageDto(newMessage);
            sessionHandler.sendNewMessage(mentionedExecutors, chatMessageDto, isPrivate);
        }
    }

    void searchMentionedExecutor(Collection<Executor> mentionedExecutors, ChatMessage newMessage, String[] loginsPrivateTable, User user,
            Session session) {
        // mentioned executors are defined by '@user' pattern
        int dogIndex = -1;
        int spaceIndex = -1;
        String login;
        String searchText = newMessage.getText();
        Executor actor;
        while (true) {
            dogIndex = searchText.indexOf('@', dogIndex + 1);
            if (dogIndex != -1) {
                spaceIndex = searchText.indexOf(' ', dogIndex);
                if (spaceIndex != -1) {
                    login = searchText.substring(dogIndex + 1, spaceIndex);
                } else {
                    login = searchText.substring(dogIndex + 1);
                }
                try {
                    actor = executorLogic.getExecutor(user, login);
                } catch (Exception e) {
                    actor = null;
                }
                if (actor != null) {
                    mentionedExecutors.add(actor);
                }

            } else {
                if ((loginsPrivateTable.length > 0) && (loginsPrivateTable[0].trim().length() != 0)) {
                    for (String loginPrivate : loginsPrivateTable) {
                        actor = executorLogic.getExecutor(user, loginPrivate);
                        if (actor != null) {
                            if (!mentionedExecutors.contains(actor)) {
                                mentionedExecutors.add(actor);
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean checkType(String messageType) {
        return messageType.equals("newMessage");
    }
}
