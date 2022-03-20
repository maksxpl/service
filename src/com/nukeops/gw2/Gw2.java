package com.nukeops.gw2;
import com.nukeops.other.File;
import com.nukeops.other.JSON;
import com.nukeops.other.Process;
import com.nukeops.other.Terminal;
import com.nukeops.color.Color;

import java.io.IOException;

import static com.nukeops.Main.initError;
import static com.nukeops.other.File.exist;

public class Gw2 {

    static String Gw2Path = JSON.parse("Gw2Path");
    static String InstallArc = JSON.parse("InstallArc");
    static String arcPath = Gw2Path+"\\bin64\\d3d9.dll";
    static String downloadedArc = "src\\d3d9.dll";
    static Boolean compareArcs = File.compare(arcPath, downloadedArc);

    public static void init(int x, int y) {
        try {
            gwStatus(x,y);
            blishStatus(x,y);
            arcStatus(x,y);
            events(x,y);
        } catch (Exception e){
            e.printStackTrace();
    }   }

    static void gwStatus(int x, int y){
        try {
            Terminal.moveCursor(x+1, y);
            System.out.print("Gw2:");
            if (Gw2.isRunningGw2()) {
                Terminal.moveCursor(x+9, y);
                System.out.print(Color.font("Online ", "green"));
            } else {
                Terminal.moveCursor(x+9, y);
                System.out.print(Color.font("Offline", "red"));
            }
        } catch (Exception e){
            e.printStackTrace();
    }   }

    static void blishStatus(int x, int y){
        try {
            Terminal.moveCursor(x+1, y+1);
            System.out.print("Blish:");
            // blish is online
            if (Blish.isRunning()) {
                Terminal.moveCursor(x+9, y+1);
                System.out.print(Color.font("Online   ", "green"));
            }
            // blish is offline
            if (!Blish.isRunning() && Blish.installed()) {
                Terminal.moveCursor(x+9, y+1);
                System.out.print(Color.font("Offline  ", "red"));
                if(isRunningGw2()){Blish.run();}
            }
            // blish doesn't exist
            if (!Blish.installed()) {
                Terminal.moveCursor(x+9, y+1);
                System.out.print(Color.font("Not Found", "red"));
        }   } catch (Exception e){e.printStackTrace();}
    }

    static void arcStatus(int x, int y) {
        try {
            Terminal.moveCursor(x+1, y+2);
            System.out.print("Arc:");
            // arc not found
            if (!exist(arcPath)||!exist(downloadedArc)) {
                Terminal.moveCursor(x+9, y+2);
                System.out.print(Color.font("Not found ", "red"));
                if (InstallArc.equals("true")){removeArc();updateArc();}
            }
            // arc outdated
            if (exist(arcPath) && exist(downloadedArc) && !compareArcs) {
                Terminal.moveCursor(x+9, y+2);
                System.out.print(Color.font("Outdated  ", "red"));
                if (InstallArc.equals("true")) {removeArc();updateArc();}
            }
            // arc up to date
            Terminal.moveCursor(x+9, y+2);
            if (InstallArc.equals("true") && compareArcs) {
                System.out.print(Color.font("Up to date", "green"));
            }
        } catch (Exception e){
            e.printStackTrace();
    }   }

    static void events(int x, int y) {
        Terminal.moveCursor(x, y+4);
        System.out.print(" Drakkar: ");
        Process.py();
    }

    static Boolean isRunningGw2() throws IOException {
        return Process.isRunning("Gw2-64.exe");
    }

    static void removeArc() {
        boolean removedDownload = File.remove(downloadedArc);
        if(!removedDownload){initError("Copying file failed");}
        boolean removedOriginal = File.remove(arcPath);
        if(!removedOriginal){initError("Copying file failed");}
    }
    public static void updateArc() {
        String url = "https://www.deltaconnected.com/arcdps/x64/d3d9.dll";
        File.download(url, downloadedArc);
        boolean copied = File.copy(downloadedArc,arcPath);
        if(!copied){initError("Copying file failed");}
    }
}
