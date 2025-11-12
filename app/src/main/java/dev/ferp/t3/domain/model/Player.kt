package dev.ferp.t3.domain.model

enum class Player {
    CIRCLE, CROSS;

    fun opposite() = if (this == CIRCLE) CROSS else CIRCLE
}