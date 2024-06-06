package cx.rain.mc.nbtedit.nbt;

import com.google.common.base.Strings;
import net.minecraft.nbt.*;

@Deprecated(forRemoval = true)
public class NBTHelper {
	public static final char SECTION_SIGN = '\u00A7';

	public static String getNBTNameSpecial(NBTTree.Node<?> nbtNode) {
		String name = nbtNode.getName();
		Tag tag = nbtNode.getTag();

		if (tag instanceof CompoundTag) {
			return (Strings.isNullOrEmpty(name) ? "(CompoundTag)" : name) + ": ";
		}

		if (tag instanceof ListTag) {
			return (Strings.isNullOrEmpty(name) ? "(ListTag)" : name) + ": ";
		}

		String s = tag.getAsString();
		return Strings.isNullOrEmpty(name) ? s : name + ": " + s + SECTION_SIGN + 'r';
	}

	public static String toString(Tag tag) {
		return tag.getAsString();
	}

	public static Tag newTag(byte type) {
		return switch (type) {
			case 0 -> ByteTag.valueOf((byte) 0);
			case 1 -> ShortTag.valueOf((short) 0);
			case 2 -> IntTag.valueOf(0);
			case 3 -> LongTag.valueOf(0);
			case 4 -> FloatTag.valueOf(0.0f);
			case 5 -> DoubleTag.valueOf(0.0);
			case 6 -> new ByteArrayTag(new byte[0]);
			case 7 -> StringTag.valueOf("");
			case 8 -> new ListTag();
			case 9 -> new CompoundTag();
			case 10 -> new IntArrayTag(new int[0]);
			case 11 -> new LongArrayTag(new long[0]);
			default -> null;
		};
	}

	public static Tag of(byte type, int value) {
		return switch (type) {
			case 0 -> EndTag.INSTANCE;
			case 1 -> ByteTag.valueOf((byte) value);
			case 2 -> ShortTag.valueOf((short) value);
			case 3 -> IntTag.valueOf(value);
			case 4 -> LongTag.valueOf(value);
			case 5 -> FloatTag.valueOf(value);
			case 6 -> DoubleTag.valueOf(value);
			case 7 -> new ByteArrayTag(new byte[0]);
			case 8 -> StringTag.valueOf("");
			case 9 -> new ListTag();
			case 10 -> new CompoundTag();
			case 11 -> new IntArrayTag(new int[0]);
			case 12 -> new LongArrayTag(new long[0]);
			default -> null;
		};
	}

	public static String getNameByButton(byte id) {
		return switch (id) {
			case 1 -> "Byte";
			case 2 -> "Short";
			case 3 -> "Int";
			case 4 -> "Long";
			case 5 -> "Float";
			case 6 -> "Double";
			case 7 -> "ByteArray";
			case 8 -> "String";
			case 9 -> "List";
			case 10 -> "Compound";
			case 11 -> "IntArray";
			case 12 -> "LongArray";
			case 13 -> "Edit";
			case 14 -> "Delete";
			case 15 -> "Paste";
			case 16 -> "Cut";
			case 17 -> "Copy";
			default -> "Unknown";
		};
	}
}
