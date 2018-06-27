/*bin/mkdir -p /tmp/.java/classes 2> /dev/null
  # Compile the program.

    javac -d /tmp/.java/classes $0

  # Run the compiled program only if compilation succeeds.

  [[ $? -eq 0 ]] && java -cp /tmp/.java/classes $(basename ${0%.*}) "$@"
  exit
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class WayPoints {

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

  public static Command cmdAdd(String alias, String absPath) {return new Command.CommandAdd(alias, absPath);}
  public static Command cmdRm(String alias) {return new Command.CommandRemove(alias);}
  public static Command cmdList() {return new Command.CommandList();}
  public static Command cmdWarp(String alias) {return new Command.CommandWarp(alias);}
  public static Command cmdHlp() {return new Command.CommandHelp();}

  public static abstract class Command {
    public abstract int visit(BiFunction<String, String, Integer> add, Function<String,Integer> remove
        ,  Supplier<Integer> list, Function<String,Integer> warp, Supplier<Integer> help);

    public static class CommandAdd extends Command {
      public final String alias;
      public final String absPath;
        public CommandAdd(String alias, String absPath) {
          this.alias = alias;
          this.absPath = absPath;
        }
      public int visit(BiFunction<String, String, Integer> add, Function<String,Integer> remove
        ,  Supplier<Integer> list, Function<String,Integer> warp, Supplier<Integer> help) {
          return add.apply(alias, absPath);
        }
      }

    public static class CommandRemove extends Command {
      public final String alias;
      public CommandRemove(String alias) {
        this.alias = alias;
      }
      public int visit(BiFunction<String, String, Integer> add, Function<String,Integer> remove
        ,  Supplier<Integer> list, Function<String,Integer> warp, Supplier<Integer> help) {
          return remove.apply(alias);
        }
    }

    public static class CommandList extends Command {
      public int visit(BiFunction<String, String, Integer> add, Function<String,Integer> remove
      ,  Supplier<Integer> list, Function<String,Integer> warp, Supplier<Integer> help) {
          return list.get();
      }
    }

    public static class CommandWarp extends Command {
      public final String alias;
      public CommandWarp(String alias) {
        this.alias = alias;
      }
      public int visit(BiFunction<String, String, Integer> add, Function<String,Integer> remove
      ,  Supplier<Integer> list, Function<String,Integer> warp, Supplier<Integer> help) {
          return warp.apply(alias);
      }
    }

    public static class CommandHelp extends Command {
      public int visit(BiFunction<String, String, Integer> add, Function<String,Integer> remove
        , Supplier<Integer> list, Function<String,Integer> warp, Supplier<Integer> help) {
        return help.get();
      }
    }
  }

  private static Command processArgs(String[] args) {
    if(args.length == 0)
      return cmdHlp();
    else
      return null; //TODO fill this hole.
  }

  private static int add(String alias, String path) {
    String newPath = processPath(path);
    HashMap<String,String> map = dataFile().exists()
      ? readData()
      : new HashMap<String,String>();
    map.put(alias, newPath);
    writeData(map);
    return 0;
  }

  private static int remove(String alias) {
    if(!dataFile().exists()){
      System.out.println("No way-points to remove.");
      return 0;
    }
    else{
     HashMap<String,String> map = readData();
     if(map != null) {
      String confirmatin = map.remove(alias) != null
       ? alias + " was removed."
       : alias + " does not exist.";
      writeData(map);
      System.out.println(confirmatin);
     } else {
       //TODO do something here.
      System.err.println("ERROR");
     }
     return 0;
    }
  }

  private static int list() {
   HashMap<String, String> map =  readData();
       //TODO do something here.
   return 0;
  }

  private static int warp(String alias) {
       //TODO do something here.
    return 0;
  }

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


  private static File dataFile() {
    return new File(dataFolder(), "points.dat");
  }

  private static File workingDir() {
    return new File(System.getProperty("user.dir"));
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

  private static String processPath(String path) {
    if(path.equals("."))
      return workingDir().getAbsolutePath();
    else if(path.equals(".."))
      return workingDir().getParentFile().getAbsolutePath();
    else 
      return null;
  }

  private static boolean writeData(HashMap<String,String> points) {
    try( ObjectOutputStream out =
        new ObjectOutputStream(new FileOutputStream(dataFile(), false)) ) {
      out.writeObject(points);
      out.close();
      return true;
    } catch (IOException ex) {
       //TODO deal with these erros or should i pass them on??
      System.out.println("error writin data");
      return false;
    }
  }

  private static HashMap<String,String> readData() {
    try( ObjectInputStream in =
        new ObjectInputStream(new FileInputStream(dataFile())) ) {
      @SuppressWarnings("unchecked")
      HashMap<String,String> map = (HashMap<String,String>)in.readObject();
      in.close();
      return map;
    } catch ( ClassNotFoundException | IOException ex) {
       //TODO deal with these erros or should i pass them on??
      System.out.println("error writin data");
      return null;
    }
  }
}
