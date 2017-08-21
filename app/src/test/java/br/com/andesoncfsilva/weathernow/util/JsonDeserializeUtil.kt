package br.com.andesoncfsilva.weathernow.util

import com.google.gson.GsonBuilder
import java.io.File


object JsonDeserializeUtil {

    fun <T> constructUsingGson(type: Class<T>, fileName: String): T {
        val classLoader = this.javaClass.classLoader
        val resource = classLoader.getResource(fileName)
        val file = File(resource.path)
        val json = file.readText()

        val gsonBuilder = GsonBuilder()

        return gsonBuilder.create().fromJson(json, type)
    }
}