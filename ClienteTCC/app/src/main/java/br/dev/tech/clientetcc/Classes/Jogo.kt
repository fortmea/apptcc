package Classes

import java.io.Serializable


class Jogo : Serializable {
    private var posicoes: MutableMap<String, Int>? = null;
    fun setPosicoes(nPosicoes: MutableMap<String, Int>) {
        this.posicoes = nPosicoes;
    }
    fun getPosicoes(): MutableMap<String, Int>? {
        return this.posicoes;
    }
}