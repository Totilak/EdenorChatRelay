package ru.edenor.edenorchatrelay.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "edenorchatrelay")
class EdenorConfig : ConfigData {
  @ConfigEntry.Category("telegram")
  @ConfigEntry.Gui.TransitiveObject
  var telegram = TelegramSettings()

  class TelegramSettings {
    @ConfigEntry.Gui.Tooltip
    var chatId: Long = 0

    @ConfigEntry.Gui.Tooltip
    var botToken: String = ""
  }
}

