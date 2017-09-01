package fr.redsarow.yml4J;/*
 * La class Config de redsarow est mis à disposition selon les termes de la
 * licence Creative Commons Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 4.0 International.
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
 *   <img alt="Licence Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png">
 * </a>
 * <br>la class Config de redsarow est mis à disposition selon les termes de la <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
 * licence Creative Commons Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 4.0 International</a>.
 * <br>
 *
 * @author redsarow
 * @since 0.1
 */
public class Config {

	private static File config;
    private static File dataFolder;
    private static YmlFile fileConfiguration;

    private static final Logger log = Logger.getLogger("");
    private static final String urlCourante = Config.class.getProtectionDomain().getCodeSource().getLocation().getFile();

    static boolean checkConfig(String name, String path, File folder, boolean checkConfig, double versConfig) throws Exception {
        if ((folder == null) ? dataFolderExists() : dataFolderExists(folder)){
            if (configFileExists(name, path, checkConfig, versConfig))
                return true;
        }
        return false;
    }

    static boolean checkConfig(String name, String path, File folder) throws Exception {
        if ((folder == null) ? dataFolderExists() : dataFolderExists(folder)){
            if (configFileExists(name, path))
                return true;
        }
        return false;
    }

    static boolean checkConfig(String name, String path) throws Exception {
        return checkConfig(name, path, null);
    }

    static boolean checkConfig(String name, String path, boolean checkConfig, double versConfig) throws Exception {
        return checkConfig(name, path, null, checkConfig, versConfig);
    }

    /**
     * permet de recuperer le docier ou ce trouve le jar:
     * String urlCourante = MyClass.class.getProtectionDomain().getCodeSource().getLocation().getFile();
     * <br> File file = new File(urlCourante);
     * <br> urlCourante=file.getParent();
     *
     * @param folder docier ou va ce trouver le ficher de config
     *
     * @return
     */
    private static boolean dataFolderExists(File folder) {
        dataFolder = folder;
        if (!dataFolder.exists()) {
           return dataFolder.mkdirs();
        }
        return true;
	}

    private static boolean dataFolderExists() {
        File file = new File(urlCourante);
        return dataFolderExists(new File(file.getParent()+File.separator+"config"));
    }

	private static boolean configFileExists(String name, String path, boolean checkConfig, double versConfig) throws Exception {
        config = getConfigFile(name, path);

        if (!config.exists()) {
            log.log(Level.INFO, name+" not found");

            String file = path.equals("")?name:path+File.separator+name;

//            Files.copy(new File(urlCourante+File.separator+file).toPath(),
//                    config.toPath());
            Files.copy(Config.class.getClassLoader().getResourceAsStream(file),
                    config.toPath());

        } else {
            log.log(Level.INFO, name +" found");
            if(checkConfig){
                if (!checkConfigVers(name, path, versConfig)) {
                    throw new Exception();
                }
            }
        }

        if(fileConfiguration == null){
            fileConfiguration = new YmlFile(config);
        }else{
            fileConfiguration.load(config);
        }

        Yml4J.listConfig.put(name, fileConfiguration);
        return true;
	}

    private static boolean configFileExists(String name, String path) throws Exception {
        return configFileExists(name, path, false, 0);
    }



    private static boolean checkConfigVers(String name, String path, double vers) {

        try {
            fileConfiguration = new YmlFile(config);
            if(fileConfiguration.getDouble("version") != vers){

                log.log(Level.INFO, name+" deprecated");


                File aConfig = new File(config.getPath()+"."+fileConfiguration.get("version"));

                fileConfiguration.clear();
                Path configPath = config.toPath();

                //evite les axsai concurentielle
                config=null;
                System.gc();

                Files.move(configPath, aConfig.toPath(),StandardCopyOption.REPLACE_EXISTING);


                String file = path.equals("") ? name : path + File.separator + name;
//                Files.copy(new File(urlCourante+File.separator+file).toPath(),
//                        config.toPath(),
//                        StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getClassLoader().getResourceAsStream(file),
                        configPath,
                        StandardCopyOption.REPLACE_EXISTING);

                config = getConfigFile(name, path);
                //fileConfiguration prent la new config
                fileConfiguration.load(config);


                List<String> lines = Files.readAllLines(config.toPath(), StandardCharsets.UTF_8);

                Param param = new Param(new Scanner(aConfig), -1, lines);
                StringBuilder stringBuilder = new StringBuilder("");

                modif(param, null, stringBuilder);

                Files.write(config.toPath(), param.lines, StandardCharsets.UTF_8);

            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void modif(Param param, Set<String> keys, StringBuilder path){

	    if(param.scanner.hasNextLine()) {
            //skip comment, version and void (ancient)
            String ligne = param.scanner.nextLine();
            System.out.println(ligne);
            param.nbLinge++;

            //if list
            if(ligne.matches("^ *-.*")){
                if(param.nbLinge<param.lines.size() && param.lines.get(param.nbLinge).matches("^ *-.*")){
                    param.lines.remove(param.nbLinge);
                    param.lines.add(param.nbLinge, ligne);
                }else{
                    //decal auto
                    param.lines.add(param.nbLinge, ligne);
                }

            }else if (!(ligne.startsWith("#") || ligne.startsWith("version") || ligne.matches("^ *$"))) {

                String[] aConfigValue = ligne.split(": |:");
                aConfigValue[0] = aConfigValue[0].replaceAll("^ *", "");

                if (aConfigValue.length == 2) {
                    if (keys == null) {
                        //si val dif alor replace
                        if (!aConfigValue[1].equalsIgnoreCase(fileConfiguration.getString(aConfigValue[0]))) {
                            param.lines.remove(param.nbLinge);
                            param.lines.add(param.nbLinge, ligne);
                        }
                    } else {
                        //si val dif alor replace
                        if (!aConfigValue[1].equalsIgnoreCase(fileConfiguration.getString(path+"."+aConfigValue[0]))) {
                            param.lines.remove(param.nbLinge);
                            param.lines.add(param.nbLinge, ligne);
                        }
                        keys.remove(aConfigValue[0]);
                        if(keys.isEmpty()){
                            return;
                        }
                    }

                } else if (aConfigValue.length == 1) {


                    String key = path.toString().equals("")
                            ? aConfigValue[0]
                            : path + "." + aConfigValue[0];


                    if (fileConfiguration.isMap(key)) {
                        Set<String> newKeys = fileConfiguration
                                .getMap(key)
                                .keySet();

                        modif(param, newKeys, new StringBuilder(key));
                    }
                }
            }
            modif(param, keys, path);
        }
        return;
    }

    public static File getConfig() {
        return config;
    }

    public static YmlFile getFileConfiguration() {
        return fileConfiguration;
    }

    private static File getConfigFile(String name, String path){
        return new File(path.equals("")
                ?dataFolder.toString()
                :dataFolder+File.separator+path, name);
    }

//    public static Map<String, FileConfiguration> getlistConfig() {
//        return listConfig;
//    }

    private static class Param{
        Scanner scanner;
        int nbLinge;
        List<String> lines;

        Param(Scanner scanner, int nbLinge, List<String> lines) {
            this.scanner = scanner;
            this.nbLinge = nbLinge;
            this.lines = lines;
        }
    }
}