package net.jacob.bygonecreatures.item.custom;

import net.jacob.bygonecreatures.item.client.AnimatedBlockItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AnimatedBlockItem extends BlockItem implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public AnimatedBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {


        event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new AnimatedBlockItemRenderer();

//            @Override
//            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//                return renderer;
//            }
        });
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                2, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}