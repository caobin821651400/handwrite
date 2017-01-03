package example.caobin.com.myhandwrite;

import android.os.Environment;

import java.io.File;

/**
 * Created by caobin on 2017/1/3.
 */
public class Constants {
    public static String FilePath;

    static {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            FilePath = Environment.getExternalStorageDirectory()
                    .getPath()
                    + File.separator
                    + "data"
                    + File.separator
                    + "saveImagePath"
                    + File.separator;
        } else {
            FilePath = "/data/data/caobin/saveImagePath/image";
        }
        File file = new File(FilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
