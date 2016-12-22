// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class GameUpdateRequestAIToHuman extends Table {
  public static GameUpdateRequestAIToHuman getRootAsGameUpdateRequestAIToHuman(ByteBuffer _bb) { return getRootAsGameUpdateRequestAIToHuman(_bb, new GameUpdateRequestAIToHuman()); }
  public static GameUpdateRequestAIToHuman getRootAsGameUpdateRequestAIToHuman(ByteBuffer _bb, GameUpdateRequestAIToHuman obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public GameUpdateRequestAIToHuman __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String host() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer hostAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String aiName() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer aiNameAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }

  public static int createGameUpdateRequestAIToHuman(FlatBufferBuilder builder,
      int hostOffset,
      int aiNameOffset) {
    builder.startObject(2);
    GameUpdateRequestAIToHuman.addAiName(builder, aiNameOffset);
    GameUpdateRequestAIToHuman.addHost(builder, hostOffset);
    return GameUpdateRequestAIToHuman.endGameUpdateRequestAIToHuman(builder);
  }

  public static void startGameUpdateRequestAIToHuman(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addHost(FlatBufferBuilder builder, int hostOffset) { builder.addOffset(0, hostOffset, 0); }
  public static void addAiName(FlatBufferBuilder builder, int aiNameOffset) { builder.addOffset(1, aiNameOffset, 0); }
  public static int endGameUpdateRequestAIToHuman(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishGameUpdateRequestAIToHumanBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}
