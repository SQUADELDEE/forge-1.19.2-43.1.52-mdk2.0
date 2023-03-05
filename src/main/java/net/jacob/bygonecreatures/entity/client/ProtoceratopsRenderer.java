package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ProtoceratopsEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ProtoceratopsRenderer extends GeoEntityRenderer<ProtoceratopsEntity> {

    public ProtoceratopsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ProtoceratopsModel());
        this.shadowRadius = 0.8f;
    }

    @Override
    public ResourceLocation getTextureLocation(ProtoceratopsEntity instance) {


        if (instance.isSleeping()){
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/protoceratops/ptexture2.png");

        } else {
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/protoceratops/ptexture.png");

        }
    }


    @Override
    public RenderType getRenderType(ProtoceratopsEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {

        if (animatable.isBaby()) {
            stack.scale(0.4F, 0.4F, 0.4F);
        } else {
            stack.scale(0.8F, 0.8F, 0.8F);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

}