/*
 * La class Main de redsarow est mis à disposition selon les termes de la
 * licence Creative Commons Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 4.0 International.
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

package fr.redsarow.yml4J;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
 * <img alt="Licence Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png">
 * </a>
 * <br>La class Main de redsarow est mis à disposition selon les termes de la <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
 * licence Creative Commons Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 4.0 International</a>.
 * <br>
 *
 * @author redsarow
 */
public class Yml4J {

    protected static Map<String, YmlFile> listConfig = new HashMap<>();


    public static boolean init(String name, String path, File folder, boolean checkConfig, double versConfig) throws Exception {
        return folder==null
                ?Config.checkConfig(name, path, checkConfig, versConfig)
                :Config.checkConfig(name, path, folder, checkConfig, versConfig);
    }

    public static boolean init(String name, String path, File folder) throws Exception {
        return init(name, path, folder, false, 0);
    }


    public static boolean init(String name, String path) throws Exception {
        return init(name, path, null);
    }

    public static boolean init(String name, String path, boolean checkConfig, double versConfig) throws Exception {
        return init(name, path, null, checkConfig, versConfig);
    }



    public static boolean load(File file){
        return listConfig.put(file.getName(), new YmlFile(file)) != null;
    }

    public static YmlFile getYmlFile(String name){
        return listConfig.get(name);
    }

    public static void main(String ... arg) {
        try {
            init("config.yml", "", true, 2.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        YmlFile file = getYmlFile("config.yml");
        System.out.println(file.getList("tt1"));
    }
}
