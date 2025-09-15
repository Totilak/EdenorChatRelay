package ru.edenor.edenorchatrelay

import com.pengrad.telegrambot.model.Message
import net.minecraft.client.MinecraftClient
import ru.edenor.edenorchatrelay.TelegramBot.chatId

object TelegramMessageHandler {
  fun onTelegramMessage(message: Message) {
    val text = message.text() ?: return

    if (message.chat().id() != chatId) {
      return
    }

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
}