package kim.biryeong.manager.gametest.impl;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameTestModInitializer implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTestModInitializer.class);
    private static final TestAnnotationLoader locator = new TestAnnotationLoader(FabricLoader.getInstance());

    @Override
    public void onInitialize() {
        if (!(FabricGameTestRunner.ENABLED || FabricLoader.getInstance().isDevelopmentEnvironment())) {
            return;
        }

        for (TestAnnotationLoader.TestMethod testMethod : locator.getTestMethods()) {
            LOGGER.debug("Registering test method {}", testMethod);
            Registry.register(BuiltInRegistries.TEST_FUNCTION, testMethod.location(), testMethod.testFunction());
        }
    }

    public static void registerDynamicEntries(List<RegistryDataLoader.Loader<?>> registriesList) {
        Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new IdentityHashMap<>(registriesList.size());

        for (RegistryDataLoader.Loader<?> loader : registriesList) {
            registries.put(loader.registry().key(), loader.registry());
        }

        Registry<GameTestInstance> testInstances = (Registry<GameTestInstance>) registries.get(Registries.TEST_INSTANCE);
        Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry = (Registry<TestEnvironmentDefinition>) registries.get(Registries.TEST_ENVIRONMENT);

        for (TestAnnotationLoader.TestMethod testMethod : locator.getTestMethods()) {
            GameTestInstance testInstance = testMethod.testInstance(testEnvironmentDefinitionRegistry);
            Registry.register(testInstances, testMethod.location(), testInstance);
        }
    }
}
