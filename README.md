# Segmantovy_displej
Display that emulates real segment display, composed from eight segments. Numbers are represented by binary code.
For example "1110 0000" means "turn on first three segments and all others turn off" whitch displays segment display representation of
number 7. Last segment is a '.' (dot) so you can display number with a dot simply by getting his binary code and increase it by one.
Constructor od display has two int arguments. By first of them you specify how many segments or how many numbers u want to have.
Seccond attribute tell, how big display will be and its measured in inner relative value 'krok' from whitch is almost everything else scales.
In class BlokSegmentu is static attribute M whitch establishes how big the gap between segments will be and its the only value in program whitch
isnt derived from 'krok'. If u put 0 to this value M, there will be no gap between segments and they'll be one coherent body.
