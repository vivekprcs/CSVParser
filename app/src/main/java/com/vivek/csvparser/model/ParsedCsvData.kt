package com.vivek.csvparser.model

data class CsvHeader(val recordType: String, val serverID: String)
data class CsvRecord(
    val recordType: String,
    val imei1: String,
    val imei2: String,
    val serialNumber: String,
    val deviceName: String
)
data class CsvTrailer(val recordType: String, val count: Int)

data class ParsedCsvData(
    val header: CsvHeader,
    val records: List<CsvRecord>,
    val trailer: CsvTrailer
)
