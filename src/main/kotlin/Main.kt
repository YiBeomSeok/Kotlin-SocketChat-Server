import socket.Server

fun main() {
    val server = Server()
    println("accept 시작")
    server.startAccept()
}