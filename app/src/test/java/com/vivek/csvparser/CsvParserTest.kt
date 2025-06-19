package com.vivek.csvparser

import com.vivek.csvparser.data.CsvParser
import com.vivek.csvparser.util.CountMismatchException
import com.vivek.csvparser.util.CsvParseException
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CsvParserTest {

    private lateinit var csvParser: CsvParser

    @Before
    fun setup() {
        csvParser = CsvParser()
    }

    @Test
    fun testValidCsvParsing() {
        val csvContent = """
            H|HDKF190320201903202020032020171931
            R|123400000000000|123400000000060|A5123456700000250|UNIPOS A5
            R|123400000000001|123400000000061|A5123456700000251|UNIPOS A5
            T|2
        """.trimIndent()

        val result = csvParser.parseFile(csvContent)

        assertEquals("HDKF190320201903202020032020171931", result.header.serverID)
        assertEquals(2, result.records.size)
        assertEquals(2, result.trailer.count)
        assertEquals("123400000000000", result.records[0].imei1)
        assertEquals("UNIPOS A5", result.records[0].deviceName)
    }

    @Test(expected = CountMismatchException::class)
    fun testCountMismatch() {
        val csvContent = """
            H|HDKF190320201903202020032020171931
            R|123400000000000|123400000000060|A5123456700000250|UNIPOS A5
            T|2
        """.trimIndent()

        csvParser.parseFile(csvContent)
    }

    @Test(expected = CsvParseException::class)
    fun testInvalidFormat() {
        val csvContent = """
            H|HDKF190320201903202020032020171931
            X|123400000000000|123400000000060|A5123456700000250|UNIPOS A5
            T|1
        """.trimIndent()

        csvParser.parseFile(csvContent)
    }
}