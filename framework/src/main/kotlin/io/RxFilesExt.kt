package org.synsystems.onlypass.framework.io

import io.reactivex.Completable
import io.reactivex.Single
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

/**
 * Checks if this exists.
 */
fun File.existsRx() = Single.fromCallable { exists() }

/**
 * Creates this as an empty file.
 *
 * @throws IOException if this already exists in any form
 */
fun File.createAsFile() = Completable.fromRunnable {
  assertNotExists()

  createNewFile()
}

/**
 * Creates this as a directory. The operation completes normally if the directory already exists.
 *
 * @throws IOException if this already exists as a file
 */
fun File.createAsDirectory() = Completable.fromRunnable {
  if (exists()) assertIsDirectory() else Completable.fromRunnable { mkdirs() }
}

/**
 * Deletes this if it exists. If this is a directory, all contained files and directories are deleted.
 */
fun File.deleteRx() = Completable.fromRunnable { if (exists()) FileUtils.forceDelete(this) }

/**
 * Gets the files and directories contained in this directory without recursion.
 *
 * @throws IOException if this doesn't exist
 * @throws IOException if this is a file
 */
fun File.getContainedFiles() = Single.fromCallable {
  assertExists()
  assertIsDirectory()

  FileUtils
      .listFiles(this, null, false) // null = no extension filter. false = no recursion.
      ?.toSet() ?: throw IOException("Unable to get files in $this.")
}

/**
 * Gets the files and directories contained in this directory recursively.
 *
 * @throws IOException if this doesn't exist
 * @throws IOException if this is a file
 */
fun File.getContainedFilesRecursively() = Single.fromCallable {
  assertExists()
  assertIsDirectory()

  FileUtils
      .listFiles(this, null, true) // null = no extension filter
      ?.toSet() ?: throw IOException("Unable to get files in $this.")
}

/**
 * Reads the contents of this file.
 *
 * @throws IOException if this doesn't exist
 * @throws IOException if this is a directory
 */
fun File.readBytes() = Single.fromCallable {
  assertExists()
  assertIsFile()

  FileUtils.readFileToByteArray(this) ?: throw IOException("Unable to read from $this.")
}

/**
 * Writes [bytes] to this file. All existing content in the file will be deleted regardless of the length of [bytes].
 *
 * @throws IOException if this doesn't exist
 * @throws IOException if this is a directory
 */
fun File.writeBytes(bytes: ByteArray) = Completable.fromRunnable {
  assertExists()
  assertIsFile()

  FileUtils.writeByteArrayToFile(this, bytes)
}

/**
 * Gets the size of this file in bytes.
 */
fun File.sizeInBytes() = Single.fromCallable {
  assertExists()
  assertIsFile()

  length()
}

/**
 * Checks if this is a file.
 */
fun File.isFileRx() = Single.fromCallable {
  assertExists()

  isFile
}

/**
 * Checks if this is a directory.
 */
fun File.isDirectoryRx() = Single.fromCallable {
  assertExists()

  isDirectory
}

private fun File.assertExists() {
  if (!exists()) throw IOException("$this does not exist.")
}

private fun File.assertNotExists() {
  if (exists()) throw IOException("$this exists")
}

private fun File.assertIsFile() {
  if (!isFile) throw IOException("$this is not a file.")
}

private fun File.assertIsDirectory() {
  if (!isDirectory) throw IOException("$this is not a directory.")
}