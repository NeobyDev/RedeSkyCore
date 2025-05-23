package br.neobydeveloper.com.redeskycore.Mysql;

import br.neobydeveloper.com.redeskycore.Manager;
import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class ProfileManager extends Manager {
    private final Map<UUID, Profile> profiles = new HashMap();

    public ProfileManager(RedeSkyCore plugin) {
        super(plugin);
    }

    public void handleProfileCreation(UUID uuid, String name) {
        this.profiles.computeIfAbsent(uuid, (id) -> new Profile(id, name));
    }

    public Profile getProfile(Player player) {
        return this.getProfile(player.getUniqueId());
    }

    public Profile getProfile(UUID uuid) {
        return (Profile)this.profiles.get(uuid);
    }

    public Profile getProfile(String name) {
        return (Profile)this.profiles.values().stream().filter((profile) -> profile.getPlayerName().equalsIgnoreCase(name)).findFirst().orElse((Profile) null);
    }

    public void removeProfile(UUID uuid) {
        this.profiles.remove(uuid);
    }

    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }
}
