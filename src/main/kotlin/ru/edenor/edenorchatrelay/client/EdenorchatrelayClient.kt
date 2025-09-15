package ru.edenor.edenorchatrelay.client

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.TelegramException
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.GetMe
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import ru.edenor.edenorchatrelay.config.EdenorConfig

class EdenorchatrelayClient : ClientModInitializer {

  override fun onInitializeClient() {
    AutoConfig.register(EdenorConfig::class.java) { definition, configClass ->
      GsonConfigSerializer(definition, configClass)
    }

    val holder = AutoConfig.getConfigHolder(EdenorConfig::class.java)
    initBot(holder.config)

    holder.registerSaveListener { _, newConfig ->
      println("[EdenorChatRelay] Config changed, validating Telegram settings...")
      initBot(newConfig)
      net.minecraft.util.ActionResult.SUCCESS
    }
  }

  private fun initBot(config: EdenorConfig) {
    val token = config.telegram.botToken
    val chatIdCopy = config.telegram.chatId // копия chatId для использования в лямбде

    if (token.isBlank() || chatIdCopy == 0L) {
      println("[EdenorChatRelay] Bot token or chatId not configured")
      telegramEnabled = false
      return
    }

    try {
      val newBot = TelegramBot(token)

      val me = newBot.execute(GetMe())
      if (!me.isOk) {
        println("[EdenorChatRelay] Invalid bot token: ${me.description()}")
        telegramEnabled = false
        return
      }

      val testMsg: SendResponse =
          newBot.execute(SendMessage(chatIdCopy, "✅ Telegram relay is active!"))
      if (!testMsg.isOk) {
        println("[EdenorChatRelay] Invalid chatId: ${testMsg.description()}")
        telegramEnabled = false
        return
      }

      println("[EdenorChatRelay] Telegram integration enabled!")

      newBot.setUpdatesListener(
          { updates ->
            for (update: Update in updates) {
              val message = update.message() ?: continue

              if (message.chat().id() != chatIdCopy) continue

              val text = message.text() ?: continue

              MinecraftClient.getInstance().execute {
                val player = MinecraftClient.getInstance().player ?: return@execute
                val networkHandler = player.networkHandler ?: return@execute

                if (text.startsWith("/")) {
                  networkHandler.sendChatCommand(text.substring(1))
                } else {
                  networkHandler.sendChatMessage(text)
                }
              }
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
          },
          { e: TelegramException ->
            if (e.response() != null) {
              println(
                  "[EdenorChatRelay] Telegram error ${e.response().errorCode()}: ${e.response().description()}")
            } else {
              e.printStackTrace()
            }
          })

      bot = newBot
      chatId = chatIdCopy
      telegramEnabled = true
    } catch (e: Exception) {
      e.printStackTrace()
      telegramEnabled = false
    }
  }

  companion object {
    var bot: TelegramBot? = null
    var telegramEnabled = false
    var chatId: Long = 0
  }
}
