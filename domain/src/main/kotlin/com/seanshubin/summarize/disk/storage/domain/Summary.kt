package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.Path

data class Summary(
    val path: Path,
    val size: Long,
    val files: Int,
    val dirs: Int
) {
    fun aggregateChild(child: Summary): Summary = Summary(
        path,
        size = size + child.size,
        files = files + child.files,
        dirs = dirs + child.dirs
    )

    companion object {
        fun compose(parent: Path, children: List<Summary>): Summary {
            val initial = Summary(parent, size = 0, files = 0, dirs = 1)
            return children.fold(initial) { accumulator, child ->
                accumulator.aggregateChild(child)
            }
        }
    }
}
