package ru.edenor.edenorchatrelay.client.parser

import ru.edenor.edenorchatrelay.client.EdenorchatrelayClient.Companion.log

class RegexClassificator(val regex: Regex, val channel: String) : Classificator {
  override fun classify(text: String): Message? {

    val result = regex.matchEntire(text) ?: return null
    val sender = result.groups["sender"]
    val content = result.groups["content"]

    if (content == null) {
      log.error("Regex ${regex.pattern} does not contain capturing group 'content'")
      return null
    }

    if (sender != null) {
      return MessageWithSender(content.value, channel, sender.value)
    }

    return Message(content.value, channel)
  }
}
