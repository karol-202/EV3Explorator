package pl.karol202.ev3explorator.server

import pl.karol202.ev3explorator.communication.InputPacket
import kotlin.reflect.KClass

interface InputPacketFromClient : InputPacket<ClientListener>
{
	enum class Type(override val clazz: KClass<InputPacket<ClientListener>>) : InputPacket.Type<ClientListener>
	{

	}
}