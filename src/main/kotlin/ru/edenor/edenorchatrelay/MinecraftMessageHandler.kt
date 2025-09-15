package ru.edenor.edenorchatrelay

object MinecraftMessageHandler {
  @JvmStatic
  fun onMinecraftMessage(text: String) {
    TelegramBot.sendToTelegram(text)
  }
}