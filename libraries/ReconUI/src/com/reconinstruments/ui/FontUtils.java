package com.reconinstruments.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {

    private static Map<Integer, Typeface> fontTypefaces = new HashMap<Integer, Typeface>();

    /**
     * Read TypeFace from a resource, caching typefaces in two places
     *  -in the apps files/fonts directory the first time the font is loaded after the app is installed
     *  -in a hashmap while the app is running
     */
    public static Typeface getFontFromRes(Context context,int id) {

        Typeface typeface = fontTypefaces.get(id);
        if(typeface != null){
            return typeface;
        }

        String fontsPath = context.getFilesDir()+"/fonts";
        String fontPath = fontsPath+"/"+id;

        if(!new File(fontPath).exists()) {
            if(!new File(fontsPath).exists())
                new File(fontsPath).mkdir();

            InputStream resInput = context.getResources().openRawResource(id);
            BufferedOutputStream bos = null;
            try {
                byte[] buffer = new byte[resInput.available()];
                bos = new BufferedOutputStream(new FileOutputStream(fontPath));
                int read = 0;
                while ((read = resInput.read(buffer)) > 0)
                    bos.write(buffer, 0, read);
                bos.close();
                typeface = Typeface.createFromFile(fontPath);
            } catch (IOException e) {
                Log.e("ReconFontUtils", "Error creating font file from font resource, using default typeface",e);
                return Typeface.DEFAULT;
            }
        } else {
            typeface = Typeface.createFromFile(fontPath);
        }

        fontTypefaces.put(id, typeface);
        return typeface;
    }

}
