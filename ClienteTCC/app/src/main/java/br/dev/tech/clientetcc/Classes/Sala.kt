package Classes

import java.io.Serializable
import java.util.UUID

class Sala : Serializable {
    private var ids: MutableSet<UUID> = mutableSetOf()
    private var jogo: Jogo = Jogo();
    private var jogadores: Int = 0;

    constructor(ids: MutableSet<UUID>, jogo: Jogo) {
        this.ids = ids
        this.jogo = jogo
    }

    fun setJogadores(int: Int) {
        this.jogadores = int
    }

    fun getJogadores(): Int {
        return this.jogadores
    }

    fun getIds(): MutableSet<UUID> {
        return this.ids
    }

    fun setIds(ids: MutableSet<UUID>) {
        this.ids = ids
    }

    fun getJogo(): Jogo {
        return this.jogo
    }


}