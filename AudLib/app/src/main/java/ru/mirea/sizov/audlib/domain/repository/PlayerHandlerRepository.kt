package ru.mirea.sizov.audlib.domain.repository

import ru.mirea.sizov.audlib.domain.models.AudioBook
import ru.mirea.sizov.audlib.domain.models.PlayerHandler

interface PlayerHandlerRepository {
    fun getPlayerSettings(): Map<String, String>
    fun changePlayerSettings(settings:Map<String, String>): Boolean
    fun getPlayerHandler() : PlayerHandler
    fun getListOfDictors(audioBook: AudioBook): List<String>
    fun getListOfChapters(audioBook: AudioBook): List<String>
}