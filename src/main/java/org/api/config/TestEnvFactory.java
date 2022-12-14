package org.api.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * Env configuration once loaded, is to remain constant for all classes using it. Thus we will
 * follow Singleton design pattern here. For future reference on this topic:
 * https://github.com/lightbend/config
 */
@Slf4j
public class TestEnvFactory {
    /**
     * With this approach, we are relying on JVM to create the unique instance of TestEnvFactory when
     * the class is loaded. The JVM guarantees that the instance will be created before any thread
     * accesses the static uniqueInstance variable. This code is thus guaranteed to be thread safe.
     */
    private static final TestEnvFactory UNIQUE_INSTANCE = new TestEnvFactory();

    private Config config;

    private TestEnvFactory() {
        config = setConfig();
    }

    public static TestEnvFactory getInstance() {
        return UNIQUE_INSTANCE;
    }

    public Config getConfig() {
        return config;
    }

    private Config setConfig() {
        log.info("Call setConfig only once for the whole test run!");

        // Standard config load behavior: https://github.com/lightbend/config#standard-behavior
        config = ConfigFactory.load();
        config = getAllConfigFromFilesInTheResourcePath("common");

        /**
         * Note: that TEST_ENV value is an enum and an uppercase (ex: DEVELOP). Our repositories (by
         * convention) are lowercase - kebab-case. In windows both uppercase and lower case directories
         * are treated the same and thus the code where we even pass the TestEnv enum name as is
         * (uppercase), it works in windows. However not in linux. In linux uppercase and lowercase
         * directories are treated different. So if we do not convert the repo to lower case, it would
         * work on windows but not in CI on linux container. Another Note: Do not change the repo name
         * to upper case to solve this problem. That would be a anti-pattern and would create new
         * problems for you.
         */
        TestEnv testEnv = config.getEnum(TestEnv.class, "TEST_ENV");
        String testEnvName = testEnv.toString().toLowerCase();

        return getAllConfigFromFilesInTheResourcePath(testEnvName);
    }

    private Config getAllConfigFromFilesInTheResourcePath(String resourceBasePath) {
        try {
            String path = String.format("src/main/resources/%s", resourceBasePath);
            log.debug("path: {}", path);

            File testEnvDir = new File(path);
            for (File file : Objects.requireNonNull(testEnvDir.listFiles())) {
                String resourceFileBasePath = String.format("%s/%s", resourceBasePath, file.getName());
                log.debug("resourceFileBasePath: {}", resourceFileBasePath);

                Config childConfig = ConfigFactory.load(resourceFileBasePath);
                config = config.withFallback(childConfig);
            }

            return config;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalStateException("Could not parse config");
        }
    }
}
