package projekt;

import knihovna.Barva;
import knihovna.IO;
import knihovna.SpravcePlatna;

/**
 * Program segmentovy displej simuluje displej s bloky segmentu,
 * kterym je posilan binarni kod jez rozsveci nebo zhasina bloky
 * a tim zobravuje na displeji cisla.
 *
 * Created by cechd on 07.12.2016.
 */
public class Displej {
    private static final SpravcePlatna SP = SpravcePlatna.getInstance();

    /**
     *
     * @param pocetCislic ...   nastavi sirku displeje tak aby se na nej vesel dany pocet cislic
     * @param velikost    ...   nastavi celkovou velikost displeje a cisel
     */
    private Displej(int pocetCislic, int velikost){
        SP.setKrokRozmer(velikost*10, 5 * pocetCislic + 1, 9);
        SP.setBarvaPozadi(Barva.CERNA);
    }


    private void stopky(){
        Cislice cislo = new Cislice(0, 1);
        Cislice cislo2 = new Cislice(0, 2);

        while(true){
            for(int j = 0; j < 10; j++){
                for (int i = 0; i < 10; i++) {
                    SP.nekresli();{
                    cislo.zobrazCislici(i);
                    cislo2.zobrazCislici(j);
                    IO.cekej(100);
                    }SP.vratKresli();
                }
            }
        }
    }



    public static void main(String[] argv) {
            Displej d = new Displej(2, 3);
        try {
            d.stopky();
        }catch (IllegalArgumentException e){ System.err.println("Moc cisel na prilis maly displej!");}
    }
}
