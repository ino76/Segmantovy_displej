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


Constructor of Display class has two int arguments. First one specify how many segments/numbers you want to make.
Seccond attribute tell, how big display will be
