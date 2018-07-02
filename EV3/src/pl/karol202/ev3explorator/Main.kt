package pl.karol202.ev3explorator

import pl.karol202.ev3explorator.server.ClientListener
import pl.karol202.ev3explorator.server.Server

fun main(args: Array<String>)
{
	Main()
}

class Main
{
	companion object
	{
		private const val PORT = 18533
	}

	private val server = Server(PORT, object : ClientListener
	{

	})
}