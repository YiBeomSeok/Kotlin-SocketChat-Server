import socket.ChatServer
import socket.FileServer
import kotlin.concurrent.thread

fun main() {
    val chatServer = ChatServer()
    chatServer.startAccept()
}