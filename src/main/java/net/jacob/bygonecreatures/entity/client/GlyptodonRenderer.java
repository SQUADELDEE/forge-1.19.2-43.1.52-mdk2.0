package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.DodoEntity;
import net.jacob.bygonecreatures.entity.custom.GlyptodonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GlyptodonRenderer extends GeoEntityRenderer<GlyptodonEntity> {

    public GlyptodonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GlyptodonModel());
        this.shadowRadius = 0.8f;
    }

    @Override
    public ResourceLocation getTextureLocation(GlyptodonEntity instance) {


        if (instance.isSaddled()){
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/glyptodon/gsaddletexturet2.png");

        } else {
            return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/glyptodon/gtexture.png");

        }
    }


    @Override
    public RenderType getRenderType(GlyptodonEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {

        if (animatable.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
        } else {
            stack.scale(1.5F, 1.5F, 1.5F);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

}
