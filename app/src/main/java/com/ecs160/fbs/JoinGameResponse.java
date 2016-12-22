// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class JoinGameResponse extends Table {
  public static JoinGameResponse getRootAsJoinGameResponse(ByteBuffer _bb) { return getRootAsJoinGameResponse(_bb, new JoinGameResponse()); }
  public static JoinGameResponse getRootAsJoinGameResponse(ByteBuffer _bb, JoinGameResponse obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public JoinGameResponse __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public byte status() { int o = __offset(4); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public com.ecs160.fbs.Game game() { return game(new com.ecs160.fbs.Game()); }
  public com.ecs160.fbs.Game game(com.ecs160.fbs.Game obj) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(o + bb_pos), bb) : null; }
  public com.ecs160.fbs.GameConstants gameConstants() { return gameConstants(new com.ecs160.fbs.GameConstants()); }
  public com.ecs160.fbs.GameConstants gameConstants(com.ecs160.fbs.GameConstants obj) { int o = __offset(8); return o != 0 ? obj.__init(__indirect(o + bb_pos), bb) : null; }

  public static int createJoinGameResponse(FlatBufferBuilder builder,
      byte status,
      int gameOffset,
      int gameConstantsOffset) {
    builder.startObject(3);
    JoinGameResponse.addGameConstants(builder, gameConstantsOffset);
    JoinGameResponse.addGame(builder, gameOffset);
    JoinGameResponse.addStatus(builder, status);
    return JoinGameResponse.endJoinGameResponse(builder);
  }

  public static void startJoinGameResponse(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addStatus(FlatBufferBuilder builder, byte status) { builder.addByte(0, status, 0); }
  public static void addGame(FlatBufferBuilder builder, int gameOffset) { builder.addOffset(1, gameOffset, 0); }
  public static void addGameConstants(FlatBufferBuilder builder, int gameConstantsOffset) { builder.addOffset(2, gameConstantsOffset, 0); }
  public static int endJoinGameResponse(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishJoinGameResponseBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

