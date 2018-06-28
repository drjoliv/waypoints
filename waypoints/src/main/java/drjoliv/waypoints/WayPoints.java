package drjoliv.waypoints;

import static drjoliv.waypoints.Command.cmdAdd;
import static drjoliv.waypoints.Command.cmdHlp;
import static drjoliv.waypoints.Command.cmdList;
import static drjoliv.waypoints.Command.cmdRm;
import static drjoliv.waypoints.Command.cmdWarp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.HashMap;

public class WayPoints {

  public static final String ESC = "\u001B";
  public static final String BRIGHT_BLUE = "[94m";
  public static final String RED = "[31m";
  public static final String RESET_COLOR = "[0m";

  public static String highlight(String s) {
    return ESC+BRIGHT_BLUE+s+ESC+RESET_COLOR;
  }

  public static String error(String s) {
    return ESC+RED+s+ESC+RESET_COLOR;
  }

  //java main entry point.
  public static void main(String[] args) {
    Command cmd = processArgs(args);
    int exitCode =
      cmd.visit(
            WayPoints::add
           ,WayPoints::remove
           ,WayPoints::list
           ,WayPoints::warp
           ,WayPoints::usage
        );
    System.exit(exitCode);
  }

  //simple function to convert command line arguments into a Command.
  private static Command processArgs(String[] args) {
    if(args.length == 0)
      return cmdHlp();
    else if(args.length == 1) {
      switch(args[0]) {
        case "help":
          return cmdHlp();
        case "list" :
          return cmdList();
        default :
          return cmdWarp(args[0]);
      }
    }
    else if(args.length == 2 && args[0].equals("rm"))
      return cmdRm(args[1]);
    else if(args.length == 3 && args[0].equals("add"))
      return cmdAdd(args[1],args[2]);
    else
      return cmdHlp();
  }

  //the logic for adding a waypoint.
  private static int add(String alias, String path) {
    String newPath = processPath(path);
    HashMap<String,String> map = dataFile().exists()
      ? readData()
      : new HashMap<String,String>();
    map.put(alias, newPath);
    boolean ok = writeData(map);
    if(ok){
      System.out.println("creating way-point:\t"+highlight(alias)+"\n");
      System.out.println(alias+"\t" + map.get(alias));
    }else{
      System.out.println(error("could not add way-point:")+"\t"+highlight(alias));
    }
    return 0;
  }

  //the logic for removing a waypoint.
  private static int remove(String alias) {
    if(!dataFile().exists()){
      System.out.println(highlight("No way-points to remove."));
      return 0;
    }
    else{
     HashMap<String,String> map = readData();
     if(map != null) {
      String confirmatin = map.remove(alias) != null
       ? "removing way-point:\t" + highlight(alias)
       : error("way-point: ") + highlight(alias) + error(" does not exist.");
      writeData(map);
      System.out.println(confirmatin);
     } else {
      System.out.println(error("Unable to load waypoints."));
     }
     return 0;
    }
  }

  //list way points.
  private static int list() {
   HashMap<String, String> map =  readData();
   System.out.println("way-points: " + highlight("(total " + map.size() +")"));
   for(String k : map.keySet())
     System.out.println(k + "\t" + map.get(k));
   return 0;
  }

  //prints the waypoints location allowing the shell script to cd to it.
  private static int warp(String alias) {
    HashMap<String,String> map = readData();
    if(map.containsKey(alias)) {
      System.out.print(map.get(alias));
      return 2;
    } else {
      return 0;
    }
  }

  //
  private static int usage() {
     System.out.println("Usage:  wp [add | rm | list | help] [args] \n");

     System.out.println("Create aliases to ditrectories allowing you to quickly navigate to them.\n");

     System.out.println("  add\tAdd an alias and directory path. \"wp add home ~/\"");
     System.out.println("  \tChange directory using an alias. \"wp home\"");
     System.out.println("  rm\tRemove an alias. \t\t \"wp rm home\"");
     System.out.println("  list\tList all aliases \t\t \"wp list\"");
     System.out.println("  help\tShow help information \t\t \"wp help\"");
    return 0;
  }

  //functions for obtaining various files and folders.
  private static File dataFile() {
    return new File(dataFolder(), "points.dat");
  }

  private static File homeDir() {
    return new File(System.getProperty("user.home"));
  }

  private static File dataFolder() {
    File dataFolder = new File(homeDir(), ".waypoints");
      if(!dataFolder.exists())
        dataFolder.mkdir();
    return dataFolder;
  }

  //take a user inputed path and convert that into an absolute path.
  private static String processPath(String path) {
      return Paths.get(path)
        .toAbsolutePath()
        .normalize()
        .toString();
    }

  //waypoints are saved using object serilization.
  //the function returns true if it succesful wrote the waypoints back to disk.
  private static boolean writeData(HashMap<String,String> points) {
    try( ObjectOutputStream out =
        new ObjectOutputStream(new FileOutputStream(dataFile(), false)) ) {
      out.writeObject(points);
      out.close();
      return true;
    } catch (IOException ex) {
      return false;
    }
  }

  //reads the waypoints from a file.
  private static HashMap<String,String> readData() {
    try( ObjectInputStream in =
        new ObjectInputStream(new FileInputStream(dataFile())) ) {
      @SuppressWarnings("unchecked")
      HashMap<String,String> map = (HashMap<String,String>)in.readObject();
      in.close();
      return map;
    } catch ( ClassNotFoundException | IOException ex) {
      return null;
    }
  }
}
