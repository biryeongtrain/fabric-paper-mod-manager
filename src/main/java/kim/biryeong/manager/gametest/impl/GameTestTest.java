package kim.biryeong.manager.gametest.impl;

import java.lang.reflect.Method;
import kim.biryeong.manager.gametest.api.CustomTestMethodInvoker;
import kim.biryeong.manager.gametest.api.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class GameTestTest implements CustomTestMethodInvoker {
    @GameTest
    public void test(GameTestHelper helper) {
        helper.destroyBlock(new BlockPos(0, 0, 0));
        helper.assertBlockPresent(Blocks.AIR, new BlockPos(0, 0, 0));
        helper.succeed();
    }

    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        context.setBlock(0, 0, 0, Blocks.AIR);
        method.invoke(this, context);
    }
}
