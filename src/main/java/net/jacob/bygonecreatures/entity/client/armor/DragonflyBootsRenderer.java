package net.jacob.bygonecreatures.entity.client.armor;

import net.jacob.bygonecreatures.item.client.custom.DragonflyBootsItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class DragonflyBootsRenderer extends GeoArmorRenderer<DragonflyBootsItem> {
    public DragonflyBootsRenderer() {
        super(new DragonflyBootsModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
