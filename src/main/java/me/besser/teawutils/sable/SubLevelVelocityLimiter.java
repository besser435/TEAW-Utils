package me.besser.teawutils.sable;

import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.platform.SableEventPlatform;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import me.besser.teawutils.util.TEAWUtilsConfig;
import org.joml.Vector3d;

public class SubLevelVelocityLimiter {

    private static volatile double subLevelMaxVelocity = 200.0;

    private SubLevelVelocityLimiter() {}

    public static void register() {
        SableEventPlatform.INSTANCE.onPostPhysicsTick(
                SubLevelVelocityLimiter::onPostPhysicsTick
        );
    }

    private static void onPostPhysicsTick(
            final SubLevelPhysicsSystem physicsSystem,
            final double timeStep
    ) {
        final double maxVelocity = subLevelMaxVelocity;

        if (maxVelocity <= 0.0 || !Double.isFinite(maxVelocity)) {
            return;
        }

        final double maxSpeedSquared = maxVelocity * maxVelocity;

        final ServerSubLevelContainer container =
                SubLevelContainer.getContainer(physicsSystem.getLevel());

        if (container == null) {
            return;
        }

        for (final ServerSubLevel subLevel : container.getAllSubLevels()) {
//            if (subLevel.isRemoved()) {
//                continue;
//            }

            final RigidBodyHandle handle =
                    physicsSystem.getPhysicsHandle(subLevel);

            if (!handle.isValid()) {
                continue;
            }

            final Vector3d velocity =
                    handle.getLinearVelocity(new Vector3d());

            final double speedSquared = velocity.lengthSquared();

            if (speedSquared <= maxSpeedSquared) {
                continue;
            }

            // Scale velocity down while preserving its direction
            final Vector3d correction = new Vector3d(velocity)
                    .mul(maxVelocity / Math.sqrt(speedSquared))
                    .sub(velocity);

            // Change linear velocity without affecting angular velocity
            handle.addLinearAndAngularVelocity(
                    correction,
                    new Vector3d()
            );
        }
    }

    public static void reloadConfig() {
        subLevelMaxVelocity =
                TEAWUtilsConfig.COMMON.subLevelMaxVelocity.get();
    }
}