package fr.hardel.whispers_of_ether.client.render.entity.model;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.entity.animation.AttackAnimation;
import fr.hardel.whispers_of_ether.client.render.entity.state.TargetDummyRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;

public class TargetDummyModel extends EntityModel<TargetDummyRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "target_dummy"), "main");
    private final KeyframeAnimation attackAnimation;

    public TargetDummyModel(ModelPart root) {
        super(root);
        ModelPart root1 = root.getChild("root");
        this.attackAnimation = AttackAnimation.dummy_attack.bake(root1);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition base = root.addOrReplaceChild("base",
                CubeListBuilder.create().texOffs(0, 9).addBox(-8.0F, -1.0F,
                        -8.0F, 16.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        base.addOrReplaceChild("stick_down_r1",
                CubeListBuilder.create().texOffs(32, 41).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 11.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("chest_r1",
                CubeListBuilder.create().texOffs(32, 26)
                        .addBox(-5.0F, -1.0F, -2.5F, 10.0F, 10.0F, 5.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 0)
                        .addBox(-16.0F, -5.0F, -2.5F, 32.0F, 4.0F, 5.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 26)
                        .addBox(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(32, 41).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 11.0F, 4.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -19.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(TargetDummyRenderState state) {
        super.setupAnim(state);
        this.attackAnimation.apply(state.attackAnimationState, state.ageInTicks);
    }
}
