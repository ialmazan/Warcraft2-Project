// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class EndGameRequest extends Table {
  public static EndGameRequest getRootAsEndGameRequest(ByteBuffer _bb) { return getRootAsEndGameRequest(_bb, new EndGameRequest()); }
  public static EndGameRequest getRootAsEndGameRequest(ByteBuffer _bb, EndGameRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public EndGameRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String winners(int j) { int o = __offset(4); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int winnersLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public String losers(int j) { int o = __offset(6); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int losersLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createEndGameRequest(FlatBufferBuilder builder,
      int winnersOffset,
      int losersOffset) {
    builder.startObject(2);
    EndGameRequest.addLosers(builder, losersOffset);
    EndGameRequest.addWinners(builder, winnersOffset);
    return EndGameRequest.endEndGameRequest(builder);
  }

  public static void startEndGameRequest(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addWinners(FlatBufferBuilder builder, int winnersOffset) { builder.addOffset(0, winnersOffset, 0); }
  public static int createWinnersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startWinnersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addLosers(FlatBufferBuilder builder, int losersOffset) { builder.addOffset(1, losersOffset, 0); }
  public static int createLosersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startLosersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endEndGameRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishEndGameRequestBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

