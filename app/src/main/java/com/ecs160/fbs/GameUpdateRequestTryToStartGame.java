// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class GameUpdateRequestTryToStartGame extends Table {
  public static GameUpdateRequestTryToStartGame getRootAsGameUpdateRequestTryToStartGame(ByteBuffer _bb) { return getRootAsGameUpdateRequestTryToStartGame(_bb, new GameUpdateRequestTryToStartGame()); }
  public static GameUpdateRequestTryToStartGame getRootAsGameUpdateRequestTryToStartGame(ByteBuffer _bb, GameUpdateRequestTryToStartGame obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public GameUpdateRequestTryToStartGame __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String host() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer hostAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }

  public static int createGameUpdateRequestTryToStartGame(FlatBufferBuilder builder,
      int hostOffset) {
    builder.startObject(1);
    GameUpdateRequestTryToStartGame.addHost(builder, hostOffset);
    return GameUpdateRequestTryToStartGame.endGameUpdateRequestTryToStartGame(builder);
  }

  public static void startGameUpdateRequestTryToStartGame(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addHost(FlatBufferBuilder builder, int hostOffset) { builder.addOffset(0, hostOffset, 0); }
  public static int endGameUpdateRequestTryToStartGame(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishGameUpdateRequestTryToStartGameBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}
