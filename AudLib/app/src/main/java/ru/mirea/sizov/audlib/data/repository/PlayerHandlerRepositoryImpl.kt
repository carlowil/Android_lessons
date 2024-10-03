package ru.mirea.sizov.audlib.data.repository

import ru.mirea.sizov.audlib.domain.models.AudioBook
import ru.mirea.sizov.audlib.domain.models.PlayerHandler
import ru.mirea.sizov.audlib.domain.repository.PlayerHandlerRepository

class PlayerHandlerRepositoryImpl : PlayerHandlerRepository {
    override fun getPlayerSettings(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override fun changePlayerSettings(settings: Map<String, String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlayerHandler(): PlayerHandler {
        TODO("Not yet implemented")
    }

    override fun getListOfDictors(audioBook: AudioBook): List<String> {
        TODO("Not yet implemented")
    }

    override fun getListOfChapters(audioBook: AudioBook): List<String> {
        TODO("Not yet implemented")
    }
}