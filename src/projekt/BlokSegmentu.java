package projekt;

import knihovna.*;

/**
 * Created by cechd on 07.12.2016.
 */

public class BlokSegmentu{
    // konstanty
    private static final SpravcePlatna  SP      = SpravcePlatna.getInstance();

    private static final int            SIRKA_P = SP.getBsirka();
    private static final int            KROK    = SP.getKrok();
    private static final int            SIRKA_B = 5 * KROK;
    private static final int            K2      = KROK / 2;
    static final int                    M       = 2;
    private static final int            M2      = M * 2;
    private static final int            M3      = M * 3;
    private static final int            M4      = M * 4;
    private static final Barva          BARVA   = Barva.BILA;


    private final Segment s1;
    private final Segment s2;
    private final Segment s3;
    private final Segment s4;
    private final Segment s5;
    private final Segment s6;
    private final Segment s7;
    private final Segment s8;

    Segment[] segmenty = new Segment[8];

    /**
     *      Konstruktor
     */
    BlokSegmentu(int poradi){

        s1 = new Segment(SIRKA_P - (SIRKA_B * poradi) + M, KROK, false);
        s2 = new Segment(SIRKA_P - (SIRKA_B * poradi) + KROK * 3 + M2, KROK + M, true);
        s3 = new Segment(SIRKA_P - (SIRKA_B * poradi) + KROK * 3 + M2, KROK + KROK * 3 + M3, true);
        s4 = new Segment(SIRKA_P - (SIRKA_B * poradi) + M, KROK + KROK * 6 + M4, false);
        s5 = new Segment(SIRKA_P - (SIRKA_B * poradi), KROK + KROK * 3 + M3, true);
        s6 = new Segment(SIRKA_P - (SIRKA_B * poradi), KROK + M, true);
        s7 = new Segment(SIRKA_P - (SIRKA_B * poradi) + M, KROK + KROK * 3 + M2, false);
        s8 = new Segment(SIRKA_P - (SIRKA_B * poradi) + KROK * 4 + M4, KROK * 8 - M4);

        segmenty[0] = s1;
        segmenty[1] = s2;
        segmenty[2] = s3;
        segmenty[3] = s4;
        segmenty[4] = s5;
        segmenty[5] = s6;
        segmenty[6] = s7;
        segmenty[7] = s8;
    }

    public void vykresliSegmenty(String ovladac){
        int delka = ovladac.length();
        if(delka == 8){
            int i = 0;
            for(Segment s : segmenty){
                if(ovladac.charAt(i) == '1'){
                    s.zobraz();
                }else
                    s.zhasni();
                i++;
            }
        }
    }


    /**
     *      Trida Segment.
     *      Stara se o zobrazeni jedineho segmentu na zadane souradnici
     *      a v zadane polarite ... vodorovne jako false a svisle jako true (01)
     */
    private class Segment implements IKresleny{

        private final boolean pol;
        private int x;
        private int y;
        private Obdelnik telo;
        private Trojuhelnik t1;
        private Trojuhelnik t2;

        public Segment(int x, int y, boolean polarizace){
            this.x = x;
            this.y = y;
            int sirka = KROK * 2;
            int vyska = KROK;
            this.pol = polarizace; // true - svisly, false - vodorovny
            if(polarizace == true){ // nastavi polaritu segmentu
                sirka = KROK;
                vyska = KROK * 2;
                telo = new Obdelnik(x, y + KROK, sirka, vyska, BARVA);
                t1 = new Trojuhelnik(x, y + K2, KROK, K2, BARVA, Smer8.SEVER);
                t2 = new Trojuhelnik(x, y + KROK*3, KROK, K2, BARVA, Smer8.JIH);
            }else{
                telo = new Obdelnik(x + KROK, y, sirka, vyska, BARVA);
                t1 = new Trojuhelnik(x + K2, y, K2, KROK, BARVA, Smer8.ZAPAD);
                t2 = new Trojuhelnik(x + KROK*3, y, K2, KROK, BARVA, Smer8.VYCHOD);
            }
        }

        public Segment(int x, int y){
            telo = new Obdelnik(x, y, KROK/3*2, KROK/3*2, BARVA);
            t1 = new Trojuhelnik(1,1,1,1,Barva.ZADNA);
            t2 = new Trojuhelnik(1,1,1,1,Barva.ZADNA);
            pol = false;
        }

        private void zobraz(){
            SP.pridej(this);
        }

        private void zhasni(){
            SP.odstran(this);
        }

        @Override
        public void nakresli(Kreslitko k) {
            telo.nakresli(k);
            t1.nakresli(k);
            t2.nakresli(k);
        }
    }
}

