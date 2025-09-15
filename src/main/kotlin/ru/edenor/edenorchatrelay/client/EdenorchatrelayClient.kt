package ru.edenor.edenorchatrelay.client

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.minecraft.util.ActionResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.edenor.edenorchatrelay.TelegramBot

class EdenorchatrelayClient : ClientModInitializer {

  override fun onInitializeClient() {
    AutoConfig.register(Config::class.java) { definition, configClass ->
      GsonConfigSerializer(definition, configClass)
    }

    val holder = AutoConfig.getConfigHolder(Config::class.java)
    TelegramBot.initBot(holder.config)

    holder.registerSaveListener { _, newConfig ->
      log.info("Config changed, validating Telegram settings...")
      TelegramBot.initBot(newConfig)
      ActionResult.SUCCESS
    }
  }


  companion object {
    val log: Logger = LoggerFactory.getLogger("EdenorChatRelay")
  }
}
