package ru.edenor.edenorchatrelay.client.parser

interface Classificator {
  fun classify(text: String): Message?
}