package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.util.stream.Stream

object FilesDelegate:FilesContract {
    override fun isDirectory(path: Path, vararg options: LinkOption): Boolean =
        Files.isDirectory(path, *options)

    override fun isRegularFile(path: Path, vararg options: LinkOption): Boolean =
        Files.isRegularFile(path, *options)

    override fun size(path: Path): Long =
        Files.size(path)

    override fun list(dir: Path): Stream<Path> =
        Files.list(dir)
}
