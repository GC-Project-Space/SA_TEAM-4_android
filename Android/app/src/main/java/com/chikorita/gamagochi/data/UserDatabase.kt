package com.chikorita.gamagochi.data

object UserDatabase {
    private val users = mutableListOf(
        User(1, "테스트23", "12345", "이도연3", "도시계획학과"),
        User(6, "테스트5", "12345", "test6", "소프트웨어학과")
    )

    fun getUsers(): List<User> {
        return users.toList()
    }

    fun addUser(user: User) {
        users.add(user)
    }

    fun findUserByUsername(username: String): User? {
        return users.find { it.username == username }
    }

    fun isUsernameTaken(username: String): Boolean {
        return users.any { it.username == username }
    }
}

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val name: String,
    val major: String
)