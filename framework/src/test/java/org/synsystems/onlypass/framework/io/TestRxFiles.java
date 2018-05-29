package org.synsystems.onlypass.framework.io;

import com.google.common.collect.ImmutableSet;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class TestRxFiles {
  @Rule
  public final TemporaryFolder temporaryFolder = new TemporaryFolder();

  private RxFiles rxFiles;

  @Before
  public void setup() {
    rxFiles = new RxFiles();
  }

  @Test
  public void testCreateDirectory_directoryAlreadyExistsAsFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .createDirectory(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();

    // Ensure file was not deleted
    assertThat(file.exists(), is(true));
  }

  @Test
  public void testCreateDirectory_directoryAlreadyExistsAsDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .createDirectory(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(dir.exists(), is(true));
  }

  @Test
  public void testCreateDirectory_parentDirectoryDoesNotExist() {
    final File parent = new File(temporaryFolder.getRoot(), "parent");
    final File child = new File(parent, "child");

    rxFiles
        .createDirectory(child)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(parent.exists(), is(true));
    assertThat(child.exists(), is(true));
  }

  @Test
  public void testCreateDirectory_directoryDoesNotExist() {
    final File parent = new File(temporaryFolder.getRoot(), "parent");
    final File child = new File(parent, "child");

    parent.mkdirs();

    rxFiles
        .createDirectory(child)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(parent.exists(), is(true));
    assertThat(child.exists(), is(true));
  }

  @Test
  public void testCreateNewFile_fileAlreadyExistsAsFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .createNewFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();

    // Ensure file was not deleted
    assertThat(file.exists(), is(true));
  }

  @Test
  public void testCreateNewFile_fileAlreadyExistsAsDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .createNewFile(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();

    // Ensure directory was not deleted
    assertThat(dir.exists(), is(true));
  }

  @Test
  public void testCreateNewFile_parentDirectoryDoesNotExist() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");
    final File file = new File(dir, "file");

    rxFiles
        .createNewFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(dir.exists(), is(true));
    assertThat(file.exists(), is(true));
  }

  @Test
  public void testCreateNewFile_fileDoesNotExist() {
    final File file = new File(temporaryFolder.getRoot(), "file");

    rxFiles
        .createNewFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(file.exists(), is(true));
  }

  @Test
  public void testDelete_emptyFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .delete(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(file.exists(), is(false));
  }

  @Test
  public void testDelete_nonExistentFile() {
    final File file = new File(temporaryFolder.getRoot(), "file");

    rxFiles
        .delete(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(file.exists(), is(false));
  }

  @Test
  public void testDelete_emptyDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .delete(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(dir.exists(), is(false));
  }

  @Test
  public void testDelete_nonExistentDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    rxFiles
        .delete(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(dir.exists(), is(false));
  }

  @Test
  public void testDelete_nonEmptyDirectory() throws IOException {
    final File root = new File(temporaryFolder.getRoot(), "root");
    final File dir1 = new File(root, "dir1");
    final File dir2 = new File(root, "dir2");
    final File file = new File(dir2, "file");

    root.mkdirs();
    dir1.mkdirs();
    dir2.mkdirs();
    file.createNewFile();

    rxFiles
        .delete(root)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(root.exists(), is(false));
    assertThat(dir1.exists(), is(false));
    assertThat(dir2.exists(), is(false));
    assertThat(file.exists(), is(false));
  }

  @Test
  public void testGetFilesInDirectory_directoryIsActuallyAFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .getFilesInDirectory(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testGetFilesInDirectory_nonExistentDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    rxFiles
        .getFilesInDirectory(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testGetFilesInDirectory_emptyDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .getFilesInDirectory(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();
  }

  @Test
  public void testGetFilesInDirectory_nonEmptyDirectory() throws IOException {
    final File rootDir = new File(temporaryFolder.getRoot(), "root");
    final File level1Dir = new File(rootDir, "dir");
    final File level2Dir = new File(level1Dir, "dir");
    final File level1File1 = new File(rootDir, "file1");
    final File level1File2 = new File(rootDir, "file2");
    final File level2File = new File(level1Dir, "file");

    rootDir.mkdirs();
    level1Dir.mkdirs();
    level2Dir.mkdirs();
    level1File1.createNewFile();
    level1File2.createNewFile();
    level2File.createNewFile();

    rxFiles
        .getFilesInDirectory(rootDir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValueSet(ImmutableSet.of(level1Dir, level1File1, level1File2))
        .assertComplete();
  }

  @Test
  public void testReadBytesFromFile_fileIsActuallyADirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .readBytesFromFile(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testReadBytesFromFile_emptyFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .readBytesFromFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(value -> value.length == 0)
        .assertComplete();
  }

  @Test
  public void testReadBytesFromFile_nonEmptyFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");
    final byte[] data = new byte[]{1, 2, 3, 0};

    file.createNewFile();

    FileUtils.writeByteArrayToFile(file, data);

    rxFiles
        .readBytesFromFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(value -> Arrays.equals(data, value))
        .assertComplete();
  }

  @Test
  public void testWriteBytesToFile_fileIsActuallyADirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .writeBytesToFile(new byte[]{}, dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testWriteBytesToFile_emptyFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    final byte[] data = new byte[]{1, 2, 3, 0};

    rxFiles
        .writeBytesToFile(data, file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(FileUtils.readFileToByteArray(file), is(data));
  }

  @Test
  public void testWriteBytesToFile_nonEmptyFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    FileUtils.writeByteArrayToFile(file, new byte[]{0, 1, 2, 3, 4, 5, 6});

    final byte[] data = new byte[]{1, 2, 3, 0};

    rxFiles
        .writeBytesToFile(data, file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();

    assertThat(FileUtils.readFileToByteArray(file), is(data));
  }

  @Test
  public void testExists_existentFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .exists(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(true)
        .assertComplete();
  }

  @Test
  public void testExists_existentDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .exists(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(true)
        .assertComplete();
  }

  @Test
  public void testExists_nonExistentFile() {
    final File file = new File(temporaryFolder.getRoot(), "file");

    rxFiles
        .exists(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(false)
        .assertComplete();
  }

  @Test
  public void testExists_nonExistentDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    rxFiles
        .exists(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(false)
        .assertComplete();
  }

  @Test
  public void testSizeInBytes_directory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .sizeInBytes(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testSizeInBytes_emptyFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .sizeInBytes(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(0L)
        .assertComplete();
  }

  @Test
  public void testSizeInBytes_nonEmptyFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    FileUtils.writeByteArrayToFile(file, new byte[]{0, 1, 2});

    rxFiles
        .sizeInBytes(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(3L)
        .assertComplete();
  }

  @Test
  public void testIsFile_existentFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .isFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(true)
        .assertComplete();
  }

  @Test
  public void testIsFile_existentDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .isFile(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(false)
        .assertComplete();
  }

  @Test
  public void testIsFile_nonExistentFile() {
    final File file = new File(temporaryFolder.getRoot(), "file");

    rxFiles
        .isFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testIsDirectory_existentFile() throws IOException {
    final File file = new File(temporaryFolder.getRoot(), "file");

    file.createNewFile();

    rxFiles
        .isDirectory(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(false)
        .assertComplete();
  }

  @Test
  public void testIsDirectory_existentDirectory() {
    final File dir = new File(temporaryFolder.getRoot(), "dir");

    dir.mkdirs();

    rxFiles
        .isDirectory(dir)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(true)
        .assertComplete();
  }

  @Test
  public void testIsDirectory_nonExistentFile() {
    final File file = new File(temporaryFolder.getRoot(), "file");

    rxFiles
        .isFile(file)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(IOException.class)
        .assertNoValues()
        .assertNotComplete();
  }
}