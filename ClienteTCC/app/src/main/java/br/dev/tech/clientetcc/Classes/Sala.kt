package Classes

import java.io.Serializable
import java.util.UUID

class Sala : Serializable {
    private var usuarios: MutableSet<Usuario> = mutableSetOf()
    private var jogo: Jogo = Jogo();
    private var placar: MutableMap<UUID, Int> = mutableMapOf()
    private var resetarTabuleiro: Boolean = false;
    private var simbolo: MutableMap<Int, UUID> = mutableMapOf()

    constructor(usuarios: MutableSet<Usuario>, jogo: Jogo) {
        this.usuarios = usuarios
        this.jogo = jogo
    }

    //2 = O 1 = X
    fun getSimbolo(): MutableMap<Int, UUID> {
        return this.simbolo;
    }

    fun setJogo(jogo: Jogo){
        this.jogo = jogo
    }
    fun setSimbolo(simbolos: MutableMap<Int, UUID>) {
        this.simbolo = simbolos
    }

    fun setResetarTabuleiro(resetarTabuleiro: Boolean) {
        this.resetarTabuleiro = resetarTabuleiro
    }

    fun getResetarTabuleiro(): Boolean {
        return this.resetarTabuleiro
    }

    fun setPlacar(placar: MutableMap<UUID, Int>) {
        this.placar = placar
    }

    fun getPlacar(): MutableMap<UUID, Int> {
        return this.placar
    }

    fun getUsuarios(): MutableSet<Usuario> {
        return this.usuarios
    }

    fun getJogadores(): Int {
        return this.usuarios.size
    }


    fun setUsuarios(usuarios: MutableSet<Usuario>) {
        this.usuarios = usuarios
    }

    fun getJogo(): Jogo {
        return this.jogo
    }


}