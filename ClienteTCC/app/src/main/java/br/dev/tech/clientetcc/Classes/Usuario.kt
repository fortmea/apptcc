package Classes

import java.util.UUID

class Usuario: java.io.Serializable {
    private var id: UUID = UUID.randomUUID();
    private var nome: String = id.toString();

    fun setNome(nome: String) {
        this.nome = nome
    }

    fun getNome(): String {
        return this.nome
    }

    fun getId(): UUID {
        return this.id
    }
}