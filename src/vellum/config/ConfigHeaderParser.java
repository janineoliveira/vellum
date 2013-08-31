/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.config;

import vellum.util.Args;

/**
 *
 * @author evan.summers
 */
public class ConfigHeaderParser {
    String type;
    String name; 
    
    public ConfigHeaderParser() {
    }

    public boolean parse(String line) {
        String[] tokens = line.split(" ");
        if (tokens.length >= 3 && tokens[tokens.length - 1].equals("{")) {
            type = tokens[0];
            name = tokens[1];
            return true;
        }
        return false;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return Args.format(type, name);
    }
                   
}
