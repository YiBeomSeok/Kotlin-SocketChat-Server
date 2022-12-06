package socket

import java.io.File
import java.net.ServerSocket
import java.net.SocketException
import kotlin.concurrent.thread

class FileServer {
    private lateinit var serverSocket: ServerSocket
    private val clientSockets = mutableListOf<ClientSocket>()

    init {
        connect()
    }

    private fun connect() {
        println("File server connect start!! port : $PORT_NUMBER")
        serverSocket = ServerSocket(PORT_NUMBER)
        println("ip : ${serverSocket.inetAddress}")
        println("lsa = ${serverSocket.localSocketAddress}")
        print(listFiles())
    }

    fun startAccept() {
        while (true) {
            val acceptedSocket = serverSocket.accept()
            val newClientSocket = ClientSocket(acceptedSocket)
            println("[File Server] client accept: ${newClientSocket.inetAddress()}")
            clientSockets.add(newClientSocket)
            println("보낼 파일: ${getFileAt(0)}")
            getFileAt(0)?.let { newClientSocket.receiveFileData(it) }
            readData(newClientSocket)
        }
    }

    private fun readData(clientSocket: ClientSocket) {
        thread {
            while (true) {
                val inputStream = clientSocket.getInputStream()
                try {
                    if (inputStream.available() > 0) {
                        inputStream.bufferedReader(Charsets.UTF_8).forEachLine {
                            println(it)
                        }
                        clientSocket.close()
                        clientSockets.remove(clientSocket)
                    }
                } catch (_: SocketException) {
                }
            }
        }
    }

    private fun getFileAt(index: Int): File? {
        val files = File(FILE_PATH).listFiles() ?: return null
        return files[index]
    }

    private fun listFiles(): String {
        val files = File(FILE_PATH).listFiles()
        val stringBuilder = StringBuilder("====File List====\n")

        if(files == null) return stringBuilder.toString()
        for(file in files) {
            stringBuilder.append(file.name + "\n")
        }
        return stringBuilder.toString()
    }

    companion object {
        const val PORT_NUMBER = 8083
        const val FILE_PATH = "./communicationFiles/"
    }
}