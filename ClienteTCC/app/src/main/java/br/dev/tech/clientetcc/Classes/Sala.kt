package Classes

import java.io.Serializable
import java.util.UUID

class Sala : Serializable {
    private var ids: MutableSet<UUID> = mutableSetOf()
    private var jogo: Jogo = Jogo();
    private var placar: MutableMap<UUID, Int> = mutableMapOf()
    private var resetarTabuleiro: Boolean = false;
    private var simbolo: MutableMap<Int, UUID> = mutableMapOf()
    constructor(ids: MutableSet<UUID>, jogo: Jogo) {
        this.ids = ids
        this.jogo = jogo
    }

    fun getSimbolo(): MutableMap<Int, UUID>{
        return this.simbolo;
    }
    fun setSimbolo(simbolos: MutableMap<Int, UUID>){
        this.simbolo = simbolos
    }
    fun setResetarTabuleiro(resetarTabuleiro: Boolean) {
        this.resetarTabuleiro = resetarTabuleiro
    }

    fun getResetarTabuleiro(): Boolean {
        return this.resetarTabuleiro
    }

    fun setPlacar(placar: MutableMap<UUID, Int>){
        this.placar = placar
    }
    fun getPlacar(): MutableMap<UUID, Int> {
        return this.placar
    }

    fun getIds(): MutableSet<UUID> {
        return this.ids
    }

    fun getJogadores(): Int {
        return this.ids.size
    }


    fun setIds(ids: MutableSet<UUID>) {
        this.ids = ids
    }

    fun getJogo(): Jogo {
        return this.jogo
    }


}