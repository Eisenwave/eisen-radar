package eisenwave.radar.model.track;

import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.data.Vec2;
import eisenwave.radar.util.VecUtil;
import eisenwave.radar.view.RadarBar;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class PlayerTracker extends RadarTracker {
    
    private final static double RANGE = 64;
    
    private final List<Vec2> list = new LinkedList<>();
    
    public PlayerTracker(World world) {
        super(world, new RadarSymbol("#"));
    }
    
    @Override
    public TrackerType getType() {
        return TrackerType.PLAYER;
    }
    
    @Override
    public void update() {
        list.clear();
        world.getPlayers().forEach(player -> list.add(posOf(player)));
    }
    
    @Override
    public void display(Player player, RadarBar bar) {
        final Vec2 playerPos = posOf(player);
        final RadarSymbol symbol = getSymbol();
        
        list.forEach(pos -> {
            if (VecUtil.inCube(playerPos, pos, RANGE))
                bar.draw(pos.minus(playerPos).getYaw() - playerPos.getYaw(), symbol);
        });
    }
    
    private static final Location buffer = new Location(null, 0, 0, 0);
    
    private synchronized static Vec2 posOf(Player player) {
        player.getLocation(buffer);
        return new Vec2(buffer.getX(), buffer.getZ());
    }
    
}
