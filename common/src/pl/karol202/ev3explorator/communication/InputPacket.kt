package pl.karol202.ev3explorator.communication

import kotlin.reflect.KClass

interface InputPacket<in L : PacketListener>
{
	interface Type<L : PacketListener>
	{
		val name: String
		val clazz: KClass<InputPacket<L>>
	}

	fun execute(listener: L)
}