# waypoints

Waypoints allows you to quickly move through directories while in the terminal. This is done by allowing the user to create waypoints, waypoints are basically bookmarks. Users assgin a waypoint an alias and the alias allows you quickly move to that waypoint.

# Usage

## list

List all the waypoints you have created.

```
drjoliv@mango:~/projects/waypoints$ wp list
way-points: (total 0)
```

## add

Add a waypoint. the supcommand add takes two arguments, the waypoint name and direcotry(wp add &lt;NAME&gt; &lt;DIRECTORY&gt;).

```
drjoliv@mango:~/projects/waypoints$ wp add ways .
creating way-point      ways

drjoliv@mango:~/projects/waypoints$ wp list
way-points: (total 1)
ways    /home/drjoliv/projects/waypoints
```

## warp

Usign a waypoint. No subcommand is need to use a waypoint jsut the name of the waypoint(wp &lt;NAME&gt;).

```
drjoliv@mango:~/projects/waypoints$ cd ~/
drjoliv@mango:~$ wp ways
drjoliv@mango:~/projects/waypoints$ 
```

## remove

Remove a way-point. The subcommand rm deletes way-points.(wp rm &lt;NAME&gt;)

```
drjoliv@mango:~/projects/waypoints$ wp rm ways
removing way-point:     ways
drjoliv@mango:~/projects/waypoints$ wp list
way-points: (total 0)
drjoliv@mango:~/projects/waypoints$ 
```

## help
To print usage information:
```
wp help
``` 



[![asciicast](https://asciinema.org/a/MQIt9I7BqQgE4UrmjPOszd9br.png)](https://asciinema.org/a/MQIt9I7BqQgE4UrmjPOszd9br)

