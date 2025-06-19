package com.vivek.csvparser.data

import com.vivek.csvparser.model.CsvHeader
import com.vivek.csvparser.model.CsvRecord
import com.vivek.csvparser.model.DeviceDetails
import com.vivek.csvparser.model.DeviceLine
import com.vivek.csvparser.model.ServerDevices
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class JsonConverter {

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun convertToJson(header: CsvHeader, records: List<CsvRecord>): String {
        val deviceLines = records.map { record ->
            DeviceLine(
                imei1 = record.imei1,
                imei2 = record.imei2,
                serialnumber = record.serialNumber,
                deviceName = record.deviceName
            )
        }

        val serverDevices = ServerDevices(
            serverID = header.serverID,
            deviceLines = deviceLines
        )

        val deviceDetails = DeviceDetails(
            devicedetails = listOf(serverDevices)
        )

        return gson.toJson(deviceDetails)
    }
}