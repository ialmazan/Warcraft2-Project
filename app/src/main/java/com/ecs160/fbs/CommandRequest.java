// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class CommandRequest extends Table {
  public static CommandRequest getRootAsCommandRequest(ByteBuffer _bb) { return getRootAsCommandRequest(_bb, new CommandRequest()); }
  public static CommandRequest getRootAsCommandRequest(ByteBuffer _bb, CommandRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public CommandRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public com.ecs160.fbs.Command command() { return command(new com.ecs160.fbs.Command()); }
  public com.ecs160.fbs.Command command(com.ecs160.fbs.Command obj) { int o = __offset(4); return o != 0 ? obj.__init(__indirect(o + bb_pos), bb) : null; }

  public static int createCommandRequest(FlatBufferBuilder builder,
      int commandOffset) {
    builder.startObject(1);
    CommandRequest.addCommand(builder, commandOffset);
    return CommandRequest.endCommandRequest(builder);
  }

  public static void startCommandRequest(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addCommand(FlatBufferBuilder builder, int commandOffset) { builder.addOffset(0, commandOffset, 0); }
  public static int endCommandRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishCommandRequestBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}
