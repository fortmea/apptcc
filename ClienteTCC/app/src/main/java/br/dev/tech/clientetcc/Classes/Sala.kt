package Classes

import io.ktor.network.sockets.*
import java.io.Serializable

class Sala : Serializable {
    private var addresses: MutableSet<SocketAddress> = mutableSetOf()
    private var jogo: Jogo = Jogo();

    constructor(addresses: MutableSet<SocketAddress>, jogo: Jogo) {
        this.addresses = addresses
        this.jogo = jogo
    }

    fun setAddresses(mAddresses: MutableSet<SocketAddress>) {
        this.addresses = mAddresses
    }

}