package com.vivek.csvparser.ui.state

data class CsvParserUiState(
    val isLoading: Boolean = false,
    val result: ParseResult? = null,
    val error: String? = null
)

data class ParseResult(
    val jsonOutput: String,
    val deviceCount: Int
)
