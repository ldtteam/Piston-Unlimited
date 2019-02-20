package com.multipiston.coremod.blocks;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.element.simple.TextField;
import com.multipiston.coremod.tileentities.TileEntityMultiPiston;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

import static com.multipiston.coremod.Constants.*;


/**
 * This Class is about the MultiPiston which takes care of pushing others around (In a non mean way).
 */
public class MultiPiston extends AbstractBlockMultiPiston<MultiPiston>
{

    /**
     * The hardness this block has.
     */
    private static final float BLOCK_HARDNESS = 0.0F;

    /**
     * This blocks name.
     */
    public static String name;

    /**
     * The resistance this block has.
     */
    private static final float RESISTANCE = 1F;

    /**
     * Constructor for the Substitution block.
     * sets the creative tab, as well as the resistance and the hardness.
     */
    public MultiPiston(String name)
    {
        super(Material.WOOD);
        initBlock(name);
    }

    /**
     * initialize the block
     * sets the creative tab, as well as the resistance and the hardness.
     */
    private void initBlock(String name)
    {
        setRegistryName(name);
        setCreativeTab(CreativeTabs.SEARCH);
        setTranslationKey(String.format("%s.%s", MOD_ID.toLowerCase(Locale.ENGLISH), name));
        setHardness(BLOCK_HARDNESS);
        setResistance(RESISTANCE);
    }

