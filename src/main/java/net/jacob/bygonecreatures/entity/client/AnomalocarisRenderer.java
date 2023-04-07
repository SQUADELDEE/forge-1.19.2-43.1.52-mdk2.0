package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.AnomaloEntity;
import net.jacob.bygonecreatures.entity.custom.ArmoredFishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class AnomalocarisRenderer extends GeoEntityRenderer<AnomaloEntity> {

    public AnomalocarisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AnomalocarisModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(AnomaloEntity instance) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/anomalocaris/anotexture.png");
    }



    @Override
    public RenderType getRenderType(AnomaloEntity animatable, float partialTicks, PoseStack stack,
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
//    protected void applyRotations(AnomaloEntity endWhale, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
//        super.applyRotations(endWhale, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
//        float f = 4.3F * Mth.sin(0.6F * ageInTicks);
//        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));
//        if (!endWhale.isInWater()) {
//            matrixStackIn.translate(0.1F, 0.1F, -0.1F);
//            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
//        }
//
//
//
//    }
}
