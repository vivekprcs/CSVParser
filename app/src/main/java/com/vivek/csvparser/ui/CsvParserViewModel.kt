package com.vivek.csvparser.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.csvparser.util.FileReadException
import com.vivek.csvparser.data.CsvParser
import com.vivek.csvparser.data.JsonConverter
import com.vivek.csvparser.ui.state.CsvParserUiState
import com.vivek.csvparser.ui.state.ParseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class CsvParserViewModel :ViewModel() {
    private val _uiState = MutableStateFlow(CsvParserUiState())
    val uiState: StateFlow<CsvParserUiState> = _uiState.asStateFlow()

    private val csvParser = CsvParser()
    private val jsonConverter = JsonConverter()

    fun processFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val content = readFileContent(context, uri)
                val parsedData = csvParser.parseFile(content)
                val jsonOutput = jsonConverter.convertToJson(parsedData.header, parsedData.records)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    result = ParseResult(jsonOutput, parsedData.records.size)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun readFileContent(context: Context, uri: Uri): String {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText()
            }
        } ?: throw FileReadException("Unable to read file")
    }

}