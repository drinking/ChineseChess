package org.drinking.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Point;

public class PacketUtils {
	// type

	public static JSONObject parse(byte[] data) throws JSONException {
		String jstr = new String(data, 0, data.length);
		JSONObject job = new JSONObject(jstr);
		return job;
	}

	public static int getType(byte[] data) throws JSONException {
		JSONObject json = parse(data);
		return json.getInt("type");
	}
	public static Point[] getStep(byte[]data)throws JSONException{
		JSONObject json = parse(data);
		Point start=new Point();
		start.x=9-json.getInt(ProtocolDefine.FROMX);
		start.y=8-json.getInt(ProtocolDefine.FROMY);
		Point end=new Point();
		end.x=9-json.getInt(ProtocolDefine.TOX);
		end.y=8-json.getInt(ProtocolDefine.TOY);
		return new Point[]{start,end};
	}
	public static boolean getAckResult(byte[]data) throws JSONException{
		JSONObject json = parse(data);
		return json.getBoolean(ProtocolDefine.ACK);
	}

	public static byte[] createMoveEvent(int fromx, int fromy, int tox,
			int toy) {
		final JSONObject obj = new JSONObject();
		try {
			obj.put(ProtocolDefine.TYPE, ProtocolDefine.TYPE_NEXTSTEP);
			obj.put(ProtocolDefine.FROMX, fromx);
			obj.put(ProtocolDefine.FROMY, fromy);
			obj.put(ProtocolDefine.TOX, tox);
			obj.put(ProtocolDefine.TOY, toy);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString().getBytes();
	}

	public static byte[] createCommand(int typename) {
		final JSONObject obj = new JSONObject();
		try {
			obj.put(ProtocolDefine.TYPE, typename);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString().getBytes();
	}
	public static byte[] createAct(int typename,boolean value) {
		final JSONObject obj = new JSONObject();
		try {
			obj.put(ProtocolDefine.TYPE, typename);
			obj.put(ProtocolDefine.ACK, value);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString().getBytes();
	}

}
