package com.github.linggify.attic.lwjgl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.linggify.attic.IFileManager;
import com.github.linggify.attic.exceptions.AtticRuntimeException;

/**
 * LwjglFileManager is a {@link IFileManager} on the Lwjgl-platform
 *
 * @author Freddy
 */
public class LwjglFileManager implements IFileManager {

    private Path mLocalRoot;

    /**
     * Creates a new {@link LwjglFileManager}
     */
    public LwjglFileManager() {
        mLocalRoot = Paths.get(System.getProperty("user.dir"));
    }


    @Override
    public String getLocalPath() {
        return mLocalRoot.toString();
    }

    @Override
    public IFileHandle getLocal(String path) throws AtticRuntimeException {
        return new LwjglFileHandle(mLocalRoot.resolve(path).toFile());
    }

    @Override
    public IFileHandle getAbsolute(String path) throws AtticRuntimeException {
        return new LwjglFileHandle(new File(path));
    }

    /**
     * LwjglFileHandle is used as a wrapper for java-io on the Lwjgl-Platform
     *
     * @author Freddy
     */
    private class LwjglFileHandle implements IFileHandle {

        private File mFile;

        /**
         * Creates a new LwjglFileHandle with the given File
         *
         * @param file
         */
        LwjglFileHandle(File file) {
            mFile = file;
        }

        @Override
        public String getName() {
            return mFile.getName().split("[.]")[0];
        }

        @Override
        public String getEnding() {
            if (!isDirectory())
                return mFile.getName().split("[.]")[1];
            else return "";
        }

        @Override
        public boolean exists() {
            return mFile.exists();
        }

        @Override
        public boolean isDirectory() {
            return mFile.isDirectory();
        }

        @Override
        public IFileHandle parent() {
            if (mFile.getParentFile() == null) return null;

            return new LwjglFileHandle(mFile.getParentFile());
        }

        @Override
        public IFileHandle child(String path) {
            if (isDirectory()) return null;

            return new LwjglFileHandle(new File(mFile, path));
        }

        @Override
        public String readString() throws AtticRuntimeException {
            if (!exists())
                throw new AtticRuntimeException("The file " + mFile.getPath() + "does not exist");

            StringBuilder result = new StringBuilder();
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(mFile));
                String line = null;
                boolean flag = false;
                while ((line = in.readLine()) != null) {
                    result.append(flag ? "\n" : "").append(line);
                    flag = true;
                }
            } catch (IOException e) {
                throw new AtticRuntimeException("Failed reading file " + mFile.getPath());
            } finally {
                try {
                    if (in != null)
                        in.close();
                } catch (IOException e) {
                    //do nothing
                }
            }

            return result.toString();
        }

        @Override
        public void writeString(String value) throws AtticRuntimeException {
            if (!exists())
                throw new AtticRuntimeException("The file " + mFile.getPath() + "does not exist");

            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(mFile));
                out.write(value);
            } catch (IOException e) {
                throw new AtticRuntimeException("Failed to write to file " + mFile.getPath());
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }

        @Override
        public InputStream openInput() throws AtticRuntimeException {
            if (!exists())
                throw new AtticRuntimeException("The file " + mFile.getPath() + "does not exist");

            try {
                return new FileInputStream(mFile);
            } catch (IOException e) {
                throw new AtticRuntimeException("Could not open InputStream to file " + mFile.getPath());
            }
        }

        @Override
        public OutputStream openOutput() throws AtticRuntimeException {
            if (!exists())
                throw new AtticRuntimeException("The file " + mFile.getPath() + "does not exist");

            try {
                return new FileOutputStream(mFile);
            } catch (IOException e) {
                throw new AtticRuntimeException("Could not open InputStream to file " + mFile.getPath());
            }
        }
    }
}
