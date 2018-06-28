package drjoliv.waypoints;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


//Commands represent possible application actions.
public abstract class Command {
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

  //static methods to create commnands.
  public static Command cmdAdd(String alias, String absPath) {return new Command.CommandAdd(alias, absPath);}
  public static Command cmdRm(String alias) {return new Command.CommandRemove(alias);}
  public static Command cmdList() {return new Command.CommandList();}
  public static Command cmdWarp(String alias) {return new Command.CommandWarp(alias);}
  public static Command cmdHlp() {return new Command.CommandHelp();}

}

