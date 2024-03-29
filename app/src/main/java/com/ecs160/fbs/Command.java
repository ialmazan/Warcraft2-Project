// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Command extends Table {
  public static Command getRootAsCommand(ByteBuffer _bb) { return getRootAsCommand(_bb, new Command()); }
  public static Command getRootAsCommand(ByteBuffer _bb, Command obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public Command __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String host() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer hostAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public int assetIds(int j) { int o = __offset(6); return o != 0 ? bb.getInt(__vector(o) + j * 4) : 0; }
  public int assetIdsLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer assetIdsAsByteBuffer() { return __vector_as_bytebuffer(6, 4); }
  public int x() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int y() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public byte type() { int o = __offset(12); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public byte targetColor() { int o = __offset(14); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public byte playerColor() { int o = __offset(16); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public byte action() { int o = __offset(18); return o != 0 ? bb.get(o + bb_pos) : 0; }

  public static int createCommand(FlatBufferBuilder builder,
      int hostOffset,
      int assetIdsOffset,
      int x,
      int y,
      byte type,
      byte targetColor,
      byte playerColor,
      byte action) {
    builder.startObject(8);
    Command.addY(builder, y);
    Command.addX(builder, x);
    Command.addAssetIds(builder, assetIdsOffset);
    Command.addHost(builder, hostOffset);
    Command.addAction(builder, action);
    Command.addPlayerColor(builder, playerColor);
    Command.addTargetColor(builder, targetColor);
    Command.addType(builder, type);
    return Command.endCommand(builder);
  }

  public static void startCommand(FlatBufferBuilder builder) { builder.startObject(8); }
  public static void addHost(FlatBufferBuilder builder, int hostOffset) { builder.addOffset(0, hostOffset, 0); }
  public static void addAssetIds(FlatBufferBuilder builder, int assetIdsOffset) { builder.addOffset(1, assetIdsOffset, 0); }
  public static int createAssetIdsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addInt(data[i]); return builder.endVector(); }
  public static void startAssetIdsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addX(FlatBufferBuilder builder, int x) { builder.addInt(2, x, 0); }
  public static void addY(FlatBufferBuilder builder, int y) { builder.addInt(3, y, 0); }
  public static void addType(FlatBufferBuilder builder, byte type) { builder.addByte(4, type, 0); }
  public static void addTargetColor(FlatBufferBuilder builder, byte targetColor) { builder.addByte(5, targetColor, 0); }
  public static void addPlayerColor(FlatBufferBuilder builder, byte playerColor) { builder.addByte(6, playerColor, 0); }
  public static void addAction(FlatBufferBuilder builder, byte action) { builder.addByte(7, action, 0); }
  public static int endCommand(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishCommandBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

