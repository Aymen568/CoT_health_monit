package tn.cot.healthmonitoring.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class Argon2Utility {
    private static final Config config = ConfigProvider.getConfig();
    private static final int saltLength;
    private static final int hashLength;
    private static final Argon2 argon2;
    private static final int iterations;
    private static final int memory;
    private static final int threadNumber;

    public Argon2Utility() {
    }

    public static boolean check(String dbHash, char[] clientHash) {
        boolean var2;
        try {
            var2 = argon2.verify(dbHash, clientHash);
        } finally {
            argon2.wipeArray(clientHash);
        }

        return var2;
    }

    public static String hash(char[] clientHash) {
        String var1;
        try {
            var1 = argon2.hash(iterations, memory, threadNumber, clientHash);
        } finally {
            argon2.wipeArray(clientHash);
        }

        return var1;
    }

    static {
        saltLength = (Integer)config.getValue("argon2.saltLength", Integer.class);
        hashLength = (Integer)config.getValue("argon2.hashLength", Integer.class);
        argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, saltLength, hashLength);
        iterations = (Integer)config.getValue("argon2.iterations", Integer.class);
        memory = (Integer)config.getValue("argon2.memory", Integer.class);
        threadNumber = (Integer)config.getValue("argon2.threadNumber", Integer.class);
    }
}

