package com.skynet.ailatrieuphu.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.constants.Constants;
import com.skynet.ailatrieuphu.dialogs.AppDialog;
import com.skynet.ailatrieuphu.dialogs.ConfirmDialog;
import com.skynet.ailatrieuphu.dialogs.LoadingDialog;
import com.skynet.ailatrieuphu.dialogs.LoginDialog;
import com.skynet.ailatrieuphu.dialogs.MessageDialog;
import com.skynet.ailatrieuphu.dialogs.OnBackListener;
import com.skynet.ailatrieuphu.dialogs.OnConfirmListener;
import com.skynet.ailatrieuphu.dialogs.OnLoginListener;
import com.skynet.ailatrieuphu.facebook.FacebookCallback;
import com.skynet.ailatrieuphu.fragments.BaseFragment;
import com.skynet.ailatrieuphu.fragments.DirectVTV3Fragment;
import com.skynet.ailatrieuphu.fragments.IndirectVTV3Fragment;
import com.skynet.ailatrieuphu.fragments.MenuFragment;
import com.skynet.ailatrieuphu.fragments.NormalFragment;
import com.skynet.ailatrieuphu.fragments.PlayFragment;
import com.skynet.ailatrieuphu.fragments.RankFragment;
import com.skynet.ailatrieuphu.fragments.SplashFragment;
import com.skynet.ailatrieuphu.models.AccountModel;
import com.skynet.ailatrieuphu.models.FacebookModel;
import com.skynet.ailatrieuphu.models.PendingMessageModel;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.sockets.ProcessHandler;
import com.skynet.ailatrieuphu.sockets.Protocol;
import com.skynet.ailatrieuphu.sockets.SendData;
import com.skynet.ailatrieuphu.sockets.SocketListenner;
import com.skynet.ailatrieuphu.sockets.SocketManger;
import com.skynet.ailatrieuphu.utilities.FaceBookUtility;
import com.skynet.ailatrieuphu.utilities.NetworkUtility;
import com.skynet.ailatrieuphu.utilities.Utilities;

