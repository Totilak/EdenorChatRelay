package ru.edenor.edenorchatrelay.client.parser

class MessageWithSender(text: String, channel: String, val sender: String) : Message(text, channel)