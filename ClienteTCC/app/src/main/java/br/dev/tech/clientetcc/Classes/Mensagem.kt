package Classes

import java.io.Serializable

class Mensagem : Serializable {
    private var criarSala: Boolean;
    private var salas: MutableList<Sala>;
    private var movimento: Boolean;
    private var sala: Sala? = null;
    private var entrar: Boolean;
    constructor(
        criarSala: Boolean? = false,
        salas: MutableList<Sala> = mutableListOf(),
        movimento: Boolean = false,

        entrar: Boolean = false
    ) {
        this.criarSala = criarSala!!
        this.salas = salas;
        this.movimento = movimento;
        this.entrar = entrar;
    }
    public fun getSala(): Sala?{
        return this.sala
    }

    public fun getEntrar():Boolean{
        return this.entrar
    }
    fun setEntrar(entrar: Boolean){
        this.entrar = entrar
    }

    public fun setSala(sala: Sala){
        this.sala = sala
    }

    public fun getCriarSala(): Boolean {
        return this.criarSala
    }

    public fun getSalas(): MutableList<Sala> {
        return this.salas
    }

    public fun setSalas(salas: MutableList<Sala>) {
        this.salas = salas
    }

    public fun setCriarSala(criarSala: Boolean?) {
        this.criarSala = criarSala!!
    }

}