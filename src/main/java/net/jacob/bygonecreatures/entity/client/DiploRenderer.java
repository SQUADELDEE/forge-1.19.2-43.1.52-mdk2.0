package net.jacob.bygonecreatures.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.DiplocaulusEntity;
import net.jacob.bygonecreatures.entity.custom.DiplocaulusEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DiploRenderer extends GeoEntityRenderer<DiplocaulusEntity> {
    public DiploRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DiploModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(DiplocaulusEntity instance) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/diplocaulus/diplotexture.png");
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("held_item")) {
            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.05D, 0.2D, -0.9D);
            stack.mulPose(Quaternion.fromXYZ(1.5708F, 1.5708F, 0.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(this.mainHand, ItemTransforms.TransformType.FIXED, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();

            bufferIn = rtb.getBuffer(RenderType.entityCutoutNoCull(this.whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }



    @Override
    public RenderType getRenderType(DiplocaulusEntity animatable, float partialTicks, PoseStack stack,
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
