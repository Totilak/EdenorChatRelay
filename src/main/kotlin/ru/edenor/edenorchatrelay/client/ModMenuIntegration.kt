package ru.edenor.edenorchatrelay.client

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig

class ModMenuIntegration : ModMenuApi {
  override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
    return ConfigScreenFactory { parent ->
      AutoConfig.getConfigScreen(Config::class.java, parent).get()
    }
  }
}
