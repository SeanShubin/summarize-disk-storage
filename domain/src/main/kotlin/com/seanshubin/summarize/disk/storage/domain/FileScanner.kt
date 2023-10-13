package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.Path

class FileScanner(private val files: FilesContract) : Scanner {
    override fun scan(path: Path): List<Summary> =
        when {
            files.isDirectory(path) -> {
                val children = loadChildrenFromDir(path)
                val summaries = children.mapNotNull(::loadSummary)
                val topSummary = Summary.compose(path, summaries)
                listOf(topSummary) + summaries
            }
            files.isRegularFile(path) -> listOf(loadSummary(path)!!)
            else -> emptyList()
        }

    private fun loadChildrenFromDir(dir: Path): List<Path> =
        files.list(dir).use { it.toList() }

    private fun loadSummary(path: Path): Summary? =
        when {
            files.isDirectory(path) -> loadSummaryFromDir(path)
            files.isRegularFile(path) -> loadSummaryFromFile(path)
            else -> null
        }

    private fun loadSummaryFromFile(file: Path): Summary {
        val size = files.size(file)
        val files = 1
        val dirs = 0
        return Summary(file, size, files, dirs)
    }

    private fun loadSummaryFromDir(dir: Path): Summary {
        val children = loadChildrenFromDir(dir)
        val summaries = children.mapNotNull(::loadSummary)
        return Summary.compose(dir, summaries)
    }
}
