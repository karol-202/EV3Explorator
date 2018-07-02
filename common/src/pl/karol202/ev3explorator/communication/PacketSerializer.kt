package pl.karol202.ev3explorator.communication

import com.beust.klaxon.Klaxon

class PacketSerializer
{
	private val klaxon = Klaxon()

	fun toJson(packet: OutputPacket) = klaxon.toJsonString(packet)
}