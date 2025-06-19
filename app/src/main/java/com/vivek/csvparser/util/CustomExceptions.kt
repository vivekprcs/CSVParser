package com.vivek.csvparser.util

class CsvParseException(message: String) : Exception(message)
class ValidationException(message: String) : Exception(message)
class CountMismatchException(message: String) : Exception(message)
class FileReadException(message: String) : Exception(message)