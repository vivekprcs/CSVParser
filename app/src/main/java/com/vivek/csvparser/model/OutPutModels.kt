package com.vivek.csvparser.model

data class DeviceDetails(val devicedetails: List<ServerDevices>)
data class ServerDevices(val serverID: String, val deviceLines: List<DeviceLine>)
data class DeviceLine(
    val imei1: String,
    val imei2: String,
    val serialnumber: String,
    val deviceName: String
)