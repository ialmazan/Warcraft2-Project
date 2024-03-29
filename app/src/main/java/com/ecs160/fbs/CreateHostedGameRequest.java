// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class CreateHostedGameRequest extends Table {
  public static CreateHostedGameRequest getRootAsCreateHostedGameRequest(ByteBuffer _bb) { return getRootAsCreateHostedGameRequest(_bb, new CreateHostedGameRequest()); }
  public static CreateHostedGameRequest getRootAsCreateHostedGameRequest(ByteBuffer _bb, CreateHostedGameRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public CreateHostedGameRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public com.ecs160.fbs.GameConstants gameConstants() { return gameConstants(new com.ecs160.fbs.GameConstants()); }
  public com.ecs160.fbs.GameConstants gameConstants(com.ecs160.fbs.GameConstants obj) { int o = __offset(4); return o != 0 ? obj.__init(__indirect(o + bb_pos), bb) : null; }

  public static int createCreateHostedGameRequest(FlatBufferBuilder builder,
      int gameConstantsOffset) {
    builder.startObject(1);
    CreateHostedGameRequest.addGameConstants(builder, gameConstantsOffset);
    return CreateHostedGameRequest.endCreateHostedGameRequest(builder);
  }

  public static void startCreateHostedGameRequest(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addGameConstants(FlatBufferBuilder builder, int gameConstantsOffset) { builder.addOffset(0, gameConstantsOffset, 0); }
  public static int endCreateHostedGameRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishCreateHostedGameRequestBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