public class AiLaTrieuPhuActivity extends BaseFragmentActivity implements
        SocketListenner {

    public static final String TAG = AiLaTrieuPhuActivity.class.getSimpleName();

    public static boolean mEnglish;
    private Handler mHandler;
    private List<PendingMessageModel> mPendingMessages;

    public LinearLayout mllBanner;
    public int mStateLogin = Constants.LOGIN_NOT;
    public int mLoginType = Constants.LOGIN_TYPE_NORMAL;
    public LoadingDialog mLoadingDialog;
    private String[] mMessage;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        mllBanner = (LinearLayout) findViewById(R.id.ll_banner);
        mStateLogin = Constants.LOGIN_NOT;
        if (Utilities.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mLoadingDialog = LoadingDialog.getInstance(this);
        NetworkUtility.getInstance(this);
        ProcessHandler.getInstance().setListenner(this);
        mPendingMessages = new ArrayList<PendingMessageModel>();
        mHandler = new Handler(new MyCallback());
        switchContent(new SplashFragment(), SplashFragment.TAG, false, null);
        getUser();
        mMessage = AiLaTrieuPhuActivity.this.getResources().getStringArray(
                R.array.dtn_login_message);
    }

    public void getUser() {
        String source = PreferenceManager.getInstance(this).getUser();
        if (AccountModel.getInstance().fromString(source)) {
            mStateLogin = Constants.LOGIN_AUTO;
            if (AccountModel.getInstance().getEmail().length() > 3) {
                mLoginType = Constants.LOGIN_TYPE_FACEBOOK;
            } else {
                mLoginType = Constants.LOGIN_TYPE_NORMAL;
            }
        } else {
            mStateLogin = Constants.LOGIN_NOT;
        }
    }

    @Override
    protected void onResume() {
        SocketManger.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        SocketManger.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SocketManger.close();
        SocketManger.stopPing();
        ProcessHandler.getInstance().setListenner(null);
        super.onDestroy();
    }

    @Override
    public void onFailed(int reason) {

        mSocketState = Constants.SOCKET_STATE_DISCONNECTED;
        if (mStateLogin == Constants.LOGIN_OK) {
            mStateLogin = Constants.LOGIN_AUTO;
        }

        AiLaTrieuPhuActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mLoadingDialog.cancel();
                if (mFragment != null
                        && mFragment instanceof DirectVTV3Fragment) {
                    mRequesting = false;
                    ((DirectVTV3Fragment) mFragment).onFailed();
                } else {
                    mLoadingDialog.cancel();
                    if (mRequesting) {
                        mRequesting = false;
                        ((BaseFragment) mFragment).onFailed();
                    }
                }
            }
        });
        mPendingMessages.clear();
        SocketManger.close();
        SocketManger.stopPing();
    }

    @Override
    public void onDisconnected() {
        mSocketState = Constants.SOCKET_STATE_DISCONNECTED;
        AiLaTrieuPhuActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mLoadingDialog.cancel();
                if (mRequesting) {
                    mRequesting = false;
                }
            }
        });
        mPendingMessages.clear();
        SocketManger.close();
        SocketManger.stopPing();
    }

    @Override
    public void onReceived(Message message) {
        AiLaTrieuPhuActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mLoadingDialog.dismiss();
            }
        });
        if (mRequesting) {
            mRequesting = false;
        }
        android.os.Message osMessage = new android.os.Message();
        osMessage.obj = message;
        mHandler.sendMessage(osMessage);
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null) {
            AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
            ((BaseFragment) mFragment).confirmFinish();
        }
    }

    @Override
    public void onConnected() {
        SocketManger.startPing();
        mSocketState = Constants.SOCKET_STATE_CONNECTED;
        for (int i = mPendingMessages.size() - 1; i >= 0; i--) {
            PendingMessageModel mPendingMessageModel = mPendingMessages.get(i);
            if (mPendingMessageModel.isNeedLogin()) {
                if (mStateLogin == Constants.LOGIN_OK) {
                    SocketManger.sendMessage(mPendingMessageModel.getMessage());
                    mPendingMessages.remove(i);
                } else {
                    if (mStateLogin == Constants.LOGIN_AUTO
                            || mStateLogin == Constants.LOGIN_NOT) {
                        mStateLogin = Constants.LOGIN_WAITING;
                        autoLogin();
                    }
                }
            } else {
                SocketManger.sendMessage(mPendingMessageModel.getMessage());
                mPendingMessages.remove(i);
            }
        }
    }

    public void showDialogDisconect() {
        final MessageDialog dialog = MessageDialog.getInstance(this);
        dialog.setMessage(getResources()
                .getString(R.string.bmv_has_not_connect));
        dialog.setOnBackListener(new OnBackListener() {

            @Override
            public void onBack() {
                dialog.dismiss();
                if (mFragment instanceof MenuFragment) {
                    return;
                } else {
                    ((BaseFragment) mFragment).finish();
                }
            }
        });
        dialog.show();
    }

    public boolean sendRequest(Message message, boolean hasDialog,
            boolean isNeedLogin) {
        if (!NetworkUtility.getInstance(this).isConnect()) {
            showDialogDisconect();
            return false;
        } else {
            mRequesting = true;
            if (hasDialog) {
                mLoadingDialog.show();
            }
            PendingMessageModel item = new PendingMessageModel(message,
                    isNeedLogin);
            if (mSocketState == Constants.SOCKET_STATE_CONNECTED) {
                if (!isNeedLogin) {
                    SocketManger.sendMessage(message);
                } else {
                    if (mStateLogin == Constants.LOGIN_NOT) {
                        mPendingMessages.add(item);
                        showDialogLogin();
                    } else if (mStateLogin == Constants.LOGIN_OK) {
                        SocketManger.sendMessage(message);
                    } else if (mStateLogin == Constants.LOGIN_AUTO) {
                        mPendingMessages.add(item);
                        autoLogin();
                    } else {
                        mPendingMessages.add(item);
                    }
                }
            } else {
                mPendingMessages.add(item);
                if (mSocketState == Constants.SOCKET_STATE_DISCONNECTED) {
                    mSocketState = Constants.SOCKET_STATE_CONNECTING;
                    SocketManger.connect();
                }
            }
            return true;
        }
    }

    public void showDialogLogin() {
        LoginDialog.showDialog(this, new OnLoginListener() {

            @Override
            public void onClickNomal(String userName, String password) {
                LoginDialog.closeDialog();
                password = Utilities.md5(password);
                AccountModel.getInstance().setUsername(userName);
                AccountModel.getInstance().setPassword(password);
                mLoginType = Constants.LOGIN_TYPE_NORMAL;
                if (mSocketState == Constants.SOCKET_STATE_CONNECTED) {
                    autoLogin();
                } else if (mSocketState == Constants.SOCKET_STATE_DISCONNECTED) {
                    SocketManger.close();
                    SocketManger.stopPing();
                    SocketManger.connect();
                }
            }

            @Override
            public void onClickFacebook() {
                FaceBookUtility.getFacebookProfile(AiLaTrieuPhuActivity.this,
                        new FacebookCallback() {

                            @Override
                            public void onSucceed(FacebookModel facebookModel) {
                                AccountModel accountModel = AccountModel
                                        .getInstance();
                                accountModel.setUsername(facebookModel.getId());
                                accountModel.setPassword(facebookModel
                                        .getToken());
                                accountModel.setEmail(facebookModel.getEmail());
                                accountModel.setName(facebookModel.getName());
                                accountModel.setBirthDay(facebookModel
                                        .getBirthday());
                                accountModel.setAddress(facebookModel
                                        .getAddress());
                                mLoginType = Constants.LOGIN_TYPE_FACEBOOK;
                                if (mSocketState == Constants.SOCKET_STATE_CONNECTED) {
                                    autoLogin();
                                } else if (mSocketState == Constants.SOCKET_STATE_DISCONNECTED) {
                                    SocketManger.close();
                                    SocketManger.stopPing();
                                    SocketManger.connect();
                                }
                                LoginDialog.closeDialog();
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
            }

            @Override
            public void onClickClose() {
                LoginDialog.closeDialog();
                if (mFragment instanceof NormalFragment) {
                    NormalFragment normalFragment = (NormalFragment) mFragment;
                    if (normalFragment.isReplaying()) {
                        normalFragment.showReplayGameConfirmDialog();
                    } else {
                        normalFragment.finish();
                    }
                } else if (mFragment instanceof DirectVTV3Fragment) {
                    ((DirectVTV3Fragment) mFragment).finishGame();
                } else if (mFragment instanceof IndirectVTV3Fragment) {
                    ((IndirectVTV3Fragment) mFragment).handleCloseDialogLogin();
                }
            }
        });

    }

    private void autoLogin() {
        if (mLoginType == Constants.LOGIN_TYPE_FACEBOOK) {
            Message msg = SendData.loginFacebook(AccountModel.getInstance());
            SocketManger.sendMessage(msg);
        } else {
            Message msg = SendData.loginNormal(AccountModel.getInstance());
            SocketManger.sendMessage(msg);
        }
    }

    public class MyCallback implements Callback {

        @Override
        public boolean handleMessage(android.os.Message msg) {
            try {
                Message message = (Message) msg.obj;
                switch (message.getServerCommand()) {
                case Protocol.CMD_REQUEST_LOGIN:
                    handleMessageLogin(message);
                    break;

                case Protocol.CMD_REQUEST_TIME_BEGIN_DIRECT_VTV3:
                    handleBeginDirectVtv3();
                    break;

                case Protocol.CMD_REQUEST_GET_NORMAL_PLAY_PACKAGE_QUESTION:
                    handleMessageRequestNormalPackageQuestion(message);
                    break;

                case Protocol.CMD_REQUEST_SUBMIT_NORMAL_PLAY_RESULT:
                    handleMessageRequestSubmitNormalPlayResult(message);
                    break;

                case Protocol.CMD_REQUEST_GET_RANK_ALL:
                    handleMessageRequestGetRankAll(message);
                    break;

                case Protocol.CMD_REQUEST_GET_RANK_VTV3:
                    handleMessageRequestGetRankVtv3(message);
                    break;

                case Protocol.CMD_REQUEST_GET_DIRECT_VTV3_PACKAGE_QUESTION:
                    handleMessageRequestGetDirectVtv3PackageQuestion(message);
                    break;

                case Protocol.CMD_REQUEST_GET_LIST_OF_WEEK:
                    handleMessageRequestGetListOfWeek(message);
                    break;

                case Protocol.CMD_REQUEST_GET_RANK_PLAYER_VTV3:
                    handleMessageRequestGetRankPlayerVtv3(message);
                    break;

                case Protocol.CMD_REQUEST_GET_SELECTED_WEEK_PACKAGE_QUESTION:
                    handleMessageRequestGetSelectedWeekPackageQuestion(message);
                    break;

                case Protocol.CMD_REQUEST_SUBMIT_INDIRECT:
                    handleMessageRequestResultIndirect(message);
                    break;
                case Protocol.CMD_REQUEST_CHECK_IN_DIRECT_VTV3:
                    handleMessageRequestCheckInDirectVtv3(message);
                    break;

                case Protocol.CMD_REQUEST_CHECK_OUT_DIRECT_VTV3:
                    handleMessageRequestCheckOutDirectVtv3(message);
                    break;

                case Protocol.CMD_REQUEST_SUBMIT_VTV3_PLAY_ANSWER:
                    handleMessageRequestSubmitVtv3PlayAnswer(message);
                    break;

                case Protocol.CMD_REQUEST_NEW_GAME:
                    AppDialog.getInstance(AiLaTrieuPhuActivity.this).parseMessage(
                            message);
                    break;

                case Protocol.CMD_REQUEST_END_DIRECT_VTV3:
                    handleMessageRequestEndDirectVtv3(message);
                    break;

                case Protocol.CMD_REQUEST_JOIN_VTV3:
                    handleMessageRequestJoinDirectVtv3(message);
                    break;

                default:
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public void handleMessageRequestResultIndirect(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof IndirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestGetDirectVtv3PackageQuestion(
                Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof DirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        public void handleMessageRequestSubmitVtv3PlayAnswer(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof DirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        public void handleMessageRequestCheckOutDirectVtv3(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof DirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        public void handleMessageRequestCheckInDirectVtv3(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof MenuFragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        public void handleMessageRequestGetSelectedWeekPackageQuestion(
                Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof IndirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestGetRankPlayerVtv3(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof DirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestGetListOfWeek(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof IndirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageLogin(Message message) {
            int result = IOUtility.readInt(message);
            String link = null;
            link = IOUtility.readString(message);
            PreferenceManager.getInstance(AiLaTrieuPhuActivity.this).setLink(link);
            if (result == 0) {
                mStateLogin = Constants.LOGIN_OK;
                PreferenceManager.getInstance(AiLaTrieuPhuActivity.this).setUser(
                        AccountModel.getInstance().toString());
                for (int i = 0; i < mPendingMessages.size(); i++) {
                    PendingMessageModel mPendingMessageModel = mPendingMessages
                            .get(i);
                    SocketManger.sendMessage(mPendingMessageModel.getMessage());
                }
                mPendingMessages.clear();
            } else {
                mLoginType = Constants.LOGIN_NOT;
                if (result == 2) {
                    Toast.makeText(AiLaTrieuPhuActivity.this, mMessage[2],
                            Toast.LENGTH_SHORT).show();
                } else if (result == 3) {
                    Toast.makeText(AiLaTrieuPhuActivity.this, mMessage[3],
                            Toast.LENGTH_SHORT).show();
                } else if (result == 4) {
                    Toast.makeText(AiLaTrieuPhuActivity.this, mMessage[4],
                            Toast.LENGTH_SHORT).show();
                }
                showDialogLogin();
            }
        }

        private void handleMessageRequestGetRankVtv3(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof RankFragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestGetRankAll(Message message) {
            boolean chekcA = mFragment != null;
            boolean checkB = mFragment instanceof RankFragment;
            if (chekcA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestSubmitNormalPlayResult(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof NormalFragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestNormalPackageQuestion(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof NormalFragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestEndDirectVtv3(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof DirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleMessageRequestJoinDirectVtv3(Message message) {
            boolean checkA = mFragment != null;
            boolean checkB = mFragment instanceof DirectVTV3Fragment;
            if (checkA && checkB) {
                ((BaseFragment) mFragment).onReceived(AiLaTrieuPhuActivity.this,
                        message);
            }
        }

        private void handleBeginDirectVtv3() {
            if (!(mFragment instanceof DirectVTV3Fragment)) {
                final ConfirmDialog dialog = ConfirmDialog
                        .getInstance(AiLaTrieuPhuActivity.this);
                dialog.setMessage(getResources().getString(
                        R.string.bmv_time_in_vtv3));
                dialog.show();
                dialog.setOnConfirmListener(new OnConfirmListener() {

                    @Override
                    public void onClickYesListener() {
                        dialog.dismiss();
                        if (mFragment instanceof MenuFragment) {
                            switchContent(new DirectVTV3Fragment(),
                                    DirectVTV3Fragment.TAG, true, null);

                        } else {
                            ((PlayFragment) mFragment).finish();
                            switchContent(new DirectVTV3Fragment(),
                                    DirectVTV3Fragment.TAG, true, null);
                        }
                    }

                    @Override
                    public void onClickNoListener() {
                        dialog.dismiss();
                        if (mFragment instanceof NormalFragment) {
                            ((NormalFragment) mFragment)
                                    .showReplayGameConfirmDialog();
                        }
                    }
                });
            }
        }
    }
}
