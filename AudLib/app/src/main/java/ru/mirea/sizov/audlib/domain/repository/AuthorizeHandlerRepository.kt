package ru.mirea.sizov.audlib.domain.repository

import ru.mirea.sizov.audlib.domain.models.AuthorizeHandler

interface AuthorizeHandlerRepository {
    fun getAuthorizeHandler(): AuthorizeHandler
}