package ru.edenor.edenorchatrelay.client

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import ru.edenor.edenorchatrelay.config.EdenorConfig

class EdenorModMenuIntegration : ModMenuApi {
  override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
    return ConfigScreenFactory { parent ->
      AutoConfig.getConfigScreen(EdenorConfig::class.java, parent).get()
    }
  }
}
