package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ArmoredFishEntity;
import net.jacob.bygonecreatures.entity.custom.DodoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ArmoredFishRenderer extends GeoEntityRenderer<ArmoredFishEntity> {
    public ArmoredFishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArmoredFishModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(ArmoredFishEntity instance) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/armoredfishentity/fishtexture.png");
    }



    @Override
    public RenderType getRenderType(ArmoredFishEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {

        if(animatable.isBaby()) {
            stack.scale(0.4F, 0.4F, 0.4F);
        } else {
            stack.scale(1.5F, 1.5F, 1.5F);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

//    @Override
//    protected void setupRotations(entity, PoseStack posestack, float ageInTicks, float rotationYaw, float partialTicks) {
//        super.setupRotations(entity, posestack, ageInTicks, rotationYaw, partialTicks);
//        float f = 4.3F * Mth.sin(0.6F * ageInTicks);
//        posestack.mulPose(Vector3f.YP.rotationDegrees(f));
//        if (!entity.isInWater()) {
//            posestack.translate(0.1F, 0.1F, -0.1F);
//            posestack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
//        }
//    }

    @Override
    protected void applyRotations(ArmoredFishEntity endWhale, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(endWhale, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
//        float f = 4.3F * Mth.sin(0.6F * ageInTicks);
//        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));
        if (!endWhale.isInWater()) {
            matrixStackIn.translate(0.1F, 0.1F, -0.1F);
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
        }



    }

}

