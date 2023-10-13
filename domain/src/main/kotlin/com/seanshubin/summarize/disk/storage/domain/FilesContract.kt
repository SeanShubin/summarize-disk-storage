package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.LinkOption
import java.nio.file.Path
import java.util.stream.Stream

interface FilesContract {
    fun isDirectory(path: Path, vararg options: LinkOption): Boolean
    fun isRegularFile(path: Path, vararg options: LinkOption): Boolean
    fun size(path: Path): Long
    fun list(dir: Path): Stream<Path>
}
