package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Collectable;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Headphones;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Paper;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Surprise;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
import sk.tuke.gamedev.iddqd.tukequest.util.RandomHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 01.05.2017.
 */
public class CollectableGenerator {

    public enum CollectableType {

        SURPRISE(22, 5, 50),
        HEADPHONES(30, 100, 145),
        PAPER(90, 60, 250);

        private int chance;
        private int minCount;
        private int maxCount;

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

            default:
                throw new RuntimeException("Attempted to spawn unknown collectable");
        }
    }

}
