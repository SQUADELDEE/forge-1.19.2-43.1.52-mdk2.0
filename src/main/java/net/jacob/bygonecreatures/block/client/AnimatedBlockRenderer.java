package net.jacob.bygonecreatures.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jacob.bygonecreatures.block.entity.custom.AnimatedBlockEntity;
import net.jacob.bygonecreatures.entity.client.AukModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class AnimatedBlockRenderer extends GeoBlockRenderer<AnimatedBlockEntity> {


    public AnimatedBlockRenderer(BlockEntityRendererProvider.Context renderManager) {
        super(renderManager, new AnimatedBlockModel());

    }

    @Override
    public RenderType getRenderType(AnimatedBlockEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
