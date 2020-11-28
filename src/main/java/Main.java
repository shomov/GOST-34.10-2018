// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

import major.Direct;
import major.FlagManager;

public class Main {

    public static void main(String[] args) {
        var flag = new FlagManager();
        flag.parsing(args);
        new Direct(flag);
        flag.parsing(args);
    }
}
