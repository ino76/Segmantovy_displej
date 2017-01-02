package knihovna;

/*******************************************************************************
 * Rozhrani doplnuje metody sveho rodice o o metodu,
 * kterou Multipresouvac zavola pote, do "dostrkal" svereny objekt
 * do zadane cilove pozice.
 *
 * @author  Rudolf PECINOVSKY
 * @version 2.05.2611 \u2014 2011-09-25
 */
public interface IMultiposuvny extends IPosuvny
{
//== VEREJNE KONSTANTY =========================================================
//== PRISTUPOVE METODY VLASTNOSTI INSTANCI =====================================
//== OSTATNI METODY K IMPLEMENTACI =============================================

    /***************************************************************************
     * Metoda vyvolana multipresouvacem pote, co dovedl svereny objekt
     * do zadane cilove pozice. Pri svem typickem pouziti metoda nastavi
     * novou cilovou pozici sve instance a instanci opet preda multipresouvaci.
     */
    public void presunuto();


//== VNORENE TRIDY =============================================================
}

