package com.skynet.ailatrieuphu.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.skynet.ailatrieuphu.facebook.FacebookCallback;
import com.skynet.ailatrieuphu.models.FacebookModel;

public class FaceBookUtility {
    private final static String TAG = FaceBookUtility.class.getSimpleName();
    public final static String URL_AVATAR = "https://graph.facebook.com/%s/picture?type=large&width=200&height=200";

    public static void authFacebookForPublish(final Activity activity,
            StatusCallback fbCallback) {
        List<String> permissions = new ArrayList<String>();
        permissions.add("publish_actions");
        permissions.add("publish_stream");
        permissions.add("email");
        permissions.add("user_location");
        permissions.add("user_checkins");
        permissions.add("offline_access");
        permissions.add("user_photos");

        Session session = Session.getActiveSession();
        if (session == null) {
            session = new Session.Builder(activity).build();
        } else if (session.getState() == SessionState.CLOSED_LOGIN_FAILED
                || session.getState() == SessionState.CLOSED) {
            session.closeAndClearTokenInformation();
            session = new Session.Builder(activity).build();
        }
        if (!session.isOpened() && !session.isClosed()) {
            Session.OpenRequest openRequest = new Session.OpenRequest(activity);

            openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);

            openRequest.setCallback(fbCallback);
            openRequest.setPermissions(permissions);
            Session.setActiveSession(session);
            session.openForPublish(openRequest);
        } else {
            session = Session.openActiveSession(activity, true, fbCallback);
        }

    }

    /**
     * Authenticate Facebook for read personal data
     * 
     * @param fbCallback
     */
    public static void authFacebookForRead(Activity activity,
            StatusCallback fbCallback) {
        OpenRequest op = new Session.OpenRequest(activity);
        // setup permission for Facebook
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("user_location");
        permissions.add("user_checkins");
        permissions.add("offline_access");
        permissions.add("user_photos");
        op.setPermissions(permissions);
        op.setCallback(fbCallback);
        Session session = Session.getActiveSession();
        if (session == null) {
            session = new Session(activity);
            Session.setActiveSession(session);
            session.openForRead(op);
            session = Session.openActiveSession(activity, true, fbCallback);
        } else {
            session = Session.openActiveSession(activity, true, fbCallback);
        }
    }

    public static void shareLinkWithDialog(final Activity ac, String name,
            String caption, String description, String link, String image) {
        final Bundle params = new Bundle();
        params.putString("name", name);
        params.putString("caption", caption);
        params.putString("description", description);
        params.putString("link", link);
        params.putString("picture", image);

        StatusCallback fbCallback = new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                if (session.isOpened()) {
                    WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(ac,
                            session, params)).setOnCompleteListener(
                            new OnCompleteListener() {

                                @Override
                                public void onComplete(Bundle values,
                                        FacebookException error) {
                                    if (error instanceof FacebookOperationCanceledException) {
                                        Toast.makeText(ac, "Publish cancelled",
                                                Toast.LENGTH_SHORT).show();
                                    } else if (error != null) {
                                        Toast.makeText(
                                                ac,
                                                "Error posting story due to network error.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }).build();
                    feedDialog.show();
                }
            }
        };

        authFacebookForPublish(ac, fbCallback);

    }

    public static void sharePhotoWidthDialog(final Activity ac,
            final String caption, final String description, final String link,
            final String image) {
        StatusCallback fbCallback = new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                if (session.isOpened()) {
                    final Bundle parameters = new Bundle(3);
                    File file = new File(image);
                    ParcelFileDescriptor descriptor;
                    try {
                        descriptor = ParcelFileDescriptor.open(file,
                                ParcelFileDescriptor.MODE_READ_ONLY);
                        parameters.putParcelable("picture", descriptor);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    parameters.putString("caption", caption);
                    parameters.putString("description", description);
                    WebDialog shareDialog = new WebDialog.RequestsDialogBuilder(
                            ac, session, parameters).build();
                    shareDialog.setOnCompleteListener(new OnCompleteListener() {

                        @Override
                        public void onComplete(Bundle values,
                                FacebookException error) {
                            if (error instanceof FacebookOperationCanceledException) {
                                Toast.makeText(ac, "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            } else if (error != null) {
                                Toast.makeText(
                                        ac,
                                        "Error posting story due to network error.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    shareDialog.show();
                }
            }
        };

        authFacebookForPublish(ac, fbCallback);

    }

    public static void shareLink(final Activity ac, String name,
            String caption, String description, String link, String image) {
        final Bundle postParams = new Bundle();
        postParams.putString("name", name);
        postParams.putString("caption", caption);
        postParams.putString("description", description);
        postParams.putString("link", link);
        postParams.putString("picture", image);

        StatusCallback fbCallback = new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                if (session.isOpened()) {
                    Request.Callback callback = new Request.Callback() {
                        public void onCompleted(Response response) {
                            if (mprogress != null) {
                                mprogress.dismiss();
                            }
                            GraphObject og = response.getGraphObject();
                            if (og != null) {
                                FacebookRequestError error = response
                                        .getError();
                                if (error != null) {
                                    Toast.makeText(ac.getApplicationContext(),
                                            error.getErrorMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ac.getApplicationContext(),
                                            "Share Successful",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    };
                    Request request = new Request(session, "me/feed",
                            postParams, HttpMethod.POST, callback);
                    request.executeAsync();
                    if (mprogress == null) {
                        mprogress = new ProgressDialog(ac);
                        mprogress.setCancelable(false);
                    }
                    mprogress.show();
                }
            }
        };
        authFacebookForPublish(ac, fbCallback);
    }

    public static void sharePhoto(final Activity ac, String caption,
            String description, String image) {
        try {
            final Bundle parameters = new Bundle(3);
            File file = new File(image);
            ParcelFileDescriptor

            descriptor = ParcelFileDescriptor.open(file,
                    ParcelFileDescriptor.MODE_READ_ONLY);

            parameters.putParcelable("picture", descriptor);
            parameters.putString("caption", caption);
            parameters.putString("description", description);

            StatusCallback fbCallback = new StatusCallback() {

                @Override
                public void call(Session session, SessionState state,
                        Exception exception) {
                    if (session.isOpened()) {
                        Request.Callback callback = new Request.Callback() {
                            public void onCompleted(Response response) {
                                if (mprogress != null) {
                                    mprogress.dismiss();
                                }
                                GraphObject og = response.getGraphObject();
                                if (og != null) {
                                    FacebookRequestError error = response
                                            .getError();
                                    if (error != null) {
                                        Toast.makeText(
                                                ac.getApplicationContext(),
                                                error.getErrorMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        };
                        Request request = new Request(session, "me/photos",
                                parameters, HttpMethod.POST, callback);
                        request.executeAsync();
                        if (mprogress == null) {
                            mprogress = new ProgressDialog(ac);
                            mprogress.setCancelable(false);
                        }
                        mprogress.show();
                    }
                }
            };
            authFacebookForPublish(ac, fbCallback);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public static void inviteFriends(final Activity ac) {
        final Bundle params = new Bundle();
        params.putString("title", "Send a Request");
        params.putString("message", "Play with me");
        params.putString("to", "");

        StatusCallback fbCallback = new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                if (session.isOpened()) {
                    WebDialog inviteDialog = (new WebDialog.Builder(ac,
                            session, "apprequests", params))
                            .setOnCompleteListener(new OnCompleteListener() {

                                @Override
                                public void onComplete(Bundle values,
                                        FacebookException error) {
                                    if (error instanceof FacebookOperationCanceledException) {
                                        Toast.makeText(ac, "Publish cancelled",
                                                Toast.LENGTH_SHORT).show();
                                    } else if (error != null) {
                                        Toast.makeText(
                                                ac,
                                                "Error posting story due to network error.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }).build();
                    Window dialog_window = inviteDialog.getWindow();
                    dialog_window.setFlags(
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    inviteDialog.show();
                }
            }

        };

        authFacebookForPublish(ac, fbCallback);
    }

    public static void getFacebookProfile(final Activity ac,
            final FacebookCallback callback) {
        StatusCallback fbCallback = new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                if (session.isOpened()) {
                    final String fbToken = session.getAccessToken();
                    Request request = Request.newMeRequest(session,
                            new GraphUserCallback() {

                                @Override
                                public void onCompleted(GraphUser user,
                                        Response response) {
                                    if (response != null) {
                                        try {
                                            FacebookModel userModel = FacebookModel
                                                    .getInstance();
                                            userModel.setId(user.getId());
                                            userModel.setName(user.getName());
                                            userModel.setEmail((String) user
                                                    .getProperty("email"));
                                            userModel.setToken(fbToken);
                                            userModel.setBirthday(user
                                                    .getBirthday());
                                            userModel.setAddress((String) user
                                                    .getProperty("locale"));
                                            Log.d(TAG,
                                                    user.getId()
                                                            + "; "
                                                            + user.getName()
                                                            + "; "
                                                            + (String) user
                                                                    .getProperty("gender")
                                                            + "; "
                                                            + (String) user
                                                                    .getProperty("email")
                                                            + "; "
                                                            + user.getBirthday()
                                                            + "; "
                                                            + (String) user
                                                                    .getProperty("locale")
                                                            + "; "
                                                            + user.getLocation());
                                            callback.onSucceed(userModel);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            callback.onFailed();
                                        }

                                    }
                                }
                            });
                    request.executeAsync();
                } else if (session.getState() == SessionState.CLOSED_LOGIN_FAILED) {
                    try {
                        session.closeAndClearTokenInformation();
                        session = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callback.onFailed();
                }
            }

        };

        authFacebookForPublish(ac, fbCallback);
    }

    public static void shareImageViaIntent(final Activity act,
            final String pathImg) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TITLE, "Game Hot");
        intent.putExtra(Intent.EXTRA_TEXT, "Cấp độ siêu đẳng");
        intent.putExtra("message", "Ai Là Triệu Phú");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pathImg)));
        Uri uri = Uri.fromFile(new File(pathImg));
        Bundle param = new Bundle();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    act.getContentResolver(), uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArrayData = stream.toByteArray();
            param.putByteArray("picture", byteArrayData);
        } catch (IOException ioe) {
            Assert.assertTrue(false);
        }
        /* Add the caption */
        param.putString("message", "Thách �?ấu Trí Tuệ");
        Session session = Session.getActiveSession();
        Request request = new Request(session, "me/photos", param,
                HttpMethod.POST, new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        if (response.getError() != null) {
                            Intent sharingIntent = new Intent(
                                    Intent.ACTION_SEND);
                            sharingIntent.setType("image/*");
                            sharingIntent.putExtra(Intent.EXTRA_TITLE,
                                    "Game Hot");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Cấp độ siêu đẳng");
                            sharingIntent.putExtra("message",
                                    "Thách �?ấu Trí Tuệ");

                            sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                    Uri.fromFile(new File(pathImg)));
                            act.startActivity(Intent.createChooser(
                                    sharingIntent, "Share image using"));
                        }
                    }
                }, null);
        RequestAsyncTask asyncTask = new RequestAsyncTask(request);
        asyncTask.execute();
        PackageManager pm = act.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName componentName = new ComponentName(
                        activityInfo.applicationInfo.packageName,
                        activityInfo.name);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.setComponent(componentName);
                act.startActivity(intent);
                break;
            }
        }

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, "Game Hot");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Cấp độ siêu đẳng");
        sharingIntent.putExtra("message", "Thách �?ấu Trí Tuệ");

        sharingIntent.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(new File(pathImg)));
        act.startActivity(Intent.createChooser(sharingIntent,
                "Share image using"));
    }

    public static void logOut(final Activity ac) {
        StatusCallback fbCallback = new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                if (!session.isClosed()) {
                    session.closeAndClearTokenInformation();
                }
            }

        };
        authFacebookForPublish(ac, fbCallback);
    }

    public static String getUrlAvatar(String user) {
        return String.format(URL_AVATAR, user);
    }

    private static ProgressDialog mprogress = null;
}