    @Override
    public boolean onBlockActivated(
            final World worldIn,
            final BlockPos pos,
            final IBlockState state,
            final EntityPlayer playerIn,
            final EnumHand hand,
            final EnumFacing facing,
            final float hitX,
            final float hitY,
            final float hitZ)
    {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!worldIn.isRemote && tileEntity instanceof TileEntityMultiPiston)
        {
            final String[] speedCache = {String.valueOf(((TileEntityMultiPiston) tileEntity).getSpeed())};
            final String[] rangeCache = {String.valueOf(((TileEntityMultiPiston) tileEntity).getRange())};

            BlockOut.getBlockOut().getProxy().getGuiController().openUI(
              playerIn,
              iGuiKeyBuilder -> iGuiKeyBuilder
                                  .forPosition(worldIn, pos)
                                  .usingDefaultData()
                                  .withDefaultItemHandlerManager()
                                  .ofFile(new ResourceLocation("multipiston:gui/blockout_new/multipiston.json"))
                                  .usingData(iBlockOutGuiConstructionDataBuilder ->
                                    {
                                        iBlockOutGuiConstructionDataBuilder
                                          .withControl("root",
                                            RootGuiElement.RootGuiConstructionDataBuilder.class,
                                            rootGuiConstructionDataBuilder -> rootGuiConstructionDataBuilder.withDependentDataContext(DependencyObjectHelper.createFromValue(
                                              tileEntity)))
                                          .withControl("speed_input",
                                            TextField.TextFieldConstructionDataBuilder.class,
                                            textFieldConstructionDataBuilder -> textFieldConstructionDataBuilder
                                                                                  .withDependentContents(DependencyObjectHelper.createFromSetterAndGetter(
                                                                                      (Object context) -> speedCache[0],
                                                                                      (Object context, String input) -> {
                                                                                          speedCache[0] = input;
                                                                                          try
                                                                                          {
                                                                                              ((TileEntityMultiPiston) context).setSpeed(Integer.parseInt(input));
                                                                                          }
                                                                                          catch (final Exception ignored)
                                                                                          {
                                                                                              //Thrown when something other then a number is inserted. disregard.
                                                                                          }
                                                                                      }
                                                                                    , "1")))
                                          .withControl("range_input",
                                            TextField.TextFieldConstructionDataBuilder.class,
                                            textFieldConstructionDataBuilder -> textFieldConstructionDataBuilder
                                                                                  .withDependentContents(DependencyObjectHelper.createFromProperty(
                                                                                    PropertyCreationHelper.createFromNonOptional(
                                                                                      (Object context) -> rangeCache[0],
                                                                                      (Object context, String input) -> {
                                                                                          rangeCache[0] = input;
                                                                                          try
                                                                                          {
                                                                                              ((TileEntityMultiPiston) context).setRange(Integer.parseInt(input));
                                                                                          }
                                                                                          catch (final Exception ignored)
                                                                                          {
                                                                                              //Thrown when something other then a number is inserted. disregard.
                                                                                          }
                                                                                      }
                                                                                    ), "3")))
                                          .withControl(BUTTON_UP,
                                            Button.ButtonConstructionDataBuilder.class,
                                            buttonConstructionDataBuilder -> buttonConstructionDataBuilder
                                                                                  .withClickedEventHandler(
                                                                                    (button, buttonClickedEventArgs) -> {
                                                                                        if (buttonClickedEventArgs != null && buttonClickedEventArgs.isStart())
                                                                                      {
                                                                                          ((TileEntityMultiPiston) tileEntity).directionButtonClicked(button,
                                                                                            buttonClickedEventArgs.getButton());
                                                                                      }
                                                                                    })
                                                                                  .withNormalBackgroundImageResource(DependencyObjectHelper.createFromProperty(
                                                                                    PropertyCreationHelper.createFromNonOptional(
                                                                                      Optional.of((context) -> ((TileEntityMultiPiston) tileEntity).getButtonResource(BUTTON_UP)), Optional.empty()
                                                                                    ), new ResourceLocation("image:plus")))
                                          )
                                          .withControl(BUTTON_DOWN,
                                            Button.ButtonConstructionDataBuilder.class,
                                            buttonConstructionDataBuilder -> buttonConstructionDataBuilder
                                                                                  .withClickedEventHandler(
                                                                                    (button, buttonClickedEventArgs) -> {
                                                                                        if (buttonClickedEventArgs != null && buttonClickedEventArgs.isStart())
                                                                                        {
                                                                                            ((TileEntityMultiPiston) tileEntity).directionButtonClicked(button,
                                                                                              buttonClickedEventArgs.getButton());
                                                                                        }
                                                                                    })
                                                                                  .withNormalBackgroundImageResource(DependencyObjectHelper.createFromProperty(
                                                                                    PropertyCreationHelper.createFromNonOptional(
                                                                                      Optional.of((context) -> ((TileEntityMultiPiston) tileEntity).getButtonResource(BUTTON_DOWN)), Optional.empty()
                                                                                    ), new ResourceLocation("image:minus")))
                                          )
                                          .withControl(BUTTON_RIGHT,
                                            Button.ButtonConstructionDataBuilder.class,
                                            buttonConstructionDataBuilder -> buttonConstructionDataBuilder
                                                                                  .withClickedEventHandler(
                                                                                    (button, buttonClickedEventArgs) -> {
                                                                                        if (buttonClickedEventArgs != null && buttonClickedEventArgs.isStart())
                                                                                        {
                                                                                            ((TileEntityMultiPiston) tileEntity).directionButtonClicked(button,
                                                                                              buttonClickedEventArgs.getButton());
                                                                                        }
                                                                                    })
                                                                                  .withNormalBackgroundImageResource(DependencyObjectHelper.createFromProperty(
                                                                                    PropertyCreationHelper.createFromNonOptional(
                                                                                      Optional.of((context) -> ((TileEntityMultiPiston) tileEntity).getButtonResource(BUTTON_RIGHT)), Optional.empty()
                                                                                    ), new ResourceLocation("image:right")))
                                          )
                                          .withControl(BUTTON_LEFT,
                                            Button.ButtonConstructionDataBuilder.class,
                                            buttonConstructionDataBuilder -> buttonConstructionDataBuilder
                                                                                  .withClickedEventHandler(
                                                                                    (button, buttonClickedEventArgs) -> {
                                                                                        if (buttonClickedEventArgs != null && buttonClickedEventArgs.isStart())
                                                                                        {
                                                                                            ((TileEntityMultiPiston) tileEntity).directionButtonClicked(button,
                                                                                              buttonClickedEventArgs.getButton());
                                                                                        }
                                                                                    })
                                                                                  .withNormalBackgroundImageResource(DependencyObjectHelper.createFromProperty(
                                                                                    PropertyCreationHelper.createFromNonOptional(
                                                                                      Optional.of((context) -> ((TileEntityMultiPiston) tileEntity).getButtonResource(BUTTON_LEFT)), Optional.empty()
                                                                                    ), new ResourceLocation("image:left")))
                                          )
                                          .withControl(BUTTON_FORWARD,
                                            Button.ButtonConstructionDataBuilder.class,
                                            buttonConstructionDataBuilder -> buttonConstructionDataBuilder
                                                                                  .withClickedEventHandler(
                                                                                    (button, buttonClickedEventArgs) -> {
                                                                                        if (buttonClickedEventArgs != null && buttonClickedEventArgs.isStart())
                                                                                        {
                                                                                            ((TileEntityMultiPiston) tileEntity).directionButtonClicked(button,
                                                                                              buttonClickedEventArgs.getButton());
                                                                                        }
                                                                                    })
                                                                                  .withNormalBackgroundImageResource(DependencyObjectHelper.createFromProperty(
                                                                                    PropertyCreationHelper.createFromNonOptional(
                                                                                      Optional.of((context) -> ((TileEntityMultiPiston) tileEntity).getButtonResource(BUTTON_FORWARD)), Optional.empty()
                                                                                    ), new ResourceLocation("image:up")))
                                          )
                                          .withControl(BUTTON_BACKWARD,
                                            Button.ButtonConstructionDataBuilder.class,
                                            buttonConstructionDataBuilder -> buttonConstructionDataBuilder
                                                                                  .withClickedEventHandler(
                                                                                    (button, buttonClickedEventArgs) -> {
                                                                                        if (buttonClickedEventArgs != null && buttonClickedEventArgs.isStart())
                                                                                        {
                                                                                            ((TileEntityMultiPiston) tileEntity).directionButtonClicked(button,
                                                                                              buttonClickedEventArgs.getButton());
                                                                                        }
                                                                                    })
                                                                                  .withNormalBackgroundImageResource(DependencyObjectHelper.createFromProperty(
                                                                                    PropertyCreationHelper.createFromNonOptional(
                                                                                      Optional.of((context) -> ((TileEntityMultiPiston) tileEntity).getButtonResource(BUTTON_BACKWARD)), Optional.empty()
                                                                                    ), new ResourceLocation("image:down")))
                                          );
                                    }
                                  )

            );
        }
        return true;
    }

    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos)
    {
        if(worldIn.isRemote)
        {
            return;
        }
        final TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityMultiPiston)
        {
            ((TileEntityMultiPiston) te).handleRedstone(worldIn.isBlockPowered(pos));
        }
    }

    @Override
    public boolean hasTileEntity(final IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@NotNull final World world, @NotNull final IBlockState state)
    {
        return new TileEntityMultiPiston();
    }

    /**
     * @deprecated (Remove this as soon as minecraft offers anything better).
     */
    @Override
    @Deprecated
    public boolean isFullBlock(final IBlockState state)
    {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks
     * for render.
     *
     * @return true
     *
     * @deprecated
     */
    //todo: remove once we no longer need to support this
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state)
    {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing face)
    {
        return false;
    }
}
