package org.synsystems.onlypass.framework.io;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * Utility for working with files and directories reactively.
 */
public class RxFiles {
  /**
   * Creates a directory, along with any required parent directories. The operation will complete successfully if
   * the directory already exists, but only if it exists as a directory and not as a file.
   *
   * @param directory
   *     the directory to create
   *
   * @return a new completable that creates the directory
   */
  @NonNull
  public Completable createDirectory(@NonNull final File directory) {
    checkNotNull(directory);

    return exists(directory).flatMapCompletable(exists -> exists ?
        checkIsDirectory(directory, makeError("Failed to create directory", directory, "Already exists as a file")) :
        Completable.fromAction(() -> FileUtils.forceMkdir(directory)));
  }

  /**
   * Creates a new empty file, along with any required parent directories. The operation will fail if the file already
   * exists (including as a directory).
   *
   * @param file
   *     the file to create
   *
   * @return a new completable that creates the file
   */
  @NonNull
  public Completable createNewFile(@NonNull final File file) {
    checkNotNull(file);

    final Completable checkDoesNotExist = checkDoesNotExist(
        file,
        makeError("Failed to create file", file, "The file/directory already exists"));

    final Completable ensureParentDirectoryExists = Single
        .fromCallable(file::getParentFile)
        .flatMapCompletable(this::createDirectory);

    final Completable createFile = Single
        .fromCallable(file::createNewFile)
        .flatMapCompletable(createdSuccessfully -> createdSuccessfully ?
            Completable.complete() :
            Completable.error(makeError("Failed to create file", file, "Reason unknown")));

    return checkDoesNotExist
        .andThen(ensureParentDirectoryExists)
        .andThen(createFile);
  }

  /**
   * Deletes a file/directory, including any and all contained files and directories. The operation will complete
   * successfully if the file/directory does not exist.
   *
   * @param file
   *     the file/directory to delete
   *
   * @return a new completable that deletes the file/directory
   */
  @NonNull
  public Completable delete(@NonNull final File file) {
    checkNotNull(file);

    return exists(file).flatMapCompletable(exists -> exists ?
        Completable.fromAction(() -> FileUtils.forceDelete(file)) :
        Completable.complete());
  }

  /**
   * Gets the files in a directory. The operation will fail if the directory is actually a file or does not exist.
   *
   * @param directory
   *     the directory to get the files from
   *
   * @return a new observable that emits the files contained in the directory
   */
  @NonNull
  public Observable<File> getFilesInDirectory(@NonNull final File directory) {
    checkNotNull(directory);

    final Completable checkExists = checkExists(
        directory,
        makeError("Cannot get files in directory", directory, "The directory does not exist."));

    final Completable checkIsDirectory = checkIsDirectory(
        directory,
        makeError("Failed to get files in", directory, "Expected a directory but found a file"));

    final Observable<File> listFiles = Observable
        .fromCallable(() -> Optional.fromNullable(directory.listFiles()))
        .flatMap(optionalFiles -> optionalFiles.isPresent() ?
            Single
                .fromCallable(optionalFiles::get)
                .map(Arrays::asList)
                .flatMapObservable(Observable::fromIterable) :
            Observable.error(makeError("Failed to get files in", directory, "Reason unknown")));

    return checkExists
        .andThen(checkIsDirectory)
        .andThen(listFiles);
  }

  /**
   * Reads the contents of a file. The operation will fail if the file does not exist or is actually a directory.
   *
   * @param file
   *     the file to read from
   *
   * @return a new single that emits the contents of the file
   */
  @NonNull
  public Single<byte[]> readBytesFromFile(@NonNull final File file) {
    checkNotNull(file);

    final Completable checkExists = checkExists(
        file,
        makeError("Cannot read bytes from file", file, "The file does not exist."));

    final Completable checkIsFile = checkIsFile(file,
        makeError("Failed to read bytes from", file, "Expected a file but found a directory"));

    final Single<byte[]> readFileContents = Single
        .just(file)
        .map(FileUtils::readFileToByteArray);

    return checkExists
        .andThen(checkIsFile)
        .andThen(readFileContents);
  }

