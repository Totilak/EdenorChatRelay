package ru.edenor.edenorchatrelay.client

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class EdenorchatrelayDataGenerator : DataGeneratorEntrypoint {

  override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
    fabricDataGenerator.createPack()
  }
}
