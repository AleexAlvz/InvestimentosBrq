package br.com.alexalves.base.repository

import br.com.alexalves.models.User

interface ExchangeRepository {
    suspend fun searchUser(userId: Long): User
    suspend fun updateUser(user: User)
}