  /**
   * Writes data to a file. The operation will fail if the file does not exist or is actually a directory.
   * <p>
   * <b>WARNING: All existing data in the file will be deleted.</b>
   *
   * @param data
   *     the data to write
   * @param file
   *     the file to write to
   *
   * @return a new completable that writes the data to the file
   */
  @NonNull
  public Completable writeBytesToFile(@NonNull final byte[] data, @NonNull final File file) {
    checkNotNull(file);
    checkNotNull(data);

    final Completable checkExists = checkExists(file, makeError("Cannot write string to", file, "File does not exist"));

    final Completable checkIsFile = checkIsFile(
        file,
        makeError("Failed to write string to", file, "Expected a file but found a directory"));

    final Completable writeToFile = Completable.fromAction(() -> FileUtils.writeByteArrayToFile(file, data));

    return checkExists
        .andThen(checkIsFile)
        .andThen(writeToFile);
  }

  /**
   * Determines if a file/directory exists.
   *
   * @param file
   *     the file/directory to check
   *
   * @return a new single that emits whether or not the file/directory exists
   */
  @NonNull
  public Single<Boolean> exists(@NonNull final File file) {
    checkNotNull(file);

    return Single.fromCallable(file::exists);
  }

  /**
   * Counts the number of bytes in a file. The operation will fail if the file does not exist or is actually a
   * directory.
   *
   * @param file
   *     the file to get the size of
   *
   * @return a new single that emits the number of bytes in the file
   */
  @NonNull
  public Single<Long> sizeInBytes(@NonNull final File file) {
    checkNotNull(file);

    final Completable checkExists = checkExists(file, makeError("Cannot get size of", file, "File does not exist"));

    final Completable checkIsFile = checkIsFile(
        file,
        makeError("Cannot get size of", file, "Expected a file but found a directory"));

    final Single<Long> getSize = Single.fromCallable(file::length);

    return checkExists
        .andThen(checkIsFile)
        .andThen(getSize);
  }

  /**
   * Determines if a file is actually a file (as opposed to a directory). The operation will fail if the file/directory
   * doesn't exist.
   *
   * @param file
   *     the file to check
   *
   * @return a new single that emits whether or not the file is actually a file
   */
  @NonNull
  public Single<Boolean> isFile(@NonNull final File file) {
    checkNotNull(file);

    final Completable checkExists = checkExists(
        file,
        new IOException(format("Cannot check if \'%1$s\' is a file. Does not exist.", file)));

    final Single<Boolean> isFile = Single.fromCallable(file::isFile);

    return checkExists.andThen(isFile);
  }

  /**
   * Determines if a directory is actually a directory (as opposed to a file). The operation will fail if the
   * file/directory doesn't exist.
   *
   * @param directory
   *     the directory to check
   *
   * @return a new single that emits whether or not the directory is actually a directory
   */
  @NonNull
  public Single<Boolean> isDirectory(@NonNull final File directory) {
    checkNotNull(directory);

    final Completable checkExists = checkExists(
        directory,
        new IOException(format("Cannot check if \'%1$s\' is a directory. Does not exist.", directory)));

    final Single<Boolean> isDirectory = Single.fromCallable(directory::isDirectory);

    return checkExists.andThen(isDirectory);
  }

  /**
   * Creates an IOException with a message in the form "${description} '${file.getAbsolutePath()}'. ${reason}.".
   * <p>
   * For example, makeError("Failed to access", someFile, "File is in use") produces an IOException with the message
   * "Failed to access '~/somedir/somefile.txt'. File is in use."
   *
   * @return the exception
   */
  @NonNull
  private IOException makeError(
      @NonNull final String description,
      @NonNull final File file,
      @NonNull final String reason) {

    return new IOException(format("%1$S \'%2$s\'. %3$s.", description, file.getAbsolutePath(), reason));
  }

  @NonNull
  private Completable checkExists(@NonNull final File file, @NonNull final Exception error) {
    return Single
        .fromCallable(file::exists)
        .flatMapCompletable(exist -> exist ?
            Completable.complete() :
            Completable.error(error));
  }

  @NonNull
  private Completable checkDoesNotExist(@NonNull final File file, @NonNull final Exception error) {
    return Single
        .fromCallable(file::exists)
        .flatMapCompletable(exist -> exist ?
            Completable.error(error) :
            Completable.complete());
  }

  @NonNull
  private Completable checkIsFile(@NonNull final File file, @NonNull final Exception error) {
    return isFile(file).flatMapCompletable(isFile -> isFile ?
        Completable.complete() :
        Completable.error(error));
  }

  @NonNull
  private Completable checkIsDirectory(@NonNull final File file, @NonNull final Exception error) {
    return isDirectory(file).flatMapCompletable(isDirectory -> isDirectory ?
        Completable.complete() :
        Completable.error(error));
  }
}