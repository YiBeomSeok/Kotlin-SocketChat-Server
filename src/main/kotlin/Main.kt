import socket.ChatServer
import socket.FileServer
import kotlin.concurrent.thread

fun main() {
    val chatServer = ChatServer()
    val fileServer = FileServer()

    thread {
        chatServer.startAccept()
    }
    thread {
        fileServer.startAccept()
    }
}