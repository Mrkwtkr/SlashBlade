package mods.flammpfeil.slashblade;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.flammpfeil.slashblade.ability.JustGuard;
import mods.flammpfeil.slashblade.named.*;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import net.minecraft.command.ICommand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;

@Mod(name = SlashBlade.modname, modid = SlashBlade.modid, version = SlashBlade.version)
public class SlashBlade implements IFuelHandler{


	public static final String modname = "SlashBlade";
    public static final String modid = "flammpfeil.slashblade";
    public static final String version = "@VERSION@";

	public static final String BrokenBladeWhiteStr = "BrokenBladeWhite";
	public static final String HundredKillSilverBambooLightStr = "HundredKillSilverBambooLight";

	public static ItemSlashBlade weapon;
	public static ItemSlashBladeDetune bladeWood;
	public static ItemSlashBladeDetune bladeBambooLight;
	public static ItemSlashBladeDetune bladeSilverBambooLight;
    public static ItemSlashBladeDetune bladeWhiteSheath;
    public static ItemSlashBladeNamed bladeNamed;

    public static ItemSlashBladeWrapper wrapBlade = null;

	public static Item proudSoul;

	public static Configuration mainConfiguration;

	public static ConfigEntityListManager manager;


	public static final String ProudSoulStr = "proudsoul";
	public static final String IngotBladeSoulStr = "ingot_bladesoul";
    public static final String SphereBladeSoulStr = "sphere_bladesoul";
    public static final String TinyBladeSoulStr = "tiny_bladesoul";

    public static final SlashBladeTab tab = new SlashBladeTab("flammpfeil.slashblade");

    public static final EventBus InitEventBus = new EventBus();

