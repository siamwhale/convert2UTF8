/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toutf8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author user
 */
public class ToUTF8 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File srcfile = new File(args[0]);
        File desfile = new File(args[0] + ".utf8");
        File errfile = new File(args[0] + ".err");
                
        transform(srcfile,"TIS-620",desfile,"UTF-8",errfile,"UTF-8");
    }
    
    public static void transform(File source, String srcEncoding, File target, String tgtEncoding, File err, String errEncoding) throws IOException {
    BufferedReader br = null;
    BufferedWriter bw = null;
    BufferedWriter be = null;
    String line = "";
    String oldline = "";
    
    String p = "^(\"\\d{10}\",\"\\d{13}\")(.*)((19\\d{2}|20\\d{2})(\\d{4}))";
    String o = "^(\",)";
    Pattern r = Pattern.compile(p);
    Pattern k = Pattern.compile(o);
    
    try{
        br = new BufferedReader(new InputStreamReader(new FileInputStream(source),srcEncoding));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding));
        be = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(err), errEncoding));
        int cnt = 0;
        //char[] buffer = new char[16384];
        //int read;
        
        while ((line = br.readLine()) != null) {
            Matcher m = r.matcher(line);
            
            if (m.find()) {
                bw.write(line);
                bw.newLine();
            } else {
                Matcher x = k.matcher(line);
                if (x.find()) {
                    line = oldline + line;
                    bw.write(line);
                    bw.newLine();
                } else {
                    oldline = line;
                }
            }
            cnt++;
        }
    } finally {
        try {
            if (br != null)
                br.close();
        } finally {
            if (bw != null)
                bw.close();
            if (be != null)
                be.close();
        }
    }
}

}
