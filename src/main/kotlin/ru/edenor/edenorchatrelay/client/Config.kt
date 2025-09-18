package ru.edenor.edenorchatrelay.client

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "edenorchatrelay")
class Config : ConfigData {
  @ConfigEntry.Category("telegram")
  @ConfigEntry.Gui.TransitiveObject
  var telegram = TelegramSettings()

  class TelegramSettings {
    @ConfigEntry.Gui.Tooltip
    var chatId: Long = 0

    @ConfigEntry.Gui.Tooltip
    var botToken: String = ""

    @ConfigEntry.Gui.Tooltip
    var sendAllChat: Boolean = false
  }
}