    //ability
    public static JustGuard abilityJustGuard;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt){
		mainConfiguration = new Configuration(evt.getSuggestedConfigurationFile());

		try{
			mainConfiguration.load();
            /*
            {
                Property prop;
                prop = mainConfiguration.get(Configuration.CATEGORY_GENERAL,"useDettuneBlades",useDetuneBlades);
                useDetuneBlades = prop.getBoolean(useDetuneBlades);
            }
            {
                Property prop;
                prop = mainConfiguration.get(Configuration.CATEGORY_GENERAL,"useWrapBlades",useWrapBlades);
                useWrapBlades = prop.getBoolean(useWrapBlades);
            }
            {
                Property prop;
                prop = mainConfiguration.get(Configuration.CATEGORY_GENERAL,"scabbardRecipeLevel",scabbardRecipeLevel,"value:0 - 2 , only work useWrapBlades:true");
                scabbardRecipeLevel = prop.getInt();
            }
            */
/*
			Property propOffsets;
			propOffsets = mainConfiguration.get(Configuration.CATEGORY_GENERAL, "OffsetX", 0.0);
			offsetX = (float)propOffsets.getDouble(0.0);

			propOffsets = mainConfiguration.get(Configuration.CATEGORY_GENERAL, "OffsetY", 0.0);
			offsetY = (float)propOffsets.getDouble(0.0);

			propOffsets = mainConfiguration.get(Configuration.CATEGORY_GENERAL, "OffsetZ", 0.0);
			offsetZ = (float)propOffsets.getDouble(0.0);
*/
		}
		finally
		{
			mainConfiguration.save();
		}


		proudSoul = (new ItemSWaeponMaterial())
				.setUnlocalizedName("flammpfeil.slashblade.proudsoul")
				.setTextureName("flammpfeil.slashblade:proudsoul")
				.setCreativeTab(tab);
		GameRegistry.registerItem(proudSoul,"proudsoul");

		ItemStack itemProudSoul = new ItemStack(proudSoul,1,0);
		itemProudSoul.setRepairCost(-10);
		GameRegistry.registerCustomItemStack(ProudSoulStr , itemProudSoul);
		ItemStack itemIngotBladeSoul = new ItemStack(proudSoul,1,1);
		itemIngotBladeSoul.setRepairCost(-25);
		GameRegistry.registerCustomItemStack(IngotBladeSoulStr , itemIngotBladeSoul);
        ItemStack itemSphereBladeSoul = new ItemStack(proudSoul,1,2);
        itemSphereBladeSoul.setRepairCost(-50);
        GameRegistry.registerCustomItemStack(SphereBladeSoulStr , itemSphereBladeSoul);
        ItemStack itemTinyBladeSoul = new ItemStack(proudSoul,1,3);
        GameRegistry.registerCustomItemStack(TinyBladeSoulStr , itemTinyBladeSoul);

        //==================================================================================================================================

		weapon = (ItemSlashBlade)(new ItemSlashBlade(ToolMaterial.IRON, 4 + ToolMaterial.EMERALD.getDamageVsEntity()))
				.setRepairMaterial(new ItemStack(Items.iron_ingot))
				.setRepairMaterialOreDic("ingotSteel","nuggetSteel")
				.setUnlocalizedName("flammpfeil.slashblade")
				.setTextureName("flammpfeil.slashblade:proudsoul")
				.setCreativeTab(tab);

		GameRegistry.registerItem(weapon, "slashblade");

        //==================================================================================================================================

        bladeWood = (ItemSlashBladeDetune)(new ItemSlashBladeDetune(ToolMaterial.WOOD, 4 + ToolMaterial.WOOD.getDamageVsEntity()))
                .setDestructable(true)
                .setModelTexture(new ResourceLocation("flammpfeil.slashblade","model/wood.png"))
                .setRepairMaterialOreDic("logWood")
                .setMaxDamage(60)
                .setUnlocalizedName("flammpfeil.slashblade.wood")
                .setTextureName("flammpfeil.slashblade:proudsoul")
                .setCreativeTab(tab);
        GameRegistry.registerItem(bladeWood, "slashbladeWood");

        bladeBambooLight = (ItemSlashBladeDetune)(new ItemSlashBladeDetune(ToolMaterial.WOOD, 4 + ToolMaterial.STONE.getDamageVsEntity()))
                .setDestructable(true)
                .setModelTexture(new ResourceLocation("flammpfeil.slashblade","model/banboo.png"))
                .setRepairMaterialOreDic("bamboo")
                .setMaxDamage(50)
                .setUnlocalizedName("flammpfeil.slashblade.bamboo")
                .setTextureName("flammpfeil.slashblade:proudsoul")
                .setCreativeTab(tab);
        GameRegistry.registerItem(bladeBambooLight, "slashbladeBambooLight");

        bladeSilverBambooLight = (ItemSlashBladeBambooLight)(new ItemSlashBladeBambooLight(ToolMaterial.WOOD, 4 + ToolMaterial.IRON.getDamageVsEntity()))
                .setDestructable(true)
                .setModelTexture(new ResourceLocation("flammpfeil.slashblade","model/silverbanboo.png"))
                .setRepairMaterialOreDic("bamboo")
                .setMaxDamage(40)
                .setUnlocalizedName("flammpfeil.slashblade.silverbamboo")
                .setTextureName("flammpfeil.slashblade:proudsoul")
                .setCreativeTab(tab);
        GameRegistry.registerItem(bladeSilverBambooLight, "slashbladeSilverBambooLight");

        bladeWhiteSheath = (ItemSlashBladeDetune)(new ItemSlashBladeDetune(ToolMaterial.IRON, 4 + ToolMaterial.IRON.getDamageVsEntity()))
                .setDestructable(false)
                .setModelTexture(new ResourceLocation("flammpfeil.slashblade","model/white.png"))
                .setRepairMaterial(new ItemStack(Items.iron_ingot))
                .setRepairMaterialOreDic("ingotSteel","nuggetSteel")
                .setMaxDamage(70)
                .setUnlocalizedName("flammpfeil.slashblade.white")
                .setTextureName("flammpfeil.slashblade:proudsoul")
                .setCreativeTab(tab);
        GameRegistry.registerItem(bladeWhiteSheath, "slashbladeWhite");

        //==================================================================================================================================

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bladeWood),
                "  #",
                " # ",
                "X  ",
                '#', "logWood",
                'X', new ItemStack(Items.wooden_sword, 1, 1)));


        GameRegistry.addRecipe(new RecipeUpgradeBlade(new ItemStack(bladeBambooLight),
                "  #",
                " # ",
                "X  ",
                '#',"bamboo",
                'X', new ItemStack(bladeWood,1, OreDictionary.WILDCARD_VALUE)));


        GameRegistry.addRecipe(new RecipeUpgradeBlade(new ItemStack(bladeSilverBambooLight),
                " TI",
                "SXK",
                "PS ",
                'T', Items.egg,
                'I', Items.iron_ingot,
                'S', Items.string,
                'X', new ItemStack(bladeBambooLight,1,OreDictionary.WILDCARD_VALUE),
                'K', "dyeBlack",
                'P', Items.paper //S
                ));
        GameRegistry.addRecipe(new RecipeUpgradeBlade(new ItemStack(bladeSilverBambooLight),
                " TI",
                "SXK",
                "PS ",
                'T', Items.egg,
                'I', "ingotSilver",
                'S', Items.string,
                'X', new ItemStack(bladeBambooLight,1,OreDictionary.WILDCARD_VALUE),
                'K', "dyeBlack",
                'P', Items.paper
                ));


        ItemStack hundredKillSilverBambooLight = new ItemStack(bladeSilverBambooLight,1,0);
        hundredKillSilverBambooLight.setItemDamage(hundredKillSilverBambooLight.getMaxDamage());
        hundredKillSilverBambooLight.setStackDisplayName("HundredKillSBL");
        ItemSlashBlade.KillCount.set(hundredKillSilverBambooLight.getTagCompound(), 100);
        GameRegistry.registerCustomItemStack(HundredKillSilverBambooLightStr, hundredKillSilverBambooLight);

        GameRegistry.addRecipe(new RecipeUpgradeBlade(new ItemStack(bladeWhiteSheath, 1, bladeWhiteSheath.getMaxDamage() / 3),
                "  #",
                " # ",
                "XG ",
                '#', Items.iron_ingot,
                'G', Items.gold_ingot,
                'X', new ItemStack(bladeWood,1,OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new RecipeUpgradeBlade(new ItemStack(bladeWhiteSheath, 1, bladeWhiteSheath.getMaxDamage() / 4),
                "  #",
                " # ",
                "XG ",
                '#', "ingotSteel",
                'G', Items.gold_ingot,
                'X', new ItemStack(bladeWood,1,OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new RecipeUpgradeBlade(new ItemStack(bladeWhiteSheath, 1),
                "  #",
                " # ",
                "XG ",
                '#', itemIngotBladeSoul,
                'G', Items.gold_ingot,
                'X', new ItemStack(bladeWood,1,OreDictionary.WILDCARD_VALUE)));

        ItemStack brokenBladeWhite = new ItemStack(bladeWhiteSheath,1,0);
        brokenBladeWhite.setItemDamage(brokenBladeWhite.getMaxDamage());
        brokenBladeWhite.setStackDisplayName("BrokenBladeWhite");
        ItemSlashBlade.IsBroken.set(brokenBladeWhite.getTagCompound(), true);
        GameRegistry.registerCustomItemStack(BrokenBladeWhiteStr, brokenBladeWhite);

        GameRegistry.addRecipe(new RecipeUpgradeBlade(new ItemStack(weapon),
                " BI",
                "L#C",
                "SG ",
                'L', Blocks.lapis_block,
                'C', Blocks.coal_block,
                'I', itemSphereBladeSoul,
                'B', Items.blaze_rod,
                'G', Items.gold_ingot,
                'S', Items.string,
                '#', brokenBladeWhite
                ));

        //==================================================================================================================================

        wrapBlade = (ItemSlashBladeWrapper)(new ItemSlashBladeWrapper(ToolMaterial.IRON))
                .setMaxDamage(40)
                .setUnlocalizedName("flammpfeil.slashblade.wrapper")
                .setTextureName("flammpfeil.slashblade:proudsoul")
                .setCreativeTab(tab);
        GameRegistry.registerItem(wrapBlade, "slashbladeWrapper");


        GameRegistry.addRecipe(new RecipeWrapBlade());

        //==================================================================================================================================


        GameRegistry.addRecipe(new ShapelessOreRecipe(GameRegistry.findItemStack(modid,TinyBladeSoulStr,2),
                itemProudSoul));

        GameRegistry.addRecipe(new ShapedOreRecipe(itemIngotBladeSoul,
                "PPP",
                "PIP",
                "PPP",
                'I', Items.iron_ingot,
                'P', itemProudSoul));

        GameRegistry.addRecipe(new ShapedOreRecipe(itemIngotBladeSoul,
                " P ",
                "PIP",
                " P ",
                'I', "ingotSteel",
                'P', itemProudSoul));

        GameRegistry.addSmelting(itemIngotBladeSoul , itemSphereBladeSoul, 2.0F);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.experience_bottle),
                "XXX",
                "XIX",
                "XXX",
                'I',Items.glass_bottle,
                'X',itemProudSoul));

        //==================================================================================================================================


        bladeNamed = (ItemSlashBladeNamed)(new ItemSlashBladeNamed(ToolMaterial.IRON, 4.0f))
                .setMaxDamage(40)
                .setUnlocalizedName("flammpfeil.slashblade.named")
                .setTextureName("flammpfeil.slashblade:proudsoul")
                .setCreativeTab(tab);
        GameRegistry.registerItem(bladeNamed, "slashbladeNamed");

        {
            ItemStack customblade = new ItemStack(bladeNamed,1,0);
            NBTTagCompound tag = new NBTTagCompound();
            customblade.setTagCompound(tag);

            customblade.addEnchantment(Enchantment.unbreaking,3);
            customblade.addEnchantment(Enchantment.smite,3);
            String name = "flammpfeil.slashblade.named.tagayasan";
            ItemSlashBladeNamed.IsDefaultBewitched.set(tag,true);
            ItemSlashBladeNamed.CurrentItemName.set(tag, name);
            ItemSlashBladeNamed.CustomMaxDamage.set(tag, 70);
            ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.IRON.getDamageVsEntity());
            ItemSlashBlade.TextureName.set(tag,"named/tagayasan");
            ItemSlashBlade.SpecialAttackType.set(tag, 1);
            ItemSlashBlade.StandbyRenderType.set(tag, 1);

            tag.setString(ItemSlashBladeNamed.RepairMaterialNameStr,"iron_ingot");

            GameRegistry.registerCustomItemStack(name, customblade);
            ItemSlashBladeNamed.NamedBlades.add(name);
            {
                ItemStack reqiredBlade = new ItemStack(bladeWood);
                reqiredBlade.setItemDamage(OreDictionary.WILDCARD_VALUE);
                NBTTagCompound reqTag = ItemSlashBlade.getItemTagCompound(reqiredBlade);
                ItemSlashBlade.KillCount.set(reqTag,1000);

                reqiredBlade.setStackDisplayName("thousandkill woodblade");

                name = "flammpfeil.slashblade.tagayasan.reqired";
                GameRegistry.registerCustomItemStack(name, reqiredBlade);
                ItemSlashBladeNamed.NamedBlades.add(name);

                GameRegistry.addRecipe(new RecipeAwakeBlade(customblade,
                        reqiredBlade,
                        "XEX",
                        "PBP",
                        "XEX",
                        'X',itemSphereBladeSoul,
                        'B',reqiredBlade,
                        'P',new ItemStack(Items.ender_pearl),
                        'E',new ItemStack(Items.ender_eye)));
            }
        }

        {
            String nameTrue = "flammpfeil.slashblade.named.yamato";
            {

                ItemStack customblade = new ItemStack(bladeNamed,1,0);
                NBTTagCompound tag = new NBTTagCompound();
                customblade.setTagCompound(tag);

                customblade.addEnchantment(Enchantment.thorns, 1);
                customblade.addEnchantment(Enchantment.featherFalling, 4);
                customblade.addEnchantment(Enchantment.power, 5);
                customblade.addEnchantment(Enchantment.punch, 2);

                ItemSlashBladeNamed.CurrentItemName.set(tag, nameTrue);
                ItemSlashBladeNamed.IsDefaultBewitched.set(tag, true);
                ItemSlashBladeNamed.CustomMaxDamage.set(tag, 40);
                ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.EMERALD.getDamageVsEntity());
                ItemSlashBlade.TextureName.set(tag,"named/yamato");
                ItemSlashBlade.ModelName.set(tag,"named/yamato");
                ItemSlashBlade.SpecialAttackType.set(tag, 0);
                ItemSlashBlade.StandbyRenderType.set(tag, 1);

                ItemSlashBlade.KillCount.set(tag,1000);
                ItemSlashBlade.ProudSoul.set(tag, 1000);
                GameRegistry.registerCustomItemStack(nameTrue,customblade);
                ItemSlashBladeNamed.NamedBlades.add(nameTrue);
            }

            {

                ItemStack customblade = new ItemStack(bladeNamed,1,0);
                NBTTagCompound tag = new NBTTagCompound();
                customblade.setTagCompound(tag);

                String nameBrokend = nameTrue + ".broken";
                ItemSlashBladeNamed.CurrentItemName.set(tag, nameBrokend);

                ItemSlashBladeNamed.CustomMaxDamage.set(tag, 40);
                ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.EMERALD.getDamageVsEntity());
                ItemSlashBlade.TextureName.set(tag, "named/yamato");
                ItemSlashBlade.ModelName.set(tag, "named/yamato");
                ItemSlashBlade.SpecialAttackType.set(tag, 0);
                ItemSlashBlade.StandbyRenderType.set(tag, 1);

                ItemSlashBlade.IsBroken.set(tag, true);
                ItemSlashBlade.IsNoScabbard.set(tag, true);
                ItemSlashBlade.IsSealed.set(tag, true);
                ItemSlashBladeNamed.TrueItemName.set(tag, nameTrue);
                GameRegistry.registerCustomItemStack(nameBrokend, customblade);
                ItemSlashBladeNamed.NamedBlades.add(nameBrokend);

                {
                    ItemStack reqiredBlade = customblade.copy();
                    reqiredBlade.setItemDamage(OreDictionary.WILDCARD_VALUE);
                    NBTTagCompound reqTag = ItemSlashBlade.getItemTagCompound(reqiredBlade);
                    ItemSlashBlade.ProudSoul.set(reqTag, 1000);

                    reqiredBlade.setStackDisplayName("thousandProudSouls");

                    String nameReqired = nameTrue + ".reqired";
                    GameRegistry.registerCustomItemStack(nameReqired, reqiredBlade);
                    ItemSlashBladeNamed.NamedBlades.add(nameReqired);

                    ItemStack yamato = GameRegistry.findItemStack(modid,nameTrue,1);
                    GameRegistry.addRecipe(new RecipeAwakeBlade(yamato,
                            reqiredBlade,
                            "XXX",
                            "XBX",
                            "XXX",
                            'X',itemSphereBladeSoul,
                            'B',reqiredBlade));
                }
            }
        }

        {
            ItemStack customblade = new ItemStack(bladeNamed,1,0);
            NBTTagCompound tag = new NBTTagCompound();
            customblade.setTagCompound(tag);

            customblade.addEnchantment(Enchantment.sharpness, 4);
            customblade.addEnchantment(Enchantment.unbreaking, 3);
            customblade.addEnchantment(Enchantment.fireAspect, 2);

            String name = "flammpfeil.slashblade.named.yuzukitukumo";
            ItemSlashBladeNamed.CurrentItemName.set(tag, name);
            ItemSlashBladeNamed.IsDefaultBewitched.set(tag, true);
            ItemSlashBladeNamed.CustomMaxDamage.set(tag, 40);
            ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.EMERALD.getDamageVsEntity());
            ItemSlashBlade.TextureName.set(tag, "named/a_tukumo");
            ItemSlashBlade.ModelName.set(tag, "named/agito");
            ItemSlashBlade.SpecialAttackType.set(tag, 3);
            ItemSlashBlade.StandbyRenderType.set(tag, 1);

            GameRegistry.registerCustomItemStack(name, customblade);
            ItemSlashBladeNamed.NamedBlades.add(name);

            {
                ItemStack custombladeReqired = new ItemStack(weapon,1,0);
                custombladeReqired.setItemDamage(OreDictionary.WILDCARD_VALUE);

                NBTTagCompound tagReqired = new NBTTagCompound();
                custombladeReqired.setTagCompound(tagReqired);

                custombladeReqired.addEnchantment(Enchantment.fireAspect, 1);

                ItemSlashBlade.KillCount.set(tagReqired, 1000);

                String nameReqired = "flammpfeil.slashblade.thousandkill";
                GameRegistry.registerCustomItemStack(nameReqired, custombladeReqired);
                ItemSlashBladeNamed.NamedBlades.add(nameReqired);

                GameRegistry.addRecipe(new RecipeAwakeBlade(customblade,
                        custombladeReqired,
                        "ESD",
                        "RBL",
                        "ISG",
                        'E', new ItemStack(Blocks.emerald_block),
                        'D', new ItemStack(Blocks.diamond_block),
                        'R', new ItemStack(Blocks.redstone_block),
                        'L', new ItemStack(Blocks.lapis_block),
                        'I', new ItemStack(Blocks.iron_block),
                        'G', new ItemStack(Blocks.gold_block),
                        'S', GameRegistry.findItemStack(SlashBlade.modid,SlashBlade.SphereBladeSoulStr,1),
                        'B', custombladeReqired));

            }
        }

        ItemSlashBladeNamed.NamedBlades.add(SlashBlade.BrokenBladeWhiteStr);
        ItemSlashBladeNamed.NamedBlades.add(SlashBlade.HundredKillSilverBambooLightStr);


        //==================================================================================================================================

        {
            //------------- false
            String nameAgito = "flammpfeil.slashblade.named.agito";
            String nameAgitoRust = nameAgito + ".rust";
            String nameAgitoReqired = nameAgito + ".reqired";
            {
                ItemStack customblade = new ItemStack(bladeNamed,1,0);
                NBTTagCompound tag = new NBTTagCompound();
                customblade.setTagCompound(tag);

                ItemSlashBladeNamed.CurrentItemName.set(tag, nameAgito);
                ItemSlashBladeNamed.CustomMaxDamage.set(tag, 60);
                ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.IRON.getDamageVsEntity());
                ItemSlashBlade.TextureName.set(tag, "named/agito_false");
                ItemSlashBlade.ModelName.set(tag, "named/agito");
                ItemSlashBlade.SpecialAttackType.set(tag, 2);
                ItemSlashBlade.StandbyRenderType.set(tag, 2);

                GameRegistry.registerCustomItemStack(nameAgito, customblade);
                ItemSlashBladeNamed.NamedBlades.add(nameAgito);
            }


            {
                ItemStack customblade = new ItemStack(bladeNamed,1,0);
                NBTTagCompound tag = new NBTTagCompound();
                customblade.setTagCompound(tag);

                ItemSlashBladeNamed.CurrentItemName.set(tag, nameAgitoRust);

                ItemSlashBladeNamed.CustomMaxDamage.set(tag, 60);
                ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.STONE.getDamageVsEntity());
                ItemSlashBlade.TextureName.set(tag, "named/agito_rust");
                ItemSlashBlade.ModelName.set(tag, "named/agito");
                ItemSlashBlade.SpecialAttackType.set(tag, 2);
                ItemSlashBlade.StandbyRenderType.set(tag, 2);

                ItemSlashBlade.IsSealed.set(tag, true);

                ItemSlashBladeNamed.TrueItemName.set(tag, nameAgito);

                GameRegistry.registerCustomItemStack(nameAgitoRust, customblade);
                ItemSlashBladeNamed.NamedBlades.add(nameAgitoRust);


                {
                    ItemStack reqiredBlade = customblade.copy();
                    reqiredBlade.setItemDamage(OreDictionary.WILDCARD_VALUE);
                    NBTTagCompound reqTag = ItemSlashBlade.getItemTagCompound(reqiredBlade);
                    ItemSlashBlade.KillCount.set(reqTag,100);
                    ItemSlashBlade.RepairCount.set(reqTag,1);

                    reqiredBlade.setStackDisplayName("agito rust");

                    GameRegistry.registerCustomItemStack(nameAgitoReqired, reqiredBlade);
                    ItemSlashBladeNamed.NamedBlades.add(nameAgitoReqired);

                    ItemStack destBlade = GameRegistry.findItemStack(modid,ItemSlashBladeNamed.TrueItemName.get(tag),1);
                    GameRegistry.addRecipe(new RecipeAwakeBlade(destBlade,
                            reqiredBlade,
                            " X ",
                            "XBX",
                            " X ",
                            'X',itemProudSoul,
                            'B',reqiredBlade));
                }
            }
        }

        //------------- true
        {
            String nameOrotiagito = "flammpfeil.slashblade.named.orotiagito";
            String nameOrotiagitoSeald = nameOrotiagito + ".seald";
            String nameOrotiagitoReqired = nameOrotiagito + ".reqired";
            String nameOrotiagitoRust = nameOrotiagito + ".rust";
            String nameOrotiagitoSealdReqired = nameOrotiagitoSeald + ".reqired";


            {
                ItemStack customblade = new ItemStack(bladeNamed,1,0);
                NBTTagCompound tag = new NBTTagCompound();
                customblade.setTagCompound(tag);

                ItemSlashBladeNamed.CurrentItemName.set(tag, nameOrotiagito);

                ItemSlashBladeNamed.CustomMaxDamage.set(tag, 60);
                ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.EMERALD.getDamageVsEntity());
                ItemSlashBlade.TextureName.set(tag, "named/orotiagito");
                ItemSlashBlade.ModelName.set(tag, "named/agito");
                ItemSlashBlade.SpecialAttackType.set(tag, 2);
                ItemSlashBlade.StandbyRenderType.set(tag, 2);

                GameRegistry.registerCustomItemStack(nameOrotiagito, customblade);
                ItemSlashBladeNamed.NamedBlades.add(nameOrotiagito);

                String brokableTest = nameOrotiagito + ".damaged";
                ItemStack brokable = customblade.copy();
                brokable.setItemDamage(brokable.getMaxDamage());
                GameRegistry.registerCustomItemStack(brokableTest, brokable);
                ItemSlashBladeNamed.NamedBlades.add(brokableTest);
            }

            {
                ItemStack customblade = new ItemStack(bladeNamed,1,0);
                NBTTagCompound tag = new NBTTagCompound();
                customblade.setTagCompound(tag);

                ItemSlashBladeNamed.CurrentItemName.set(tag, nameOrotiagitoSeald);

                ItemSlashBladeNamed.CustomMaxDamage.set(tag, 60);
                ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.IRON.getDamageVsEntity());
                ItemSlashBlade.TextureName.set(tag,"named/agito_true");
                ItemSlashBlade.ModelName.set(tag,"named/agito");
                ItemSlashBlade.SpecialAttackType.set(tag, 2);
                ItemSlashBlade.StandbyRenderType.set(tag, 2);

                ItemSlashBladeNamed.TrueItemName.set(tag, nameOrotiagito);

                GameRegistry.registerCustomItemStack(nameOrotiagitoSeald, customblade);
                ItemSlashBladeNamed.NamedBlades.add(nameOrotiagitoSeald);


                {
                    ItemStack reqiredBlade = customblade.copy();
                    reqiredBlade.setItemDamage(OreDictionary.WILDCARD_VALUE);
                    NBTTagCompound reqTag = ItemSlashBlade.getItemTagCompound(reqiredBlade);
                    ItemSlashBlade.KillCount.set(reqTag, 1000);
                    ItemSlashBlade.ProudSoul.set(reqTag,1000);
                    ItemSlashBlade.RepairCount.set(reqTag, 10);

                    reqiredBlade.setStackDisplayName("orotiagito seald");

                    GameRegistry.registerCustomItemStack(nameOrotiagitoReqired, reqiredBlade);
                    ItemSlashBladeNamed.NamedBlades.add(nameOrotiagitoReqired);

                    ItemStack destBlade = GameRegistry.findItemStack(modid,ItemSlashBladeNamed.TrueItemName.get(tag),1);
                    GameRegistry.addRecipe(new RecipeAwakeBlade(destBlade,
                            reqiredBlade,
                            "PXP",
                            "XBX",
                            "PXP",
                            'X',itemSphereBladeSoul,
                            'P',itemProudSoul,
                            'B',reqiredBlade));
                }
            }


            {
                ItemStack customblade = new ItemStack(bladeNamed,1,0);
                NBTTagCompound tag = new NBTTagCompound();
                customblade.setTagCompound(tag);

                ItemSlashBladeNamed.CurrentItemName.set(tag, nameOrotiagitoRust);

                ItemSlashBladeNamed.CustomMaxDamage.set(tag, 60);
                ItemSlashBlade.setBaseAttackModifier(tag, 4 + ToolMaterial.STONE.getDamageVsEntity());
                ItemSlashBlade.TextureName.set(tag, "named/agito_rust_true");
                ItemSlashBlade.ModelName.set(tag, "named/agito");
                ItemSlashBlade.SpecialAttackType.set(tag, 2);
                ItemSlashBlade.StandbyRenderType.set(tag, 2);

                ItemSlashBlade.IsSealed.set(tag, true);

                ItemSlashBladeNamed.TrueItemName.set(tag, nameOrotiagitoSeald);

                GameRegistry.registerCustomItemStack(nameOrotiagitoRust, customblade);
                ItemSlashBladeNamed.NamedBlades.add(nameOrotiagitoRust);

                {
                    ItemStack reqiredBlade = customblade.copy();
                    reqiredBlade.setItemDamage(OreDictionary.WILDCARD_VALUE);
                    NBTTagCompound reqTag = ItemSlashBlade.getItemTagCompound(reqiredBlade);
                    ItemSlashBlade.KillCount.set(reqTag, 100);
                    ItemSlashBlade.RepairCount.set(reqTag, 1);

                    reqiredBlade.setStackDisplayName("agito rust");

                    GameRegistry.registerCustomItemStack(nameOrotiagitoSealdReqired, reqiredBlade);
                    ItemSlashBladeNamed.NamedBlades.add(nameOrotiagitoSealdReqired);

                    ItemStack destBlade = GameRegistry.findItemStack(modid,ItemSlashBladeNamed.TrueItemName.get(tag),1);
                    GameRegistry.addRecipe(new RecipeAwakeBlade(destBlade,
                            reqiredBlade,
                            " X ",
                            "XBX",
                            " X ",
                            'X',itemProudSoul,
                            'B',reqiredBlade));
                }
            }
        }
        //==================================================================================================================================

        GameRegistry.addRecipe(new RecipeAdjustPos());

        RecipeInstantRepair recipeRepair = new RecipeInstantRepair();
        GameRegistry.addRecipe(recipeRepair);

        FMLCommonHandler.instance().bus().register(recipeRepair);

		GameRegistry.registerFuelHandler(this);

		InitProxy.proxy.initializeItemRenderer();

		manager = new ConfigEntityListManager();

        FMLCommonHandler.instance().bus().register(manager);

        PacketHandler.init();

        RecipeSorter.register("flammpfeil.slashblade:upgrade", RecipeUpgradeBlade.class, SHAPED, "after:forge:shaped");
        RecipeSorter.register("flammpfeil.slashblade:wrap", RecipeWrapBlade.class, SHAPED, "after:forge:shaped");
        RecipeSorter.register("flammpfeil.slashblade:adjust", RecipeAdjustPos.class, SHAPED, "after:forge:shaped");
        RecipeSorter.register("flammpfeil.slashblade:repair", RecipeInstantRepair.class, SHAPED, "after:forge:shaped");
        RecipeSorter.register("flammpfeil.slashblade:awake", RecipeAwakeBlade.class, SHAPED, "after:forge:shaped");


        InitEventBus.register(new PSSange());
        InitEventBus.register(new Fox());
        InitEventBus.register(new Tizuru());
    }

    StatManager statManager;

    @EventHandler
    public void init(FMLInitializationEvent evt){
        EntityRegistry.registerModEntity(EntityDrive.class, "Drive", 1, this, 250, 1, true);
        EntityRegistry.registerModEntity(EntityPhantomSword.class, "PhantomSword", 2, this, 250, 1, true);
        EntityRegistry.registerModEntity(EntityDirectAttackDummy.class, "DirectAttackDummy", 3, this, 250, 1, true);


        MinecraftForge.EVENT_BUS.register(new DropEventHandler());

        MinecraftForge.EVENT_BUS.register(new SlashBladeItemDestroyEventHandler());

        //ability
        abilityJustGuard = new JustGuard();
        MinecraftForge.EVENT_BUS.register(abilityJustGuard);

        statManager = new StatManager();
        MinecraftForge.EVENT_BUS.register(statManager);

        statManager.registerItemStat(weapon, weapon, "SlashBlade");
        statManager.registerItemStat(bladeWood, weapon, "SlashBlade");
        statManager.registerItemStat(bladeBambooLight, weapon, "SlashBlade");
        statManager.registerItemStat(bladeSilverBambooLight, weapon, "SlashBlade");
        statManager.registerItemStat(bladeWhiteSheath, weapon, "SlashBlade");
        statManager.registerItemStat(wrapBlade, weapon, "SlashBlade");
        statManager.registerItemStat(bladeNamed, weapon, "SlashBlade");

        DropEventHandler.registerEntityDrop("HardcoreEnderExpansion.Dragon", 1.0f, GameRegistry.findItemStack(modid, "flammpfeil.slashblade.named.yamato.broken", 1));
        DropEventHandler.registerEntityDrop("EnderDragon", 1.0f, GameRegistry.findItemStack(modid, "flammpfeil.slashblade.named.yamato.broken", 1));

        DropEventHandler.registerEntityDrop("TwilightForest.Hydra", 0.3f, GameRegistry.findItemStack(modid, "flammpfeil.slashblade.named.orotiagito.rust", 1));
        DropEventHandler.registerEntityDrop("TwilightForest.Naga",0.3f,GameRegistry.findItemStack(modid,"flammpfeil.slashblade.named.agito.rust",1));

        InitEventBus.post(new LoadEvent.InitEvent(evt));
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent evt)
    {
        ArrayList<ItemStack> items = OreDictionary.getOres("bamboo");
        if(0 == items.size()){
            ItemStack itemSphereBladeSoul =
                    GameRegistry.findItemStack(modid, SphereBladeSoulStr, 1);

            GameRegistry.addRecipe(new ShapedOreRecipe(wrapBlade,
                    "RBL",
                    "CIC",
                    "LBR",
                    'C', Blocks.coal_block,
                    'R', Blocks.lapis_block,
                    'B', Blocks.obsidian,
                    'I', itemSphereBladeSoul,
                    'L', "logWood"));
        }

        InitEventBus.post(new LoadEvent.PostInitEvent(evt));
    }


	@Override
	public int getBurnTime(ItemStack fuel) {
		return (fuel.getItem() == this.proudSoul && fuel.getItemDamage() == 0) ? 20000 : 0;
	}


/*
    ICommand command;
    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        command = new CommandHandler();
        evt.registerServerCommand(command);
    }
*/


    public static ItemStack getCustomBlade(String modid,String name){
        ItemStack blade = GameRegistry.findItemStack(modid, name, 1);

        if(blade != null){
            NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);

            if(ItemSlashBladeNamed.IsDefaultBewitched.get(tag)){
                blade.setStackDisplayName(blade.getDisplayName());
            }
        }

        return blade;
    }
    public static ItemStack getCustomBlade(String key){
        String modid;
        String name;
        {
            String str[] = key.split(":",2);
            if(str.length == 2){
                modid = str[0];
                name = str[1];
            }else{
                modid = SlashBlade.modid;
                name = key;
            }
        }

        return getCustomBlade(modid,name);
    }
}
