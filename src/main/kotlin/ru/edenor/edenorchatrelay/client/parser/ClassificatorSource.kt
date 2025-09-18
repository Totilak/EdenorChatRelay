package ru.edenor.edenorchatrelay.client.parser

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStreamReader
import java.util.function.Supplier

interface ClassificatorSource : Supplier<List<Classificator>>

open class JsonClassificatorSource(private val jsonFileReader: InputStreamReader) : ClassificatorSource {
  private val gson = Gson()
  private val token: TypeToken<List<ClassificatorData>> = object : TypeToken<List<ClassificatorData>>() {}

  data class ClassificatorData(val regex: String, val channel: String)

  override fun get(): List<Classificator> {
    jsonFileReader.use { reader ->
      return gson.fromJson(reader, token).map { RegexClassificator(it.regex.toRegex(), it.channel) }
    }
  }
}

class FileClassificatorSource(file: File) : JsonClassificatorSource(file.reader())

class DirectoryClassificatorSource(val directory: File) : ClassificatorSource {
  override fun get(): List<Classificator> =
    directory.listFiles { file -> file.extension == "json" }
      ?.map(::FileClassificatorSource)
      ?.flatMap(ClassificatorSource::get)
      ?: emptyList()
}

class ResourceClassificatorSource(resourcePath: String) :
  JsonClassificatorSource(ClassLoader.getSystemResourceAsStream(resourcePath)!!.reader())