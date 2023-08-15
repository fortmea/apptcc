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
    fun checkGameStatus(): String {
        val board = posicoes
        // Define todas as possíveis combinações de vitória
        val winCombinations = listOf(
            listOf("A1", "A2", "A3"),
            listOf("B1", "B2", "B3"),
            listOf("C1", "C2", "C3"),
            listOf("A1", "B1", "C1"),
            listOf("A2", "B2", "C2"),
            listOf("A3", "B3", "C3"),
            listOf("A1", "B2", "C3"),
            listOf("A3", "B2", "C1")
        )

        // Verifica se alguma combinação de vitória ocorreu
        for (combination in winCombinations) {
            val values = combination.map { board!![it] }
            if (values.all { it == 1 }) {
                return "X venceu"
            } else if (values.all { it == 2 }) {
                return "O venceu"
            }
        }

        // Verifica se o tabuleiro está cheio (empate)
        if (board!!.values.all { it != 0 }) {
            return "Empate"
        }

        // Caso nenhum resultado tenha sido determinado
        return "Jogo em andamento"
    }
}