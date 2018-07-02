package pl.karol202.ev3explorator.communication

import com.beust.klaxon.Klaxon
import java.io.StringReader

class PacketFactory<T : InputPacket.Type<L>, L : PacketListener>(private val availableTypes: Array<T>)
{
	private val klaxon = Klaxon()

	fun createFromJson(json: String) =
			StringReader(json).use { reader ->
				val jsonObject = klaxon.parseJsonObject(reader)
				val type = availableTypes.find { it.name == jsonObject["type"] }?.clazz ?: return@use null
				klaxon.fromJsonObject(jsonObject, type.java, type) as? InputPacket<L>
			}
}