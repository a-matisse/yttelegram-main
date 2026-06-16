package cs.youtrade.telegram.buttons.def.message;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@SuperBuilder(toBuilder = true, builderMethodName = "defaultBuilder")
@Log4j2
public class DefaultMessageProcessor<USER extends AbstractUserData, EDIT> extends DefaultOnErrorProcessor<USER> {
    @NonNull
    private TelegramClient bot;
    @NonNull
    private Update update;
    @NonNull
    private USER user;
    private Class<EDIT> supportedEdit;

    @Override
    public void processMessage(MessageSentData data) {
        int prevMessageId = user.getLastMessageId();
        // Deleting the old message,
        // because there should be no menu duplicates (at least minimize them)
        if (prevMessageId <= 0) {
            if (update.hasCallbackQuery()) {
                Integer callbackId = update.getCallbackQuery().getMessage().getMessageId();
                if (callbackId != null)
                    prevMessageId = callbackId;
            }
        }
        if (prevMessageId > 0) {
            try {
                sender.deleteMes(bot, user, prevMessageId, null);
            } catch (Exception e) {
                log.error("Menu deletion aborted: {}", e.getMessage());
            }
        }
        user.setLastMessageId(data.getMessageId());
        // Setting the new mesSupportedEdit
        user.setSupportedEdit(supportedEdit);
        // Refreshing the update flag
        user.setUpdated(false);
    }
}
