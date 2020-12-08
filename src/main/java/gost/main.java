// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package gost;

import gost.major.Direct;
import gost.major.FlagManager;

public class main {

    public static void main(String[] args) throws Exception {
        var flag = new FlagManager();
        flag.parsing(args);
        if (args.length > 2)
            new Direct(flag);
    }
}
