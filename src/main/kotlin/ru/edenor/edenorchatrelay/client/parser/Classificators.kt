package ru.edenor.edenorchatrelay.client.parser

object Classificators {
  private val classificators: MutableList<Classificator> = mutableListOf()

  fun registerClassificator(classificator: Classificator) {
    classificators.add(classificator)
  }

  fun parse(text: String) : Message =
      classificators.firstNotNullOfOrNull { it.classify(text) } ?: Message(text, "other")
}
