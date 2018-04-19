/*
 * Copyright (c) 2018, chris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package bapers.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author chris
 */
public class BackupService {

    /**
     *
     */
    protected static final String CONNSTR = "--server=root:haddockexecellipsis@localhost";

    /**
     *
     */
    public static final DateFormat BACKUPDATE = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     *
     */
    protected static final File BACKUP = new File("backups");    
    
    /**
     *
     * @param file
     * @throws IOException
     */
    public static void restoreFromBackup(File file) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("mysqldbimport", "-i", "both",
                CONNSTR, file.getAbsolutePath());
        if (!file.exists())
            throw new IOException("cannot load file");
        pb.start();
    }
    
    /**
     *
     * @throws IOException
     */
    public static void backup() throws IOException { 
        ProcessBuilder pb = new ProcessBuilder("mysqldbexport", CONNSTR, "-e", 
                "both", "bapers");
        
        Process p = pb.start();
        InputStream is = p.getInputStream();
        if (!BACKUP.exists()) {
            BACKUP.mkdir();
        }
        File file = new File(BACKUP.getAbsoluteFile()+"/"
                + BACKUPDATE.format(new Date())+".sql");
//        Date date = DATE.parse(filename here);        
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (int b; (b = is.read()) != -1; ) {
                fos.write(b);
            }        
        }        
    }
    
    /**
     *
     * @return
     */
    public static List<File> getBackupList() {
        File[] files = BACKUP.listFiles(File::isFile);
        return files == null ? new ArrayList() : Arrays.asList(files);
    }
}