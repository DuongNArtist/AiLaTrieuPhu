package com.android.vending.billing.utilities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;

public class MyBilling {
	static final String TAG = MyBilling.class.getSimpleName();
	static final int RC_REQUEST = 10101;

	private static MyBilling mInstance;

	private IabHelper mHelper;
	private AiLaTrieuPhuActivity mContext;
	private PreferenceManager mPreferenceManager;

	private List<ShopModel> mListItem = new ArrayList<ShopModel>();

	private MyBilling() {
	}

	public static MyBilling getInstance() {
		if (mInstance == null) {
			mInstance = new MyBilling();
		}
		return mInstance;
	}

	public void init(AiLaTrieuPhuActivity ac) {
		mContext = ac;
		mPreferenceManager = PreferenceManager.getInstance(mContext);
		loadData();
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAry5m9TG38Fj313NiYvv0xqfYmAkkPKJy97i2QfSM8rNwx0hjmQUrSb5qWnj8lp0uCJZvMO4GceVrxLFy5cwTP7Opeei8bsUrDZgxHDW6fNZDYwLuupkvFozuZHZRUwmsfototFA/iqnodAqvnGOikcksWaYaVFGVdcBIduZH0vTQSu92q+N313oAFy9gFE42u7wxUaOthIDvXrz2IOB8VfGQYLEgfT9rxI4uxS2sGEZLWBFxHKMwtqGAJgb4wdaIT85WZ9n9uFX30H1tFRXfrIv4JWbKtZKAm4/trxaZ4B7yQ7ciycFuRxPNek0CGI0AYRIjZv5FMs0TxrMdy3WQbwIDAQAB";

		mHelper = new IabHelper(mContext, base64EncodedPublicKey);
		mHelper.enableDebugLogging(true);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					complain("Problem setting up in-app billing: " + result);
					return;
				}
				if (mHelper == null)
					return;

				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			try {
				List<SkuDetails> list = inventory.getAllSku();
				Log.d(TAG, "SkuDetails is " + list);
				for (int i = 0; i < list.size(); i++) {
					Log.d(TAG, list.get(i).toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				List<Purchase> list = inventory.getAllPurchases();
				Log.d(TAG, "Purchase is " + list);
				for (int i = 0; i < list.size(); i++) {
					Log.d(TAG, list.get(i).toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
			// mIsPremium = (premiumPurchase != null &&
			// verifyDeveloperPayload(premiumPurchase));
			// Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" :
			// "NOT PREMIUM"));

			// Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
			// if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
			// Log.d(TAG, "We have gas. Consuming it.");
			// mHelper.consumeAsync(inventory.getPurchase(SKU_GAS),
			// mConsumeFinishedListener);
			// return;
			// }
		}
	};

	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mHelper == null)
			return false;

		return mHelper.handleActivityResult(requestCode, resultCode, data);
	}

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

		// return mPayload.equals(payload);
		return "".equals(payload);
	}

	public void buyItem(String sku) {

	}

	public void purchaseItem(String sku, String payload) {
		// mPayload = GoogleUtil.generatePurchaseString();
		mHelper.launchPurchaseFlow(mContext, sku, RC_REQUEST,
				mPurchaseFinishedListener, payload);
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				complain("Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				return;
			}

			// if (purchase.getSku().equals(SKU_GAS)) {
			// Log.d(TAG, "Purchase is gas. Starting gas consumption.");
			// mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			// }
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (mHelper == null)
				return;

			if (result.isSuccess()) {
				saveData();
			} else {
				complain("Error while consuming: " + result);
			}
		}
	};

	public void onDestroy() {
		try {
			if (mHelper != null) {
				mHelper.dispose();
				mHelper = null;
			}
			if (mListItem != null) {
				mListItem.clear();
			}
			mListItem = null;
			mContext = null;
			mGotInventoryListener = null;
			mPurchaseFinishedListener = null;
			mConsumeFinishedListener = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(mContext);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	void saveData() {
		String mString = "";
		for (int i = 0; i < mListItem.size(); i++) {
			mString += (mListItem.get(i).getId() + prefix);
			mString += mListItem.get(i).getItemId();
		}
		Log.i(TAG, mString);
	}

	private String prefix = "prefixmp;";

	void loadData() {
		try {
			mListItem.clear();
			String ldata = mPreferenceManager.getListPurchase();
			String[] arr = ldata.split(prefix);
			if (arr != null && arr.length % 2 == 0) {
				for (int i = 0; i < arr.length; i++) {
					int id = Integer.parseInt(arr[i++]);
					String idItm = arr[i];
					ShopModel s = new ShopModel();
					s.setId(id);
					s.setItemId(idItm);

					mListItem.add(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ShopModel> getListItem() {
		return mListItem;
	}

}
