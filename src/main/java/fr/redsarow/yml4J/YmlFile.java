/*
 * La class YmlFile de redsarow est mis à disposition selon les termes de la
 * licence Creative Commons Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 4.0 International.
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

package fr.redsarow.yml4J;


import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
 * <img alt="Licence Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png">
 * </a>
 * <br>La class YmlFile de redsarow est mis à disposition selon les termes de la <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
 * licence Creative Commons Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 4.0 International</a>.
 * <br>
 *
 * @author redsarow
 */
public class YmlFile {

    private File file;

    LinkedHashMap val;

    public YmlFile(File file) {
        load(file);
    }

    public boolean load(File file){
        try {
            this.file = file;
            Yaml yaml = new Yaml();
            Iterable<Object> objects = yaml.loadAll(new FileInputStream(file));
            Iterator<Object> iterator = objects.iterator();
            if(iterator.hasNext()){
                Object next = iterator.next();
//                System.out.println(next.toString());

                val = (LinkedHashMap)next;

            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clear(){
        this.file=null;
        this.val.clear();
//        System.gc();
    }

    public Object get(String path){
        String[] pathSplit = path.split("\\.");

        if(pathSplit.length<1){
            pathSplit = new String[1];
            pathSplit[0]=path;
        }

        LinkedHashMap tmpVal = val;

        for (int i =0; i< pathSplit.length-1; i++){
            Object o = tmpVal.get(pathSplit[i]);

            if(o instanceof LinkedHashMap){
                tmpVal=(LinkedHashMap) o;
            }else{
                return null;
            }
        }
        return tmpVal.get(pathSplit[pathSplit.length-1]);

    }

    public String getString(String path) {
        return (String) get(path);
    }

    public int getInt(String path) {
        return (int) get(path);
    }

    public double getDouble(String path) {
        return (double) get(path);
    }

    public List getList(String path) {
        return (List) get(path);
    }

    public List<String> getStringList(String path) {
        return (List<String>) get(path);
    }

    public List<Integer> getIntegerList(String path) {
        return (List<Integer>) get(path);
    }

    public List<Double> getDoubleList(String path) {
        return (List<Double>) get(path);
    }

    public Map<String, ?> getMap(String path){
        return (Map<String, ?>) get(path);
    }


    public boolean isList(String path){
        return get(path) instanceof List;
    }

    public boolean isMap(String path){
        return get(path) instanceof Map;
    }
}
