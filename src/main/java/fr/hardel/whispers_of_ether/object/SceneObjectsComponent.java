package fr.hardel.whispers_of_ether.object;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

/**
 * World-level persistent storage for scene objects. Server authoritative; sync
 * to players via CCA.
 */
public class SceneObjectsComponent implements AutoSyncedComponent {
    private final Level world;
    private final Map<String, SceneObject> objectsById = new LinkedHashMap<>();

    public SceneObjectsComponent(Level world) {
        this.world = world;
    }

    public Collection<SceneObject> getAll() {
        return Collections.unmodifiableCollection(objectsById.values());
    }

    public Optional<SceneObject> get(String id) {
        return Optional.ofNullable(objectsById.get(id));
    }

    public boolean upsert(SceneObject object) {
        boolean isNew = !objectsById.containsKey(object.id());
        objectsById.put(object.id(), object);
        sync();
        return isNew;
    }

    public boolean remove(String id) {
        if (objectsById.remove(id) != null) {
            sync();
            return true;
        }
        return false;
    }

    public void clear() {
        if (!objectsById.isEmpty()) {
            objectsById.clear();
            sync();
        }
    }

    private void sync() {
        if (world instanceof ServerLevel serverWorld) {
            SceneObjectsComponents.SCENE_OBJECTS.sync(serverWorld);
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
        objectsById.clear();
        if (tag.contains("objects")) {
            SceneObject.CODEC.listOf()
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("objects"))
                .result()
                .ifPresent(list -> {
                    for (SceneObject obj : list) {
                        objectsById.put(obj.id(), obj);
                    }
                });
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
        SceneObject.CODEC.listOf()
            .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), new ArrayList<>(objectsById.values()))
            .result()
            .ifPresent(encoded -> tag.put("objects", encoded));
    }
}
