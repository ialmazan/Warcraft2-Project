// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class GetJoinableGamesResponse extends Table {
  public static GetJoinableGamesResponse getRootAsGetJoinableGamesResponse(ByteBuffer _bb) { return getRootAsGetJoinableGamesResponse(_bb, new GetJoinableGamesResponse()); }
  public static GetJoinableGamesResponse getRootAsGetJoinableGamesResponse(ByteBuffer _bb, GetJoinableGamesResponse obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public GetJoinableGamesResponse __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public byte status() { int o = __offset(4); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public com.ecs160.fbs.Game joinableGames(int j) { return joinableGames(new com.ecs160.fbs.Game(), j); }
  public com.ecs160.fbs.Game joinableGames(com.ecs160.fbs.Game obj, int j) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int joinableGamesLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createGetJoinableGamesResponse(FlatBufferBuilder builder,
      byte status,
      int joinable_gamesOffset) {
    builder.startObject(2);
    GetJoinableGamesResponse.addJoinableGames(builder, joinable_gamesOffset);
    GetJoinableGamesResponse.addStatus(builder, status);
    return GetJoinableGamesResponse.endGetJoinableGamesResponse(builder);
  }

  public static void startGetJoinableGamesResponse(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addStatus(FlatBufferBuilder builder, byte status) { builder.addByte(0, status, 0); }
  public static void addJoinableGames(FlatBufferBuilder builder, int joinableGamesOffset) { builder.addOffset(1, joinableGamesOffset, 0); }
  public static int createJoinableGamesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startJoinableGamesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endGetJoinableGamesResponse(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishGetJoinableGamesResponseBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

