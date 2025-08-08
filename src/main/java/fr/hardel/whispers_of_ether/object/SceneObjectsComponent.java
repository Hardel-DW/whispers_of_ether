package fr.hardel.whispers_of_ether.object;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

/**
 * World-level persistent storage for scene objects. Server authoritative; sync
 * to players via CCA.
 */
public class SceneObjectsComponent implements AutoSyncedComponent {
    private final World world;
    private final Map<String, SceneObject> objectsById = new LinkedHashMap<>();

    public SceneObjectsComponent(World world) {
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
        if (world instanceof ServerWorld serverWorld) {
            SceneObjectsComponents.SCENE_OBJECTS.sync(serverWorld);
        }
    }

    @Override
    public void readData(ReadView readView) {
        objectsById.clear();
        readView.read("objects", SceneObject.CODEC.listOf()).ifPresent(list -> {
            for (SceneObject obj : list) {
                objectsById.put(obj.id(), obj);
            }
        });
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.put("objects", SceneObject.CODEC.listOf(), new ArrayList<>(objectsById.values()));
    }
}
