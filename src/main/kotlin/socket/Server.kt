package socket

import java.io.Serializable
import java.net.ServerSocket
import java.net.SocketException
import kotlin.concurrent.thread

class Server : Serializable {
    private lateinit var serverSocket: ServerSocket
    private val clientSockets = mutableListOf<ClientSocket>()

    init {
        connect()
    }

    fun isClose(): Boolean {
        if (this::serverSocket.isInitialized) {
            return serverSocket.isClosed
        }
        return true
    }

    fun startAccept() {
        while (true) {
            val acceptedSocket = serverSocket.accept()
            val newClientSocket = ClientSocket(acceptedSocket)
            println("client accept: ${newClientSocket.inetAddress()}")
            clientSockets.add(newClientSocket)
            readData(newClientSocket)
        }
    }

    private fun connect() {
        println("server connect start!! port : $PORT_NUMBER")
        serverSocket = ServerSocket(PORT_NUMBER)
        println("ip : ${serverSocket.inetAddress}")
        println("lsa = ${serverSocket.localSocketAddress}")
    }

    private fun connectClose() {
        serverSocket.close()
    }

    private fun readData(clientSocket: ClientSocket) {
        thread {
            while (true) {
                val inputStream = clientSocket.getInputStream()
                try {
                    if (inputStream.available() > 0) {
                        inputStream.bufferedReader(Charsets.UTF_8).forEachLine {
                            println(it)
                            sendDataToAllClient(it)
                        }
                        clientSocket.close()
                        clientSockets.remove(clientSocket)
                    }
                } catch (_: SocketException) {
                }
            }
        }
    }

    private fun sendDataToAllClient(data: String) {
        if (data == "") {
            return
        }
        println("size = ${clientSockets.count()}")
        for (index in 0 until clientSockets.count()) {
            if(clientSockets[index].isClosed()) {
                clientSockets.removeAt(index)
                continue
            }
            println("모든 client에게 보내는중 : $data")
            clientSockets[index].receiveData(data)
        }
    }

    companion object {
        const val PORT_NUMBER = 8082
    }
}