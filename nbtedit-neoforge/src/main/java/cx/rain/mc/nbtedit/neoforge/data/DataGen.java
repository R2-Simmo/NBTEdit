package cx.rain.mc.nbtedit.neoforge.data;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.data.provider.LanguageProviderENUS;
import cx.rain.mc.nbtedit.neoforge.data.provider.LanguageProviderZHCN;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        var exHelper = event.getExistingFileHelper();
        var packOutput = generator.getPackOutput();

        if (event.includeClient()) {
        }

        if (event.includeServer()) {
            generator.addProvider(true, new LanguageProviderENUS(packOutput));
            generator.addProvider(true, new LanguageProviderZHCN(packOutput));
        }
    }
}