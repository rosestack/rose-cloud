/*
 * Copyright © 2025 rose-group.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rose.core.io;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class TemporaryFileSystemResource extends FileSystemResource {

    public TemporaryFileSystemResource(final File file) {
        super(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FilterInputStream(super.getInputStream()) {

            @Override
            public void close() throws IOException {
                closeThenDeleteFile(this.in);
            }
        };
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public ReadableByteChannel readableChannel() throws IOException {
        val readableChannel = super.readableChannel();
        return new ReadableByteChannel() {

            @Override
            public boolean isOpen() {
                return readableChannel.isOpen();
            }

            @Override
            public void close() throws IOException {
                closeThenDeleteFile(readableChannel);
            }

            @Override
            public int read(final ByteBuffer dst) throws IOException {
                return readableChannel.read(dst);
            }
        };
    }

    private void closeThenDeleteFile(final Closeable closeable) throws IOException {
        try {
            closeable.close();
        } finally {
            deleteFile();
        }
    }

    private void deleteFile() {
        try {
            Files.delete(getFile().toPath());
        } catch (final IOException ex) {
            val msg = String.format("Failed to delete temporary heap dump file %s", getFile());
            log.warn(msg, ex);
        }
    }
}
