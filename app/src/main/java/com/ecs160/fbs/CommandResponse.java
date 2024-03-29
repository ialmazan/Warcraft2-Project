// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class CommandResponse extends Table {
  public static CommandResponse getRootAsCommandResponse(ByteBuffer _bb) { return getRootAsCommandResponse(_bb, new CommandResponse()); }
  public static CommandResponse getRootAsCommandResponse(ByteBuffer _bb, CommandResponse obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public CommandResponse __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public byte status() { int o = __offset(4); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public com.ecs160.fbs.Command playerCommands(int j) { return playerCommands(new com.ecs160.fbs.Command(), j); }
  public com.ecs160.fbs.Command playerCommands(com.ecs160.fbs.Command obj, int j) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int playerCommandsLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createCommandResponse(FlatBufferBuilder builder,
      byte status,
      int playerCommandsOffset) {
    builder.startObject(2);
    CommandResponse.addPlayerCommands(builder, playerCommandsOffset);
    CommandResponse.addStatus(builder, status);
    return CommandResponse.endCommandResponse(builder);
  }

  public static void startCommandResponse(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addStatus(FlatBufferBuilder builder, byte status) { builder.addByte(0, status, 0); }
  public static void addPlayerCommands(FlatBufferBuilder builder, int playerCommandsOffset) { builder.addOffset(1, playerCommandsOffset, 0); }
  public static int createPlayerCommandsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startPlayerCommandsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endCommandResponse(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishCommandResponseBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

