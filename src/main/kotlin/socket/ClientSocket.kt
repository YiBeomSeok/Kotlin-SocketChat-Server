package socket

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.lang.StringBuilder
import java.net.InetAddress
import java.net.Socket
import java.nio.charset.Charset

class ClientSocket (
    private val acceptedSocket: Socket
) {
    private val inputStream: DataInputStream = DataInputStream(acceptedSocket.getInputStream())
    private val outputStream: DataOutputStream = DataOutputStream(acceptedSocket.getOutputStream())

    fun receiveData(data: String) {
        println("clientSocket receiveData : $data")
        outputStream.write(
            (data + "\n").toByteArray(Charsets.UTF_8)
        )
        outputStream.flush()
    }

    fun getInputStream(): InputStream {
        return inputStream
    }

    fun close() {
        acceptedSocket.close()
    }

    fun isClosed(): Boolean {
        if(!acceptedSocket.isConnected || acceptedSocket.isClosed) {
            acceptedSocket.close()
            return true
        }
        return false
    }

    fun inetAddress(): InetAddress {
        return acceptedSocket.inetAddress
    }
}