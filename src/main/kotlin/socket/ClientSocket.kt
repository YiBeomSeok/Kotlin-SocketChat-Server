package socket

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.StringBuilder
import java.net.InetAddress
import java.net.Socket
import java.nio.charset.Charset

class ClientSocket(
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

    fun receiveFileData(file: File) {
        val bufferedOutputStream = BufferedOutputStream(acceptedSocket.getOutputStream())
        val fileInputStream = FileInputStream(file)
        val bufferedInputStream = BufferedInputStream(fileInputStream)

        println("보낼 파일: $file")
        var bytes = 0
        while (true) {
            bytes = bufferedInputStream.read()
            if (bytes == -1) {
                break
            }
            bufferedOutputStream.write(bytes)
            println("전송 완료: $bytes")
        }
        bufferedOutputStream.flush()
        println("연결 닫기")
        acceptedSocket.close()
    }

    fun getInputStream(): InputStream {
        return inputStream
    }

    fun close() {
        inputStream.close()
        outputStream.close()
        acceptedSocket.close()
    }

    fun isClosed(): Boolean {
        if (!acceptedSocket.isConnected || acceptedSocket.isClosed) {
            acceptedSocket.close()
            return true
        }
        return false
    }

    fun inetAddress(): InetAddress {
        return acceptedSocket.inetAddress
    }
}