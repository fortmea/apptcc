package Classes

import io.ktor.network.sockets.*
import java.io.Serializable

class Sala : Serializable {
    private var addresses: MutableSet<SocketAddress> = mutableSetOf()
    private var jogo: Jogo = Jogo();
    private var jogadores: Int = 0;

    constructor(addresses: MutableSet<SocketAddress>, jogo: Jogo) {
        this.addresses = addresses
        this.jogo = jogo
    }

    fun setJogadores(int: Int) {
        this.jogadores = int
    }

    fun getJogadores(): Int {
        return this.jogadores
    }

    fun getAddresses(): MutableSet<SocketAddress> {
        return this.addresses
    }

    fun setAddresses(mAddresses: MutableSet<SocketAddress>) {
        this.addresses = mAddresses
    }

    fun getJogo(): Jogo {
        return this.jogo
    }


}