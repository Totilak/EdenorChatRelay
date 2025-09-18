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

    val configHolder = AutoConfig.getConfigHolder(Config::class.java)
    config = configHolder.get()
    TelegramBot.initBot()

    configHolder.registerSaveListener { _, newConfig ->
      config = newConfig
      log.info("Config changed, validating Telegram settings...")
      TelegramBot.initBot()
      ActionResult.SUCCESS
    }
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger("EdenorChatRelay")
    lateinit var config: Config
  }
}
