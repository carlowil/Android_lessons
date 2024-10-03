package ru.mirea.sizov.audlib.domain.repository

import ru.mirea.sizov.audlib.domain.models.PlayerHandler
import ru.mirea.sizov.audlib.domain.models.Profile

interface ProfileRepository {
    fun getUserProfile() : Profile
    fun changeUserProfile(profile: Profile) : Boolean
}