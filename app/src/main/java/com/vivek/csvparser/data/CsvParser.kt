package com.vivek.csvparser.data

import com.vivek.csvparser.util.CountMismatchException
import com.vivek.csvparser.util.CsvParseException
import com.vivek.csvparser.util.ValidationException
import com.vivek.csvparser.model.CsvHeader
import com.vivek.csvparser.model.CsvRecord
import com.vivek.csvparser.model.CsvTrailer
import com.vivek.csvparser.model.ParsedCsvData

class CsvParser {

    fun parseFile(content: String): ParsedCsvData {
        val lines = content.trim().split('\n').map { it.trim() }

        if (lines.isEmpty()) {
            throw CsvParseException("File is empty")
        }

        var header: CsvHeader? = null
        val records = mutableListOf<CsvRecord>()
        var trailer: CsvTrailer? = null

        for (line in lines) {
            when {
                line.startsWith("H|") -> {
                    if (header != null) {
                        throw CsvParseException("Multiple header lines found")
                    }
                    header = parseHeader(line)
                }
                line.startsWith("R|") -> {
                    records.add(parseRecord(line))
                }
                line.startsWith("T|") -> {
                    if (trailer != null) {
                        throw CsvParseException("Multiple trailer lines found")
                    }
                    trailer = parseTrailer(line)
                }
                else -> {
                    throw CsvParseException("Invalid line format: $line")
                }
            }
        }

        header ?: throw CsvParseException("Header line not found")
        trailer ?: throw CsvParseException("Trailer line not found")

        validateData(records, trailer)

        return ParsedCsvData(header, records, trailer)
    }

    private fun parseHeader(line: String): CsvHeader {
        val parts = line.split('|')
        if (parts.size != 2) {
            throw CsvParseException("Invalid header format: $line")
        }

        return CsvHeader(
            recordType = parts[0],
            serverID = parts[1]
        )
    }

    private fun parseRecord(line: String): CsvRecord {
        val parts = line.split('|')
        if (parts.size != 5) {
            throw CsvParseException("Invalid record format: $line")
        }

        return CsvRecord(
            recordType = parts[0],
            imei1 = parts[1],
            imei2 = parts[2],
            serialNumber = parts[3],
            deviceName = parts[4]
        )
    }

    private fun parseTrailer(line: String): CsvTrailer {
        val parts = line.split('|')
        if (parts.size != 2) {
            throw CsvParseException("Invalid trailer format: $line")
        }

        val count = parts[1].toIntOrNull()
            ?: throw CsvParseException("Invalid count in trailer: ${parts[1]}")

        return CsvTrailer(
            recordType = parts[0],
            count = count
        )
    }

    private fun validateData(records: List<CsvRecord>, trailer: CsvTrailer) {
        if (records.size != trailer.count) {
            throw CountMismatchException(
                "Record count mismatch: found ${records.size}, expected ${trailer.count}"
            )
        }

        records.forEachIndexed { index, record ->
            if (record.imei1.isBlank() || record.imei2.isBlank() ||
                record.serialNumber.isBlank() || record.deviceName.isBlank()) {
                throw ValidationException("Empty field found in record ${index + 1}")
            }
        }
    }
}