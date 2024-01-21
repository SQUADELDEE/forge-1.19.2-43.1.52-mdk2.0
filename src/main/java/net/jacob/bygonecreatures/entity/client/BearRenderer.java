package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.BearEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BearRenderer extends GeoEntityRenderer<BearEntity> {

    public BearRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BearModel());
        this.shadowRadius = 0.8f;
    }

    @Override
    public ResourceLocation getTextureLocation(BearEntity instance) {

        if (instance.isKnockedOut()){
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/bear/sleepintext.png");

        }


        if (instance.isSleeping()){
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/bear/sleepintext.png");

        } else {
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/bear/beartexture.png");

        }
    }


    @Override
    public RenderType getRenderType(BearEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {

        if (animatable.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
        } else {
            stack.scale(1.0F, 1.0F, 1.0F);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

}