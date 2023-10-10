// Projet 1 CDA
// 
// Caussarieu Pierre-Olivier
//

package projet;
public class Projet
{
    public static void main(String a_args[])
    {
        Site site = new Site();
        GUISite ihm = new GUISite(site);
        System.out.println(site.getStock());
    }
}
