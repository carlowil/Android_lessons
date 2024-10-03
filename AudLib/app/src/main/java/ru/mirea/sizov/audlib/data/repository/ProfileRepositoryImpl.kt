package ru.mirea.sizov.audlib.data.repository

import ru.mirea.sizov.audlib.domain.models.Profile
import ru.mirea.sizov.audlib.domain.repository.ProfileRepository

class ProfileRepositoryImpl : ProfileRepository {
    override fun getUserProfile(): Profile {
        TODO("Not yet implemented")
    }

    override fun changeUserProfile(profile: Profile): Boolean {
        TODO("Not yet implemented")
    }
}