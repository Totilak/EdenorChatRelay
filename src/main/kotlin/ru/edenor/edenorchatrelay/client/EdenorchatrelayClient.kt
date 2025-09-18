package ru.edenor.edenorchatrelay.client

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.minecraft.util.ActionResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.edenor.edenorchatrelay.TelegramBot
import ru.edenor.edenorchatrelay.client.parser.ClassificatorSource
import ru.edenor.edenorchatrelay.client.parser.Classificators
import ru.edenor.edenorchatrelay.client.parser.DirectoryClassificatorSource
import ru.edenor.edenorchatrelay.client.parser.ResourceClassificatorSource
import java.io.File

class EdenorchatrelayClient : ClientModInitializer {

  override fun onInitializeClient() {
    AutoConfig.register(Config::class.java) { definition, configClass ->
      GsonConfigSerializer(definition, configClass)
    }

    val configHolder = AutoConfig.getConfigHolder(Config::class.java)
    config = configHolder.get()
    TelegramBot.initBot()

    registerClassificators()

    configHolder.registerSaveListener { _, newConfig ->
      config = newConfig
      log.info("Config changed, validating Telegram settings...")
      TelegramBot.initBot()
      ActionResult.SUCCESS
    }
  }

  private fun registerClassificators() {
    val classificatorsDir = File("config/edenorchatrelay/classificators")
    if (!classificatorsDir.exists()) {
      classificatorsDir.mkdirs()
    }

    listOf(
      ResourceClassificatorSource("classificator/edenor.json"),
      DirectoryClassificatorSource(classificatorsDir)
    )
      .flatMap(ClassificatorSource::get)
      .forEach(Classificators::registerClassificator)
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger("EdenorChatRelay")
    lateinit var config: Config
  }
}
