package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.*;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Poruban;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
import sk.tuke.gamedev.iddqd.tukequest.util.RandomHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Steve on 01.05.2017.
 */
public class CollectableGenerator {

    public enum CollectableType {

        SURPRISE(22, 5, 50),
        HEADPHONES(30, 60, 95, () -> !Poruban.isPorubanLevel),
        PAPER(90, 60, 250),
        PUBLIC_CLASS(15, 10, 50, () -> Poruban.isPorubanLevel);

        private int chance;
        private int minCount;
        private int maxCount;
        public Supplier<Boolean> condition;

        CollectableType(int chance, int minCount, int maxCount, Supplier<Boolean> condition) {
            this(chance, minCount, maxCount);
            this.condition = condition;
        }

        CollectableType(int chance, int minCount, int maxCount) {
            this.chance = chance;
            this.minCount = minCount;
            this.maxCount = maxCount;
            if (minCount > maxCount) {
                throw new RuntimeException("Design level assertion problem");
            }
        }

    }

    private static Map<CollectableType, Integer> typeCount = new HashMap<>();

    public static void reset() {
        typeCount = new HashMap<>();
    }

    public static Collectable createForPlatform(Platform platform) {
        Collectable collectable = null;
        float x = platform.getX() + platform.getAnimation().getWidth() / 2;
        float y = platform.getY() + platform.getAnimation().getHeight();
        for (CollectableType type : CollectableType.values()) {
            int count = typeCount.getOrDefault(type, 0);
            typeCount.put(type, count + 1);

            if (collectable == null
                && (type.condition == null || type.condition.get())
                && count > type.minCount
                && (RandomHelper.random.nextInt(type.chance) == 0 || count > type.maxCount)) {
                collectable = create(type, x, y);
                typeCount.put(type, 0);
            }
        }
        return collectable;
    }

    private static Collectable create(CollectableType type, float x, float y) {
        Log.d(CollectableGenerator.class, type.name() + " spawned");
        switch (type) {
            case SURPRISE:
                return new Surprise(x, y);
            case HEADPHONES:
                return new Headphones(x, y);
            case PAPER:
                return new Paper(x, y);
            case PUBLIC_CLASS:
                return new PublicClass(x, y);

            default:
                throw new RuntimeException("Attempted to spawn unknown collectable");
        }
    }

}
