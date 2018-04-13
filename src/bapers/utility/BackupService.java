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
import java.util.Date;
import java.util.List;

/**
 *
 * @author chris
 */
public abstract class BackupService {
    
    protected static final DateFormat DATE = new SimpleDateFormat("yyyyMMddHHmmss");
    protected static final File BACKUP = new File("backups");    
    
    public static void restoreFromBackup(File file) throws IOException {
        
    }
    
    public static void backup() throws IOException { 
        ProcessBuilder pb 
                = new ProcessBuilder("mysqldbexport", 
                        "--server=root:haddockexecellipsis@localhost", "-e", 
                        "both", "bapers");
        Process p = pb.start();
        InputStream is = p.getInputStream();
        if (!BACKUP.exists()) {
            BACKUP.mkdir();
        }
        File file = new File(BACKUP.getAbsoluteFile()+"/"
                +DATE.format(new Date())+".sql");
//        Date date = DATE.parse(filename here);        
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (byte[] buffer = new byte[512]; is.read(buffer) != -1; ) {
                fos.write(buffer);
            }        
        }        
    }
    
    public abstract List<File> getBackupList();
}
