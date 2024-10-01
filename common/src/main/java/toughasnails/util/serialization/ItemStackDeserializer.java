/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package toughasnails.util.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class ItemStackDeserializer {

  private static final String NAME = "id";
  private static final String STACK_SIZE = "Count";
  private static final String TAG_COMPOUND = "tag";

  public static ItemStack deserialize(JsonObject jsonObject) {
    String name = null;
    int stackSize = 1;
    CompoundTag tagCompound = null;

    if (jsonObject.has(NAME)) {
      name = GsonHelper.getAsString(jsonObject, NAME);
    }

    if (jsonObject.has(STACK_SIZE)) {
      stackSize = GsonHelper.getAsInt(jsonObject, STACK_SIZE);
    }

    if (jsonObject.has(TAG_COMPOUND)) {
      try {
        var tagJsonObject = GsonHelper.getAsJsonObject(jsonObject, TAG_COMPOUND);
        tagCompound = TagParser.parseTag(tagJsonObject.toString());
      } catch (CommandSyntaxException e) {
        e.printStackTrace();
      }
    }

    if (name != null) {
      ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(name)), stackSize);
      if (tagCompound != null) {
        itemStack.setTag(tagCompound);
      }
      return itemStack;
    }

    return null;
  }

  public static JsonElement serialize(ItemStack itemStack) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(NAME, BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getPath());
    jsonObject.addProperty(STACK_SIZE, itemStack.getCount());
    CompoundTag tag = itemStack.getTag();
    if (tag != null) {
      jsonObject.addProperty(TAG_COMPOUND, tag.toString());
    }
    return jsonObject;
  }
}