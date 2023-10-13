package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.Path

interface Scanner {
    fun scan(path: Path): List<Summary>
}
