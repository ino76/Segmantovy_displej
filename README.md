# Segment_displej
This was my final project after reading book about Java >>> https://www.databazeknih.cz/knihy/myslime-objektove-v-jazyku-java-10929

This software emulates functionality of real segment display (like on calculator or so). Numbers are represented by binary code.
For example "1110 0000" means "turn on first three segments and all others turn off". Since first segment is the top one order is clockwise this binary display representation of number 7. 1 would be displayes as vertical line on right side, so seccond and third segments would be on -> "0110 0000" and so on.
<pre>
     1
  =======
6|       | 2
 |   7   |
  =======
5|       | 3
 |       |
  =======   = 8
     4
     
</pre>
Display that emulates real segment display, composed from eight segments. Numbers are represented by binary code.
For example "1110 0000" means "turn on first three segments and all others turn off" whitch displays segment display representation of
number 7. Last segment is a '.' (dot) so you can display number with a dot simply by getting his binary code and increase it by one.
Constructor od display has two int arguments. By first of them you specify how many segments or how many numbers you want to have.
Seccond attribute tell, how big display will be and its measured in inner relative value 'krok' from whitch is almost everything else scales.
In class BlokSegmentu is static attribute M whitch establishes how big the gap between segments will be and its the only value in program whitch
isnt derived from 'krok'. If u put 0 to this value M, there will be no gap between segments and they'll be one coherent body.
