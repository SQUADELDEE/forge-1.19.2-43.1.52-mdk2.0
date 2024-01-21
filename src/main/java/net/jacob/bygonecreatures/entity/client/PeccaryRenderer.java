package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.PeccaryEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PeccaryRenderer extends GeoEntityRenderer<PeccaryEntity> {

    public PeccaryRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PeccaryModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(PeccaryEntity instance) {


        if (instance.isBaby()){
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/peccary/pectext.png");

        } else {
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/peccary/pectext.png");

        }
    }



    @Override
    public RenderType getRenderType(PeccaryEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {

        if(animatable.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
        } else {
            stack.scale(1F, 1F, 1F);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
