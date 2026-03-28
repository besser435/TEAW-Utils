//package me.besser.teawutils.block;
//
//import net.minecraft.ChatFormatting;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.neoforge.items.IItemHandler;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class WarMonitorBlockEntity extends BlockEntity implements IItemHandler {
//    private int points = 0;
//    private final Map<Item, Integer> progress = new HashMap<>();
//    private final Map<Item, Integer> goals = new HashMap<>();

// TODO: right now you only have to insert one kind of item to get war points. make it so you must insert both items
// before you are given a war point.

//    public WarMonitorBlockEntity(BlockPos pos, BlockState state) {
//        super(ModBlockEntities.WAR_MONITOR_BE.get(), pos, state);
//        addBlockGoal("minecraft:stone", 256);
//        addBlockGoal("teawutils:nether_star_frame", 256);
//    }
//
//    @Override
//    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        if (stack.isEmpty() || !isItemValid(slot, stack)) {
//            return stack;
//        }
//
//        Item item = stack.getItem();
//        int goal = goals.get(item);
//        int current = progress.getOrDefault(item, 0);
//
//        int roomLeft = goal - current;
//        if (roomLeft <= 0)
//            return stack;
//
//        int toAdd = Math.min(stack.getCount(), roomLeft);
//
//        if (!simulate) {
//            int newCount = current + toAdd;
//            if (newCount >= goal) {
//                points++;
//                progress.put(item, 0);
//            } else {
//                progress.put(item, newCount);
//            }
//            setChanged();
//        }
//
//        if (toAdd < stack.getCount()) {
//            return stack.copyWithCount(stack.getCount() - toAdd);
//        }
//
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, ItemStack stack) {
//        return goals.containsKey(stack.getItem());
//    }
//
//    @Override
//    public @NotNull ItemStack getStackInSlot(int slot) { return ItemStack.EMPTY; }
//
//    @Override
//    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) { return ItemStack.EMPTY; }
//
//    @Override
//    public int getSlots() { return 1; }
//
//    @Override
//    public int getSlotLimit(int slot) { return 64; }
//
//
//    public void addBlockGoal(String itemResLoc, int quantityPerPoint) {
//        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemResLoc));
//        if (item != Items.AIR) goals.put(item, quantityPerPoint);
//    }
//
//    // TODO: Should this be Goggle Info? Might be too much for goggles.
//    public void sendStatus(Player player) {
//        player.sendSystemMessage(Component.literal("--- War Monitor Progress ---").withStyle(ChatFormatting.GOLD));
//        player.sendSystemMessage(Component.literal("Total Points: ").append(Component.literal(String.valueOf(points)).withStyle(ChatFormatting.AQUA)));
//
//        goals.forEach((item, goal) -> {
//            int current = progress.getOrDefault(item, 0);
//            MutableComponent itemName = Component.translatable(item.getDescriptionId());
//
//            player.sendSystemMessage(Component.literal("- ")
//                    .append(itemName)
//                    .append(Component.literal(": " + current + "/" + goal)));
//        });
//    }
//
//    @Override
//    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
//        super.saveAdditional(tag, registries);
//        tag.putInt("Points", points);
//        CompoundTag progressTag = new CompoundTag();
//
//        progress.forEach((item, count) -> progressTag.putInt(BuiltInRegistries.ITEM.getKey(item).toString(), count));
//        tag.put("Progress", progressTag);
//    }
//
//    @Override
//    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
//        super.loadAdditional(tag, registries);
//        this.points = tag.getInt("Points");
//        CompoundTag progressTag = tag.getCompound("Progress");
//
//        for (String key : progressTag.getAllKeys()) {
//            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(key));
//            this.progress.put(item, progressTag.getInt(key));
//        }
//    }
//}