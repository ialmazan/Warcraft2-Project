// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class SendEndGameResultsRequest extends Table {
  public static SendEndGameResultsRequest getRootAsSendEndGameResultsRequest(ByteBuffer _bb) { return getRootAsSendEndGameResultsRequest(_bb, new SendEndGameResultsRequest()); }
  public static SendEndGameResultsRequest getRootAsSendEndGameResultsRequest(ByteBuffer _bb, SendEndGameResultsRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public SendEndGameResultsRequest __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String winners(int j) { int o = __offset(4); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int winnersLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public String losers(int j) { int o = __offset(6); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int losersLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createSendEndGameResultsRequest(FlatBufferBuilder builder,
      int winnersOffset,
      int losersOffset) {
    builder.startObject(2);
    SendEndGameResultsRequest.addLosers(builder, losersOffset);
    SendEndGameResultsRequest.addWinners(builder, winnersOffset);
    return SendEndGameResultsRequest.endSendEndGameResultsRequest(builder);
  }

  public static void startSendEndGameResultsRequest(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addWinners(FlatBufferBuilder builder, int winnersOffset) { builder.addOffset(0, winnersOffset, 0); }
  public static int createWinnersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startWinnersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addLosers(FlatBufferBuilder builder, int losersOffset) { builder.addOffset(1, losersOffset, 0); }
  public static int createLosersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startLosersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endSendEndGameResultsRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishSendEndGameResultsRequestBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}
