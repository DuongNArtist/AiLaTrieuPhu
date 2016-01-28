package com.skynet.ailatrieuphu.sockets;

import java.util.ArrayList;

import com.skynet.ailatrieuphu.constants.Constants;
import com.skynet.ailatrieuphu.models.AccountModel;
import com.skynet.ailatrieuphu.models.ResultModel;
import com.skynet.ailatrieuphu.utilities.NetworkUtility;

public class SendData {

	public static void sendProvider() {
		Message message = new Message(Protocol.CMD_REQUEST_SEND_PROVIDER);
		IOUtility.writeByte(Constants.PROVIDER_ID, message);
		SocketManger.sendMessage(message);
	}

	public static void sendPlatform(String platf, String did) {
		Message message = new Message(Protocol.CMD_REQUEST_SEND_PLATFORM);
		IOUtility.writeString(platf, message);
		IOUtility.writeString(did, message);
		SocketManger.sendMessage(message);
	}

	public static Message loginFacebook(AccountModel userModel) {
		Message message = new Message(Protocol.CMD_REQUEST_LOGIN);
		IOUtility.writeInt(1, message);
		IOUtility.writeString(userModel.getUsername(), message);
		IOUtility.writeString(userModel.getPassword(), message);
		IOUtility.writeString(userModel.getEmail(), message);
		IOUtility.writeString(userModel.getName(), message);
		IOUtility.writeString(userModel.getBirthDay(), message);
		IOUtility.writeString(userModel.getAddress(), message);
		IOUtility.writeString(NetworkUtility.getMacAddress(), message);
		return message;
	}

	public static Message loginNormal(AccountModel accountModel) {
		Message message = new Message(Protocol.CMD_REQUEST_LOGIN);
		IOUtility.writeInt(0, message);
		IOUtility.writeString(accountModel.getUsername(), message);
		IOUtility.writeString(accountModel.getPassword(), message);
		IOUtility.writeString(NetworkUtility.getMacAddress(), message);
		return message;
	}

	public static Message requestNormalPackageQuestion(String language) {
		Message message = new Message(
				Protocol.CMD_REQUEST_GET_NORMAL_PLAY_PACKAGE_QUESTION);
		IOUtility.writeString(language, message);
		return message;
	}

	public static Message requestSubmitNormalPlayResult(long id,
			ArrayList<ResultModel> resultModels, boolean quit) {
		Message message = new Message(
				Protocol.CMD_REQUEST_SUBMIT_NORMAL_PLAY_RESULT);
		IOUtility.writeInt(resultModels.size(), message);
		if (resultModels.size() > 0) {
			IOUtility.writeLong(id, message);
			for (ResultModel resultModel : resultModels) {
				IOUtility.writeInt(resultModel.getId(), message);
				IOUtility.writeInt(resultModel.getLevel(), message);
				IOUtility.writeInt(resultModel.getTime(), message);
				IOUtility.writeBoolean(resultModel.isRight(), message);
			}
			IOUtility.writeBoolean(quit, message);
		}
		return message;
	}

	public static Message requestGetRankAll() {
		Message message = new Message(Protocol.CMD_REQUEST_GET_RANK_ALL);
		return message;
	}

	public static Message requestGetRankVtv3() {
		Message message = new Message(Protocol.CMD_REQUEST_GET_RANK_VTV3);
		return message;
	}

	public static Message requestCheckInDirectVtv3(String language) {
		Message message = new Message(Protocol.CMD_REQUEST_CHECK_IN_DIRECT_VTV3);
		IOUtility.writeString(language, message);
		return message;
	}

	public static Message requestCheckOutDirectVtv3() {
		Message message = new Message(
				Protocol.CMD_REQUEST_CHECK_OUT_DIRECT_VTV3);
		return message;
	}

	public static Message requestSubmitVtv3InDirectResult(int id,
			ArrayList<ResultModel> resultModels) {
		Message message = new Message(Protocol.CMD_REQUEST_SUBMIT_INDIRECT);
		IOUtility.writeInt(resultModels.size(), message);
		if (resultModels.size() > 0) {
			IOUtility.writeInt(id, message);
			for (ResultModel resultModel : resultModels) {
				IOUtility.writeInt(resultModel.getId(), message);
				IOUtility.writeInt(resultModel.getLevel(), message);
				IOUtility.writeInt(resultModel.getTime(), message);
				IOUtility.writeBoolean(resultModel.isRight(), message);
			}
		}
		return message;
	}

	public static Message requestSubmitVtv3DirectResult(ResultModel resultModel) {
		Message message = new Message(
				Protocol.CMD_REQUEST_SUBMIT_VTV3_PLAY_ANSWER);
		IOUtility.writeInt(resultModel.getId(), message);
		IOUtility.writeInt(resultModel.getLevel(), message);
		if (resultModel.isRight()) {
			IOUtility.writeInt(1, message);
		} else {
			IOUtility.writeInt(0, message);
		}
		IOUtility.writeInt(resultModel.getTime(), message);

		return message;
	}

	public static Message requestGetSelectedWeekPackageQuestion(int id) {
		Message message = new Message(
				Protocol.CMD_REQUEST_GET_SELECTED_WEEK_PACKAGE_QUESTION);
		IOUtility.writeInt(id, message);
		return message;
	}

	public static Message requestGetListOfWeek(int page, String language) {
		Message message = new Message(Protocol.CMD_REQUEST_GET_LIST_OF_WEEK);
		IOUtility.writeInt(page, message);
		IOUtility.writeString(language, message);
		return message;
	}

	public static Message requestGetListOfPlayerDirectVtv3(int page) {
		Message message = new Message(Protocol.CMD_REQUEST_GET_RANK_PLAYER_VTV3);
		IOUtility.writeInt(page, message);
		return message;
	}

	public static Message requestGetNewGame() {
		Message message = new Message(Protocol.CMD_REQUEST_NEW_GAME);
		return message;
	}

	public static Message requestJoinVtv3() {
		Message message = new Message(Protocol.CMD_REQUEST_JOIN_VTV3);
		return message;
	}
}