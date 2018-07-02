package pl.karol202.ev3explorator.server

import pl.karol202.ev3explorator.communication.OutputPacket
import pl.karol202.ev3explorator.communication.PacketFactory
import pl.karol202.ev3explorator.communication.PacketSerializer
import java.io.IOException
import java.net.ServerSocket
import java.nio.ByteBuffer
import javax.naming.CommunicationException

class Server(
		port: Int,
		private val listener: ClientListener
) {
	private val serverSocket = ServerSocket(port)
	private val packetFactory = PacketFactory(InputPacketFromClient.Type.values())
	private val packetSerializer = PacketSerializer()

	private var running = false
	private var connection: Connection? = null

	fun start() = Thread { run() }.start()

	private fun run()
	{
		println("Starting server")
		running = true
		while(running) handleClient()
	}

	private fun handleClient()
	{
		try
		{
			connect()
			listen()
		}
		catch(e: CommunicationException)
		{
			println(e)
		}
		catch(e: IOException)
		{
			println(e)
		}
		finally
		{
			close()
		}
	}

	private fun connect()
	{
		println("Waiting for client")
		connection = Connection.create(serverSocket)
		println("Connected to client")
	}

	private fun listen()
	{
		while(connection?.open == true)
		{
			val length = readInt() ?: throw CommunicationException("Cannot read packet length.")
			val content = readString(length) ?: throw CommunicationException("Cannot read packet content.")
			val packet = packetFactory.createFromJson(content)
			packet?.execute(listener)
		}
	}

	private fun readInt(): Int?
	{
		val bytes = ByteArray(4)
		if(connection?.inputStream?.read(bytes) != 4) return null
		val buffer = ByteBuffer.wrap(bytes)
		return buffer.int
	}

	private fun readString(length: Int): String?
	{
		var wait = true
		Thread {
			Thread.sleep(1000)
			if(!wait) return@Thread
			wait = false
			close()
			throw CommunicationException("String read timeout")
		}.start()

		val bytes = ByteArray(length)
		var read = 0
		while(read != length) read += connection?.inputStream?.read(bytes) ?: break
		wait = false
		return if(read == length) String(bytes) else null
	}

	private fun close()
	{
		connection?.close()
		connection = null
	}

	fun sendPacket(packet: OutputPacket)
	{
		val content = packetSerializer.toJson(packet)
		writeInt(content.length)
		writeString(content)
	}

	private fun writeInt(value: Int)
	{
		val bytes = ByteBuffer.allocate(4)
		bytes.putInt(value)
		connection?.outputStream?.write(bytes.array()) ?: throw CommunicationException("No connection")
	}

	private fun writeString(value: String)
	{
		connection?.outputStream?.write(value.toByteArray()) ?: throw CommunicationException("No connection")
	}
}