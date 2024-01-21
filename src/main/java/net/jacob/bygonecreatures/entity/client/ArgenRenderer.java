package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ArgenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ArgenRenderer extends GeoEntityRenderer<ArgenEntity> {

public ArgenRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArgenModel());
        this.shadowRadius = 0.3f;
        }

@Override
public ResourceLocation getTextureLocation(ArgenEntity instance) {

        if (instance.isSaddled()){
                return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/argen/argsaddled.png");

        } else {
                return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/argen/argtexture.png");

        }
}
      
       


        






@Override
public RenderType getRenderType(ArgenEntity animatable, float partialTicks, PoseStack stack,
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
