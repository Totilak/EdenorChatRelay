package ru.edenor.edenorchatrelay

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.TelegramException
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.GetMe
import com.pengrad.telegrambot.request.SendMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.edenor.edenorchatrelay.client.EdenorchatrelayClient.Companion.config
import java.util.concurrent.Executors

object TelegramBot {
  var bot: TelegramBot? = null
  var chatId: Long = 0

  val log: Logger = LoggerFactory.getLogger(TelegramBot::class.java)

  val ex = Executors.newFixedThreadPool(4)

  fun sendToTelegram(text: String, quiet: Boolean = false) {
    if (bot == null) return

    try {
      val sendMessage = SendMessage(chatId, text)
      sendMessage.disableNotification(quiet)
      ex.execute {
        try {
          bot!!.execute(sendMessage)
        } catch (e: TelegramException) {
          log.error("Failed to send message: ${e.message}")
        }
      }
    } catch (e: TelegramException) {
      log.error("Failed to queue message: ${e.message}")
    }
  }

  fun initBot() {
    bot = null
    val token = config.telegram.botToken
    val chatId = config.telegram.chatId

    if (token.isBlank() || chatId == 0L) {
      log.error("Bot token or chatId not configured")
      return
    }

    try {
      val newBot = TelegramBot(token)
      val me = newBot.execute(GetMe())
      if (!me.isOk) {
        log.error("Invalid bot token: ${me.description()}")
        return
      }
      log.info("Telegram integration enabled. bot: {}", me.user().username())
      newBot.setUpdatesListener(::onUpdate, ::onException)
      bot = newBot
      this.chatId = chatId
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  fun onUpdate(updates: List<Update>): Int {
    for (update in updates) {
      val message = update.message() ?: continue
      TelegramMessageHandler.onTelegramMessage(message)
    }
    return UpdatesListener.CONFIRMED_UPDATES_ALL
  }

  fun onException(e: TelegramException) {
    if (e.response() != null) {
      log.error("Telegram error {}: {}", e.response().errorCode(), e.response().description())
    } else {
      e.printStackTrace()
    }
  }
}
