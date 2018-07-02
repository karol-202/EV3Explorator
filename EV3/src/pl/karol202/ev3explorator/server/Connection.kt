package pl.karol202.ev3explorator.server

import java.net.ServerSocket
import java.net.Socket

class Connection(private val socket: Socket)
{
	companion object
	{
		fun create(serverSocket: ServerSocket) = Connection(serverSocket.accept())
	}

	val inputStream = socket.getInputStream()
	val outputStream = socket.getOutputStream()

	val open: Boolean
		get() = socket.isConnected && !socket.isClosed

	fun close()
	{
		socket.close()
	}
}