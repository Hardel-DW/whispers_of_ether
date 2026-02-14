package fr.hardel.whispers_of_ether;

import fr.hardel.whispers_of_ether.component.DataComponent;
import fr.hardel.whispers_of_ether.server.runic_attribute.AttributeDataLoader;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hardel.whispers_of_ether.world.attribute.ModAttribute;
import fr.hardel.whispers_of_ether.world.level.block.ModBlocks;
import fr.hardel.whispers_of_ether.world.level.block.entity.ModBlockEntities;
import fr.hardel.whispers_of_ether.server.commands.SpellCommand;
import fr.hardel.whispers_of_ether.world.item.crafting.ModRecipes;
import fr.hardel.whispers_of_ether.server.commands.WaypointCommand;
import fr.hardel.whispers_of_ether.server.commands.EtherObjectCommand;
import fr.hardel.whispers_of_ether.world.effect.ModEffects;
import fr.hardel.whispers_of_ether.world.entity.ModEntities;
import fr.hardel.whispers_of_ether.world.item.ModItems;
import fr.hardel.whispers_of_ether.world.item.CreativeModeTabs;
import fr.hardel.whispers_of_ether.world.inventory.ModMenuTypes;
import fr.hardel.whispers_of_ether.core.particles.ModParticle;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import fr.hardel.whispers_of_ether.world.object.SceneObjectType;
import fr.hardel.whispers_of_ether.world.object.SceneObjectTypes;
import fr.hardel.whispers_of_ether.world.spell.SpellResourceReloadListener;
import fr.hardel.whispers_of_ether.world.spell.action.ActionType;
import fr.hardel.whispers_of_ether.world.spell.target.TargetType;
import fr.hardel.whispers_of_ether.world.spell.target.position.PositionTargetType;
import fr.hardel.whispers_of_ether.world.spell.target.shape.ShapeType;
import fr.hardel.whispers_of_ether.world.spell.timeline.OrganizationType;
import fr.hardel.whispers_of_ether.world.spell.timeline.offset.LoopOffsetType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public class WhispersOfEther implements ModInitializer {
    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModAttribute.register();
        ModEntities.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
        DataComponent.register();
        ModMenuTypes.register();
        ModRecipes.register();
        ModParticle.register();
        CreativeModeTabs.register();
        OrganizationType.register();
        ActionType.register();
        TargetType.register();
        ShapeType.register();
        PositionTargetType.register();
        LoopOffsetType.register();
        SceneObjectType.register();
        SceneObjectTypes.register();
        ModEffects.register();

        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(Identifier.fromNamespaceAndPath(MOD_ID, "spells"), new SpellResourceReloadListener());
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(Identifier.fromNamespaceAndPath(MOD_ID, "rune"), new AttributeDataLoader());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SpellCommand.register(dispatcher);
            WaypointCommand.register(dispatcher);
            EtherObjectCommand.register(dispatcher);
        });

        NetworkHandler.registerServerPackets();
    }
}
