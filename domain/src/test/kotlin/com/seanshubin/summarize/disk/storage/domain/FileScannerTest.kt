package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

class FileScannerTest {
    @Test
    fun singleFile() {
        // given
        val pathName = "file.txt"
        val path = Paths.get(pathName)
        val summary = Summary(path, size = 1000, files = 1, dirs = 0)
        val root = Element(pathName, fileType = FileType.FILE, size = 1000)
        val files = FilesStub(root)
        val fileScanner = FileScanner(files)
        val expected = listOf(summary)

        // when
        val actual = fileScanner.scan(path)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun emptyDir() {
        // given
        val pathName = "dir"
        val path = Paths.get(pathName)
        val summary = Summary(path, size = 0, files = 0, dirs = 1)
        val root = Element(pathName, fileType = FileType.DIRECTORY)
        val files = FilesStub(root)
        val fileScanner = FileScanner(files)
        val expected = listOf(summary)

        // when
        val actual = fileScanner.scan(path)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun dirWithFiles() {
        // given
        val subDir = Element("base/sub-dir", FileType.DIRECTORY)
        val regularFile = Element("base/regular-file.txt", FileType.FILE)
        val otherFile = Element("base/other-file.txt", FileType.OTHER)
        val children = listOf(subDir, regularFile, otherFile)
        val pathName = "base"
        val root = Element(pathName, fileType = FileType.DIRECTORY, children = children)
        val path = Paths.get(pathName)
        val files = FilesStub(root)
        val fileScanner = FileScanner(files)
        val expected = listOf(
            Summary(Paths.get("base"), size=0, files=1, dirs=2),
            Summary(Paths.get("base/sub-dir"), size=0, files=0, dirs=1),
            Summary(Paths.get("base/regular-file.txt"), size=0, files=1, dirs=0)
        )

        // when
        val actual = fileScanner.scan(path)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun notFileOrDir() {
        // given
        val pathName = "symlink"
        val path = Paths.get(pathName)
        val root = Element(pathName, fileType = FileType.OTHER)
        val files = FilesStub(root)
        val fileScanner = FileScanner(files)
        val expected = emptyList<Summary>()

        // when
        val actual = fileScanner.scan(path)

        // then
        assertEquals(expected, actual)
    }

    enum class FileType {
        FILE,
        DIRECTORY,
        OTHER
    }

    data class Element(val pathName:String, val fileType:FileType, val size:Long = 0, val children:List<Element> = emptyList()){
        fun findByPath(path:Path):Element? {
            if(pathName == path.toString()) return this
            else children.forEach {
                val result = it.findByPath(path)
                if(result != null) return result
            }
            return null
        }
    }

    class FilesStub(private val root:Element) : FilesContract {
        override fun isDirectory(path: Path, vararg options: LinkOption): Boolean {
            return findElement(path).fileType == FileType.DIRECTORY
        }

        override fun isRegularFile(path: Path, vararg options: LinkOption): Boolean {
            return findElement(path).fileType == FileType.FILE
        }

        override fun size(path: Path): Long =
            findElement(path).size

        override fun list(dir: Path): Stream<Path> =
            findElement(dir).children.map{Paths.get(it.pathName)}.stream()

        private fun findElement(path:Path):Element = root.findByPath(path)!!
    }
}