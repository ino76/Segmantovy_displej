package knihovna;

/*******************************************************************************
 * Rozhrani IKresleny musi implementovat vsechny tridy, ktere chteji,
 * aby jejich instance byly zobrazeny na animacnim platne.
 *
 * @author  Rudolf PECINOVSKY
 * @version 2.05.2611 \u2014 2011-09-25
 */
public interface IKresleny
{
//== VEREJNE KONSTANTY =========================================================
//== PRISTUPOVE METODY VLASTNOSTI INSTANCI =====================================
//== OSTATNI METODY K IMPLEMENTACI =============================================

    /***************************************************************************
     * Za pomoci dodaneho kreslitka vykresli obraz sve instance
     * na animacni platno.
     *  
     * @param kreslitko   Kreslitko, kterym se instance nakresli na platno.     
     */
    public void nakresli(Kreslitko kreslitko);


//== VNORENE TRIDY =============================================================
}

