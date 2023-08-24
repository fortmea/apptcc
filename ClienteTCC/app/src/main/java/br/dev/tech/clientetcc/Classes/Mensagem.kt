package Classes

import java.io.Serializable

class Mensagem : Serializable {
    private var criarSala: Boolean;
    private var salas: MutableMap<Int, Sala>;
    private var movimento: Boolean;
    private var sala: Sala? = null;
    private var idSala: Int? = null;
    private var entrar: Boolean;
    private var usuario: Usuario? = null;
    private var sair: Boolean = false;

    constructor(
        criarSala: Boolean? = false,
        salas: MutableMap<Int, Sala> = mutableMapOf(),
        movimento: Boolean = false,

        entrar: Boolean = false
    ) {
        this.criarSala = criarSala!!
        this.salas = salas;
        this.movimento = movimento;
        this.entrar = entrar;
    }

    fun setSair(sair: Boolean) {
        this.sair = sair
    }

    fun getSair(): Boolean {
        return this.sair
    }

    fun setUsuario(usuario: Usuario) {
        this.usuario = usuario
    }

    fun getUsuario(): Usuario? {
        return this.usuario
    }

    fun getSala(): Sala? {
        return this.sala
    }

    fun setIdSala(idSala: Int) {
        this.idSala = idSala
    }

    fun getIdSala(): Int? {
        return this.idSala
    }

    fun getEntrar(): Boolean {
        return this.entrar
    }

    fun setEntrar(entrar: Boolean) {
        this.entrar = entrar
    }

    fun setSala(sala: Sala) {
        this.sala = sala
    }

    fun getCriarSala(): Boolean {
        return this.criarSala
    }

    fun getSalas(): MutableMap<Int, Sala> {
        return this.salas
    }

    fun setSalas(salas: MutableMap<Int, Sala>) {
        this.salas = salas
    }

    fun setCriarSala(criarSala: Boolean?) {
        this.criarSala = criarSala!!
    }

    fun setMovimento(movimento: Boolean) {
        this.movimento = movimento
    }

    fun getMovimento(): Boolean {
        return this.movimento
    }
}