package projekt;

/**
 * Created by cechd on 07.12.2016.
 */
public class Cislice {
    private static final String[] cislice = {    "11111100" // nula
                                                ,"01100000" // jedna
                                                ,"11011010" // dva
                                                ,"11110010" // tri
                                                ,"01100110" // ctyri
                                                ,"10110110" // pet
                                                ,"10111110" // sest
                                                ,"11100000" // sedm
                                                ,"11111110" // osm
                                                ,"11110110" // devet
    };
    private int cislo;
    private BlokSegmentu segmentovaCislice;

    Cislice(int cislo, int pozice){
        segmentovaCislice = new BlokSegmentu(pozice);
        zobrazCislici(cislo);
    }

    void zobrazCislici(int i){
        segmentovaCislice.vykresliSegmenty(cislice[i]);
        this.cislo = i;
    }

    int getCislo(){
        return this.cislo;
    }

}
